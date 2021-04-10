package br.com.tsi.utfpr.xenon.structure.dtos.inputs;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum TypeModeSystem {
    AUTOMATIC, MANUAL, NONE;

    @JsonCreator
    public static TypeModeSystem fromString(String value) {
        return Stream.of(TypeModeSystem.values())
                .filter(valueEnum -> valueEnum.name().equals(value))
                .findFirst()
                .orElse(null);

    }
}
