package br.com.tsi.utfpr.xenon.e2e;

import static org.junit.jupiter.api.Assertions.*;

import br.com.tsi.utfpr.xenon.e2e.utils.GetElementDom;
import com.gargoylesoftware.htmlunit.html.DomNode;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Test - e2e - Exibição de menus baseados nos papeis")
public class SideBarViewMenusByRolesTest extends AbstractEndToEndTest {

    private static final String HTTP_LOCALHOST_8080_HOME = "http://localhost:8080/home";
    private static final String DASHBOARD = "Dashboard";
    private static final String ACCESS = "Acessos";
    private static final String RECOGNIZER = "Reconhecimentos";
    private static final String USER = "Usuários";
    private static final String ALL_USERS = "Todos usuários";
    private static final String NEW_USER = "Novo usuário";
    private static final String SETTINGS = "Configurações";
    private static final String ADJUSTMENT = "Ajuste fino";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    @DisplayName("Deve exibir menus para administrador")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldHaveViewMenusAdmin() throws IOException {
        var listAdmin = Arrays.asList(
            DASHBOARD,
            ACCESS,
            RECOGNIZER,
            USER,
            ALL_USERS,
            NEW_USER,
            SETTINGS,
            ADJUSTMENT
        );

        var listItemsVisible = getMenusItems();

        Collections.sort(listAdmin);
        Collections.sort(listItemsVisible);

        assertEquals(listAdmin, listItemsVisible);
    }

    @Test
    @DisplayName("Deve exibir menus para Operador")
    @WithUserDetails(value = "beltrano_operator@operator.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldHaveViewMenusOperator() throws IOException {
        var listOperator = Arrays.asList(
            DASHBOARD,
            ACCESS,
            RECOGNIZER,
            USER,
            ALL_USERS
        );

        var listItemsVisible = getMenusItems();

        Collections.sort(listOperator);
        Collections.sort(listItemsVisible);

        assertEquals(listOperator, listItemsVisible);
    }

    @Test
    @DisplayName("Deve exibir menus para Motorista")
    @WithUserDetails(value = "beltrano_user@udrt.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldHaveViewMenusDrive() throws IOException {
        var listOperator = List.of(DASHBOARD);

        var listItemsVisible = getMenusItems();

        Collections.sort(listItemsVisible);

        assertEquals(listOperator, listItemsVisible);
    }

    @Override
    WebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }

    private List<String> getMenusItems() throws IOException {
        return GetElementDom.start(webClient, HTTP_LOCALHOST_8080_HOME)
            .navigation("ul", "id", "main-menu")
            .getListChildSpan("class", "title")
            .stream()
            .map(DomNode::getTextContent)
            .collect(Collectors.toList());
    }
}
