package br.com.tsi.utfpr.xenon.unit.web.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyCollection;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.application.service.SecurityApplicationService;
import br.com.tsi.utfpr.xenon.application.service.UserApplicationService;
import br.com.tsi.utfpr.xenon.structure.dtos.RoleDTO;
import br.com.tsi.utfpr.xenon.structure.dtos.UserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.ParamsSearchRequestDto;
import br.com.tsi.utfpr.xenon.web.controller.UserController;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@DisplayName("Test - Unidade - UserController")
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserApplicationService userApplicationService;

    @Mock
    private SecurityApplicationService securityApplicationService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

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
        var params = new ParamsSearchRequestDto();
        when(userApplicationService.findAllPageableUsers(eq(params))).thenReturn(Page.empty());

        userController.findAll(params);

        verify(userApplicationService).findAllPageableUsers(eq(params));
    }

    @Test
    @DisplayName("Deve retonar byte[] quando requisitado avatar")
    void shouldReturnByteArrayWhenRequestAvatar() throws IOException {
        var file = Files.createTempFile("", "");

        when(userApplicationService.getAvatar(anyLong())).thenReturn(file.toFile());

        userController.getAvatar(10L);

        verify(userApplicationService).getAvatar(anyLong());
    }

    @Test
    @DisplayName("Deve retornar ModelAndView com view 'user/create/index'")
    void shouldReturnModelAndViewUserCreate() {
        var modelAndView = mock(ModelAndView.class);
        when(securityApplicationService.getAllRoles())
            .thenReturn(List.of(RoleDTO.builder().build()));

        var result = userController.createNew(modelAndView);

        assertEquals(modelAndView, result);

        verify(modelAndView).setViewName("user/create/index");
        verify(modelAndView).addObject(eq("input"), any(InputUserDto.class));
        verify(modelAndView).addObject(eq("roles"), anyCollection());
    }

    @Test
    @DisplayName("Deve retonar redirect para '/usuarios/todos'")
    void shouldReturnRedirectToAllUser() {
        var input = new InputUserDto();

        when(bindingResult.hasErrors()).thenReturn(Boolean.FALSE);

        var result = userController.saveNewUser(input, bindingResult, redirectAttributes);

        assertEquals("redirect:/usuarios/todos", result);
    }

    @Test
    @DisplayName("Deve retonar redirect para '/usuarios/novo' quando formulário está invalido")
    void shouldReturnRedirectToFormWhenFormIsInvalid() {
        var input = new InputUserDto();
        var fieldError = mock(FieldError.class);

        when(bindingResult.hasErrors()).thenReturn(Boolean.TRUE);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        var result = userController.saveNewUser(input, bindingResult, redirectAttributes);

        assertEquals("redirect:/usuarios/novo", result);
        verify(redirectAttributes)
            .addFlashAttribute(eq("errors"), eq("Campos com errors verifique e tente novamente"));
    }

    @Test
    @DisplayName("Deve ModelAndView com view 'user/profile/profile'")
    void shouldReturnModelAndViewUserProfile() {
        var modelAndView = mock(ModelAndView.class);
        var user = UserDto.builder().build();
        when(securityApplicationService.currentUser()).thenReturn(user);

        var result = userController.profile(modelAndView);

        assertEquals(modelAndView, result);
        verify(modelAndView).setViewName("user/profile/profile");
        verify(modelAndView).addObject(eq("user"), eq(user));
    }
}
