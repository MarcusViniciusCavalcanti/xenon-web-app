package br.com.tsi.utfpr.xenon.e2e.users;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.tsi.utfpr.xenon.domain.security.repository.AccessCardRepository;
import br.com.tsi.utfpr.xenon.domain.user.aggregator.Token;
import br.com.tsi.utfpr.xenon.domain.user.aggregator.TokenAdapter;
import br.com.tsi.utfpr.xenon.e2e.AbstractEndToEndTest;
import br.com.tsi.utfpr.xenon.e2e.TestRedisConfiguration;
import br.com.tsi.utfpr.xenon.e2e.utils.InsertFormDom;
import com.gargoylesoftware.htmlunit.html.DomNode;
import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Test - e2e - Funcionalidade estudante completar seu registro")
@SpringBootTest(classes = TestRedisConfiguration.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
    "classpath:/sql/user_default_insert.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
    "classpath:/sql/user_default_delete.sql"})
class FeatureCompleteNewRegistryStudentsTest extends AbstractEndToEndTest {

    private static final String CADASTRO_ESTUDANTE = "http://localhost:8080/cadastro-estudante";
    private static final String REGISTRY_USER = "registry-user";
    private static final String DIV = "div";
    private static final String ATTRIBUTE_CLASS = "class";
    private static final String ATTRIBUTE_ID = "id";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccessCardRepository accessCardRepository;

    @Autowired
    private TokenAdapter tokenAdapter;

    @Override
    protected WebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }

    @Test
    @DisplayName("Deve exibir alert com errors de validação de campos")
    void shouldShowAlertErrorValidation() throws IOException {
        webClient.getOptions().setJavaScriptEnabled(false);
        InsertFormDom.init(webClient, CADASTRO_ESTUDANTE)
                .insertForm(REGISTRY_USER)
                .clickButton()
                .navigate(DIV, ATTRIBUTE_CLASS, "errors-container")
                .getDiv(ATTRIBUTE_CLASS, "alert alert-danger")
                .executeAssertion(div -> assertEquals("Nome - Campo contém error verifique\n" +
                                "Modelo do carro - Campo contém error verifique\n" +
                                "Placa do carro - Campo contém error verifique\n" +
                                "E-mail - E-mail deve ser o institucional\n" +
                                "Senha - Campo contém error verifique",
                        div.getVisibleText()));
        webClient.getOptions().setJavaScriptEnabled(true);
    }

    @Test
    @DisplayName("Deve exibir span com errors de validação de campos")
    void shouldShowSpanErrorValidation() throws IOException {
        var spans = InsertFormDom.init(webClient, CADASTRO_ESTUDANTE)
            .insertForm(REGISTRY_USER)
            .clickButton()
            .navigate("form", ATTRIBUTE_ID, "registry_students")
            .getListChildSpan(ATTRIBUTE_CLASS, "validate-has-error");

        var errors = spans.stream()
            .map(DomNode::getVisibleText)
            .collect(Collectors.joining(","));

        assertEquals(
            "Campo obrigatório,Campo obrigatório,Campo obrigatório,Campo inválido,Campo obrigatório,Campo obrigatório",
            errors);
    }

    @Test
    @DisplayName("Deve cadastrar com sucesso")
    void shouldRegistrySuccessFully() throws IOException {
        var email = "alunos@alunos.utfpr.edu.br";
        var name = "Alunos Test";
        var modelCar = "Model Car";
        var plate = "ABC1234";
        var uuid = UUID.nameUUIDFromBytes(email.getBytes(UTF_8));
        var key = String.format("%s - %s", email, uuid);

        var token = new Token(key);
        token.generateNewToken();
        tokenAdapter.saveToken(token, 5L);
        var url = CADASTRO_ESTUDANTE + "?email=alunos%40alunos.utfpr.edu.br&token=" + token
            .getToken();

        InsertFormDom.init(webClient, url)
            .insertForm(REGISTRY_USER)
            .setInputValue("name", name)
            .setInputValue("modelCar", modelCar)
            .setInputValue("plateCar", plate)
            .setInputValue("password", "1234567")
            .setInputValue("confirmPassword", "1234567")
            .clickButton();

        var accessCard = accessCardRepository.findByUsername("alunos@alunos.utfpr.edu.br").get();

        assertEquals(name, accessCard.getUser().getName());
        assertEquals(modelCar, accessCard.getUser().getCar().getModel());
        assertEquals(plate, accessCard.getUser().getCar().getPlate());
    }
}
