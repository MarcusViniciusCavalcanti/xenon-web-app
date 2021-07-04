package br.com.tsi.utfpr.xenon.domain.user.usecase;

import br.com.tsi.utfpr.xenon.domain.user.aggregator.EmailSenderAdapter;
import br.com.tsi.utfpr.xenon.domain.user.factory.UserFactory;
import br.com.tsi.utfpr.xenon.domain.user.repository.UserRepository;
import br.com.tsi.utfpr.xenon.domain.user.service.ValidatorUserRegistry;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegistryUser {

    private final ValidatorUserRegistry validatorUserRegistry;
    private final UserFactory userFactory;
    private final UserRepository userRepository;
    private final EmailSenderAdapter emailSenderAdapter;

    @Transactional
    public void save(InputUserDto userDto) {
        validatorUserRegistry.validateToRegistry(userDto);
        var pass = getPlanPass();
        userDto.setPassword(pass);

        var user = userFactory.createUser(userDto);
        userRepository.save(user);

        emailSenderAdapter
            .sendMailPasswordToCompleteRegistry(pass, user.getAccessCard().getUsername());
    }

    private String getPlanPass() {
        return RandomStringUtils.randomAlphabetic(8, 8);
    }
}
