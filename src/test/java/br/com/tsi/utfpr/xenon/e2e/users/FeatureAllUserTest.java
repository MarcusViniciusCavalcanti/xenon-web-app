package br.com.tsi.utfpr.xenon.e2e.users;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.tsi.utfpr.xenon.e2e.AbstractEndToEndTest;
import br.com.tsi.utfpr.xenon.e2e.utils.GetElementDom;
import br.com.tsi.utfpr.xenon.e2e.utils.InsertFormDom;
import br.com.tsi.utfpr.xenon.e2e.utils.StreamUtils;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Test - e2e - Funcionalidade todos Usuários")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
    "classpath:/sql/user_default_insert.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
    "classpath:/sql/user_default_delete.sql"})
class FeatureAllUserTest extends AbstractEndToEndTest {

    public static final String DESCRIPTION_ADMIN = "Perfil Administrador";
    public static final String ROLE_DRIVER = "ROLE_DRIVER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_OPERATOR = "ROLE_OPERATOR";
    public static final String DESCRIPTION_DRIVER = "Perfil Motorista";
    public static final String DESCRIPTION_OPERATOR = "Perfil Operador";
    private static final String URL_USUARIOS_TODOS = "http://localhost:8080/usuarios/todos";
    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_CLASS = "class";
    private static final int ID_OPERATOR = 154;
    private static final int ID_ADMIN = 153;
    private static final int ID_USER_WITH_CAR = 200;
    private static final int ID_USER_WITHOUT_CAR = 201;
    private static final String BELTRANO_ADMIN = "Beltrano Admin";
    private static final String BELTRANO_OPERADOR = "Beltrano Operador";
    private static final String BELTRANO_COM_CARRO = "Beltrano com carro";
    private static final String BELTRANO_SEM_CARRO = "Beltrano sem carro";
    private static final String BELTRANO = "Beltrano";
    private static final int ID_STUDENT = 1;
    private static final String DIV = "div";
    private static final String TABLE = "table";
    private static final String UL = "ul";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    @DisplayName("Deve Redirecionar para Pagina de Não autorizado quando usuário operador")
    @WithUserDetails(value = "beltrano_operator@operator.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldRedirectToAccessDeniedWhenUserRolesOperator() throws IOException {
        assertUnauthorized(URL_USUARIOS_TODOS);
    }

    @Test
    @DisplayName("Deve Redirecionar para Pagina de Não autorizado quando usuário motorista")
    @WithUserDetails(value = "beltrano_operator@operator.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldRedirectToAccessDeniedWhenUserRoleDriver() throws IOException {
        assertUnauthorized(URL_USUARIOS_TODOS);
    }

    @Test
    @DisplayName("Deve conter o titulo UTFPR - Xenon - Todos Usuários")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldHaveTitleAllUser() throws IOException {
        var pageAllUsers = GetElementDom.start(webClient, URL_USUARIOS_TODOS).getHtmlPage();
        var expectedTitle = getDefaultFormatTitle("Todos Usuários");
        assertEquals(expectedTitle, pageAllUsers.getTitleText());
    }

    @Test
    @DisplayName("Deve estar ativo menu item")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldHaveMenuItemActive() throws IOException {
        GetElementDom.start(webClient, URL_USUARIOS_TODOS)
            .navigation(UL, ATTRIBUTE_ID, "main-menu")
            .getChildLi(ATTRIBUTE_ID, "users")
            .executeAssertion(menu -> {
                var openedClass = menu.getAttribute(ATTRIBUTE_CLASS);
                assertTrue(openedClass.contains("opened"), "verificando classe opened");
            })
            .getChildLi(ATTRIBUTE_ID, "users_all")
            .executeAssertion(menu -> {
                var activeClass = menu.getAttribute(ATTRIBUTE_CLASS);
                assertTrue(activeClass.contains("active"), "verificando classe active");
            });
    }

