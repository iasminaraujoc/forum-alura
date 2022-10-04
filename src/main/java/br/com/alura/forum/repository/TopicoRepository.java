package br.com.alura.forum.repository;

import br.com.alura.forum.modelo.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//como é uma interface, o spring já encontra automaticamente. Não precisa de anotação
// duas coisas no generics: a classe armazenada e o tipo do seu id
public interface TopicoRepository extends JpaRepository<Topico, Long> {

    // a classe fica vazia pq já foi tudo herdado de JPaRepository
    //padrão de nomenclatura spring: findByNomeAtributo, ele gera a query
    Page<Topico> findByTitulo(String nomeCurso, Pageable paginacao);

    //se quiser usar outra nomenclatura, tem que usar a anotação @Query e montar a query em jpql
}
