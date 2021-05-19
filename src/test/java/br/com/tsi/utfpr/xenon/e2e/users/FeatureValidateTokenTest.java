package br.com.tsi.utfpr.xenon.e2e.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import br.com.tsi.utfpr.xenon.domain.user.aggregator.Token;
import br.com.tsi.utfpr.xenon.domain.user.aggregator.TokenAdapter;
import br.com.tsi.utfpr.xenon.e2e.AbstractEndToEndTest;
import br.com.tsi.utfpr.xenon.e2e.TestRedisConfiguration;
import br.com.tsi.utfpr.xenon.e2e.utils.InsertFormDom;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlHeading3;
import java.io.IOException;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Test - e2e - Funcionalidade estudante validando token")
@SpringBootTest(classes = TestRedisConfiguration.class)
public class FeatureValidateTokenTest extends AbstractEndToEndTest {

    private static final String TOKEN_VALIDATE = "token-validate";
    private static final String ATTRIBUTE_CLASS = "class";
    private static final String ATTRIBUTE_ID = "id";
    private static final String DIV = "div";
    private static final String EXPECTED_MESSAGE_TOKEN_ERROR_INVALID =
        "token com formato inválido verifique e tente novamente";
    private static final String EXPECTED_MESSAGE_TOKEN_ERROR_EMPTY = "O campo deve ser preenchido";
    private static final String URL =
        "http://localhost:8080/validar-token?email=test%40alunos.utfpr.edu.br";
    public static final String TOKEN = "token";
    public static final String BODY = "body";
    public static final String BODY_PAGE_ERROR_ENV = "page-body page-error-env";
    public static final String ERROR_CENTERED = "page-error centered";
    public static final String EMAIL = "test@alunos.utfpr.edu.br";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TokenAdapter tokenAdapter;

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "token fora do formato"})
    @DisplayName("Deve retornar error quando token não foi preenchido")
    void shouldReturnErrorWhenTokenIsInvalid(String token) throws IOException {
        webClient.getOptions().setJavaScriptEnabled(false);

        InsertFormDom.init(webClient, URL)
            .insertForm(TOKEN_VALIDATE)
            .setInputValue(TOKEN, token)
            .clickButton()
            .navigate(DIV, ATTRIBUTE_CLASS, "errors-container")
            .getDiv(ATTRIBUTE_CLASS, "alert alert-danger text-center")
            .getSpan(ATTRIBUTE_ID, "message_error")
            .executeAssertion(span ->
                assertEquals(
                    "Token inválido Verifique no seu e-mail o token e tente novamente ou clique em reenviar token",
                    span.getVisibleText()
                )
            );

        webClient.getOptions().setJavaScriptEnabled(true);
    }

    @ParameterizedTest
    @MethodSource("providerArgumentsEmailInvalid")
    @DisplayName("Deve retornar indicar erro na tela quando token está inválido")
    void shouldShowLabelError(String token, String msg) throws IOException {
        InsertFormDom.init(webClient, URL)
            .insertForm(TOKEN_VALIDATE)
            .setInputValue(TOKEN, token)
            .clickButton()
            .navigate(DIV, ATTRIBUTE_CLASS, "form-group validate-has-error")
            .getSpan(ATTRIBUTE_ID, "input_token-error")
            .executeAssertion(span -> assertEquals(msg, span.getVisibleText()));
    }

    @Test
    @DisplayName("Deve redirecionar para tela de erro quando token não corresponde")
    void shouldHaveRedirectErrorPage() throws IOException {
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        var token = new Token(EMAIL);
        token.generateNewToken();
        tokenAdapter.saveToken(token, 5);

        var page = InsertFormDom.init(webClient, URL)
            .insertForm(TOKEN_VALIDATE)
            .setInputValue(TOKEN, UUID.randomUUID().toString())
            .clickButton()
            .navigate(BODY, ATTRIBUTE_CLASS, BODY_PAGE_ERROR_ENV)
            .getDiv(ATTRIBUTE_CLASS, ERROR_CENTERED)
            .executeAssertion(div -> {
                var h2 = div.<HtmlHeading2>getFirstByXPath("//h2");
                assertEquals("Error\nCadastro não pode ser efetuado!", h2.getVisibleText());
            }).getHtmlPage();

        var response = page.getWebResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
    }

    @Test
    @DisplayName("Deve redirecionar para tela de erro quando token não expirado")
    void shouldHaveRedirectErrorPageWhenTokenExpire() throws IOException {
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        var page = InsertFormDom.init(webClient, URL)
            .insertForm(TOKEN_VALIDATE)
            .setInputValue(TOKEN, UUID.randomUUID().toString())
            .clickButton()
            .navigate(BODY, ATTRIBUTE_CLASS, BODY_PAGE_ERROR_ENV)
            .getDiv(ATTRIBUTE_CLASS, ERROR_CENTERED)
            .executeAssertion(div -> {
                var h2 = div.<HtmlHeading2>getFirstByXPath("//h2");
                assertEquals("Error\nCadastro não pode ser efetuado!", h2.getVisibleText());
            }).getHtmlPage();

        var response = page.getWebResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
    }

    @Test
    @DisplayName("Deve validar o token com sucesso")
    void shouldValidateToken() throws IOException {
        var token = new Token(EMAIL);
        token.generateNewToken();
        tokenAdapter.saveToken(token, 5);

        InsertFormDom.init(webClient, URL)
            .insertForm(TOKEN_VALIDATE)
            .setInputValue(TOKEN, token.getToken())
            .clickButton()
            .navigate(DIV, ATTRIBUTE_CLASS, "panel-form-registry")
            .getDiv(ATTRIBUTE_CLASS, "panel panel-flat")
            .executeAssertion(div -> {
                var h3 = div.<HtmlHeading3>getFirstByXPath("//h3");
                assertEquals("Estamos quase acabando...", h3.getVisibleText());
            });
    }

    @Test
    @DisplayName("Deve enviar pedido de novo token")
    void shouldSendNewTokenRequest() throws IOException {
        var token = new Token(EMAIL);
        token.generateNewToken();

        var oldToken = token.getToken();
        tokenAdapter.saveToken(token, 5);

        InsertFormDom.init(webClient, URL)
            .insertForm("renew-token")
            .clickButton();

        var newToken = tokenAdapter.getToken(EMAIL).get();

        assertNotEquals(oldToken, newToken);
    }

    @Override
    protected WebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }

    private static Stream<Arguments> providerArgumentsEmailInvalid() {
        return Stream.of(
            Arguments.of("", EXPECTED_MESSAGE_TOKEN_ERROR_EMPTY),
            Arguments.of(" ", EXPECTED_MESSAGE_TOKEN_ERROR_EMPTY),
            Arguments.of("token not patter", EXPECTED_MESSAGE_TOKEN_ERROR_INVALID)
        );
    }
}
