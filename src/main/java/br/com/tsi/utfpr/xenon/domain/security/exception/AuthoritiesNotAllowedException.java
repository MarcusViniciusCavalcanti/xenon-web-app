package br.com.tsi.utfpr.xenon.domain.security.exception;

public class AuthoritiesNotAllowedException extends RuntimeException {

    public AuthoritiesNotAllowedException(String typeUser) {
        super(String.format("O tipo de usuário não pode conter os papeis listados [%s]", typeUser));
    }
}
