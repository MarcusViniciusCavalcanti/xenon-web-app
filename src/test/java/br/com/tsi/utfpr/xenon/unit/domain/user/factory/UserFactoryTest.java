package br.com.tsi.utfpr.xenon.unit.domain.user.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.domain.security.entity.AccessCard;
import br.com.tsi.utfpr.xenon.domain.security.entity.Role;
import br.com.tsi.utfpr.xenon.domain.security.factory.AccessCardFactory;
import br.com.tsi.utfpr.xenon.domain.security.repository.RoleRepository;
import br.com.tsi.utfpr.xenon.domain.security.service.RoleService;
import br.com.tsi.utfpr.xenon.domain.user.entity.Car;
import br.com.tsi.utfpr.xenon.domain.user.entity.TypeUser;
import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.domain.user.factory.CarFactory;
import br.com.tsi.utfpr.xenon.domain.user.factory.UserFactory;
import br.com.tsi.utfpr.xenon.structure.dtos.TypeUserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputNewStudent;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest(classes = {
    AccessCardFactory.class,
    UserFactory.class,
    CarFactory.class,
    RoleService.class,
    BCryptPasswordEncoder.class
})
@DisplayName("Test - Unidade - UserFactory")
class UserFactoryTest {

    private static final String MOCK_USER_TEST = "Mock User Test";
    private static final long ID = 1L;
    private static final LocalDateTime DATE = LocalDateTime.now();
    private static final TypeUser TYPE_USER_SERVICE = TypeUser.SERVICE;
    private static final TypeUser TYPE_USER_STUDENTS = TypeUser.STUDENTS;
    private static final String TRANSLATE_TYPE_USER = "Servidor";
    private static final int NUMBER_ACCESS = 100;
    private static final String MOCK_USERNAME_COM_BR = "mock@username.com.br";
    private static final String PASSWORD = "1234567";
    private static final String ROLE_OPERATOR = "ROLE_OPERATOR";
    private static final String DESCRIPTION_ROLE_OPERATOR = "Perfil Operador";
    private static final String ROLE_DRIVER = "ROLE_DRIVER";
    private static final String DESCRIPTION_ROLE_DRIVER = "Perfil Motorista";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String DESCRIPTION_ROLE_ADMIN = "Perfil Administrador";
    private static final long ID_ROLE_OPERATOR = 1L;
    private static final long ID_ROLE_DRIVE = 2L;
    private static final long ID_ROLE_ADMIN = 3L;
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();
    private static final String MODEL_CAR = "Model Car";
    private static final String PLATE_CAR = "Plate Car";
    public static final String AVATAR = "avatar";
    private static final int STATUS_REGISTRY = 100;

    @Autowired
    private UserFactory userFactory;

    @MockBean
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Deve criar UserDto com sucesso sem carro cadastrado")
    void shouldReturnUserDtoWithoutCar() {
        var mockUser = setupMocksDefault();
        var userDto = userFactory.createUserDto(mockUser);

        assertEquals(MOCK_USER_TEST, userDto.getName());
        assertEquals(ID, userDto.getId());
        assertEquals(TYPE_USER_SERVICE.name(), userDto.getTypeUser().name());
        assertEquals(TRANSLATE_TYPE_USER, userDto.getTypeUser().getTranslaterName());
        assertTrue(userDto.getAuthorisedAcces());
        assertEquals(NUMBER_ACCESS, userDto.getNumberAccess());
        assertEquals(STATUS_REGISTRY, userDto.getStatusRegistry());
        assertNull(userDto.getCar());
    }

    @Test
    @DisplayName("Deve criar UserDto com sucesso com carro cadastrado")
    void shouldReturnUserDto() {
        var mockUser = setupMocksDefault();
        var mockCar = createCar(mockUser);
        mockUser.setCar(mockCar);

        var userDto = userFactory.createUserDto(mockUser);
        assertEquals(MOCK_USER_TEST, userDto.getName());
        assertEquals(ID, userDto.getId());
        assertEquals(TYPE_USER_SERVICE.name(), userDto.getTypeUser().name());
        assertEquals(TRANSLATE_TYPE_USER, userDto.getTypeUser().getTranslaterName());
        assertTrue(userDto.getAuthorisedAcces());
        assertEquals(NUMBER_ACCESS, userDto.getNumberAccess());
        assertEquals(STATUS_REGISTRY, userDto.getStatusRegistry());
        assertEquals(AVATAR, userDto.getAvatar());

        var carDto = userDto.getCar();
        assertEquals(MODEL_CAR, carDto.getModel());
        assertEquals(PLATE_CAR, carDto.getPlate());
        assertEquals(ID, carDto.getId());
        assertEquals(LOCAL_DATE_TIME, carDto.getLastAccess());
    }

    @ParameterizedTest
    @MethodSource("providerArgsWithoutCar")
    @DisplayName("Deve criar e retonar um usuário sem carro cadastrado")
    void shouldReturnUserWithoutCar(InputUserDto input) {
        var driverRole = createRole(ID_ROLE_DRIVE, ROLE_DRIVER, DESCRIPTION_ROLE_DRIVER);

        when(roleRepository.findAllById(anyList())).thenReturn(List.of(driverRole));

        var user = userFactory.createUser(input);

        assertEquals(MOCK_USER_TEST, user.getName());
        assertNull(user.getId());
        assertEquals(TYPE_USER_STUDENTS.name(), user.getTypeUser().name());
        assertNull(user.getNumberAccess());
        assertNull(user.getAvatar());
        assertNull(user.getCar());
        assertEquals(25, user.getStatusRegistry());
    }

