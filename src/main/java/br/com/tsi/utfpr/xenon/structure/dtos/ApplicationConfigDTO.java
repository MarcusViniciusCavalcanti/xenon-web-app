package br.com.tsi.utfpr.xenon.structure.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationConfigDTO {

    private final String modeSystem;

    private final String ip;
}
