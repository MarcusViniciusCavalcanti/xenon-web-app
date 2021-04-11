package br.com.tsi.utfpr.xenon.e2e;


import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.tsi.utfpr.xenon.e2e.utils.GetElementDom;
import br.com.tsi.utfpr.xenon.e2e.utils.InsertFormDom;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Test - e2e - Login")
public class LoginTest extends AbstractEndToEndTest {

    private static final String HTTP_LOCALHOST_8080_LOGIN = "http://localhost:8080/login";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    @DisplayName("Deve retonar erro quando usuário não encontrado")
    void shouldThrowsExceptionWhenUserNotFound() throws IOException {

        var redirectErrorLogin = InsertFormDom.init(webClient, HTTP_LOCALHOST_8080_LOGIN)
            .insertForm("form-login")
            .setInputValue("username", "beltrano")
            .setInputValue("password", "beltrano")
            .clickSubmitButton();

        assertEquals("/login", redirectErrorLogin.getBaseURL().getPath());
        assertEquals("error", redirectErrorLogin.getBaseURL().getQuery());

        var messageError = GetElementDom.start(redirectErrorLogin)
            .navigation("div", "class", "errors-container")
            .getDiv("class", "alert alert-danger text-center")
            .getElement()
            .getVisibleText();

        assertEquals("Usuário ou senha invalido!", messageError);
    }

    @Override
    WebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }

}
