package br.com.alura.forum.config.validacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//Classe "interceptador" que vale para todos os controllers.
// Toda vez que ocorre uma exception no controller, ela é chamada
@RestControllerAdvice
public class ErroDeValidacaoController {

    //messagesource é uma classe do spring para nos ajudar a lidar com as mensagens de erro,
    // configuradas de acordo com idioma, por exemplo. Estamos injetando para usar no método handler
    @Autowired
    private MessageSource messageSource;

    //o spring acha que, como fizemos o tratamento, pode devolver o código 200.
    //Porém, queremos continuar devolvendo 400
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    //caso aconteça a exceção do method argument, o método handle é chamado
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErroDeFormularioDto> handle(MethodArgumentNotValidException exception){
        List<ErroDeFormularioDto> dto = new ArrayList<>();

        //lista que armazena todos os erros de formulário
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        fieldErrors.forEach(e->{
            String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            ErroDeFormularioDto erro = new ErroDeFormularioDto(e.getField(), mensagem);
            dto.add(erro);
        });

        return dto;
    }
}
