package io.github.brunnodanyel.projetovendas.exception;

import io.github.brunnodanyel.projetovendas.ApiErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerException {

    @ExceptionHandler(ClienteNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handleRegraNegocioException(ClienteNaoEncontradoException ex) {
        String mensagemErro = ex.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(EnderecoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handleRegraNegocioException(EnderecoNaoEncontradoException ex) {
        String mensagemErro = ex.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(CepNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handleRegraNegocioException(CepNaoEncontradoException ex) {
        String mensagemErro = ex.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handleRegraNegocioException(ProdutoNaoEncontradoException ex) {
        String mensagemErro = ex.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(ServicoCepException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrors handleRegraNegocioException(ServicoCepException ex) {
        String mensagemErro = ex.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(ProdutoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleRegraNegocioException(ProdutoException ex) {
        String mensagemErro = ex.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(PedidoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleRegraNegocioException(PedidoException ex) {
        String mensagemErro = ex.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(SenhaInvalidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleRegraNegocioException(SenhaInvalidaException ex) {
        String mensagemErro = ex.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(SenhaIncorretaException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrors handleRegraNegocioException(SenhaIncorretaException ex) {
        String mensagemErro = ex.getMessage();
        return new ApiErrors(mensagemErro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleMethodNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(erro -> erro.getDefaultMessage())
                .collect(Collectors.toList());
        return new ApiErrors(errors);
    }
}
