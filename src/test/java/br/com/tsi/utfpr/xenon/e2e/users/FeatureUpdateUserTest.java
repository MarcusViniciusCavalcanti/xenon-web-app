package br.com.tsi.utfpr.xenon.e2e.users;


import br.com.tsi.utfpr.xenon.domain.user.repository.UserRepository;
import br.com.tsi.utfpr.xenon.e2e.AbstractEndToEndTest;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Test - e2e - Funcionalidade todos Usuários")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
    "classpath:/sql/user_default_insert.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
    "classpath:/sql/user_default_delete.sql"})
class FeatureUpdateUserTest extends AbstractEndToEndTest {

    private static final String URL_UPDATE_USER_PATTERN =
        "http://localhost:8080/usuario/atualizar/%d";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Override
    protected WebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }

    @Test
    @DisplayName("Deve Redirecionar para Pagina de Não autorizado quando usuário operador")
    @WithUserDetails(value = "beltrano_operator@operator.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldRedirectToAccessDeniedWhenUserRolesOperator() throws IOException {
        var user = userRepository.findById(153L).get();
        assertUnauthorized(String.format(URL_UPDATE_USER_PATTERN, user.getId()));
    }

    @Test
    @DisplayName("Deve Redirecionar para Pagina de Não autorizado quando usuário motorista")
    @WithUserDetails(value = "beltrano_operator@operator.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldRedirectToAccessDeniedWhenUserRoleDriver() throws IOException {
        var user = userRepository.findById(1L).get();
        assertUnauthorized(String.format(URL_UPDATE_USER_PATTERN, user.getId()));
    }
}
