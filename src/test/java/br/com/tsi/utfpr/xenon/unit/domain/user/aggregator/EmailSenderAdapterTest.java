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

        var exception = assertThrows(IllegalStateException.class, () -> emailSenderAdapter.sendMail(token));
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

        emailSenderAdapter.sendMail(token);

        verify(javaMailSender).send(emailCaptor.capture());

        var message = emailCaptor.getValue();
        assertEquals("noreply@xenon.utfpr.edu.br", message.getFrom());
        assertEquals(email, Objects.requireNonNull(message.getTo())[0]);
        assertEquals("Token de cadastro do Xenon", message.getSubject());
        assertEquals(String.format(TEMPLATE_MESSAGE, token.getToken()), message.getText());
    }
}
