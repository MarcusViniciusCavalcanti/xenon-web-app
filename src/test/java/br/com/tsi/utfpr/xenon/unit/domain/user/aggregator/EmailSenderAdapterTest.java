package br.com.tsi.utfpr.xenon.unit.domain.user.aggregator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import br.com.tsi.utfpr.xenon.domain.user.aggregator.EmailSenderAdapter;
import br.com.tsi.utfpr.xenon.domain.user.aggregator.Token;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@DisplayName("Test - Unidade - EmailSenderAdapter")
@ExtendWith(MockitoExtension.class)
class EmailSenderAdapterTest {

    private static final String TEMPLATE_MESSAGE = "Seu cadastro foi efeturado com sucesso!"
        + "\n\n"
        + "Agora valide seu e-mail utilizando o token abaixo, para continuar seu cadastro:\n"
        + "%s";
    private static final String TEMPLATE_MESSAGE_PASSWORD_COMPLETE_REGISTRY =
        "Seu cadastro foi concluído com sucesso!" +
            "\n\n" +
            "Sua senha: %s" +
            "\n" +
            "Você pode alterar a senha padrão acessando seu perfil";

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailSenderAdapter emailSenderAdapter;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> emailCaptor;

    @Test
    @DisplayName("Deve lançar exception quando estado da aplicação está inválido - token vazio")
    void shouldThrowsExceptionWhenEmailEmpty() {
        var token = new Token("email@email.com");

        var exception = assertThrows(IllegalStateException.class,
            () -> emailSenderAdapter.sendMailToken(token));
        assertEquals("Token está vázio, verifique o preenchimento", exception.getMessage());
    }

    @Test
    @DisplayName("Deve enviar e-mail com sucesso")
    void shouldHaveSendEmail() {
        var email = "email@email.com";
        var token = new Token(email);
        token.generateNewToken();

        doNothing()
            .when(javaMailSender)
            .send(any(SimpleMailMessage.class));

        emailSenderAdapter.sendMailToken(token);

        verify(javaMailSender).send(emailCaptor.capture());

        var message = emailCaptor.getValue();
        assertEquals("noreply@xenon.utfpr.edu.br", message.getFrom());
        assertEquals(email, Objects.requireNonNull(message.getTo())[0]);
        assertEquals("Token de cadastro do Xenon", message.getSubject());
        assertEquals(String.format(TEMPLATE_MESSAGE, token.getToken()), message.getText());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Deve exception quando password está em branco")
    void shouldThrowsExceptionWhenPassIsBlank(String value) {
        var exception = assertThrows(IllegalStateException.class, () ->
            emailSenderAdapter.sendMailPasswordToCompleteRegistry(value, ""));
        assertEquals("Password está vázio, verifique o preenchimento", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Deve exception quando password está em branco")
    void shouldThrowsExceptionWheEmailIsBlank(String value) {
        var exception = assertThrows(IllegalStateException.class, () ->
            emailSenderAdapter.sendMailPasswordToCompleteRegistry("1234567", value));
        assertEquals("E-mail está vázio, verifique o preenchimento", exception.getMessage());
    }

    @Test
    @DisplayName("Deve enviar e-mail com senha")
    void shouldHaveSendEmailWithPass() {
        var email = "email@email.com";
        var pass = "123456";
        doNothing()
            .when(javaMailSender)
            .send(any(SimpleMailMessage.class));

        emailSenderAdapter.sendMailPasswordToCompleteRegistry(pass, email);

        verify(javaMailSender).send(emailCaptor.capture());

        var message = emailCaptor.getValue();
        assertEquals("noreply@xenon.utfpr.edu.br", message.getFrom());
        assertEquals(email, Objects.requireNonNull(message.getTo())[0]);
        assertEquals("Cadastro concluído com sucesso", message.getSubject());
        assertEquals(String.format(TEMPLATE_MESSAGE_PASSWORD_COMPLETE_REGISTRY, pass),
            message.getText());
    }
}
