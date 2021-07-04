package br.com.tsi.utfpr.xenon.domain.security.service;

import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import java.util.function.Consumer;

public class UpdatersAccessCardService implements Consumer<User> {

    private final InputUserDto inputUserDto;
    private final RoleService roleService;

    private UpdatersAccessCardService(InputUserDto inputUserDto,
        RoleService roleService) {
        this.inputUserDto = inputUserDto;
        this.roleService = roleService;
    }

    @Override
    public void accept(User user) {
        var accessCard = user.getAccessCard();
        var roles = roleService.verifyAndGetRoleBy(inputUserDto);

        accessCard.setRoles(roles);
        accessCard.setAccountNonExpired(inputUserDto.isAccountNonExpired());
        accessCard.setCredentialsNonExpired(inputUserDto.isCredentialsNonExpired());
        accessCard.setAccountNonLocked(inputUserDto.isAccountNonLocked());
        accessCard.setEnabled(inputUserDto.isEnabled());
    }

    public static UpdatersAccessCardService getNewInstance(InputUserDto inputUserDto,
        RoleService roleService) {
        return new UpdatersAccessCardService(inputUserDto, roleService);
    }
}
