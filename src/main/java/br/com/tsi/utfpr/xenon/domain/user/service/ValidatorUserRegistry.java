package br.com.tsi.utfpr.xenon.domain.user.service;

import br.com.tsi.utfpr.xenon.domain.security.entity.AccessCard;
import br.com.tsi.utfpr.xenon.domain.security.repository.AccessCardRepository;
import br.com.tsi.utfpr.xenon.domain.user.exception.RegistrationException;
import br.com.tsi.utfpr.xenon.domain.user.exception.UsernameException;
import br.com.tsi.utfpr.xenon.domain.user.repository.UserRepository;
import br.com.tsi.utfpr.xenon.structure.dtos.ResultCheckerDto;
import br.com.tsi.utfpr.xenon.structure.dtos.ResultCheckerDto.Result;
import br.com.tsi.utfpr.xenon.structure.dtos.ResultUsernameCheckerDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputNewStudent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ValidatorUserRegistry {

    private static final String MESSAGE_ERROR_USER_EXIST = "Usuário já cadastrado";
    private static final ExampleMatcher USERNAME_MATCHER;
    private static final String PLATE_EXIST = "Placa %s já cadastrada para outro usuário";

    static {
        USERNAME_MATCHER = ExampleMatcher.matching()
            .withIgnorePaths(
                "id",
                "password",
                "accountNonExpired",
                "accountNonLocked",
                "credentialsNonExpired",
                "enabled",
                "user",
                "role"
            );
    }

    private final AccessCardRepository accessCardRepository;
    private final UserRepository userRepository;

    public void validateToInclude(String email) {
        checkUsernameExist(email);
    }

    public void validateToRegistry(InputNewStudent input) {
        checkUsernameExist(input.getEmail());
        checkPlate(input.getPlateCar());
    }

    public void checkPlate(String plate) {
        if (Boolean.TRUE.equals(userRepository.existsUserByCarPlate(plate))) {
            throw new RegistrationException(String.format(PLATE_EXIST, plate));
        }
    }

    private void checkUsernameExist(String email) {
        var probe = new AccessCard();
        probe.setUsername(email);
        var example = Example.of(probe, USERNAME_MATCHER);

        var exist = accessCardRepository.exists(example);

        if (exist) {
            var resultChecker = ResultUsernameCheckerDto.builder()
                .result(ResultCheckerDto.builder()
                    .value(Result.USER_CAN_BE_REGISTERED)
                    .reason(MESSAGE_ERROR_USER_EXIST)
                    .build())
                .username(email)
                .build();

            throw new UsernameException(resultChecker);
        }
    }

}
