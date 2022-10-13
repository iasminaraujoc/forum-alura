package br.com.alura.forum.config.security;

//ao invés de colocarmos as configurações no application.properties,
//adicionamos numa classe por ser mais dinâmico

import br.com.alura.forum.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
//padrão: bloquear tudo
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Bean //deixando o AuthenticationManager injetável
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    //configura a parte de autenticação
    //autenticação tradicional = não é uma boa alternativa para o modelo rest
    //modelo session(tradicional) -> cada sessão tem um id, criada na memória. O navegador armazena isso como um cookie
    //consome espaço de memória, se o servidor cai, perdemos tudo
    //o servidor deve ser serverless, no modelo rest
    //com o spring, podemos implementar o modelo stateless.
    //não se cria sessions, mas o servidor nao entende se ta logado ou não.
    //para ele saber disso, o cliente deve passar um parâmetro, uma informação pra se identificar
    //isso é papel do jwt, json web token
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //o userdetailservice diz qual é a classe que tem as conf. de autenticação
        //o password encoder é quem transforma a senha do database em hash
        auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
    }

    //configura autorização, perfil de acesso, quem usa qual url
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/topicos").permitAll()
                .antMatchers(HttpMethod.GET, "/topicos/*").permitAll()
                .antMatchers(HttpMethod.POST, "/auth").permitAll() //liberar url de login para logar
                .anyRequest().authenticated()
                //.and().formLogin(); - tirando sessions
                .and().csrf().disable() //csrf - cross site request forgery - tipo de ataque hacker que pode ocorrer
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class);
    }

    //configura recursos estáticos(js, css, imagens,etc.)
    @Override
    public void configure(WebSecurity web) throws Exception {
    }

    //obtendo o hash de 123456
//    public static void main(String[] args) {
//        System.out.println(new BCryptPasswordEncoder().encode("123456"));
//    }

}
