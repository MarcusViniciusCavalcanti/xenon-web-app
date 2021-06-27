package br.com.tsi.utfpr.xenon.unit.web.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.application.service.RegistryStudentsApplicationService;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputEmailDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputNewStudent;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputValidateTokenDto;
import br.com.tsi.utfpr.xenon.web.controller.StudentRegistryController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@DisplayName("Test - Unidade - StudentRegistryController")
@ExtendWith(MockitoExtension.class)
class StudentRegistryControllerTest {

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private RegistryStudentsApplicationService registryStudentsApplicationService;

    @InjectMocks
    private StudentRegistryController studentRegistryController;

    @Test
    @DisplayName("Deve retornar ModelAndView com input na view 'user/registry/email-registry'")
    void shouldReturnModelAndViewUserRegistryEmailRegistry() {
        var modelAndView = mock(ModelAndView.class);
        var input = new InputEmailDto();

        var result = studentRegistryController.newRegistry(modelAndView, input);

        verify(modelAndView).setViewName(eq("user/registry/email-registry"));
        verify(modelAndView).addObject(eq("inputEmail"), eq(input));

        assertEquals(modelAndView, result);
    }

    @Test
    @DisplayName("Deve retornar Redirect quando contém error no input")
    void shouldReturnRedirectWhenInputContainsError() {
        var input = new InputEmailDto();
        var fieldError = mock(FieldError.class);

        when(bindingResult.hasErrors()).thenReturn(Boolean.TRUE);
        when(bindingResult.getFieldError(eq("email"))).thenReturn(fieldError);

        var result =
            studentRegistryController.includeNewRegistry(input, bindingResult, redirectAttributes);

        assertEquals("redirect:/novo-registro", result);

        verify(redirectAttributes).addFlashAttribute(eq("error"), eq(fieldError));
        verify(registryStudentsApplicationService, never()).includeNewRegistry(any(String.class));
    }

    @Test
    @DisplayName("Deve retornar ModelAndView na view 'error-registry'")
    void shouldReturnModelAndViewToErrorRegistry() {
        var model = mock(ModelAndView.class);

        var result = studentRegistryController.userExist(model);

        assertEquals(model, result);

        verify(model).setViewName(eq("error-registry"));
    }

    @Test
    @DisplayName("Deve retornar Redirect '/novo-registro' quando acessar '/validar-token' e e-mail no [InputToken] esta nulo")
    void shouldReturnRedirectWhenEmailIsNull() {
        var input = new InputValidateTokenDto();
        var model = mock(ModelAndView.class);

        var result = studentRegistryController.validateToken(input, model);

        assertNotEquals(model, result);

        verify(model, never()).setViewName(eq("user/registry/token-registry"));
        verify(model, never()).addObject(eq("inputToken"), eq(input));
    }

    @Test
    @DisplayName("Deve retornar Redirect '/novo-registro' quando acessar '/validar-token' e e-mail no [InputToken] esta vazio")
    void shouldReturnRedirectWhenEmailIsEmpty() {
        var input = new InputValidateTokenDto();
        input.setEmail("");
        var model = mock(ModelAndView.class);

        var result = studentRegistryController.validateToken(input, model);

        assertNotEquals(model, result);

        verify(model, never()).setViewName(eq("user/registry/token-registry"));
        verify(model, never()).addObject(eq("inputToken"), eq(input));
    }

    @Test
    @DisplayName("Deve retornar Redirec '/validar-token' quando input contém error")
    void shouldReturnRedirectToValidarToken() {
        var input = new InputValidateTokenDto();
        var fieldError = mock(FieldError.class);

        when(bindingResult.hasErrors()).thenReturn(Boolean.TRUE);
        when(bindingResult.getFieldError(eq("token"))).thenReturn(fieldError);

        var result =
            studentRegistryController.validateToken(input, bindingResult, redirectAttributes);

        assertEquals("redirect:/validar-token", result);

        verify(bindingResult).hasErrors();
        verify(redirectAttributes).addAttribute(eq("email"), any());
        verify(redirectAttributes).addFlashAttribute(eq("error"), eq(fieldError));

        verify(registryStudentsApplicationService, never()).validateToken(any());
    }

    @Test
    @DisplayName("Deve retornar ModelAndView na view 'user/registry/student-registry'")
    void shouldReturnModelAndViewUserRegistryStudentRegistry() {
        var input = new InputValidateTokenDto();
        var token = "token";
        input.setEmail("email");

        when(bindingResult.hasErrors()).thenReturn(Boolean.FALSE);
        when(registryStudentsApplicationService.validateToken(eq(input))).thenReturn(token);

        var result =
            studentRegistryController.validateToken(input, bindingResult, redirectAttributes);

        assertEquals("redirect:/cadastro-estudante", result);

        verify(redirectAttributes).addAttribute(eq("email"), eq(input.getEmail()));
        verify(registryStudentsApplicationService).validateToken(eq(input));

        verify(redirectAttributes, never()).addFlashAttribute(any(), any());
    }

    @Test
    @DisplayName("Deve retornar ModelAndView na view 'user/registry/student-registry' com [InputNewStudent]'")
    void shouldReturnModelAndViewUserRegistryStudent() {
        var model = mock(ModelAndView.class);
        var input = new InputNewStudent();

        var result = studentRegistryController.registryStudent(model, input);

        assertEquals(model, result);

        verify(model).setViewName(eq("user/registry/student-registry"));
        verify(model).addObject(eq("input"), eq(input));
    }

    @Test
    @DisplayName("Deve retonar Redirect '/cadastro-estudante' quando [InputNewStudent] for invalido")
    void shouldReturnRedirectWithErrors() {
        when(bindingResult.hasErrors()).thenReturn(Boolean.TRUE);
        studentRegistryController.registryNewStudent(new InputNewStudent(), bindingResult, redirectAttributes);

        verify(bindingResult).hasErrors();
        verify(bindingResult).getFieldErrors();
        verify(redirectAttributes).addFlashAttribute(eq("errors"), any());
        verify(redirectAttributes).addAttribute(eq("token"), any());
        verify(redirectAttributes).addAttribute(eq("email"), any());
    }

    @Test
    @DisplayName("Deve cadastrar novo estudante e redirecionar para login")
    void shouldRegistryUser() {
        var input = new InputNewStudent();
        when(bindingResult.hasErrors()).thenReturn(Boolean.FALSE);
        doNothing().when(registryStudentsApplicationService).registryNewStudent(input);

        var redirect = studentRegistryController.registryNewStudent(input, bindingResult, redirectAttributes);

        verify(registryStudentsApplicationService).registryNewStudent(input);
        verify(bindingResult).hasErrors();

        assertEquals("redirect:/concluido", redirect);
    }
}
