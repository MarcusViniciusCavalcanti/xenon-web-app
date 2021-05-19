package br.com.tsi.utfpr.xenon.unit.domain.security.filter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.domain.security.filter.LoggingAccessDeniedHandler;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@DisplayName("Test - Unidade LoggingAccessDeniedHandler")
@ExtendWith(MockitoExtension.class)
class LoggingAccessDeniedHandlerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AccessDeniedException exception;

    @InjectMocks
    private LoggingAccessDeniedHandler loggingAccessDeniedHandler;

    @Test
    @DisplayName("Deve redirecionar usuário autendicado para rota '/access-denied' quando não autorizado")
    void shouldHaveRedirectAccessDeniedPageWhenUnauthorized() throws IOException {
        var authentication = mock(Authentication.class);
        var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        loggingAccessDeniedHandler.handle(request, response, exception);

        verify(response).sendRedirect(argThat(url -> url.endsWith("/access-denied")));
    }

    @Test
    @DisplayName("Deve redirecionar usuário não autendicado para rota '/access-denied-public' quando não autorizado")
    void shouldHaveRedirectAccessDeniedPublicPageWhenUnauthorized() throws IOException {
        loggingAccessDeniedHandler.handle(request, response, exception);

        verify(response).sendRedirect(argThat(url -> url.endsWith("/access-denied-public")));
    }

    @Test
    @DisplayName("Deve retornar IOException quando tenta escrever na respostas com login")
    void shouldThrowsIOExceptionLogged() throws IOException {
        var authentication = mock(Authentication.class);
        var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        doThrow(new IOException()).when(response).sendRedirect(any());

        SecurityContextHolder.setContext(securityContext);

        loggingAccessDeniedHandler.handle(request, response, exception);
    }

    @Test
    @DisplayName("Deve retornar IOException quando tenta escrever na respostas não logado")
    void shouldThrowsIOExceptionNotLogged() throws IOException {
        doThrow(new IOException()).when(response).sendRedirect(any());
        loggingAccessDeniedHandler.handle(request, response, exception);
    }

}
