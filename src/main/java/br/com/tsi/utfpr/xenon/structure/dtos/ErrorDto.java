package br.com.tsi.utfpr.xenon.structure.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDto {

    private final String msg;
}
