package br.com.tsi.utfpr.xenon.unit.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.application.service.impl.SecurityApplicationServiceImpl;
import br.com.tsi.utfpr.xenon.domain.security.service.RoleService;
import br.com.tsi.utfpr.xenon.domain.user.usecase.GetterCurrentUser;
import br.com.tsi.utfpr.xenon.structure.dtos.RoleDTO;
import br.com.tsi.utfpr.xenon.structure.dtos.UserDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test - Unidade - SecurityApplicationService")
class SecurityApplicationServiceImplTest {

    @Mock
    private GetterCurrentUser getterCurrentUser;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private SecurityApplicationServiceImpl securityApplicationService;

    @Test
    @DisplayName("Deve retonar usuário logado")
    void shouldReturnCurrentUser() {
        var dto = UserDto.builder().build();
        when(getterCurrentUser.currentUser()).thenReturn(dto);

        var result = securityApplicationService.currentUser();

        assertEquals(dto, result);
        verify(getterCurrentUser).currentUser();
    }

    @Test
    @DisplayName("Deve retornar todas as roles")
    void shouldReturnAllRoles() {
        var dtos = List.of(RoleDTO.builder().build());
        when(roleService.getAll()).thenReturn(dtos);

        var result = securityApplicationService.getAllRoles();

        assertEquals(dtos, result);
        verify(roleService).getAll();
    }
}