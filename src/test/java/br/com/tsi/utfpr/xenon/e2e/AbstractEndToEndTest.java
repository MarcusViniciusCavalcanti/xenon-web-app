package br.com.tsi.utfpr.xenon.e2e;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.gargoylesoftware.htmlunit.WebClient;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
            .build();

        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setCssEnabled(true);
        webClient.waitForBackgroundJavaScript(TIMEOUT_MILLIS);
    }

    protected static String getDefaultFormatTitle(String title) {
        return String.format("UTFPR - Xenon - %s", title);
    }
}
