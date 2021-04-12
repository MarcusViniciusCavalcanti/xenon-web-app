package br.com.tsi.utfpr.xenon.e2e.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.tsi.utfpr.xenon.e2e.AbstractEndToEndTest;
import br.com.tsi.utfpr.xenon.e2e.utils.GetElementDom;
import br.com.tsi.utfpr.xenon.e2e.utils.InsertFormDom;
import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Test - e2e - Bloqueando acesso")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
    "classpath:/sql/user_block_insert.sql"})
@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = {
    "classpath:/sql/user_block_delete.sql"})
public class BlockAccessTest extends AbstractEndToEndTest {

    private static final String HTTP_LOCALHOST_8080_LOGIN = "http://localhost:8080/login";
    private static final String FORM_LOGIN = "form-login";
    private static final String USERNAME_INPUT = "username";
    private static final String PASSWORD_INPUT = "password";
    private static final String ATTRIBUTE_CLASS = "class";
    private static final String PASSWORD = "1234567";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @ParameterizedTest
    @MethodSource("providerAccountsFail")
    @DisplayName("Deve não deve conceder acesso para :->")
    void shouldHaveBlockUserExpiredAccount(String username, String expectedMsg) throws IOException {
        var redirectErrorLogin = InsertFormDom.init(webClient, HTTP_LOCALHOST_8080_LOGIN)
            .insertForm(FORM_LOGIN)
            .setInputValue(USERNAME_INPUT, username)
            .setInputValue(PASSWORD_INPUT, PASSWORD)
            .clickSubmitButton();

        assertEquals("/login", redirectErrorLogin.getBaseURL().getPath());
        assertEquals("error", redirectErrorLogin.getBaseURL().getQuery());

        var messageError = GetElementDom.start(redirectErrorLogin)
            .navigation("div", ATTRIBUTE_CLASS, "errors-container")
            .getDiv(ATTRIBUTE_CLASS, "alert alert-danger text-center")
            .getElement()
            .getVisibleText();

        assertEquals(expectedMsg, messageError);
    }

    @Override
    protected WebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }

    private static Stream<Arguments> providerAccountsFail() {
        return Stream.of(
            Arguments.of("beltrano_expired_account@user.com",
                "Conta expirada por favor contate o administrador do sistema."),
            Arguments.of("beltrano_locked_account@user.com",
                "Conta bloqueada por favor contate o administrador do sistema."),
            Arguments.of("beltrano_expired_credentials@user.com",
                "Credenciais expiradas, verifique no e-mail cadastrado a resolução."),
            Arguments.of("beltrano_disable_account@user.com",
                "Usuário desativado por favor contate o administrador do sistema.")
        );
    }
}
