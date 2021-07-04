package br.com.tsi.utfpr.xenon.unit.domain.security.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.domain.security.entity.AccessCard;
import br.com.tsi.utfpr.xenon.domain.security.entity.Role;
import br.com.tsi.utfpr.xenon.domain.security.service.RoleService;
import br.com.tsi.utfpr.xenon.domain.security.service.UpdatersAccessCardService;
import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Test - Unidade - UpdatersAccessCardService")
@ExtendWith(MockitoExtension.class)
class UpdatersAccessCardServiceTest {

    @Mock
    private RoleService roleService;

    @Test
    @DisplayName("Deve atualizar accessCard do usuário")
    void shouldHaveUpateAccessCard() {
        var mockUser = mock(User.class);
        var mockAccessCard = mock(AccessCard.class);
        var roles = List.of(new Role());

        var input = new InputUserDto();

        when(roleService.verifyAndGetRoleBy(any())).thenReturn(roles);

        lenient().when(mockUser.getAccessCard()).thenReturn(mockAccessCard);
        lenient().doNothing().when(mockAccessCard).setRoles(roles);
        lenient().doNothing().when(mockAccessCard)
            .setAccountNonExpired(input.isAccountNonExpired());
        lenient().doNothing().when(mockAccessCard).setAccountNonLocked(input.isAccountNonLocked());
        lenient().doNothing().when(mockAccessCard)
            .setCredentialsNonExpired(input.isCredentialsNonExpired());
        lenient().doNothing().when(mockAccessCard).setEnabled(input.isEnabled());

        UpdatersAccessCardService.getNewInstance(new InputUserDto(), roleService).accept(mockUser);

        verify(mockUser).getAccessCard();
        verify(roleService).verifyAndGetRoleBy(input);
        verify(mockAccessCard).setRoles(roles);
        verify(mockAccessCard).setAccountNonExpired(input.isAccountNonExpired());
        verify(mockAccessCard).setAccountNonLocked(input.isAccountNonLocked());
        verify(mockAccessCard).setCredentialsNonExpired(input.isCredentialsNonExpired());
        verify(mockAccessCard).setEnabled(input.isEnabled());

        verify(mockAccessCard, never()).setUsername(any());
        verify(mockAccessCard, never()).setPassword(any());
    }

}