package br.com.tsi.utfpr.xenon.e2e.users;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import br.com.tsi.utfpr.xenon.domain.user.repository.UserRepository;
import br.com.tsi.utfpr.xenon.e2e.AbstractEndToEndTest;
import br.com.tsi.utfpr.xenon.e2e.utils.InsertFormDom;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import com.gargoylesoftware.htmlunit.html.DomNode;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Test - e2e - Funcionalidade atualizar Usuários")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
    "classpath:/sql/user_default_insert.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
    "classpath:/sql/user_default_delete.sql"})
class FeatureUpdateUserTest extends AbstractEndToEndTest {

    private static final String URL_UPDATE_USER_PATTERN =
        "http://localhost:8080/usuario/atualizar/%d";
    private static final String MESSAGE_ERROR = "message_error";
    private static final String MODEL_CAR = "model car";
    private static final List<Long> AUTHORIZED_ADMIN = List.of(1L, 2L, 3L);
    private static final List<Long> AUTHORIZED_DRIVE = List.of(1L);
    private static final String FORM_USER = "form_update_user";
    private static final String DIV = "div";
    private static final String ATTRIBUTE_CLASS = "class";
    private static final String ATTRIBUTE_ID = "id";
    private static final String CAR_PLATE = "abc1234";
    private static final String TABLE = "table";
    private static final String UL = "ul";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Override
    protected WebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }

    @Test
    @DisplayName("Deve Redirecionar para Pagina de Não autorizado quando usuário operador")
    @WithUserDetails(value = "beltrano_operator@operator.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldRedirectToAccessDeniedWhenUserRolesOperator() throws IOException {
        var user = userRepository.findById(153L).get();
        assertUnauthorized(String.format(URL_UPDATE_USER_PATTERN, user.getId()));
    }

    @Test
    @DisplayName("Deve Redirecionar para Pagina de Não autorizado quando usuário motorista")
    @WithUserDetails(value = "beltrano_operator@operator.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldRedirectToAccessDeniedWhenUserRoleDriver() throws IOException {
        var user = userRepository.findById(1L).get();
        assertUnauthorized(String.format(URL_UPDATE_USER_PATTERN, user.getId()));
    }

    @ParameterizedTest
    @MethodSource("providerArgsAlertError")
    @DisplayName("Deve exibir alert com errors de validação de campos")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldShowAlertWhitErrorsForm(InputUserDto input, String msg) throws IOException {
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        var user = userRepository.findById(200L).get();
        var url = String.format(URL_UPDATE_USER_PATTERN, user.getId());
        InsertFormDom.init(webClient, url)
            .insertForm(FORM_USER)
            .setInputValue("name", input.getName())
            .setInputValue("carModel", input.getCarModel())
            .setInputValue("username", input.getUsername())
            .setInputValue("carPlate", input.getCarPlate())
            .setSelectMultiple("authorities", input.getAuthorities())
            .clickButton()
            .navigate(DIV, ATTRIBUTE_CLASS, "errors-container")
            .getDiv(ATTRIBUTE_CLASS, "alert alert-danger")
            .executeAssertion(div -> assertEquals(msg, div.getVisibleText()));
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
    }

    @Test
    @DisplayName("Deve exibir span com errors de validação de campos")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldShowSpanErrorValidation() throws IOException {
        var user = userRepository.findById(200L).get();
        var url = String.format(URL_UPDATE_USER_PATTERN, user.getId());
        var spans = InsertFormDom.init(webClient, url)
            .insertForm(FORM_USER)
            .setInputValue("name", "")
            .setInputValue("carModel", "")
            .setInputValue("username", "")
            .setInputValue("carPlate", "")
            .setSelectMultiple("authorities", Collections.emptyList())
            .clickButton()
            .navigate("form", ATTRIBUTE_ID, FORM_USER)
            .getListChildSpan(ATTRIBUTE_CLASS, "validate-has-error");

        var errors = spans.stream()
            .map(DomNode::getVisibleText)
            .collect(Collectors.joining(","));

        assertEquals(
            "O campo deve ser preenchido,O campo deve ser preenchido", errors);
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldUpdate() throws IOException {
        var user = userRepository.findById(200L).get();
        var lastUpdate = user.getUpdatedAt();
        var url = String.format(URL_UPDATE_USER_PATTERN, user.getId());
        InsertFormDom.init(webClient, url)
            .insertForm(FORM_USER)
            .setInputValue("name", "new Name")
            .setInputValue("carModel", "new Model Car")
            .setInputValue("carPlate", "abc7676")
            .clickButton()
            .navigate(TABLE, ATTRIBUTE_CLASS,
                "table table-small-font table-bordered table-striped")
            .getTbody(ATTRIBUTE_ID, "user_table_list");

        var userUpdated = userRepository.findById(200L).get();
        assertEquals("new Name", userUpdated.getName());
        assertEquals("new Model Car", userUpdated.getCar().getModel());
        assertEquals("abc7676", userUpdated.getCar().getPlate());
        assertNotEquals(lastUpdate, userUpdated.getUpdatedAt());
    }

    private static Stream<Arguments> providerArgsAlertError() {
        var inputNameEmpty = buildInput("", "user@user.com", AUTHORIZED_ADMIN);
        var inputUserNameEmpty = buildInput("User Name", "", AUTHORIZED_DRIVE);
        var inputAuthorizedEmpty = buildInput("User Name", "speaker@user.com", List.of(0L));
        var inputAllFieldsEmpty = buildInput("", "", List.of(0L));

        return Stream.of(
            Arguments.of(inputNameEmpty, "Nome - Campo contém error, verifique"),
            Arguments.of(inputUserNameEmpty, "E-mail - Campo contém error, verifique"),
            Arguments
                .of(inputAuthorizedEmpty, "Perfil do usuário- Campo contém error, verifique"),
            Arguments.of(inputAllFieldsEmpty,
                "Nome - Campo contém error, verifique\nE-mail - Campo contém error, verifique\nPerfil do usuário- Campo contém error, verifique")
        );
    }

    private static InputUserDto buildInput(String name, String username, List<Long> authorized) {
        var input = new InputUserDto();

        input.setCarModel(MODEL_CAR);
        input.setCarPlate(CAR_PLATE);
        input.setName(name);
        input.setUsername(username);
        input.setAuthorities(authorized);

        return input;
    }
}
