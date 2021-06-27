package br.com.tsi.utfpr.xenon.e2e.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.tsi.utfpr.xenon.e2e.AbstractEndToEndTest;
import br.com.tsi.utfpr.xenon.e2e.utils.GetElementDom;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Test - e2e - Deve redirecionar para Acesso Negado")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
    "classpath:/sql/user_default_insert.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
    "classpath:/sql/user_default_delete.sql"})
class RedirectAccessDeniedTest extends AbstractEndToEndTest {

    private static final String BASE_URL = "http://localhost:8080%s";
    private static final String ATTRIBUTE_CLASS = "class";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @ParameterizedTest
    @ValueSource(strings = {
        "/usuarios/todos",
        "/users/all",
    })
    @DisplayName("Deve redirecionar para '/access-denied' quando perfil é [STUDENTS]")
    @WithUserDetails(value = "beltrano_user@alunos.utfpr.edu.br", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldRedirectAccessDeniedStudents(String url) throws IOException {
        assertionAccessDenied(url);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "/usuarios/todos",
        "/users/all",
    })
    @DisplayName("Deve redirecionar para '/access-denied' quando perfil é [OPERATOR]")
    @WithUserDetails(value = "beltrano_operator@operator.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldRedirectAccessDeniedOperator(String url) throws IOException {
        assertionAccessDenied(url);
    }

    private void assertionAccessDenied(String url) throws IOException {
        var page = GetElementDom.start(webClient, String.format(BASE_URL, url))
            .navigation("body", ATTRIBUTE_CLASS, "page-body page-error-env")
            .getDiv(ATTRIBUTE_CLASS, "page-error centered")
            .executeAssertion(div -> {
                var h2 = div.<HtmlHeading2>getFirstByXPath("//h2");
                assertEquals("Error 403\nAcesso Negado!", h2.getVisibleText());
            }).getHtmlPage();

        assertEquals("Xenon - 403", page.getTitleText());
        assertEquals("http://localhost:8080/access-denied",
            page.getWebResponse().getWebRequest().getUrl().toString());
    }

    @Override
    protected WebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }
}
