package br.com.tsi.utfpr.xenon.domain.security.config;

import br.com.tsi.utfpr.xenon.domain.security.filter.LoggingAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String SEND_PLATE = "/send/plate/**";
    private static final String NOVO_REGISTRO = "/novo-registro";
    private static final String VALIDAR_TOKEN = "/validar-token";
    private static final String VALIDE_TOKEN = "/valide-token";
    private static final String VALIDATE_PLATE = "/validate-plate";
    private static final String CADASTRO_ESTUDANTE = "/cadastro-estudante";
    private static final String REGISTRY_STUDENTS = "/registry-students";
    private static final String CADASTRE_NOVO_ESTUDANTE = "/cadastre-novo-estudante";
    private static final String CONCLUIDO = "/concluido";
    private static final String INCLUIR_REGISTRO = "/incluir-registro";
    private static final String ACCESS_DENIED_PUBLIC = "/access-denied-public";
    private static final String ERROR_USUARIO_CADASTRADO = "/error/usuario-cadastrado";

    private final LoggingAccessDeniedHandler accessDeniedHandler;
    private final UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        configureSecurityCsrf(http);
        configureSecurityPostUrl(http);
        configureSecurityGetUrl(http);
        configureSecurityResource(http);
        configureLoginAndSesseionSecurity(http);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    private void configureLoginAndSesseionSecurity(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated()
            .and()
            .formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/home")
            .and()
            .logout()
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/login?logout")
            .permitAll()
            .and()
            .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

    private void configureSecurityCsrf(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers(SEND_PLATE)
            .and().headers().frameOptions().sameOrigin()
            .and();
    }

    private void configureSecurityPostUrl(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers(HttpMethod.POST,
                SEND_PLATE,
                INCLUIR_REGISTRO,
                VALIDE_TOKEN,
                VALIDATE_PLATE,
                CADASTRE_NOVO_ESTUDANTE,
                REGISTRY_STUDENTS)
            .permitAll();
    }

    private void configureSecurityGetUrl(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers(HttpMethod.GET,
                NOVO_REGISTRO,
                CADASTRO_ESTUDANTE,
                VALIDAR_TOKEN,
                ACCESS_DENIED_PUBLIC,
                ERROR_USUARIO_CADASTRADO,
                CONCLUIDO)
            .permitAll();
    }

    private void configureSecurityResource(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/js/**", "/css/**", "/images/**", "/headless-content.js.map",
                "/avatar/**", "/vendor/**").permitAll();
    }
}
