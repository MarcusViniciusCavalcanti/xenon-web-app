package br.com.tsi.utfpr.xenon.domain.user.factory;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import br.com.tsi.utfpr.xenon.domain.security.factory.AccessCardFactory;
import br.com.tsi.utfpr.xenon.domain.user.entity.TypeUser;
import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.structure.dtos.TypeUserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.UserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserFactory {

    private final AccessCardFactory accessCardFactory;
    private final CarFactory carFactory;

    public UserDto createUserDto(User user) {
        var accessCardDto = accessCardFactory.createAccessCardDto(user);

        return UserDto.builder()
            .id(user.getId())
            .name(user.getName())
            .accessCard(accessCardDto)
            .car(carFactory.createCarDto(user))
            .typeUser(TypeUserDto.valueOf(user.getTypeUser().name()))
            .numberAccess(user.getNumberAccess())
            .authorisedAcces(user.getAuthorisedAccess())
            .build();
    }

    public User createUser(InputUserDto inputUser) {
        var user = new User();
        user.setName(inputUser.getName());
        user.setTypeUser(TypeUser.valueOf(inputUser.getType().name()));
        user.setAuthorisedAccess(inputUser.isAuthorizedAccess());

        if (isNotEmpty(inputUser.getCarModel()) && isNotEmpty(inputUser.getCarPlate())) {
            var car =
                carFactory.createCarToUser(inputUser.getCarPlate(), inputUser.getCarModel(), user);
            user.setCar(car);
        }

        var accessCard = accessCardFactory.createAccessCardToUser(inputUser, user);
        user.setAccessCard(accessCard);

        return user;
    }
}
