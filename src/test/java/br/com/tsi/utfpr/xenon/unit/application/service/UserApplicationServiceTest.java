package br.com.tsi.utfpr.xenon.unit.application.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.application.service.impl.UserApplicationServiceImpl;
import br.com.tsi.utfpr.xenon.domain.user.usecase.GetterFiles;
import br.com.tsi.utfpr.xenon.domain.user.usecase.GetterPageUser;
import br.com.tsi.utfpr.xenon.domain.user.usecase.RegistryUser;
import br.com.tsi.utfpr.xenon.domain.user.usecase.UpdateUser;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.ParamsSearchRequestDto;
import java.io.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test - Unidade - UserApplicationService")
class UserApplicationServiceTest {

    @Mock
    private GetterPageUser getterPageUser;

    @Mock
    private GetterFiles getterFiles;

    @Mock
    private RegistryUser registryUser;

    @Mock
    private UpdateUser updateUser;

    @InjectMocks
    private UserApplicationServiceImpl userApplicationService;

    @Test
    @DisplayName("Deve retonar uma objeto Page com a lista de usuários")
    void shouldReturnPageOfUser() {
        var params = new ParamsSearchRequestDto();

        when(getterPageUser.getAllUserByFilter(eq(params))).thenReturn(Page.empty());
        userApplicationService.findAllPageableUsers(params);

        verify(getterPageUser).getAllUserByFilter(eq(params));
    }

    @Test
    @DisplayName("Deve retonar um objeto tipo File do avatar")
    void shouldReturnFileAvatar() {
        var file = mock(File.class);

        when(getterFiles.getAvatar(anyLong())).thenReturn(file);

        userApplicationService.getAvatar(10L);

        verify(getterFiles).getAvatar(anyLong());
    }

    @Test
    @DisplayName("Deve retornar salvar usuário com sucesso")
    void shouldHaveSaveUser() {
        var input = new InputUserDto();
        doNothing()
            .when(registryUser)
            .save(input);

        userApplicationService.saveNewUser(input);

        verify(registryUser).save(input);
    }

    @Test
    @DisplayName("Deve retonar um objeto InputUserDto")
    void shouldReturnInputUserDto() {
        var input = new InputUserDto();
        when(updateUser.findUserUpdate(anyLong())).thenReturn(input);

        userApplicationService.findUserUpdate(1L);

        verify(updateUser).findUserUpdate(anyLong());
    }

}
