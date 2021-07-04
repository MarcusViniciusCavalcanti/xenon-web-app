package br.com.tsi.utfpr.xenon.structure.dtos;

import lombok.Getter;

public enum TypeUserDto {
    STUDENTS("Estudante"), SERVICE("Servidor"), SPEAKER("Palestrante");

    @Getter
    private final String translaterName;

    TypeUserDto(String translaterName) {
        this.translaterName = translaterName;
    }
}
