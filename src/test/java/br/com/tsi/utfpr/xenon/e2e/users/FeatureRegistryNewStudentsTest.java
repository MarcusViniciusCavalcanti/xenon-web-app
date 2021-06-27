package br.com.tsi.utfpr.xenon.e2e.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import br.com.tsi.utfpr.xenon.e2e.AbstractEndToEndTest;
import br.com.tsi.utfpr.xenon.e2e.TestRedisConfiguration;
import br.com.tsi.utfpr.xenon.e2e.utils.InsertFormDom;
import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Test - e2e - Funcionalidade estudante se registrando")
@SpringBootTest(classes = TestRedisConfiguration.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
    "classpath:/sql/user_default_insert.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
    "classpath:/sql/user_default_delete.sql"})
class FeatureRegistryNewStudentsTest extends AbstractEndToEndTest {

    public static final String MESSAGE_ERROR = "message_error";
    private static final String EXPECTED_MESSAGE_EMAIL_ERROR_INVALID = "Utilize o e-mail institucional, ex: ...@alunos.utfpr.edu.br";
    private static final String EXPECTED_MESSAGE_EMAIL_ERROR_EMPTY = "O campo deve ser preenchido";
    private static final String ATTRIBUTE_CLASS = "class";
    private static final String NOVO_REGISTRO = "http://localhost:8080/novo-registro";
    private static final String EMAIL_REGISTRY = "email-registry";
    private static final String INPUT_EMAIL = "email";
    private static final String ATTRIBUTE_ID = "id";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static Stream<Arguments> providerArgumentsEmailInvalid() {
        return Stream.of(
                Arguments.of("email@email.com", EXPECTED_MESSAGE_EMAIL_ERROR_INVALID),
                Arguments.of("@alunos.utfpr.edu.br", EXPECTED_MESSAGE_EMAIL_ERROR_INVALID),
                Arguments.of("", EXPECTED_MESSAGE_EMAIL_ERROR_EMPTY)
        );
    }

    @ParameterizedTest
    @MethodSource("providerArgumentsEmailInvalid")
    @DisplayName("Deve retornar indicar erro na tela quando e-mail está inválido")
    void shouldShowErrorInputEmailWhenInvalid(String value, String msg) throws IOException {
        InsertFormDom.init(webClient, NOVO_REGISTRO)
                .insertForm(EMAIL_REGISTRY)
                .setInputValue(INPUT_EMAIL, value)
                .clickButton()
                .navigateForm()
                .getDiv(ATTRIBUTE_CLASS, "form-group validate-has-error")
                .getSpan(ATTRIBUTE_ID, "input_email-error")
                .executeAssertion(span ->
                        assertEquals(msg, span.getVisibleText())
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "email_nao_institucional@email.com"})
    @DisplayName("Deve retornar error quando email é inválido")
    void shouldReturnErrorWhenTokenIsInvalid(String email) throws IOException {
        webClient.getOptions().setJavaScriptEnabled(false);
        InsertFormDom.init(webClient, NOVO_REGISTRO)
                .insertForm(EMAIL_REGISTRY)
                .setInputValue("email", email)
                .clickButton()
                .navigate("div", ATTRIBUTE_CLASS, "errors-container")
                .getDiv(ATTRIBUTE_CLASS, "alert alert-danger text-center")
                .getSpan(ATTRIBUTE_ID, MESSAGE_ERROR)
                .executeAssertion(span ->
                        assertEquals(
                                "E-mail inválido Revise as informações e tente novamente!",
                                span.getVisibleText()
                        )
                );

        webClient.getOptions().setJavaScriptEnabled(true);
    }

    @Test
    @DisplayName("Deve exibir mensagem com alert de erro que e-mail já é cadastrado")
    void shouldShowErrorInputEmailWhenExist() throws IOException {
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        var email = "beltrano_user@alunos.utfpr.edu.br";
        InsertFormDom.init(webClient, NOVO_REGISTRO)
                .insertForm(EMAIL_REGISTRY)
                .setInputValue(INPUT_EMAIL, email)
                .clickButton()
                .navigate("body", ATTRIBUTE_CLASS, "page-body page-error-env")
                .getDiv(ATTRIBUTE_CLASS, "page-error centered")
                .getP(ATTRIBUTE_ID, MESSAGE_ERROR)
                .executeAssertion(p ->
                        assertEquals(
                                String.format("Usuário %s não pode ser cadastrado porque: Usuário já cadastrado", email),
                                p.getVisibleText()
                        )
                );
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
    }

    @Test
    @DisplayName("Deve criar token com sucesso")
    void shouldHaveCreateToken() throws IOException {
        var email = "testes@alunos.utfpr.edu.br";
        InsertFormDom.init(webClient, NOVO_REGISTRO)
                .insertForm(EMAIL_REGISTRY)
                .setInputValue(INPUT_EMAIL, email)
                .clickButton();

        var token = redisTemplate.opsForValue().get(email);
        assertNotNull(token);
    }

    @Override
    protected WebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }
}