    @Test
    @DisplayName("Deve retonar page de usuários quando executado uma request ajax")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldReturnAllUser() throws Exception {
        var matcherAllTrue =
            containsInAnyOrder(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE);
        mockMvc.perform(get("/users/all").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("content", hasSize(5)))
            .andExpect(jsonPath("totalElements", is(5)))
            .andExpect(jsonPath("totalPages", is(1)))
            .andExpect(jsonPath("content[*].id",
                containsInAnyOrder(ID_STUDENT, ID_OPERATOR, ID_ADMIN, ID_USER_WITH_CAR,
                    ID_USER_WITHOUT_CAR)))
            .andExpect(jsonPath("content[*].name",
                containsInAnyOrder(BELTRANO, BELTRANO_ADMIN, BELTRANO_OPERADOR,
                    BELTRANO_COM_CARRO, BELTRANO_SEM_CARRO)))
            .andExpect(jsonPath("content[*].numberAccess",
                containsInAnyOrder(1693228998, 144412489, 99101783, 1693228998, 144412489)))
            .andExpect(jsonPath("content[*].typeUser",
                containsInAnyOrder("STUDENTS", "SERVICE", "SERVICE", "STUDENTS",
                    "STUDENTS")))
            .andExpect(jsonPath("content[*].authorisedAcces",
                containsInAnyOrder(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                    Boolean.FALSE)))
            .andExpect(jsonPath("content[*].accessCard.accountNonExpired", matcherAllTrue))
            .andExpect(jsonPath("content[*].accessCard.accountNonLocked", matcherAllTrue))
            .andExpect(jsonPath("content[*].accessCard.credentialsNonExpired", matcherAllTrue))
            .andExpect(jsonPath("content[*].accessCard.enabled", matcherAllTrue))
            .andExpect(jsonPath("content[*].accessCard.username",
                containsInAnyOrder("beltrano_user@alunos.utfpr.edu.br",
                    "beltrano_admin@admin.com",
                    "beltrano_operator@operator.com", "beltrano_with_car@user.com",
                    "beltrano_without_car@user.com")))
            .andExpect(
                jsonPath("content[*].accessCard.roles[*].name",
                    containsInAnyOrder(ROLE_DRIVER, ROLE_DRIVER, ROLE_ADMIN,
                        ROLE_OPERATOR,
                        ROLE_DRIVER,
                        ROLE_DRIVER,
                        ROLE_DRIVER,
                        ROLE_OPERATOR)))
            .andExpect(
                jsonPath("content[*].accessCard.roles[*].id",
                    containsInAnyOrder(1, 1, 2, 3, 1, 3, 1, 1)))
            .andExpect(
                jsonPath("content[*].accessCard.roles[*].description",
                    containsInAnyOrder(DESCRIPTION_DRIVER, DESCRIPTION_DRIVER,
                        DESCRIPTION_ADMIN,
                        DESCRIPTION_OPERATOR,
                        DESCRIPTION_DRIVER,
                        DESCRIPTION_DRIVER,
                        DESCRIPTION_DRIVER,
                        DESCRIPTION_OPERATOR)));
    }

    @Test
    @DisplayName("Deve retonar page de usuário com filtro de nome")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldReturnUserWithFilterName() throws Exception {
        mockMvc.perform(get("/users/all?name=admin").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("content", hasSize(1)))
            .andExpect(jsonPath("totalElements", is(1)))
            .andExpect(jsonPath("totalPages", is(1)))
            .andExpect(jsonPath("content[*].id", hasItem(ID_ADMIN)))
            .andExpect(jsonPath("content[*].name", hasItem(BELTRANO_ADMIN)));
    }

    @Test
    @DisplayName("Deve retonar page de usuário com filtro de tipo")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldReturnUserWithFilterType() throws Exception {
        mockMvc.perform(get("/users/all?type=SERVICE").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("content", hasSize(2)))
            .andExpect(jsonPath("totalElements", is(2)))
            .andExpect(jsonPath("totalPages", is(1)))
            .andExpect(jsonPath("content[*].id", containsInAnyOrder(ID_OPERATOR, ID_ADMIN)))
            .andExpect(jsonPath("content[*].name",
                containsInAnyOrder(BELTRANO_ADMIN, BELTRANO_OPERADOR)));
    }

