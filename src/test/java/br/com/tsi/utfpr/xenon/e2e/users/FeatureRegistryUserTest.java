package br.com.tsi.utfpr.xenon.e2e.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;

import br.com.tsi.utfpr.xenon.domain.user.aggregator.EmailSenderAdapter;
import br.com.tsi.utfpr.xenon.domain.user.entity.TypeUser;
import br.com.tsi.utfpr.xenon.e2e.AbstractEndToEndTest;
import br.com.tsi.utfpr.xenon.e2e.utils.GetElementDom;
import br.com.tsi.utfpr.xenon.e2e.utils.InsertFormDom;
import br.com.tsi.utfpr.xenon.structure.dtos.TypeUserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import com.gargoylesoftware.htmlunit.html.DomNode;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Test - e2e - Funcionalidade Criar Novo Usuários")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
    "classpath:/sql/user_default_insert.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
    "classpath:/sql/user_default_delete.sql"})
class FeatureRegistryUserTest extends AbstractEndToEndTest {

    private static final String MESSAGE_ERROR = "message_error";
    private static final String MODEL_CAR = "model car";
    private static final List<Long> AUTHORIZED_ADMIN = List.of(1L, 2L, 3L);
    private static final List<Long> AUTHORIZED_DRIVE = List.of(1L);
    private static final String FORM_NEW_USER = "form_new_user";
    private static final String URL_CREATE_NEW_USER = "http://localhost:8080/usuarios/novo";
    private static final String DIV = "div";
    private static final String ATTRIBUTE_CLASS = "class";
    private static final String ATTRIBUTE_ID = "id";
    private static final String CAR_PLATE = "abc1234";
    private static final String TABLE = "table";
    private static final String UL = "ul";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private EmailSenderAdapter emailSenderAdapter;