    @Test
    @DisplayName("Deve criar e retonar um usuário com carro cadastrado")
    void shouldReturnUserWithCar() {
        var input = createInputUserDto();
        input.setCarModel(MODEL_CAR);
        input.setCarPlate(PLATE_CAR);

        var driverRole = createRole(ID_ROLE_DRIVE, ROLE_DRIVER, DESCRIPTION_ROLE_DRIVER);

        when(roleRepository.findAllById(anyList())).thenReturn(List.of(driverRole));

        var user = userFactory.createUser(input);

        assertEquals(MOCK_USER_TEST, user.getName());
        assertNull(user.getId());
        assertEquals(TYPE_USER_STUDENTS.name(), user.getTypeUser().name());
        assertNull(user.getNumberAccess());
        assertNull(user.getAvatar());
        assertEquals(50, user.getStatusRegistry());

        var car = user.getCar();
        assertEquals(MODEL_CAR, car.getModel());
        assertEquals(PLATE_CAR, car.getPlate());
        assertNull(car.getId());
        assertNull(car.getLastAccess());
    }

    @Test
    @DisplayName("Deve criar e retonar um usuário do tipo estudante")
    void shouldReturnUserStudent() {
        var input = new InputNewStudent();
        input.setToken("token");
        input.setPassword(PASSWORD);
        input.setConfirmPassword(PASSWORD);
        input.setEmail(MOCK_USERNAME_COM_BR);
        input.setPlateCar(PLATE_CAR);
        input.setModelCar(MODEL_CAR);
        input.setName(MOCK_USER_TEST);

        var driverRole = createRole(ID_ROLE_DRIVE, ROLE_DRIVER, DESCRIPTION_ROLE_DRIVER);

        when(roleRepository.findAllById(anyList())).thenReturn(List.of(driverRole));

        var user = userFactory.createTypeStudent(input);

        assertEquals(MOCK_USER_TEST, user.getName());
        assertNull(user.getId());
        assertEquals(TYPE_USER_STUDENTS.name(), user.getTypeUser().name());
        assertNull(user.getNumberAccess());
        assertEquals(25, user.getStatusRegistry());
        assertNull(user.getAvatar());

        var car = user.getCar();
        assertEquals(MODEL_CAR, car.getModel());
        assertEquals(PLATE_CAR, car.getPlate());
        assertNull(car.getId());
        assertNull(car.getLastAccess());
    }

    private User setupMocksDefault() {
        var mockUser = createUserBuild();
        var mockAccessCard = createAccessCard(mockUser);
        var mockListRoles = createListRoles();

        mockUser.setAccessCard(mockAccessCard);
        mockAccessCard.setUser(mockUser);
        mockAccessCard.setRoles(mockListRoles);

        return mockUser;
    }

    private User createUserBuild() {
        var mockUser = new User();
        mockUser.setName(MOCK_USER_TEST);
        mockUser.setId(ID);
        mockUser.setCreatedAt(DATE);
        mockUser.setUpdatedAt(DATE);
        mockUser.setTypeUser(TYPE_USER_SERVICE);
        mockUser.setAuthorisedAccess(Boolean.TRUE);
        mockUser.setNumberAccess(NUMBER_ACCESS);
        mockUser.setStatusRegistry(STATUS_REGISTRY);
        mockUser.setAvatar(AVATAR);

        return mockUser;
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

    private AccessCard createAccessCard(User user) {
        var mockAccessCard = new AccessCard();
        mockAccessCard.setUser(user);
        mockAccessCard.setId(ID);
        mockAccessCard.setUsername(MOCK_USERNAME_COM_BR);
        mockAccessCard.setPassword(PASSWORD);
        mockAccessCard.setAccountNonExpired(Boolean.TRUE);
        mockAccessCard.setAccountNonLocked(Boolean.TRUE);
        mockAccessCard.setCredentialsNonExpired(Boolean.TRUE);
        mockAccessCard.setEnabled(Boolean.TRUE);
        mockAccessCard.setCreatedAt(DATE);
        mockAccessCard.setUpdatedAt(DATE);

        return mockAccessCard;
    }

    private List<Role> createListRoles() {
        var operatorRole = createRole(ID_ROLE_OPERATOR, ROLE_OPERATOR, DESCRIPTION_ROLE_OPERATOR);
        var driverRole = createRole(ID_ROLE_DRIVE, ROLE_DRIVER, DESCRIPTION_ROLE_DRIVER);
        var adminRole = createRole(ID_ROLE_ADMIN, ROLE_ADMIN, DESCRIPTION_ROLE_ADMIN);

        return List.of(operatorRole, driverRole, adminRole);
    }

    private Role createRole(long idRole, String roleName, String roleDescription) {
        var operatorRole = new Role();
        operatorRole.setId(idRole);
        operatorRole.setName(roleName);
        operatorRole.setDescription(roleDescription);
        return operatorRole;
    }

    private static InputUserDto createInputUserDto() {
        var input = new InputUserDto();
        input.setUsername(MOCK_USERNAME_COM_BR);
        input.setName(MOCK_USER_TEST);

        input.setAccountNonExpired(Boolean.TRUE);
        input.setAccountNonExpired(Boolean.TRUE);
        input.setAccountNonLocked(Boolean.TRUE);
        input.setCredentialsNonExpired(Boolean.TRUE);
        input.setEnabled(Boolean.TRUE);
        input.setPassword(PASSWORD);
        input.setAuthorizedAccess(Boolean.TRUE);
        input.setType(TypeUserDto.STUDENTS);
        input.setAuthorities(TypeUser.STUDENTS.getAllowedProfiles());

        return input;
    }

    private static Stream<Arguments> providerArgsWithoutCar() {
        var input1 = createInputUserDto();
        input1.setCarModel(MODEL_CAR);

        var input2 = createInputUserDto();
        input2.setCarPlate(PLATE_CAR);

        return Stream.of(Arguments.of(input1, input2));
    }
}
