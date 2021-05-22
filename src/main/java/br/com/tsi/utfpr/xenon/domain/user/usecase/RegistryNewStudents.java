package br.com.tsi.utfpr.xenon.domain.user.usecase;

import br.com.tsi.utfpr.xenon.domain.user.aggregator.EmailSenderAdapter;
import br.com.tsi.utfpr.xenon.domain.user.aggregator.Token;
import br.com.tsi.utfpr.xenon.domain.user.aggregator.TokenAdapter;
import br.com.tsi.utfpr.xenon.domain.user.factory.UserFactory;
import br.com.tsi.utfpr.xenon.domain.user.repository.UserRepository;
import br.com.tsi.utfpr.xenon.domain.user.service.ValidatorUserRegistry;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputNewStudent;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegistryNewStudents {

    private final ValidatorUserRegistry validatorUserRegistry;
    private final ValidateToken validateToken;
    private final TokenAdapter tokenAdapter;
    private final EmailSenderAdapter emailSenderAdapter;
    private final UserRepository userRepository;
    private final UserFactory userFactory;

    @Async
    public void includeNew(String email) {
        validatorUserRegistry.validateToInclude(email);
        executeProcess(email);
    }

    public void registry(InputNewStudent inputNewStudent) {
        validateToken.validateRegistry(inputNewStudent.getEmail(), inputNewStudent.getToken());
        validatorUserRegistry.validateToRegistry(inputNewStudent);

        var user = userFactory.createTypeStudent(inputNewStudent);
        userRepository.save(user);
    }

    private void executeProcess(String email) {
        CompletableFuture.runAsync(() -> {
            var token = new Token(email);
            token.generateNewToken();
            tokenAdapter.saveToken(token, 5);

            emailSenderAdapter.sendMail(token);
        });
    }

}
