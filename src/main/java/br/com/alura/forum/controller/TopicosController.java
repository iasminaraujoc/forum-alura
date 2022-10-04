package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.DetalhesTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//@Controller
//como a url "topicos" vai aparecer sempre, podemos deixá-la na classe toda
@RestController
@RequestMapping(value = "/topicos")
public class TopicosController {

    //aqui poderíamos simplesmente injetar as configurações do database, porém não é uma boa prática.
    // Costumamos isolar o banco de dados em outra classe e injeta essa classe no controller
    //utilizava-se o padrão DAO, no qual na classe dao, ocorriam todos os acessos encapsulados ao banco de dados
    //todas as classes são parecidas, só muda a entidade. o spring pensou nessa abstração e fez uma interface
    //Utilizaremos o padrão repository

    //@ResponseBody -> ao utilizar a anotação RestController, o Spring entende que os métodos estão automaticamente mapeados com ResponseBody
    //ao devolver uma lista com a entidade (própria classe), o Jackson serializa tudo, todos os atributos. Não é uma boa prática
    //criar uma nova classe só com os valores que queremos devolver - DTO - Data Transfer Object
//    public List<TopicoDto> lista(){
//        Topico topico = new Topico("Dúvida", "Dúvida com Spring", new Curso("Spring", "Programação"));
//        //biblioteca jackson, usada pelo spring por baixo dos panos, converte o objeto em string no formato json
//        return TopicoDto.converter(Arrays.asList(topico, topico, topico));
//    }

    //injetar o repository

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    //usando "method" o spring entende que estamos falando de métodos com mesmo valor, mas requisições diferentes
    //é um correspondente ao requestbody do post
    //@RequestMapping(value = "/topicos", method = RequestMethod.GET)
    @GetMapping
    //usar o requestparam diz que é obrigatorio passar o parametro, o atributo required=false diz que é opcional
    public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso,
                                 @PageableDefault(sort="id",direction = Sort.Direction.ASC, page=0, size=10) Pageable paginacao){

        //usa o PageableDefault só se o usuário não digitar os parametros da paginacao
        //url: localhost:8080/topicos?page=0&size=10&sort=id,asc&sort=dataCriacao,desc

        if(nomeCurso == null){
            //o findAll com pageable não retorna uma lista, mas sim uma Page, para dar detalhes para o usuário
            Page<Topico> topicos = topicoRepository.findAll(paginacao);
            return TopicoDto.converter(topicos);
        } else {
            Page<Topico> topicos = topicoRepository.findByTitulo(nomeCurso, paginacao);
            //findByCursoNome ou findByCurso_Nome ele reconhece tbm
            return TopicoDto.converter(topicos);
        }

    }

    //no navegador: topicos?nomeCurso=Spring+Boot

    @PostMapping
    //@RequestBody para contar para o spring que estamos falando de dados que vem dentro da requisição, e naõ como parametros no navegador
    //o método até poderia ter um retorno do tipo void, ele funciona. Mas é legal usar outra coisa para detalhar o que rolou:

    @Transactional
    //utilizar @valid para falar para o spring rodar as validações do BeanValidation.
    // Se der errado, devolve o código 400. Porém, um json gigantesco e complicado. Podemos simplificá-lo!
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder){
    //public void cadastrar(@RequestBody TopicoForm form){
        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);

        //devolvemos duas coisas para o cliente com o código 201: um cabeçalho chamado location, com a url do recurso criado
        // e, no corpo da resposta, uma representação do recurso

        //o uribuilder é uma classe do spring que podemos injetar aqui, passando apenas o caminho relativo

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @GetMapping("/{id}")
    //para dizer que o parâmetro faz parte do path, usamos a anotação @PathVariable
    // o spring percebe que o nome no parametro do metodo e no path são o mesmo
    public ResponseEntity<DetalhesTopicoDto> detalhar(@PathVariable Long id){
        Optional<Topico> topico = topicoRepository.findById(id);
        if(topico.isPresent()){
            return ResponseEntity.ok(new DetalhesTopicoDto(topico.get()));
        }
        return ResponseEntity.notFound().build();
    }

    //temos os métodos put e patch - qual usar?
    //put - sobrescrever o recurso, atualizar todas as informações
    //patch - atualiza apenas alguma coisinha
    //porém, como não sabemos o que vai acontecer na aplicação, é melhor usar o que é mais geral-patch
    @PutMapping("/{id}")
    //não precisa chamar update para o jpa, pq ele já entende automaticamente que é pra atualizar
    //porém, tem que usar essa anotação
    @Transactional
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form){
        Optional<Topico> optional = topicoRepository.findById(id);

        if(optional.isPresent()){
            Topico topico = form.atualizar(id, topicoRepository);
            return ResponseEntity.ok(new TopicoDto(topico));
        }
        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity remover(@PathVariable Long id){

        Optional<Topico> optional = topicoRepository.findById(id);

        if(optional.isPresent()){
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();

    }

}
