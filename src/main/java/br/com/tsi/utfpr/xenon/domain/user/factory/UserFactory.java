package br.com.tsi.utfpr.xenon.domain.user.factory;

import br.com.tsi.utfpr.xenon.domain.security.factory.AccessCardFactory;
import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.structure.dtos.TypeUserDTO;
import br.com.tsi.utfpr.xenon.structure.dtos.UserDto;
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
            .typeUser(TypeUserDTO.valueOf(user.getTypeUser().name()))
            .numberAccess(user.getNumberAccess())
            .authorisedAcces(user.getAuthorisedAccess())
            .build();
    }
}
