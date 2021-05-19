package br.com.tsi.utfpr.xenon.structure.dtos;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenRegistry {
    private final String value;
}
