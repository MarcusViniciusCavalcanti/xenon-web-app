package br.com.tsi.utfpr.xenon.domain.user.aggregator;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmailSenderAdapter {

    private static final String TEMPLATE_MESSAGE = "Seu cadastro foi efeturado com sucesso!"
        + "\n\n"
        + "Agora valide seu e-mail utilizando o token abaixo, para continuar seu cadastro:\n"
        + "%s";

    private final JavaMailSender javaMailSender;

    public void sendMail(Token token) {
        Assert.state(isNotEmpty(token.getToken()), "Token está vázio, verifique o preenchimento");

        var emailMessage = new SimpleMailMessage();
        emailMessage.setFrom("noreply@xenon.utfpr.edu.br");
        emailMessage.setTo(token.getKey());
        emailMessage.setSubject("Token de cadastro do Xenon");
        emailMessage.setText(String.format(TEMPLATE_MESSAGE, token.getToken()));

        javaMailSender.send(emailMessage);
    }
}
