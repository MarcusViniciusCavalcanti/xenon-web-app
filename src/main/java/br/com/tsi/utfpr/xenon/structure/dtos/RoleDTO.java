package br.com.tsi.utfpr.xenon.structure.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoleDTO {
    private final Long id;

    private final String name;

    private final String description;
}
