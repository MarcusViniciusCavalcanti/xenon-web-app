package br.com.tsi.utfpr.xenon.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import br.com.tsi.utfpr.xenon.e2e.utils.GetElementDom;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Execution(ExecutionMode.SAME_THREAD)
@ActiveProfiles("test")
@ContextConfiguration
@SpringBootTest
@WebAppConfiguration
public abstract class AbstractEndToEndTest {

    private static final int TIMEOUT_MILLIS = 5000 * 2;
    protected WebClient webClient;
    protected MockMvc mockMvc;

    protected abstract WebApplicationContext getWebApplicationContext();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(getWebApplicationContext())
            .apply(springSecurity())
            .build();

        webClient = MockMvcWebClientBuilder
            .mockMvcSetup(mockMvc)
            .withDelegate(new WebClient(BrowserVersion.CHROME ))
            .build();

        webClient.getOptions().setWebSocketEnabled(true);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setCssEnabled(true);
        webClient.waitForBackgroundJavaScript(TIMEOUT_MILLIS);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
    }

    @AfterEach
    public void tearDown() {
        webClient.close();
    }

    protected static String getDefaultFormatTitle(String title) {
        return String.format("UTFPR - Xenon - %s", title);
    }

    protected void assertUnauthorized(String url) throws IOException {
        var unauthorizedPage = GetElementDom.start(webClient, url).getHtmlPage();
        assertEquals("Xenon - 403", unauthorizedPage.getTitleText());
    }
}
