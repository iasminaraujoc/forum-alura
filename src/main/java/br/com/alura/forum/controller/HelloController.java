package br.com.alura.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
    @RequestMapping("/")
    //se não usamos responsebody, o spring acha que helloworld é uma página e tenta procurá-la
    @ResponseBody
    public String hello(){
        return "Hello World!";
    }
    /*o spring boot só reconhece coisas que estão dentro do pacote main
    spring é um dos mais antigos frameworks
    antes, se usava j2ee
    o criador fez o livro com a biblioteca, que começou a ser muito baixada
    inversão de controle e injeção de dependências
    spring boot - gerar uma aplicação com servidor embutido
    casou com a ideia de microsserviços
    */
}
