package br.com.alura.forum.config.security;

//ao invés de colocarmos as configurações no application.properties,
//adicionamos numa classe por ser mais dinâmico

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
//padrão: bloquear tudo
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

    @Autowired
    private AutenticacaoService autenticacaoService;
    //configura a parte de autenticação
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
                .anyRequest().authenticated()
                .and().formLogin();
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
