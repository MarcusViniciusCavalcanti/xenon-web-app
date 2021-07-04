package br.com.tsi.utfpr.xenon.unit.domain.user.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import br.com.tsi.utfpr.xenon.domain.user.entity.TypeUser;
import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.domain.user.service.UpdatesUserService;
import br.com.tsi.utfpr.xenon.structure.dtos.TypeUserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test - Unidade - UpdatesUserService")
class UpdatesUserServiceTest {

    @Test
    @DisplayName("Deve lançcar exception quando input está null")
    void shouldThrowsExceptionWhenInputIsNull() {
        var updatersCarService = UpdatesUserService.getNewInstance(null);

        assertThrows(IllegalStateException.class, () -> updatersCarService.accept(new User()));
    }

    @Test
    @DisplayName("Deve atualizar usuário")
    void shouldHaveUpdateDataUser() {
        var mockUser = mock(User.class);

        var input = new InputUserDto();
        input.setName("new name");
        input.setType(TypeUserDto.SPEAKER);
        input.setAuthorizedAccess(Boolean.FALSE);

        lenient().doNothing().when(mockUser).setTypeUser(eq(TypeUser.SPEAKER));
        lenient().doNothing().when(mockUser).setName(input.getName());
        lenient().doNothing().when(mockUser).setAuthorisedAccess(input.isAuthorizedAccess());

        var updatersCarService = UpdatesUserService.getNewInstance(input);
        updatersCarService.accept(mockUser);

        verify(mockUser).setTypeUser(eq(TypeUser.SPEAKER));
        verify(mockUser).setName(input.getName());
        verify(mockUser).setAuthorisedAccess(input.isAuthorizedAccess());
    }
}