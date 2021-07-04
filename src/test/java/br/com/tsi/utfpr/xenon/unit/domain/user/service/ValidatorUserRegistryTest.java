package br.com.tsi.utfpr.xenon.unit.domain.user.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.domain.security.repository.AccessCardRepository;
import br.com.tsi.utfpr.xenon.domain.user.exception.RegistrationException;
import br.com.tsi.utfpr.xenon.domain.user.exception.UsernameException;
import br.com.tsi.utfpr.xenon.domain.user.repository.UserRepository;
import br.com.tsi.utfpr.xenon.domain.user.service.ValidatorUserRegistry;
import br.com.tsi.utfpr.xenon.structure.dtos.ResultCheckerDto.Result;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputNewStudent;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test - Unidade - ValidatorUserRegistry")
class ValidatorUserRegistryTest {

    private static final String USERNAME_USERNAME_COM = "username@username.com";
    private static final String ERROR_PLATE = "Placa plate já cadastrada para outro usuário";
    private static final String ERROR_EMAIL =
        "Usuário email não pode ser cadastrado porque: Usuário já cadastrado";
    private static final String ERROR_USER_EXIST = "Usuário já cadastrado";

    @Mock
    private AccessCardRepository accessCardRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ValidatorUserRegistry validatorUserRegistry;

    @Test
    @DisplayName("Deve lançar Exception quando usuário já cadastrado")
    void shouldThrowsUsernameException() {
        when(accessCardRepository.exists(any())).thenReturn(Boolean.TRUE);

        var exception = assertThrows(UsernameException.class,
            () -> validatorUserRegistry.validateToInclude(USERNAME_USERNAME_COM));

        var resultUsernameCheckerDto = exception.getResultUsernameCheckerDto();

        assertEquals(Result.USER_CAN_BE_REGISTERED,
            resultUsernameCheckerDto.getResult().getValue());
        assertEquals(ERROR_USER_EXIST, resultUsernameCheckerDto.getResult().getReason());
        assertEquals(USERNAME_USERNAME_COM, resultUsernameCheckerDto.getUsername());
    }

    @Test
    @DisplayName("Deve lançar exception quando placa já existe")
    void shouldThrowsRegistrationExceptionWhenPlateExist() {
        var input = getInputNewStudent();
        var plate = input.getPlateCar();

        when(accessCardRepository.exists(any())).thenReturn(Boolean.FALSE);
        when(userRepository.existsUserByCarPlate(eq(plate))).thenReturn(Boolean.TRUE);

        var exception = assertThrows(
            RegistrationException.class,
            () -> validatorUserRegistry.validateToRegistry(input)
        );
        assertEquals(ERROR_PLATE, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exception quando validação para placa do carro já existe")
    void shouldThrowsRegistrationExceptionWhenValidationToPlateExist() {
        var input = getInputUserDto();
        var plate = input.getCarPlate();

        when(accessCardRepository.exists(any())).thenReturn(Boolean.FALSE);
        when(userRepository.existsUserByCarPlate(eq(plate))).thenReturn(Boolean.TRUE);

        var exception = assertThrows(
            RegistrationException.class,
            () -> validatorUserRegistry.validateToRegistry(input)
        );

        assertEquals(ERROR_PLATE, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exception quando validação para email já existe")
    void shouldThrowsRegistrationExceptionWhenValidationToEmailExist() {
        var input = getInputNewStudent();

        when(accessCardRepository.exists(any())).thenReturn(Boolean.TRUE);
        var exception = assertThrows(
            UsernameException.class,
            () -> validatorUserRegistry.validateToRegistry(input)
        );

        assertEquals(ERROR_EMAIL, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exception quando validação para email já existe")
    void shouldThrowsRegistrationUserExceptionWhenValidationToEmailExist() {
        var input = getInputUserDto();

        when(accessCardRepository.exists(any())).thenReturn(Boolean.TRUE);
        var exception = assertThrows(
            UsernameException.class,
            () -> validatorUserRegistry.validateToRegistry(input)
        );

        assertEquals(ERROR_EMAIL, exception.getMessage());
    }

    @Test
    @DisplayName("Deve validar cadastro com sucesso 'registro estudante'")
    void shouldHaveValidateRegistryStudents() {
        var input = getInputNewStudent();

        when(accessCardRepository.exists(any())).thenReturn(Boolean.FALSE);
        when(userRepository.existsUserByCarPlate(eq(input.getPlateCar())))
            .thenReturn(Boolean.FALSE);

        assertDoesNotThrow(() -> validatorUserRegistry.validateToRegistry(input));
    }

    @Test
    @DisplayName("Deve validar cadastro com sucesso 'registro usuário'")
    void shouldHaveValidateRegistryUser() {
        var input = getInputUserDto();

        when(accessCardRepository.exists(any())).thenReturn(Boolean.FALSE);
        when(userRepository.existsUserByCarPlate(eq(input.getCarPlate())))
            .thenReturn(Boolean.FALSE);

        assertDoesNotThrow(() -> validatorUserRegistry.validateToRegistry(input));
    }

    private static InputNewStudent getInputNewStudent() {
        var input = new InputNewStudent();
        input.setPlateCar("plate");
        input.setEmail("email");
        return input;
    }

    private static InputUserDto getInputUserDto() {
        var input = new InputUserDto();
        input.setCarPlate("plate");
        input.setUsername("email");

        return input;
    }
}
