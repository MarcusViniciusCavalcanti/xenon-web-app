package br.com.tsi.utfpr.xenon.domain.user.usecase;

import br.com.tsi.utfpr.xenon.domain.security.entity.AccessCard;
import br.com.tsi.utfpr.xenon.domain.security.repository.AccessCardRepository;
import br.com.tsi.utfpr.xenon.domain.user.exception.UsernameException;
import br.com.tsi.utfpr.xenon.domain.user.factory.UserFactory;
import br.com.tsi.utfpr.xenon.domain.user.repository.UserRepository;
import br.com.tsi.utfpr.xenon.domain.user.service.ValidatorUserRegistry;
import br.com.tsi.utfpr.xenon.structure.dtos.ResultCheckerDto;
import br.com.tsi.utfpr.xenon.structure.dtos.ResultCheckerDto.Result;
import br.com.tsi.utfpr.xenon.structure.dtos.ResultUsernameCheckerDto;
import br.com.tsi.utfpr.xenon.structure.dtos.UserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserRegistry {

    private static final String MESSAGE_ERROR_USER_EXIST = "Usuário já existe";
    private static final ExampleMatcher USERNAME_MATCHER;

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

    private final UserRepository userRepository;
    private final AccessCardRepository accessCardRepository;
    private final ValidatorUserRegistry validatorUserRegistry;
    private final UserFactory userFactory;

    public UserDto registerNewUser(InputUserDto inputUserDto) {
        validatorUserRegistry.check(inputUserDto);

        checkUsernameExist(inputUserDto.getUsername());

        var newUser = userFactory.createUser(inputUserDto);
        var user = userRepository.save(newUser);

        return userFactory.createUserDto(user);
    }

    private void checkUsernameExist(String username) {
        var probe = new AccessCard();
        probe.setUsername(username);
        var example = Example.of(probe, USERNAME_MATCHER);

        var exist = accessCardRepository.exists(example);

        if (exist) {
            var resultChecker = ResultUsernameCheckerDto.builder()
                .result(ResultCheckerDto.builder()
                    .value(Result.USER_CAN_BE_REGISTERED)
                    .reason(MESSAGE_ERROR_USER_EXIST)
                    .build())
                .username(username)
                .build();

            throw new UsernameException(resultChecker);
        }
    }
}
