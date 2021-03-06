package br.com.tsi.utfpr.xenon.unit.domain.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import br.com.tsi.utfpr.xenon.domain.security.entity.Role;
import br.com.tsi.utfpr.xenon.domain.user.entity.TypeUser;
import br.com.tsi.utfpr.xenon.domain.user.repository.UserRepository;
import br.com.tsi.utfpr.xenon.domain.user.service.GetterAllUserSpec;
import br.com.tsi.utfpr.xenon.domain.user.service.ParametersGetAllSpec;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@DisplayName("Test - Unidade - GetterAllUserSpec")
@DataJpaTest
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
    "classpath:/sql/user_block_insert.sql",
    "classpath:/sql/user_default_insert.sql"
})

@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = {
    "classpath:/sql/user_block_delete.sql",
    "classpath:/sql/user_default_delete.sql"})
class GetterAllUserSpecTest {

    private static final int YEAR = 2019;
    private static final int MONTH = 10;
    private static final int DAY_OF_MONTH_23 = 23;
    private static final int DAY_OF_MONTH_28 = 28;
    private static final int DAY_OF_MONTH_05 = 5;
    private static final int DAY_OF_MONTH_02 = 2;
    private final GetterAllUserSpec getterAllUserSpec = new GetterAllUserSpec();

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve retonar usuarios com apenas contas desativada")
    void shouldReturnUsersAccountEnable() {
        var params = ParametersGetAllSpec.builder()
            .enabled(Boolean.FALSE)
            .build();

        var spec = getterAllUserSpec.filterBy(params);
        var users = userRepository.findAll(spec);
        var user = users.get(0);

        assertEquals(1, users.size());
        assertEquals("beltrano_disable_account@user.com", user.getAccessCard().getUsername());
        assertEquals(LocalDateTime.of(YEAR, MONTH, 27, 0, 0), user.getAccessCard().getCreatedAt());
        assertNotNull(user.getAccessCard().getId());
        assertEquals(LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH_23, 0, 0),
            user.getAccessCard().getUpdatedAt());
        assertEquals(LocalDateTime.of(YEAR, MONTH, 8, 0, 0), user.getCreatedAt());
        assertEquals(LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH_23, 0, 0), user.getUpdatedAt());
        assertEquals("Beltrano conta desativada", user.getName());
        assertEquals(TypeUser.STUDENTS, user.getTypeUser());
        assertFalse(user.getAccessCard().isEnabled());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "conta exipirada",
        "exipirada",
        "beltrano_expired_account@user.com",
        "beltrano_expired_account",
        "beltrano_expired_a",
    })
    @DisplayName("Deve retonar usuarios com nomes")
    void shouldReturnUsers(String value) {
        var params = ParametersGetAllSpec.builder()
            .name(value)
            .build();

        var spec = getterAllUserSpec.filterBy(params);
        var users = userRepository.findAll(spec);

        var user = users.get(0);

        assertEquals(1, users.size());
        assertEquals("beltrano_expired_account@user.com", user.getAccessCard().getUsername());
        assertEquals(LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH_28, 0, 0),
            user.getAccessCard().getCreatedAt());
        assertNotNull(user.getAccessCard().getId());
        assertEquals(LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH_05, 0, 0), user.getCreatedAt());
        assertEquals(LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH_23, 0, 0), user.getUpdatedAt());
        assertEquals("Beltrano conta exipirada", user.getName());
        assertEquals(TypeUser.STUDENTS, user.getTypeUser());
        assertFalse(user.getAccessCard().isAccountNonExpired());

        spec.toString();
    }

    @Test
    @DisplayName("Deve retonar usuarios com papel especifico")
    void shouldReturnUsersProfile() {
        var params = ParametersGetAllSpec.builder()
            .profile("ROLE_ADMIN")
            .build();

        var spec = getterAllUserSpec.filterBy(params);
        var users = userRepository.findAll(spec);

        var user = users.get(0);

        assertEquals(1, users.size());
        assertEquals("beltrano_admin@admin.com", user.getAccessCard().getUsername());
        assertEquals(LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH_02, 0, 0),
            user.getAccessCard().getCreatedAt());
        assertNotNull(user.getAccessCard().getId());
        assertEquals(LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH_02, 0, 0), user.getCreatedAt());
        assertEquals(LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH_23, 0, 0), user.getUpdatedAt());
        assertEquals("Beltrano Admin", user.getName());
        assertEquals(TypeUser.SERVICE, user.getTypeUser());
        assertLinesMatch(
            List.of("ROLE_ADMIN", "ROLE_DRIVER", "ROLE_OPERATOR"),
            user.getAccessCard().getRoles().stream()
                .map(Role::getName)
                .sorted()
                .collect(Collectors.toList())
        );
    }

    @Test
    @DisplayName("Deve retonar usuarios com tipo de usu??rio especifico")
    void shouldReturnUsersType() {
        var params = ParametersGetAllSpec.builder()
            .type(TypeUser.SERVICE.name())
            .build();

        var spec = getterAllUserSpec.filterBy(params);
        var users = userRepository.findAll(spec);

        assertEquals(2, users.size());
    }

}
