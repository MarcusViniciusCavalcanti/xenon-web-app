package br.com.tsi.utfpr.xenon.e2e.users;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.tsi.utfpr.xenon.e2e.AbstractEndToEndTest;
import br.com.tsi.utfpr.xenon.e2e.utils.GetElementDom;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Test - e2e - Funcionalidade todos Usuários")
public class FeatureAllUserTest extends AbstractEndToEndTest {

    public static final String DESCRIPTION_ADMIN = "Perfil Administrador";
    public static final String ROLE_DRIVER = "ROLE_DRIVER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_OPERATOR = "ROLE_OPERATOR";
    public static final String DESCRIPTION_DRIVER = "Perfil Motorista";
    public static final String DESCRIPTION_OPERATOR = "Perfil Operador";
    private static final String URL_USUARIOS_TODOS = "http://localhost:8080/usuarios/todos";
    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_CLASS = "class";
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    @DisplayName("Deve Redirecionar para Pagina de Não autorizado quando usuário operador")
    @WithUserDetails(value = "beltrano_operator@operator.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldRedirectToAccessDeniedWhenUserRolesOperator() throws IOException {
        assertUnauthorized();
    }

    @Test
    @DisplayName("Deve Redirecionar para Pagina de Não autorizado quando usuário motorista")
    @WithUserDetails(value = "beltrano_operator@operator.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldRedirectToAccessDeniedWhenUserRoleDriver() throws IOException {
        assertUnauthorized();
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
            .navigation("ul", ATTRIBUTE_ID, "main-menu")
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
            containsInAnyOrder(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        mockMvc.perform(get("/users/all").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("content", hasSize(3)))
            .andExpect(jsonPath("totalElements", is(3)))
            .andExpect(jsonPath("totalPages", is(1)))
            .andExpect(jsonPath("content[*].id", containsInAnyOrder(1, 154, 153)))
            .andExpect(jsonPath("content[*].name",
                containsInAnyOrder("Beltrano", "Beltrano Admin", "Beltrano Operador")))
            .andExpect(jsonPath("content[*].numberAccess",
                containsInAnyOrder(1693228998, 144412489, 99101783)))
            .andExpect(jsonPath("content[*].typeUser",
                containsInAnyOrder("STUDENTS", "SERVICE", "SERVICE")))
            .andExpect(jsonPath("content[*].authorisedAcces", matcherAllTrue))
            .andExpect(jsonPath("content[*].accessCard.accountNonExpired", matcherAllTrue))
            .andExpect(jsonPath("content[*].accessCard.accountNonLocked", matcherAllTrue))
            .andExpect(jsonPath("content[*].accessCard.credentialsNonExpired", matcherAllTrue))
            .andExpect(jsonPath("content[*].accessCard.enabled", matcherAllTrue))
            .andExpect(jsonPath("content[*].accessCard.username",
                containsInAnyOrder("beltrano_user@udrt.com", "beltrano_admin@admin.com",
                    "beltrano_operator@operator.com")))
            .andExpect(
                jsonPath("content[*].accessCard.roles[*].name",
                    containsInAnyOrder(ROLE_DRIVER, ROLE_DRIVER, ROLE_ADMIN, ROLE_OPERATOR,
                        ROLE_DRIVER,
                        ROLE_OPERATOR)))
            .andExpect(
                jsonPath("content[*].accessCard.roles[*].id", containsInAnyOrder(1, 1, 2, 3, 1, 3)))
            .andExpect(
                jsonPath("content[*].accessCard.roles[*].description",
                    containsInAnyOrder(DESCRIPTION_DRIVER, DESCRIPTION_DRIVER, DESCRIPTION_ADMIN,
                        DESCRIPTION_OPERATOR,
                        DESCRIPTION_DRIVER,
                        DESCRIPTION_OPERATOR)));
    }

    @Override
    protected WebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }

    private void assertUnauthorized() throws IOException {
        var unauthorizedPage = GetElementDom.start(webClient, URL_USUARIOS_TODOS).getHtmlPage();
        assertEquals("Xenon - 403", unauthorizedPage.getTitleText());
    }
}
