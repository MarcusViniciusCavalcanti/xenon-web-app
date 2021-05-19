package br.com.tsi.utfpr.xenon.unit.domain.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import br.com.tsi.utfpr.xenon.domain.security.entity.Role;
import br.com.tsi.utfpr.xenon.domain.security.exception.AuthoritiesNotAllowedException;
import br.com.tsi.utfpr.xenon.domain.security.repository.RoleRepository;
import br.com.tsi.utfpr.xenon.domain.security.service.RoleService;
import br.com.tsi.utfpr.xenon.structure.dtos.TypeUserDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Test - Unidade - RoleService")
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    public static final List<Long> AUTHORITIES_ADMIN = List.of(1L, 2L, 3L);
    public static final List<Long> AUTHORITIES_DRIVER = List.of(1L);

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @ParameterizedTest
    @EnumSource(value = TypeUserDto.class, names = {"STUDENTS", "SPEAKER"}, mode = Mode.INCLUDE)
    @DisplayName("Deve retonar erro quando permissões não concide com o tipo de usuário")
    void shouldThrowsAuthoritiesNotAllowedExceptionWhenTypeUserNotPermissions(TypeUserDto typeUser) {
        var exception = assertThrows(
            AuthoritiesNotAllowedException.class,
            () -> roleService.verifyAndGetRoleBy(typeUser, AUTHORITIES_ADMIN)
        );

        var msgErrorExpected = String.format(
            "O tipo de usuário não pode conter os papeis listados [%s]",
            typeUser.getTranslaterName()
        );

        assertEquals(msgErrorExpected, exception.getMessage());
    }

    @ParameterizedTest
    @EnumSource(
        value = TypeUserDto.class,
        names = {"STUDENTS", "SPEAKER", "SERVICE"},
        mode = Mode.INCLUDE
    )
    @DisplayName("Deve retornar a lista de papeis de um tipo de usuário")
    void shouldReturnListRoles(TypeUserDto typeUser) {
        when(roleRepository.findAllById(eq(AUTHORITIES_DRIVER))).thenReturn(List.of(new Role()));

        roleService.verifyAndGetRoleBy(typeUser, AUTHORITIES_DRIVER);

        verify(roleRepository).findAllById(eq(AUTHORITIES_DRIVER));
    }
}
