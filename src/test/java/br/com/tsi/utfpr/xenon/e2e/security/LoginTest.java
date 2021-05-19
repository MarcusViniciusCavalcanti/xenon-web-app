package br.com.tsi.utfpr.xenon.e2e.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.tsi.utfpr.xenon.e2e.AbstractEndToEndTest;
import br.com.tsi.utfpr.xenon.e2e.utils.GetElementDom;
import br.com.tsi.utfpr.xenon.e2e.utils.InsertFormDom;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Test - e2e - Login")
public class LoginTest extends AbstractEndToEndTest {

    private static final String HTTP_LOCALHOST_8080_LOGIN = "http://localhost:8080/login";
    private static final String FORM_LOGIN = "form-login";
    private static final String USERNAME_INPUT = "username";
    private static final String PASSWORD_INPUT = "password";
    private static final String ATTRIBUTE_CLASS = "class";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    @DisplayName("Deve retonar erro quando usuário não encontrado")
    void shouldThrowsExceptionWhenUserNotFound() throws IOException {
        var defaultValue = "beltrano";
        var redirectErrorLogin = InsertFormDom.init(webClient, HTTP_LOCALHOST_8080_LOGIN)
            .insertForm(FORM_LOGIN)
            .setInputValue(USERNAME_INPUT, defaultValue)
            .setInputValue(PASSWORD_INPUT, defaultValue)
            .clickSubmitButton();

        assertEquals("/login", redirectErrorLogin.getBaseURL().getPath());
        assertEquals("error", redirectErrorLogin.getBaseURL().getQuery());

        var messageError = GetElementDom.start(redirectErrorLogin)
            .navigation("div", ATTRIBUTE_CLASS, "errors-container")
            .getDiv(ATTRIBUTE_CLASS, "alert alert-danger text-center")
            .getElement()
            .getVisibleText();

        assertEquals("Usuário ou senha inválidos!", messageError);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "beltrano_user@alunos.utfpr.edu.br", "beltrano_admin@admin.com", "beltrano_operator@operator.com"
    })
    @DisplayName("Deve logar com sucesso")
    void shouldHaveLoggingSuccessfully(String username) throws IOException {
        var homePage = InsertFormDom.init(webClient, HTTP_LOCALHOST_8080_LOGIN)
            .insertForm(FORM_LOGIN)
            .setInputValue(USERNAME_INPUT, username)
            .setInputValue(PASSWORD_INPUT, "1234567")
            .clickSubmitButton();

        assertEquals("/home", homePage.getBaseURL().getPath());
    }

    @Override
    protected WebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }

}
