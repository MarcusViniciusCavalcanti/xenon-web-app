package br.com.tsi.utfpr.xenon.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import br.com.tsi.utfpr.xenon.e2e.utils.GetElementDom;
import com.gargoylesoftware.htmlunit.WebClient;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
@ContextConfiguration
@SpringBootTest
@WebAppConfiguration
@DisplayName("Test e2e - Redirecionar de home -> login e vice-versa")
public class RedirectLoginTest {

    private static final int TIMEOUT_MILLIS = 5000 * 2;
    private static final String HTTP_LOCALHOST_8080_HOME = "http://localhost:8080/home";
    private static final String UTFPR_XENON = "UTFPR - Xenon";
    private static final String ATTRIBUTE_CLASS = "class";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private WebClient webClient;

    @BeforeEach
    public void setup() {
        var mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply(springSecurity())
            .build();

        webClient = MockMvcWebClientBuilder
            .mockMvcSetup(mockMvc)
            .build();

        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setCssEnabled(true);
        webClient.waitForBackgroundJavaScript(TIMEOUT_MILLIS);
    }

    @Test
    @DisplayName("Deve redirecionar para home quando logado")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldRedirectToHomeWhenLogged() throws IOException {
        var url = "http://localhost:8080/login";

        var page = GetElementDom.start(webClient, url).getHtmlPage();

        assertEquals("UTFPR - Xenon - Home", page.getTitleText());
    }

    @Test
    @DisplayName("Deve redirecionar para login quando deslogado")
    void shouldRedirectToLoginWhenLogOut() throws IOException {
        var url = HTTP_LOCALHOST_8080_HOME;

        var page = GetElementDom.start(webClient, url).getHtmlPage();

        assertEquals(UTFPR_XENON, page.getTitleText());
    }

    @Test
    @DisplayName("Deve redirecionar para login quando executar logout")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldRedirectToLoginWhenExecLogout() throws IOException {
        var url = HTTP_LOCALHOST_8080_HOME;

        var redirectPage = GetElementDom.start(webClient, url)
            .navigation("li", ATTRIBUTE_CLASS, "dropdown user-profile")
            .setAttributeInElement(ATTRIBUTE_CLASS, "dropdown user-profile open")
            .getChildUl(ATTRIBUTE_CLASS, "dropdown-menu user-profile-menu list-unstyled")
            .getChildLi(ATTRIBUTE_CLASS, "last")
            .getChildAnchor("href", "/logout")
            .redirectOnClick();

        assertEquals(UTFPR_XENON, redirectPage.getTitleText());
        assertEquals(redirectPage.getBaseURL().getPath(), "/login");
    }
}
