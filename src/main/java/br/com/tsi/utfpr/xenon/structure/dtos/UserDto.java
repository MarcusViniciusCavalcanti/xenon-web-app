package br.com.tsi.utfpr.xenon.structure.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {

    private final Long id;
    private final String name;
    private final AccessCardDTO accessCard;
    private final CarDto car;
    private final TypeUserDto typeUser;
    private final Integer numberAccess;
    private final Boolean authorisedAcces;

}
