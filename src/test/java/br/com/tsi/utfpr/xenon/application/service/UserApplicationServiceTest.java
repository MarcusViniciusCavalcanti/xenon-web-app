package br.com.tsi.utfpr.xenon.application.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.application.service.impl.UserApplicationServiceImpl;
import br.com.tsi.utfpr.xenon.domain.user.usecase.GetterPageUser;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.ParamsSearchRequestDTO;
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

    @InjectMocks
    private UserApplicationServiceImpl userApplicationService;

    @Test
    @DisplayName("Deve retonar uma objeto Page com a lista de usuários")
    void shouldReturnPageOfUser() {
        var params = new ParamsSearchRequestDTO();

        when(getterPageUser.getAllUserByFilter(eq(params))).thenReturn(Page.empty());
        userApplicationService.findAllPageableUsers(params);

        verify(getterPageUser).getAllUserByFilter(eq(params));
    }

}
