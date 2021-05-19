package br.com.tsi.utfpr.xenon.e2e.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.tsi.utfpr.xenon.e2e.AbstractEndToEndTest;
import br.com.tsi.utfpr.xenon.e2e.utils.GetElementDom;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Test - e2e - Exibição dropdown menu do navbar baseados nos papeis")
public class NavBarViewItemsByRolesTest extends AbstractEndToEndTest {

    private static final String HTTP_LOCALHOST_8080_HOME = "http://localhost:8080/home";
    private static final String SETTINGS = "Configurações";
    private static final String PROFILE = "Perfil";
    private static final String LOGOUT = "Logout";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    @DisplayName("Deve exibir menu configuraçoes para administrador")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldHaveViewDropdownAdmin() throws IOException {
        assertionBaseDropdown(Arrays.asList(SETTINGS, PROFILE, LOGOUT));
    }

    @Test
    @DisplayName("Deve exibir menu configuraçoes para Operador")
    @WithUserDetails(value = "beltrano_operator@operator.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldHaveViewDropdownOperator() throws IOException {
        assertionBaseDropdown(Arrays.asList(PROFILE, LOGOUT));
    }

    @Test
    @DisplayName("Deve exibir menu configuraçoes para Motorista")
    @WithUserDetails(value = "beltrano_user@alunos.utfpr.edu.br", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldHaveViewDropdownDrive() throws IOException {
        assertionBaseDropdown(Arrays.asList(PROFILE, LOGOUT));
    }

    @Override
    protected WebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }

    private void assertionBaseDropdown(List<String> strings) throws IOException {
        var listDropdownVisible = getMenusItems();

        Collections.sort(strings);
        Collections.sort(listDropdownVisible);

        assertEquals(strings, listDropdownVisible);
    }

    private List<String> getMenusItems() throws IOException {
        return GetElementDom.start(webClient, HTTP_LOCALHOST_8080_HOME)
            .navigation("li", "id", "user_menu")
            .getChildUl("class", "dropdown-menu user-profile-menu list-unstyled")
            .getListChildAnchor()
            .stream()
            .map(a -> a.getTextContent().trim())
            .collect(Collectors.toList());
    }
}
