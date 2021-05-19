package br.com.tsi.utfpr.xenon.unit.web.error;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import br.com.tsi.utfpr.xenon.domain.user.exception.TokenException;
import br.com.tsi.utfpr.xenon.domain.user.exception.UsernameException;
import br.com.tsi.utfpr.xenon.structure.dtos.ResultCheckerDto;
import br.com.tsi.utfpr.xenon.structure.dtos.ResultCheckerDto.Result;
import br.com.tsi.utfpr.xenon.structure.dtos.ResultUsernameCheckerDto;
import br.com.tsi.utfpr.xenon.web.error.StudentErrorHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Test - Unidade - StudentErrorHandler")
@ExtendWith(MockitoExtension.class)
class StudentErrorHandlerTest {

    private static final String ERROR_ERROR_REGISTRY = "error/error-registry";

    @InjectMocks
    private StudentErrorHandler studentErrorHandler;

    @Test
    @DisplayName("Deve retornar ModelAndView com view 'error/error-registry' quando userExist")
    void shouldReturnModelAndViewUserExist() {
        var result = ResultUsernameCheckerDto.builder()
            .username("email")
            .result(ResultCheckerDto.builder()
                .reason("reason")
                .value(Result.USER_CANNOT_BE_REGISTERED)
                .build())
            .build();
        var exception = new UsernameException(result);

        var expected = studentErrorHandler.userExist(exception);
        assertEquals(ERROR_ERROR_REGISTRY, expected.getViewName());
        assertEquals(BAD_REQUEST, expected.getStatus());
        assertEquals("Usuário email não pode ser cadastrado porque: reason", expected.getModel().get("reason"));
    }

    @Test
    @DisplayName("Deve retonar ModelAndView com view 'error/error-registry' quando invalidToken")
    void shouldReturnModelAndViewInvalidToken() {
        var exception = new TokenException("token");

        var expected = studentErrorHandler.invalidToken(exception);
        assertEquals(ERROR_ERROR_REGISTRY, expected.getViewName());
        assertEquals(BAD_REQUEST, expected.getStatus());
        assertEquals("Token inválido token", expected.getModel().get("reason"));
    }
}