    @Test
    @DisplayName("Deve retonar page de usuário com filtro de Perfil")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldReturnUserWithFilterProfile() throws Exception {
        mockMvc.perform(get("/users/all?profile=OPERATOR").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("content", hasSize(2)))
            .andExpect(jsonPath("totalElements", is(2)))
            .andExpect(jsonPath("totalPages", is(1)))
            .andExpect(jsonPath("content[*].id",
                containsInAnyOrder(ID_OPERATOR, ID_ADMIN)))
            .andExpect(jsonPath("content[*].name",
                containsInAnyOrder(BELTRANO_ADMIN, BELTRANO_OPERADOR)));
    }

    @Test
    @DisplayName("Deve conter na tabela de usuários dos usuários cadastrados")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldVerifyPageUsers() throws IOException {
        var domElements = GetElementDom.start(webClient, URL_USUARIOS_TODOS)
            .navigation(TABLE, ATTRIBUTE_CLASS,
                "table table-small-font table-bordered table-striped")
            .getTbody(ATTRIBUTE_ID, "user_table_list")
            .awaitPage(300)
            .getListElement(ATTRIBUTE_CLASS, "user_item");

        assertListContent(
            "first page",
            domElements,
            "Sem cadastro Beltrano beltrano_user@alunos.utfpr.edu.br Não informado Não informado Estudante Não Sim Sim \nAtualizar\nDesativar",
            "Sem cadastro Beltrano Admin beltrano_admin@admin.com Não informado Não informado Servidor Não Sim Sim \nAtualizar\nDesativar",
            "Sem cadastro Beltrano Operador beltrano_operator@operator.com Não informado Não informado Servidor Não Sim Sim \nAtualizar\nDesativar",
            "Confirmar? Beltrano com carro beltrano_with_car@user.com ABC9801 Gol Estudante Não Sim Sim \nAtualizar\nDesativar",
            "Sem cadastro Beltrano sem carro beltrano_without_car@user.com Não informado Não informado Estudante Não Sim Não \nAtualizar\nDesativar"
        );
    }

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
        "classpath:/sql/user_block_insert.sql",
        "classpath:/sql/user_default_insert.sql"
    })
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
        "classpath:/sql/user_block_delete.sql",
        "classpath:/sql/user_default_delete.sql"
    })
    @Test
    @DisplayName("Deve navegar pelas paginas exibidas")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldNavigatePage() throws IOException {
        GetElementDom.start(webClient, URL_USUARIOS_TODOS)
            .navigation(DIV, ATTRIBUTE_ID, "users_list")
            .awaitPage(300)
            .getChildUl(ATTRIBUTE_CLASS, "pagination")
            .executeAssertion(ul -> {
                var liList = StreamUtils.asStream(ul.getChildElements().iterator())
                    .collect(Collectors.toList());

                try {
                    var nextPage = liList.get(1).getFirstElementChild().<HtmlPage>click();
                    nextPage.getWebClient().waitForBackgroundJavaScript(300);

                    var domElements = GetElementDom.start(nextPage)
                        .awaitPage(300)
                        .navigation(TABLE, ATTRIBUTE_CLASS,
                            "table table-small-font table-bordered table-striped")
                        .getTbody(ATTRIBUTE_ID, "user_table_list")
                        .getListElement(ATTRIBUTE_CLASS, "user_item");

                    assertListContent("first page", domElements,
                        "Sem cadastro Beltrano Operador beltrano_operator@operator.com Não informado Não informado Servidor Não Sim Sim \nAtualizar\nDesativar",
                        "Sem cadastro Beltrano Admin beltrano_admin@admin.com Não informado Não informado Servidor Não Sim Sim \nAtualizar\nDesativar",
                        "Sem cadastro Beltrano beltrano_user@alunos.utfpr.edu.br Não informado Não informado Estudante Não Sim Sim \nAtualizar\nDesativar");

                    nextPage = liList.get(0).getFirstElementChild().click();
                    nextPage.getWebClient().waitForBackgroundJavaScript(300);

                    domElements = GetElementDom.start(nextPage)
                        .awaitPage(300)
                        .navigation(TABLE, ATTRIBUTE_CLASS,
                            "table table-small-font table-bordered table-striped")
                        .getTbody(ATTRIBUTE_ID, "user_table_list")
                        .getListElement(ATTRIBUTE_CLASS, "user_item");

                    assertListContent("second page", domElements,
                        "Sem cadastro Beltrano sem carro beltrano_without_car@user.com Não informado Não informado Estudante Não Sim Não \nAtualizar\nDesativar",
                        "Confirmar? Beltrano com carro beltrano_with_car@user.com ABC9801 Gol Estudante Não Sim Sim \nAtualizar\nDesativar",
                        "Sem cadastro Beltrano credenciais expiradas beltrano_expired_credentials@user.com Não informado Não informado Estudante Não Sim Sim \nAtualizar\nDesativar",
                        "Sem cadastro Beltrano conta exipirada beltrano_expired_account@user.com Não informado Não informado Estudante Não Sim Sim \nAtualizar\nDesativar",
                        "Sem cadastro Beltrano conta bloqueada beltrano_locked_account@user.com Não informado Não informado Estudante Sim Sim Sim \nAtualizar\nDesativar");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
        "classpath:/sql/user_block_insert.sql",
        "classpath:/sql/user_default_insert.sql"
    })
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
        "classpath:/sql/user_block_delete.sql",
        "classpath:/sql/user_default_delete.sql"
    })
    @Test
    @DisplayName("Deve aumentar a quantidade de elementos exibidos na tabela")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldShowAmountElement() throws IOException {
        GetElementDom.start(webClient, URL_USUARIOS_TODOS)
            .navigation(DIV, ATTRIBUTE_ID, "users_list")
            .awaitPage(300)
            .getSelect(ATTRIBUTE_ID, "size_element")
            .executeAssertion(select -> {
                assertionElementsShow(select, 1, 8);
                assertionElementsShow(select, 0, 5);
            });
    }

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
        "classpath:/sql/user_block_insert.sql",
        "classpath:/sql/user_default_insert.sql"
    })
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
        "classpath:/sql/user_block_delete.sql",
        "classpath:/sql/user_default_delete.sql"
    })
    @Test
    @DisplayName("Deve exibir os usuários com base no filtro")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldReturnUserByFilter() throws IOException {
        InsertFormDom.init(webClient, URL_USUARIOS_TODOS)
            .insertForm("filter")
            .setInputValue("search_name", "expirada")
            .setSelect("search_profile", 1)
            .setSelect("search_type_user", 1)
            .clickButtonType()
            .navigate(DIV, ATTRIBUTE_ID, "users_list")
            .awaitPage(300L)
            .getTbody(ATTRIBUTE_ID, "user_table_list")
            .executeAssertion(table -> {
                List<DomElement> elem = StreamUtils.asStream(table.getChildElements().iterator())
                    .collect(Collectors.toList());

                assertEquals(1, elem.size());
            });
    }

    private void assertionElementsShow(HtmlElement select, int index, int expectedSize) {
        ((HtmlSelect) select).setSelectedIndex(index);

        var page = select.getHtmlPageOrNull();

        var domElements = GetElementDom.start(page)
            .awaitPage(300)
            .navigation(TABLE, ATTRIBUTE_CLASS,
                "table table-small-font table-bordered table-striped")
            .getTbody(ATTRIBUTE_ID, "user_table_list")
            .getListElement(ATTRIBUTE_CLASS, "user_item");

        assertEquals(expectedSize, domElements.size());
    }

    private void assertListContent(String reason, List<DomElement> domElements, String... content) {
        var listTextVisible = domElements.stream()
            .map(DomNode::getVisibleText)
            .collect(Collectors.toList());

        assertThat(reason, listTextVisible, containsInAnyOrder(content));
    }

    @Override
    protected WebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }
}
