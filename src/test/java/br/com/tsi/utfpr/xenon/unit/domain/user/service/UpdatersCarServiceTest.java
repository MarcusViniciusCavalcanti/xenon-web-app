package br.com.tsi.utfpr.xenon.unit.domain.user.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.domain.user.entity.Car;
import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.domain.user.factory.CarFactory;
import br.com.tsi.utfpr.xenon.domain.user.service.UpdatersCarService;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test - Unidade - UpdatersCarService")
class UpdatersCarServiceTest {

    @Mock
    private CarFactory carFactory;

    @Test
    @DisplayName("Deve lançcar exception quando input está null")
    void shouldThrowsExceptionWhenInputIsNull() {
        var updatersCarService = UpdatersCarService.getNewInstance(null, carFactory);

        assertThrows(IllegalStateException.class, () -> updatersCarService.accept(new User()));
    }

    @Test
    @DisplayName("Deve atualizar informações do carro quando carro já cadastrado")
    void shouldHaveUpdateCarWhenCarExist() {
        var mockUser = mock(User.class);
        var mockCar = mock(Car.class);

        var input = new InputUserDto();
        input.setCarModel("new model car");
        input.setCarPlate("new plate car");

        lenient().when(mockUser.car()).thenReturn(Optional.of(mockCar));
        lenient().when(mockUser.getStatusRegistry()).thenReturn(100);
        lenient().doNothing().when(mockUser).setStatusRegistry(75);
        lenient().doNothing().when(mockUser).setCar(mockCar);
        lenient().doNothing().when(mockCar).setModel(input.getCarModel());
        lenient().doNothing().when(mockCar).setPlate(input.getCarPlate());
        lenient().doNothing().when(mockCar).setUser(mockUser);
        lenient().doNothing().when(mockCar).setDocument(Boolean.FALSE);

        var updatersCarService = UpdatersCarService.getNewInstance(input, carFactory);
        updatersCarService.accept(mockUser);

        verify(mockUser, times(2)).car();
        verify(mockUser).getStatusRegistry();
        verify(mockUser).setStatusRegistry(75);
        verify(mockUser).setCar(mockCar);

        verify(mockCar).setModel(input.getCarModel());
        verify(mockCar).setPlate(input.getCarPlate());
    }

    @Test
    @DisplayName("Deve criar um carro quando carro não cadastrado")
    void shouldHaveCreateNewCar() {
        var mockUser = mock(User.class);
        var mockCar = mock(Car.class);

        var input = new InputUserDto();
        input.setCarModel("new model car");
        input.setCarPlate("new plate car");

        lenient().when(mockUser.car()).thenReturn(Optional.empty());
        lenient().doNothing().when(mockUser).setCar(mockCar);
        lenient().when(mockUser.getStatusRegistry()).thenReturn(75);
        lenient().doNothing().when(mockUser).setStatusRegistry(100);
        when(carFactory.createCarToUser(input.getCarPlate(), input.getCarModel(), mockUser))
            .thenReturn(mockCar);

        var updatersCarService = UpdatersCarService.getNewInstance(input, carFactory);
        updatersCarService.accept(mockUser);

        verify(mockUser, times(2)).car();
        verify(mockUser).getStatusRegistry();
        verify(mockUser).setStatusRegistry(100);

        verify(carFactory).createCarToUser(input.getCarPlate(), input.getCarModel(), mockUser);
    }

    @Test
    @DisplayName("Não deve criar um carro equando input está nulo os campos carPlate e carModel")
    void shouldNotHaveCreateCar() {
        var mockUser = mock(User.class);
        var mockCar = mock(Car.class);

        var input = new InputUserDto();

        lenient().when(mockUser.car()).thenReturn(Optional.empty());

        var updatersCarService = UpdatersCarService.getNewInstance(input, carFactory);
        updatersCarService.accept(mockUser);

        verify(mockUser).car();
        verify(mockUser, never()).setCar(any());
        verify(mockUser, never()).getStatusRegistry();
        verify(mockUser, never()).setStatusRegistry(any());
        verify(mockCar, never()).setUser(any());
        verify(mockCar, never()).setModel(any());
        verify(mockCar, never()).setPlate(any());
    }

    @Test
    @DisplayName("Não deve atualizr um carro equando input está nulo os campos carPlate e carModel")
    void shouldNotHaveUpdateCar() {
        var mockUser = mock(User.class);

        var input = new InputUserDto();

        lenient().when(mockUser.car()).thenReturn(Optional.empty());

        var updatersCarService = UpdatersCarService.getNewInstance(input, carFactory);
        updatersCarService.accept(mockUser);

        assertionNotUpdateOrCreateCar(mockUser);
    }

    @Test
    @DisplayName("Não deve atualizr um carro equando input está nulo os campos carModel")
    void shouldNotHaveUpdateCarWhenCarModelIsnull() {
        var mockUser = mock(User.class);

        var input = new InputUserDto();
        input.setCarPlate("new plate car");

        lenient().when(mockUser.car()).thenReturn(Optional.empty());

        var updatersCarService = UpdatersCarService.getNewInstance(input, carFactory);
        updatersCarService.accept(mockUser);

        assertionNotUpdateOrCreateCar(mockUser);
    }

    @Test
    @DisplayName("Não deve atualizr um carro equando input está nulo os campos carPlate")
    void shouldNotHaveUpdateCarWhenCarPlateIsnull() {
        var mockUser = mock(User.class);

        var input = new InputUserDto();
        input.setCarModel("new model car");

        lenient().when(mockUser.car()).thenReturn(Optional.empty());

        var updatersCarService = UpdatersCarService.getNewInstance(input, carFactory);
        updatersCarService.accept(mockUser);

        assertionNotUpdateOrCreateCar(mockUser);
    }

    private void assertionNotUpdateOrCreateCar(User mockUser) {
        verify(mockUser).car();
        verify(mockUser, never()).setCar(any());
        verify(mockUser, never()).getStatusRegistry();
        verify(mockUser, never()).setStatusRegistry(any());
        verify(carFactory, never()).createCarToUser(any(), any(), any());
    }
}