    @Override
    protected WebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }

    @Test
    @DisplayName("Deve Redirecionar para Pagina de Não autorizado quando usuário operador")
    @WithUserDetails(value = "beltrano_operator@operator.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldRedirectToAccessDeniedWhenUserRolesOperator() throws IOException {
        assertUnauthorized(URL_CREATE_NEW_USER);
    }

    @Test
    @DisplayName("Deve Redirecionar para Pagina de Não autorizado quando usuário motorista")
    @WithUserDetails(value = "beltrano_operator@operator.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldRedirectToAccessDeniedWhenUserRoleDriver() throws IOException {
        assertUnauthorized(URL_CREATE_NEW_USER);
    }

    @ParameterizedTest
    @MethodSource("providerArgsAlertError")
    @DisplayName("Deve exibir alert com errors de validação de campos")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldShowAlertWhitErrorsForm(InputUserDto input, String msg) throws IOException {
        webClient.getOptions().setJavaScriptEnabled(false);
        InsertFormDom.init(webClient, URL_CREATE_NEW_USER)
            .insertForm(FORM_NEW_USER)
            .setInputValue("name", input.getName())
            .setInputValue("username", input.getUsername())
            .setInputValue("carModel", input.getCarModel())
            .setInputValue("carPlate", input.getCarPlate())
            .setSelectMultiple("authorities", input.getAuthorities())
            .clickButton()
            .navigate(DIV, ATTRIBUTE_CLASS, "errors-container")
            .getDiv(ATTRIBUTE_CLASS, "alert alert-danger")
            .executeAssertion(div -> assertEquals(msg, div.getVisibleText()));
        webClient.getOptions().setJavaScriptEnabled(true);
    }

    @Test
    @DisplayName("Deve exibir span com errors de validação de campos")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldShowSpanErrorValidation() throws IOException {
        var spans = InsertFormDom.init(webClient, URL_CREATE_NEW_USER)
            .insertForm(FORM_NEW_USER)
            .clickButton()
            .navigate("form", ATTRIBUTE_ID, FORM_NEW_USER)
            .getListChildSpan(ATTRIBUTE_CLASS, "validate-has-error");

        var errors = spans.stream()
            .map(DomNode::getVisibleText)
            .collect(Collectors.joining(","));

        assertEquals(
            "O campo deve ser preenchido,O campo deve ser preenchido,O campo deve ser preenchido",
            errors);
    }

    @Test
    @DisplayName("Deve conter o titulo UTFPR - Xenon - Todos Usuários")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldHaveTitleAllUser() throws IOException {
        var pageAllUsers = GetElementDom.start(webClient, URL_CREATE_NEW_USER).getHtmlPage();
        var expectedTitle = getDefaultFormatTitle("Cadastrar Novo Usuários");
        assertEquals(expectedTitle, pageAllUsers.getTitleText());
    }

    @Test
    @DisplayName("Deve estar ativo menu item")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldHaveMenuItemActive() throws IOException {
        GetElementDom.start(webClient, URL_CREATE_NEW_USER)
            .navigation(UL, ATTRIBUTE_ID, "main-menu")
            .getChildLi(ATTRIBUTE_ID, "users")
            .executeAssertion(menu -> {
                var openedClass = menu.getAttribute(ATTRIBUTE_CLASS);
                assertTrue(openedClass.contains("opened"), "verificando classe opened");
            })
            .getChildLi(ATTRIBUTE_ID, "users_new")
            .executeAssertion(menu -> {
                var activeClass = menu.getAttribute(ATTRIBUTE_CLASS);
                assertTrue(activeClass.contains("active"), "verificando classe active");
            });
    }

    @ParameterizedTest
    @EnumSource(value = TypeUserDto.class, mode = Mode.EXCLUDE, names = {"SERVICE"})
    @DisplayName("Deve redirecionar para página de erro quando tipo de usuário não pode ser atribuido para admininstrador")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldRedirectToPageErrorWhenValidationRole(TypeUserDto type) throws IOException {
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        var input =
            buildInput("User Name", "speaker@user.com", TypeUser.SERVICE.getAllowedProfiles());
        input.setType(type);

        InsertFormDom.init(webClient, URL_CREATE_NEW_USER)
            .insertForm(FORM_NEW_USER)
            .setInputValue("name", input.getName())
            .setInputValue("username", input.getUsername())
            .setInputValue("carModel", input.getCarModel())
            .setInputValue("carPlate", input.getCarPlate())
            .setSelectMultiple("authorities", input.getAuthorities())
            .setSelect("type", type.ordinal())
            .clickButton()
            .navigate("body", ATTRIBUTE_CLASS, "page-body page-error-env")
            .getDiv(ATTRIBUTE_CLASS, "page-error centered")
            .getP(ATTRIBUTE_ID, MESSAGE_ERROR)
            .executeAssertion(p ->
                assertEquals(
                    String.format(
                        "O tipo de Usuário: [%s] não poderá ser atribuido o perfil Administrador",
                        type.getTranslaterName()),
                    p.getVisibleText()
                )
            );
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
    }

    @Test
    @DisplayName("Deve cadastrar usuário com sucesso")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldHaveRegistryUser() throws IOException {
        var input = buildInput("Name User", "user@user.com", AUTHORIZED_DRIVE);
        doNothing()
            .when(emailSenderAdapter)
            .sendMailPasswordToCompleteRegistry(anyString(), eq(input.getUsername()));

        InsertFormDom.init(webClient, URL_CREATE_NEW_USER)
            .insertForm(FORM_NEW_USER)
            .setInputValue("name", input.getName())
            .setInputValue("username", input.getUsername())
            .setInputValue("carModel", input.getCarModel())
            .setInputValue("carPlate", input.getCarPlate())
            .setSelectMultiple("authorities", input.getAuthorities())
            .clickButton()
            .navigate(TABLE, ATTRIBUTE_CLASS,
                "table table-small-font table-bordered table-striped")
            .getTbody(ATTRIBUTE_ID, "user_table_list")
            .executeAssertion(tbody -> {
                var tbRow =
                    GetElementDom.tbRowByIndex(tbody, ATTRIBUTE_CLASS, "user_item", 0);

                var userName = tbRow.getCells().get(1).getVisibleText();
                var login = tbRow.getCells().get(2).getVisibleText();
                var plateCar = tbRow.getCells().get(3).getVisibleText();
                var modelCar = tbRow.getCells().get(4).getVisibleText();

                assertEquals(input.getName(), userName);
                assertEquals(input.getUsername(), login);
                assertEquals(input.getCarPlate(), plateCar);
                assertEquals(input.getCarModel(), modelCar);
            });
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
