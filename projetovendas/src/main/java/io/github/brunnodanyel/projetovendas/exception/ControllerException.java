package io.github.brunnodanyel.projetovendas.exception;

import io.github.brunnodanyel.projetovendas.ApiErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ControllerException {

    @ExceptionHandler(EntidadeNaoEncontrada.class)
    @ResponseStatus(NOT_FOUND)
    public ApiErrors handleRegraNegocioException(EntidadeNaoEncontrada ex) {
        String mensagemErro = ex.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(ServicoCepException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiErrors handleRegraNegocioException(ServicoCepException ex) {
        String mensagemErro = ex.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(EntidadeExistenteException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleRegraNegocioException(EntidadeExistenteException ex) {
        String mensagemErro = ex.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(BadRequestExecption.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleRegraNegocioException(BadRequestExecption ex) {
        String mensagemErro = ex.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(IdadeException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleRegraNegocioException(IdadeException ex) {
        String mensagemErro = ex.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(SenhaInvalidaException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleRegraNegocioException(SenhaInvalidaException ex) {
        String mensagemErro = ex.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(SenhaIncorretaException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ApiErrors handleRegraNegocioException(SenhaIncorretaException ex) {
        String mensagemErro = ex.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleMethodNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(erro -> erro.getDefaultMessage())
                .collect(Collectors.toList());
        return new ApiErrors(errors);
    }
}
