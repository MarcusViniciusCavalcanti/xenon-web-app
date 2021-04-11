package br.com.tsi.utfpr.xenon.domain.security;

import static org.junit.jupiter.api.Assertions.*;

import br.com.tsi.utfpr.xenon.domain.security.entity.Role;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@DisplayName("Test - Unidade - Papeis")
public class RolesBasicTest {

    public static final int AMOUNT_ROLES_DEFAULT = 3;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Deve retornar Papeis basicos")
    void shouldReturnRoles() {
        var allRoles = entityManager.getEntityManager()
            .createQuery("select role from Role role", Role.class)
            .getResultList()
            .stream()
            .sorted(Comparator.comparingLong(Role::getId))
            .collect(Collectors.toList());

        assertEquals(AMOUNT_ROLES_DEFAULT, allRoles.size());
        assertAll("Verificando os papeis",
            () -> assertAll("Verificando nome dos papeis",
                () -> assertEquals("ROLE_DRIVER", allRoles.get(0).getName()),
                () -> assertEquals("ROLE_ADMIN", allRoles.get(1).getName()),
                () -> assertEquals("ROLE_OPERATOR", allRoles.get(2).getName())
            ),
            () -> assertAll("Verificando descrição",
                () -> assertEquals("Perfil Motorista", allRoles.get(0).getDescription()),
                () -> assertEquals("Perfil Administrador", allRoles.get(1).getDescription()),
                () -> assertEquals("Perfil Operador", allRoles.get(2).getDescription())
            )
        );

        var listId = allRoles.stream()
            .map(Role::getId)
            .sorted()
            .collect(Collectors.toList());

        var listDefaultIds = Arrays.asList(1L, 2L, 3L);

        assertEquals(listDefaultIds, listId);
    }
}
