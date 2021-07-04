package br.com.tsi.utfpr.xenon.domain.user.service;

import br.com.tsi.utfpr.xenon.domain.user.entity.TypeUser;
import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import java.util.Objects;
import java.util.function.Consumer;
import org.springframework.util.Assert;

public class UpdatesUserService implements Consumer<User> {

    private final InputUserDto inputUserDto;

    private UpdatesUserService(InputUserDto inputUserDto) {
        this.inputUserDto = inputUserDto;
    }

    @Override
    public void accept(User user) {
        Assert.state(Objects.nonNull(inputUserDto), "InputUserDto is null");

        user.setTypeUser(TypeUser.valueOf(inputUserDto.getType().name()));
        user.setName(inputUserDto.getName());
        user.setAuthorisedAccess(inputUserDto.isAuthorizedAccess());
    }

    public static UpdatesUserService getNewInstance(InputUserDto inputUserDto) {
        return new UpdatesUserService(inputUserDto);
    }
}
