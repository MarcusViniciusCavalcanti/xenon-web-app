package br.com.tsi.utfpr.xenon.domain.user.service;

import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.domain.user.factory.CarFactory;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import java.util.Objects;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public class UpdatersCarService implements Consumer<User> {

    private final InputUserDto inputUserDto;
    private final CarFactory carFactory;

    private UpdatersCarService(InputUserDto inputUserDto, CarFactory carFactory) {
        this.inputUserDto = inputUserDto;
        this.carFactory = carFactory;
    }

    @Override
    public void accept(User user) {
        Assert.state(Objects.nonNull(inputUserDto), "InputUserDto is null");
        var isUpdateCar = Boolean.FALSE;
        var isContentCar = user.car().isPresent();

        if (Boolean.TRUE.equals(validateValues())) {
            isUpdateCar(user);
            isUpdateCar = Boolean.TRUE;
        }

        if (Boolean.TRUE.equals(isUpdateCar) && Boolean.TRUE.equals(isContentCar)) {
            var actual = user.getStatusRegistry();
            var next = actual - 25;
            user.setStatusRegistry(next);
        } else if (Boolean.TRUE.equals(isUpdateCar)) {
            var actual = user.getStatusRegistry();
            var next = actual + 25;
            user.setStatusRegistry(next);
        }
    }

    private Boolean validateValues() {
        return StringUtils.isNoneBlank(inputUserDto.getCarModel())
            && StringUtils.isNoneBlank(inputUserDto.getCarPlate());
    }

    private void isUpdateCar(User user) {
        var carPlate = inputUserDto.getCarPlate();
        var carModel = inputUserDto.getCarModel();
        var updateCar = user.car()
            .map(car -> {
                car.setModel(carModel);
                car.setPlate(carPlate);
                car.setDocument(Boolean.FALSE);
                return car;
            }).orElseGet(
                () -> carFactory.createCarToUser(carPlate, carModel, user));

        user.setCar(updateCar);
    }

    public static UpdatersCarService getNewInstance(InputUserDto inputUserDto,
        CarFactory carFactory) {
        return new UpdatersCarService(inputUserDto, carFactory);
    }
}
