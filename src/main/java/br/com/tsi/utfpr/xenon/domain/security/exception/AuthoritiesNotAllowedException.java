package br.com.tsi.utfpr.xenon.domain.security.exception;

import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import lombok.Getter;

public class AuthoritiesNotAllowedException extends RuntimeException {

    @Getter
    private InputUserDto inputUserDto;

    public AuthoritiesNotAllowedException(String typeUser) {
        super(String.format("O tipo de usuário não pode conter os papeis listados [%s]", typeUser));
    }

    public AuthoritiesNotAllowedException(InputUserDto userDto) {
        this(userDto.getType().getTranslaterName());
        inputUserDto = userDto;
    }
}
