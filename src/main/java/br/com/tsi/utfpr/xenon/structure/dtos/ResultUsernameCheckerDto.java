package br.com.tsi.utfpr.xenon.structure.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResultUsernameCheckerDto {

    private final String username;
    private final ResultCheckerDto result;
}
