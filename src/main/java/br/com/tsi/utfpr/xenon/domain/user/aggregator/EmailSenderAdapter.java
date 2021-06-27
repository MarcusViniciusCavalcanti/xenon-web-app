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

    private static final String TEMPLATE_MESSAGE_TOKEN = "Seu cadastro foi efeturado com sucesso!"
        + "\n\n"
        + "Agora valide seu e-mail utilizando o token abaixo, para continuar seu cadastro:\n"
        + "%s";
    private static final String TEMPLATE_MESSAGE_PASSWORD_COMPLETE_REGISTRY =
        "Seu cadastro foi concluído com sucesso!"
            + "\n\n"
            + "Sua senha: %s"
            + "\n"
            + "Você pode alterar a senha padrão acessando seu perfil";
    private static final String FROM = "noreply@xenon.utfpr.edu.br";

    private final JavaMailSender javaMailSender;

    public void sendMailToken(Token token) {
        Assert.state(isNotEmpty(token.getToken()), "Token está vázio, verifique o preenchimento");

        var emailMessage = new SimpleMailMessage();
        emailMessage.setFrom(FROM);
        emailMessage.setTo(token.getKey());
        emailMessage.setSubject("Token de cadastro do Xenon");
        emailMessage.setText(String.format(TEMPLATE_MESSAGE_TOKEN, token.getToken()));

        javaMailSender.send(emailMessage);
    }

    public void sendMailPasswordToCompleteRegistry(String password, String email) {
        Assert.state(isNotEmpty(password), "Password está vázio, verifique o preenchimento");
        Assert.state(isNotEmpty(email), "E-mail está vázio, verifique o preenchimento");

        var emailMessage = new SimpleMailMessage();
        emailMessage.setFrom(FROM);
        emailMessage.setTo(email);
        emailMessage.setSubject("Cadastro concluído com sucesso");
        emailMessage.setText(String.format(TEMPLATE_MESSAGE_PASSWORD_COMPLETE_REGISTRY, password));

        javaMailSender.send(emailMessage);
    }
}
