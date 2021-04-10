package br.com.tsi.utfpr.xenon.domain.user.entity;

import java.util.List;
import lombok.Getter;

public enum TypeUser {
    STUDENTS(List.of(1L)),
    SPEAKER(List.of(1L)),
    SERVICE(List.of(1L, 2L, 3L));

    @Getter
    private final List<Long> allowedProfiles;

    TypeUser(List<Long> allowedProfiles) {
        this.allowedProfiles = allowedProfiles;
    }
}
