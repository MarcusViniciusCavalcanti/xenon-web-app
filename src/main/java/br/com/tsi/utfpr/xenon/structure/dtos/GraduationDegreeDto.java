package br.com.tsi.utfpr.xenon.structure.dtos;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GraduationDegreeDto {
    private final Integer id;
    private final String name;
}
