package br.com.tsi.utfpr.xenon.domain.user.exception;

public class TokenException extends RuntimeException {

    public TokenException(String token) {
        super(String.format("Token inválido %s", token));
    }
}
