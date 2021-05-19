package br.com.tsi.utfpr.xenon.unit.web.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.application.service.UserApplicationService;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.ParamsSearchRequestDTO;
import br.com.tsi.utfpr.xenon.web.controller.UserController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

@DisplayName("Test - Unidade - UserController")
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserApplicationService userApplicationService;

    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("Deve retonar ModelAndView com a view 'user/all/index'")
    void shouldReturnModeAndViewUserAllIndex() {
        var model = userController.allUser();
        assertEquals("user/all/index", model.getViewName());
    }

    @Test
    @DisplayName("Deve retornar Page de usuários")
    void shouldReturnPageUser() {
        var params = new ParamsSearchRequestDTO();
        when(userApplicationService.findAllPageableUsers(eq(params))).thenReturn(Page.empty());

        userController.findAll(params);

        verify(userApplicationService).findAllPageableUsers(eq(params));
    }
}
