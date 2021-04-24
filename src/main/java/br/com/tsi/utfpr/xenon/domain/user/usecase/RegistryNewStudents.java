package br.com.tsi.utfpr.xenon.domain.user.usecase;

import br.com.tsi.utfpr.xenon.domain.user.aggregator.Token;
import br.com.tsi.utfpr.xenon.domain.user.aggregator.TokenAdapter;
import br.com.tsi.utfpr.xenon.domain.user.service.ValidatorUserRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegistryNewStudents {

    private final ValidatorUserRegistry validatorUserRegistry;
    private final TokenAdapter tokenAdapter;

    public void includeNew(String email) {
        validatorUserRegistry.validateToToken(email);
        var token = new Token(email);
        token.generateNewToken();
        tokenAdapter.saveToken(token);
    }

}
