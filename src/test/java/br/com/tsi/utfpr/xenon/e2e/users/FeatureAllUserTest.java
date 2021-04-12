package br.com.tsi.utfpr.xenon.e2e.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.tsi.utfpr.xenon.e2e.AbstractEndToEndTest;
import br.com.tsi.utfpr.xenon.e2e.utils.GetElementDom;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Test - e2e - Funcionalidade todos Usuários")
public class FeatureAllUserTest extends AbstractEndToEndTest {

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

    @Override
    protected WebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }

    private void assertUnauthorized() throws IOException {
        var unauthorizedPage = GetElementDom.start(webClient, URL_USUARIOS_TODOS).getHtmlPage();
        assertEquals("Xenon - 403", unauthorizedPage.getTitleText());
    }
}
