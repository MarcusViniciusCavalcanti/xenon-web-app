package br.com.tsi.utfpr.xenon.structure.dtos;

import lombok.Getter;

public enum  TypeUserDTO {
    STUDENTS("Estudante"), SERVICE("Servidor"), SPEAKER("Palestrante");

    @Getter
    private final String translaterName;

    TypeUserDTO(String translaterName) {
        this.translaterName = translaterName;
    }
}
