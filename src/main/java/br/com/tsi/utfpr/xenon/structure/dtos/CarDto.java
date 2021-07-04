package br.com.tsi.utfpr.xenon.structure.dtos;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CarDto {

    private final Long id;

    private final String plate;

    private final String model;

    private final LocalDateTime lastAccess;

    private final Boolean document;

}
