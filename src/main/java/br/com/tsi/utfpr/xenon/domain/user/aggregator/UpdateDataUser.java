package br.com.tsi.utfpr.xenon.domain.user.aggregator;

import br.com.tsi.utfpr.xenon.domain.security.service.RoleService;
import br.com.tsi.utfpr.xenon.domain.security.service.UpdatersAccessCardService;
import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.domain.user.factory.CarFactory;
import br.com.tsi.utfpr.xenon.domain.user.service.UpdatersCarService;
import br.com.tsi.utfpr.xenon.domain.user.service.UpdatesUserService;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UpdateDataUser {

    private final CarFactory carFactory;
    private final RoleService roleService;

    public Consumer<User> process(InputUserDto inputUserDto) {
        return UpdatesUserService.getNewInstance(inputUserDto)
            .andThen(UpdatersCarService.getNewInstance(inputUserDto, carFactory))
            .andThen(UpdatersAccessCardService.getNewInstance(inputUserDto, roleService));
    }
}
