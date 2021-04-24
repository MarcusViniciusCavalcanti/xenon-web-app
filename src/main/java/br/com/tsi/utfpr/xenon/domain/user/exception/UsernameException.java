package br.com.tsi.utfpr.xenon.domain.user.exception;

import br.com.tsi.utfpr.xenon.structure.dtos.ResultUsernameCheckerDto;
import lombok.Getter;

public class UsernameException extends RuntimeException {

    @Getter
    private final ResultUsernameCheckerDto resultUsernameCheckerDto;

    public UsernameException(ResultUsernameCheckerDto resultUsernameCheckerDto) {
        super(String.format("Usuário %s não pode ser cadastrado porque: %s",
            resultUsernameCheckerDto.getUsername(),
            resultUsernameCheckerDto.getResult().getReason()));

        this.resultUsernameCheckerDto = resultUsernameCheckerDto;
    }
}
