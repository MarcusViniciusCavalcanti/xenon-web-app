package br.com.tsi.utfpr.xenon.unit.domain.user.usecase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.domain.security.entity.AccessCard;
import br.com.tsi.utfpr.xenon.domain.security.entity.Role;
import br.com.tsi.utfpr.xenon.domain.user.aggregator.UpdateDataUser;
import br.com.tsi.utfpr.xenon.domain.user.entity.Car;
import br.com.tsi.utfpr.xenon.domain.user.entity.TypeUser;
import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.domain.user.repository.UserRepository;
import br.com.tsi.utfpr.xenon.domain.user.service.FileService;
import br.com.tsi.utfpr.xenon.domain.user.usecase.UpdateUser;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test - Unidade - UpdateUser")
class UpdateUserTest {

    public static final String AVATAR = "avatar";
    private static final String MOCK_USER_TEST = "Mock User Test";
    private static final long ID = 1L;
    private static final LocalDateTime DATE = LocalDateTime.now();
    private static final TypeUser TYPE_USER_SERVICE = TypeUser.SERVICE;
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

    @Mock
    private FileService fileService;

    @Mock
    private UpdateDataUser updateDataUser;

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

    @Test
    @DisplayName("Deve retornar exception quando usuário não encontrado")
    void shouldThrowsExceptionWhenUserNotFound() throws IOException {
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        var mockFile = new MockMultipartFile("file", InputStream.nullInputStream());

        Assertions.assertThrows(EntityNotFoundException.class,
            () -> updateUser.updateAvatar(ID, mockFile));

        verify(fileService, never()).saveAvatar(anyLong(), any());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar false quando não possível salvar avatar no disco")
    void shouldThrowsExceptionWhenErrorInSaveDiskAvatar() throws IOException {
        var user = createUserBuild();
        var accessCard = createAccessCard(user);
        var car = createCar(user);
        var roles = createListRoles();

        accessCard.setRoles(roles);
        user.setAccessCard(accessCard);
        user.setCar(car);

        var multipartFile = new MockMultipartFile("file", InputStream.nullInputStream());

        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(fileService.saveAvatar(eq(ID), any())).thenThrow(IOException.class);

        var result = updateUser.updateAvatar(user.getId(), multipartFile);

        assertFalse(result);

        verify(userRepository).findById(eq(ID));
        verify(fileService).saveAvatar(eq(ID), any());
        verify(userRepository, never()).save(user);
    }

    @Test
    @DisplayName("Deve retornar true quando sucesso na atualização do avatar")
    void shouldReturnTrueWhenSaveDiskAvatar() throws IOException {
        var user = createUserBuild();
        var accessCard = createAccessCard(user);
        var car = createCar(user);
        var roles = createListRoles();

        accessCard.setRoles(roles);
        user.setAccessCard(accessCard);
        user.setCar(car);

        var multipartFile = new MockMultipartFile("file", InputStream.nullInputStream());

        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(fileService.saveAvatar(eq(ID), any())).thenReturn(Path.of("file"));

        var result = updateUser.updateAvatar(user.getId(), multipartFile);

        assertTrue(result);

        verify(userRepository).findById(eq(ID));
        verify(fileService).saveAvatar(eq(ID), any());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Não deve adicionar taxa de status registry quando usuário já tiver cadastrado avatar anteriormente")
    void shouldNotIncrementStatusRegistry() throws IOException {
        var mockUser = mock(User.class);

        lenient().when(mockUser.getAvatar()).thenReturn("avatar_content");
        lenient().when(mockUser.getStatusRegistry()).thenReturn(50);

        when(userRepository.findById(any())).thenReturn(Optional.of(mockUser));
        when(fileService.saveAvatar(any(), any())).thenReturn(Path.of("file"));

        var multipartFile = new MockMultipartFile("file", InputStream.nullInputStream());

        updateUser.updateAvatar(1L, multipartFile);

        verify(mockUser, never()).setStatusRegistry(any());
    }

    @Test
    @DisplayName("Deve atualizar dados do usuário")
    void shouldHaveUpdateUser() {
        var input = new InputUserDto();
        var user = new User();
        Consumer<User> consumer = actual -> Assertions.assertEquals(actual, user);

        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(updateDataUser.process(input)).thenReturn(consumer);

        updateUser.update(ID, input);

        verify(userRepository).findById(ID);
        verify(updateDataUser).process(input);

    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando usuário não encontrado")
    void shouldThrowEntityNotFoundExceptionWhenUserNotFound() {
        doReturn(Optional.empty())
            .when(userRepository)
            .findById(ID);
        doReturn((Consumer<User>) user -> System.out.println("testes"))
            .when(updateDataUser)
            .process(any());

        var exception = assertThrows(EntityNotFoundException.class,
            () -> updateUser.update(ID, new InputUserDto()));

        assertEquals("User by id 1 not found", exception.getMessage());

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException na tentativa de corfirmar documento quando o usuário não encontrado")
    void shouldThrowsEntityNotFoundExceptionInConfirmDoc() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException =
            assertThrows(EntityNotFoundException.class, () -> updateUser.confirmDocument(1L));

        assertEquals("Not confirm document, because user by id 1 not found",
            entityNotFoundException.getMessage());

        verify(userRepository).findById(any());
    }

    @Test
    @DisplayName("Deve retornar EntityNotFoundException quando usuário não tem carro cadastrado")
    void shouldThrowEntityNotFoundExceptionInCofirmDocButNotCarRegister() {
        var user = createUserBuild();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        EntityNotFoundException entityNotFoundException =
            assertThrows(EntityNotFoundException.class, () -> updateUser.confirmDocument(1L));

        assertEquals("Não é possível confirmar entrega de documentos da conta informada",
            entityNotFoundException.getMessage());

        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve atualizar o usuário com a informação de documento confirmado")
    void shouldHaveUpdateUserConfirmDoc() {
        var userMock = mock(User.class);
        var carMock = mock(Car.class);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userMock));
        when(userMock.car()).thenReturn(Optional.of(carMock));
        when(userMock.getStatusRegistry()).thenReturn(75);
        when(userMock.getConfirmDocument()).thenReturn(Boolean.TRUE);

        doNothing()
            .when(userMock)
            .setConfirmDocument(Boolean.TRUE);
        doNothing()
            .when(userMock)
            .setStatusRegistry(100);
        doNothing()
            .when(carMock)
            .setDocument(Boolean.TRUE);

        updateUser.confirmDocument(1L);

        verify(userRepository).findById(1L);
        verify(userMock).car();
        verify(carMock).setDocument(Boolean.TRUE);
        verify(userMock).setConfirmDocument(Boolean.TRUE);
        verify(userMock).getStatusRegistry();
        verify(userMock).setStatusRegistry(100);

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