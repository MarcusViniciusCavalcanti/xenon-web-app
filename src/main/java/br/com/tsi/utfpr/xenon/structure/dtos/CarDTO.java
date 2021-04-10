package br.com.tsi.utfpr.xenon.structure.dtos;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class CarDTO {

    private Long id;

    private String plate;

    private String model;

    private LocalDateTime lastAccess;

}
