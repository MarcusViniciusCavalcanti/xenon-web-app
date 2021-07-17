package br.com.tsi.utfpr.xenon.domain.user.factory;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import br.com.tsi.utfpr.xenon.domain.security.factory.AccessCardFactory;
import br.com.tsi.utfpr.xenon.domain.user.entity.TypeUser;
import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.structure.dtos.TypeUserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.UserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputNewStudent;
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
            .statusRegistry(user.getStatusRegistry())
            .avatar(user.getAvatar())
            .confirmDocument(user.getConfirmDocument())
            .build();
    }

    public User createUser(InputUserDto inputUser) {
        var user = createUserNew(
            inputUser.getName(),
            inputUser.getType().name(),
            inputUser.isAuthorizedAccess()
        );

        configureCar(inputUser.getCarModel(), inputUser.getCarPlate(), user);
        var accessCard = accessCardFactory.createAccessCardToUser(inputUser, user);
        user.setAccessCard(accessCard);

        return user;
    }

    public User createTypeStudent(InputNewStudent inputNewStudent) {
        var user = createUserNew(
            inputNewStudent.getName(),
            TypeUser.STUDENTS.name(),
            Boolean.FALSE
        );

        var car = carFactory
            .createCarToUser(inputNewStudent.getPlateCar(), inputNewStudent.getModelCar(), user);
        user.setCar(car);
        var accessCard = accessCardFactory.createAccessCardActiveStudentsRole(
            inputNewStudent.getEmail(),
            inputNewStudent.getPassword(),
            user
        );
        user.setAccessCard(accessCard);

        return user;
    }

    private User createUserNew(String name, String type, Boolean authorisedAccess) {
        var user = new User();
        user.setName(name);
        user.setTypeUser(TypeUser.valueOf(type));
        user.setAuthorisedAccess(authorisedAccess);
        user.setStatusRegistry(25);

        return user;
    }

    private void configureCar(String carModel, String carPlate, User user) {
        if (isNotEmpty(carModel) && isNotEmpty(carPlate)) {
            var car = carFactory.createCarToUser(carPlate, carModel, user);
            user.setCar(car);
            var actual = user.getStatusRegistry();
            var next = actual + 25;
            user.setStatusRegistry(next);
        }
    }
}
