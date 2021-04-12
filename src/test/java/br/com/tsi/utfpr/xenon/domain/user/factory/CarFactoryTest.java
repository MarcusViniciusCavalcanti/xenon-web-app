package br.com.tsi.utfpr.xenon.domain.user.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import br.com.tsi.utfpr.xenon.domain.user.entity.Car;
import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {CarFactory.class})
@DisplayName("Test - Unidade - CarFactory")
class CarFactoryTest {

    private static final LocalDate DATE = LocalDate.now();
    private static final long ID = 1L;
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();
    private static final String MODEL_CAR = "Model Car";
    private static final String PLATE_CAR = "Plate Car";

    @Autowired
    private CarFactory carFactory;

    @Test
    @DisplayName("Deve retonar um carDto com sucesso")
    void shouldReturnCar() {
        var user = new User();
        user.setCar(createCar(user));

        var carDto = carFactory.createCarDto(user);

        assertEquals(MODEL_CAR, carDto.getModel());
        assertEquals(PLATE_CAR, carDto.getPlate());
        assertEquals(LOCAL_DATE_TIME, carDto.getLastAccess());
        assertEquals(ID, carDto.getId());
    }

    @Test
    @DisplayName("Não Deve retonar um carDto")
    void shouldReturnNull() {
        var user = new User();

        var carDto = carFactory.createCarDto(user);

        assertNull(carDto);
    }

    private Car createCar(User user) {
        var car = new Car();
        car.setId(ID);
        car.setCreatedAt(DATE);
        car.setUpdatedAt(DATE);
        car.setModel(MODEL_CAR);
        car.setPlate(PLATE_CAR);
        car.setLastAccess(LOCAL_DATE_TIME);
        car.setUser(user);

        return car;
    }
}
