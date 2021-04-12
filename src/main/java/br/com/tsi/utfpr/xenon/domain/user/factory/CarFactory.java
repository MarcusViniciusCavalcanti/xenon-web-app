package br.com.tsi.utfpr.xenon.domain.user.factory;

import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.structure.dtos.CarDto;
import org.springframework.stereotype.Component;

@Component
public class CarFactory {

    public CarDto createCarDto(User user) {
        return user.car().map(car -> CarDto.builder()
            .id(car.getId())
            .lastAccess(car.getLastAccess())
            .model(car.getModel())
            .plate(car.getPlate())
            .build())
            .orElse(null);
    }
}
