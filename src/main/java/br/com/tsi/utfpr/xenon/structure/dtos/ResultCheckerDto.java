package br.com.tsi.utfpr.xenon.structure.dtos;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResultCheckerDto {

    private final Result value;
    private final String reason;

    public enum Result {
        USER_CAN_BE_REGISTERED, USER_CANNOT_BE_REGISTERED
    }

}
