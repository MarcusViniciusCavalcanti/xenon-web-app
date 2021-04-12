package br.com.tsi.utfpr.xenon.domain.user.service;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ParametersGetAllSpec {

    private final String name;
    private final String profile;
    private final String type;
    private final Boolean enabled;
}
