package br.com.tsi.utfpr.xenon.e2e.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.tsi.utfpr.xenon.e2e.AbstractEndToEndTest;
import br.com.tsi.utfpr.xenon.e2e.utils.GetElementDom;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Test - e2e - Redirecionar de home -> login e vice-versa")
public class RedirectLoginTest extends AbstractEndToEndTest {

    private static final String HTTP_LOCALHOST_8080_HOME = "http://localhost:8080/home";
    private static final String UTFPR_XENON = "UTFPR - Xenon";
    private static final String ATTRIBUTE_CLASS = "class";
    private static final String HTTP_LOCALHOST_8080_LOGIN = "http://localhost:8080/login";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    @DisplayName("Deve redirecionar para home quando logado")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldRedirectToHomeWhenLogged() throws IOException {
        var page = GetElementDom.start(webClient, HTTP_LOCALHOST_8080_LOGIN).getHtmlPage();

        assertEquals("UTFPR - Xenon - Home", page.getTitleText());
    }

    @Test
    @DisplayName("Deve redirecionar para login quando deslogado")
    void shouldRedirectToLoginWhenLogOut() throws IOException {
        var page = GetElementDom.start(webClient, HTTP_LOCALHOST_8080_HOME).getHtmlPage();

        assertEquals(UTFPR_XENON, page.getTitleText());
    }

    @Test
    @DisplayName("Deve redirecionar para login quando executar logout")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldRedirectToLoginWhenExecLogout() throws IOException {
        var redirectPage = GetElementDom.start(webClient, HTTP_LOCALHOST_8080_HOME)
            .navigation("li", ATTRIBUTE_CLASS, "dropdown user-profile")
            .setAttributeInElement(ATTRIBUTE_CLASS, "dropdown user-profile open")
            .getChildUl(ATTRIBUTE_CLASS, "dropdown-menu user-profile-menu list-unstyled")
            .getChildLi(ATTRIBUTE_CLASS, "last")
            .getChildAnchor("href", "/logout")
            .redirectOnClick();

        assertEquals(UTFPR_XENON, redirectPage.getTitleText());
        assertEquals(redirectPage.getBaseURL().getPath(), "/login");
    }

    @Override
    protected WebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }
}
