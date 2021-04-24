package br.com.tsi.utfpr.xenon.domain.user.service;

import br.com.tsi.utfpr.xenon.domain.security.entity.AccessCard;
import br.com.tsi.utfpr.xenon.domain.security.repository.AccessCardRepository;
import br.com.tsi.utfpr.xenon.domain.user.exception.UsernameException;
import br.com.tsi.utfpr.xenon.structure.dtos.ResultCheckerDto;
import br.com.tsi.utfpr.xenon.structure.dtos.ResultCheckerDto.Result;
import br.com.tsi.utfpr.xenon.structure.dtos.ResultUsernameCheckerDto;
import br.com.tsi.utfpr.xenon.structure.dtos.TypeUserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputEmailDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ValidatorUserRegistry {

    private static final String MESSAGE_ERROR_DOES_NOT_BELONGS_UTFPR_DOMAIN =
        "E-mail não pertence ao dominio da UTFPR";
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

    private final AccessCardRepository accessCardRepository;

    public void validateToToken(String email) {
        checkUsernameExist(email);
    }

    public void check(InputUserDto inputUserDto) {
        if (inputUserDto.getType() == TypeUserDto.STUDENTS) {
            var result = verifyInApiService(inputUserDto.getUsername());

            if (result.getResult().getValue() == Result.USER_CANNOT_BE_REGISTERED) {
                throw new UsernameException(result);
            }
        }
    }

    public ResultUsernameCheckerDto check(InputEmailDto username) {
        return verifyInApiService(username.getValue());
    }

    // TODO: 1# verifica na utfpr se existe o usuário
    private ResultUsernameCheckerDto verifyInApiService(String username) {
        return ResultUsernameCheckerDto.builder()
            .result(ResultCheckerDto.builder()
                .value(Result.USER_CAN_BE_REGISTERED)
                .reason(MESSAGE_ERROR_DOES_NOT_BELONGS_UTFPR_DOMAIN)
                .build())
            .username(username)
            .build();
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
