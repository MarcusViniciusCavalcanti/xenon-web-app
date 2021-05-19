package br.com.tsi.utfpr.xenon.unit.web.controller;

import static org.junit.jupiter.api.Assertions.*;

import br.com.tsi.utfpr.xenon.web.controller.SecurityController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Test - Unidade - SecurityController")
@ExtendWith(MockitoExtension.class)
class SecurityControllerTest {

    @InjectMocks
    private SecurityController securityController;

    @Test
    @DisplayName("Deve retornar view '/login'")
    void shouldReturnRedirectLogin() {
        var result = securityController.login();
        assertEquals("security/login", result);
    }

    @Test
    @DisplayName("Deve retornar view '/error/access-denied'")
    void shouldReturnRedirectAccessDenied() {
        var result = securityController.accessDenied();
        assertEquals("/error/access-denied", result);
    }

    @Test
    @DisplayName("Deve retornar view '/error/access-denied'")
    void shouldReturnRedirectAccessDeniedPublic() {
        var result = securityController.accessDeniedPublic();
        assertEquals("/error/access-denied", result);
    }
}
