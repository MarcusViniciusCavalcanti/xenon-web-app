package br.com.tsi.utfpr.xenon.unit.domain.user.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import br.com.tsi.utfpr.xenon.domain.user.entity.Car;
import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.domain.user.factory.CarFactory;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {CarFactory.class})
@DisplayName("Test - Unidade - CarFactory")
class CarFactoryTest {

    private static final LocalDateTime DATE = LocalDateTime.now();
    private static final long ID = 1L;
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();
    private static final String MODEL_CAR = "Model Car";
    private static final String PLATE_CAR = "Plate Car";
    public static final Boolean DOCUMENT = Boolean.TRUE;

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
        assertEquals(DOCUMENT, carDto.getDocument());
        assertEquals(ID, carDto.getId());
    }

    @Test
    @DisplayName("Não Deve retonar um carDto")
    void shouldReturnNull() {
        var user = new User();

        var carDto = carFactory.createCarDto(user);

        assertNull(carDto);
    }

    @Test
    @DisplayName("Deve criar um novo carro")
    void shouldHaveCreateNewInstance() {
        var user = new User();

        var car = carFactory.createCarToUser(PLATE_CAR, MODEL_CAR, user);

        assertEquals(MODEL_CAR, car.getModel());
        assertEquals(PLATE_CAR, car.getPlate());
        assertNull(car.getLastAccess());
        assertFalse(car.getDocument());
        assertNull(car.getId());
    }

    private Car createCar(User user) {
        var car = new Car();
        car.setId(ID);
        car.setCreatedAt(DATE);
        car.setUpdatedAt(DATE);
        car.setModel(MODEL_CAR);
        car.setPlate(PLATE_CAR);
        car.setLastAccess(LOCAL_DATE_TIME);
        car.setDocument(DOCUMENT);
        car.setUser(user);

        return car;
    }
}
