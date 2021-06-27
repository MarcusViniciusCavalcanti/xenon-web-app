package br.com.tsi.utfpr.xenon.unit.domain.user.usecase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.domain.security.entity.AccessCard;
import br.com.tsi.utfpr.xenon.domain.security.entity.Role;
import br.com.tsi.utfpr.xenon.domain.user.entity.Car;
import br.com.tsi.utfpr.xenon.domain.user.entity.TypeUser;
import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.domain.user.repository.UserRepository;
import br.com.tsi.utfpr.xenon.domain.user.usecase.UpdateUser;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateUserTest {

    public static final String AVATAR = "avatar";
    private static final String MOCK_USER_TEST = "Mock User Test";
    private static final long ID = 1L;
    private static final LocalDate DATE = LocalDate.now();
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
    private static final int STATUS_REGISTRY = 100;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpdateUser updateUser;

    @Test
    @DisplayName("Deve lançar exception EntityNotFoundException quando usuário não encontrado")
    void shouldThrowsEntityNotFoundWhenUserNotFoundInDatabase() {
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> updateUser.findUserUpdate(ID));
    }

    @Test
    @DisplayName("Deve retornar um objeto InputUserDto preenchi com os dodos do usuário")
    void shouldReturnUserUpdateInWrapperInputUserDto() {
        var user = createUserBuild();
        var accessCard = createAccessCard(user);
        var car = createCar(user);
        var roles = createListRoles();

        accessCard.setRoles(roles);
        user.setAccessCard(accessCard);
        user.setCar(car);

        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        var userUpdate = updateUser.findUserUpdate(ID);

        verify(userRepository).findById(ID);

        assertEquals(MODEL_CAR, userUpdate.getCarModel());
        assertEquals(PLATE_CAR, userUpdate.getCarPlate());
        assertEquals(MOCK_USER_TEST, userUpdate.getName());
        assertEquals(TYPE_USER_SERVICE.name(), userUpdate.getType().name());
        assertEquals(MOCK_USERNAME_COM_BR, userUpdate.getUsername());

        assertThat(userUpdate.getAuthorities(), containsInAnyOrder(1L, 2L, 3L));

        assertTrue(userUpdate.isAccountNonExpired());
        assertTrue(userUpdate.isAuthorizedAccess());
        assertTrue(userUpdate.isAccountNonLocked());
        assertTrue(userUpdate.isCredentialsNonExpired());
        assertTrue(userUpdate.isEnabled());
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
}