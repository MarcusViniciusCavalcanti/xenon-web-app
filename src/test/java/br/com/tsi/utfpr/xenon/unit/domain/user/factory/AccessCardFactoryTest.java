package br.com.tsi.utfpr.xenon.unit.domain.user.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.domain.security.entity.AccessCard;
import br.com.tsi.utfpr.xenon.domain.security.entity.Role;
import br.com.tsi.utfpr.xenon.domain.security.factory.AccessCardFactory;
import br.com.tsi.utfpr.xenon.domain.security.service.RoleService;
import br.com.tsi.utfpr.xenon.domain.user.entity.TypeUser;
import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.structure.FactoryException;
import br.com.tsi.utfpr.xenon.structure.dtos.TypeUserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test - Unidade - AccessCardFactory")
class AccessCardFactoryTest {

    public static final String ERROR_MSG_ACCESS_CARD_IS_NULL =
        "Object AccessCard cannot be created for this reason: is null";
    public static final String ERROR_MSG_ROLE_IS_NULL =
        "Object List Role cannot be created for this reason: is null or is empty";

    private static final String MOCK_USER_TEST = "Mock User Test";
    private static final long ID = 1L;
    private static final LocalDate DATE = LocalDate.now();
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
    private static final String DESCRIPTION_PROPERTY_NAME = "description";
    private static final String ID_PROPERTY_NAME = "id";
    private static final String NAME_PROPERTY_NAME = "name";
    private static final int SIZE_3 = 3;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private AccessCardFactory accessCardFactory;

    @Test
    @DisplayName("Deve lançar exception na criação do dto quando AccessCard do usuário esta nulo")
    void shouldThrowsExceptionWhenAccessCardIsNull() {
        var exception = assertThrows(FactoryException.class, () -> accessCardFactory.createAccessCardDto(new User()));
        assertEquals(ERROR_MSG_ACCESS_CARD_IS_NULL, exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Deve lançar exception na criação do dto quando AccessCard do usuário esta nulo")
    void shouldThrowsExceptionWhenRoleIsNullOrEmpty(List<Role> roles) {
        var user = new User();
        var accessCard = new AccessCard();

        user.setAccessCard(accessCard);
        accessCard.setUser(user);
        accessCard.setRoles(roles);

        var exception = assertThrows(FactoryException.class, () -> accessCardFactory.createAccessCardDto(user));
        assertEquals(ERROR_MSG_ROLE_IS_NULL, exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar dto com sucesso")
    void shouldCreateDto() {
        var user = createUserBuild();
        var accessCard = createAccessCard(user);
        var roles = createListRoles();

        user.setAccessCard(accessCard);
        accessCard.setRoles(roles);

        var accessCardDto = accessCardFactory.createAccessCardDto(user);
        assertEquals(MOCK_USERNAME_COM_BR, accessCardDto.getUsername());
        assertTrue(accessCardDto.isAccountNonExpired());
        assertTrue(accessCardDto.isEnabled());
        assertTrue(accessCardDto.isCredentialsNonExpired());
        assertTrue(accessCardDto.isAccountNonLocked());

        var rolesDto = accessCardDto.getRoles();
        assertThat(rolesDto, hasSize(SIZE_3));
        assertThat(rolesDto, containsInAnyOrder(
            hasProperty(ID_PROPERTY_NAME, is(ID_ROLE_ADMIN)),
            hasProperty(ID_PROPERTY_NAME, is(ID_ROLE_OPERATOR)),
            hasProperty(ID_PROPERTY_NAME, is(ID_ROLE_DRIVE))
        ));

        assertThat(rolesDto, containsInAnyOrder(
            hasProperty(NAME_PROPERTY_NAME, is(ROLE_OPERATOR)),
            hasProperty(NAME_PROPERTY_NAME, is(ROLE_ADMIN)),
            hasProperty(NAME_PROPERTY_NAME, is(ROLE_DRIVER))
        ));

        assertThat(rolesDto, containsInAnyOrder(
            hasProperty(DESCRIPTION_PROPERTY_NAME, is(DESCRIPTION_ROLE_ADMIN)),
            hasProperty(DESCRIPTION_PROPERTY_NAME, is(DESCRIPTION_ROLE_DRIVER)),
            hasProperty(DESCRIPTION_PROPERTY_NAME, is(DESCRIPTION_ROLE_OPERATOR))
        ));

    }

    @Test
    @DisplayName("Deve retornar AccessCard para User quando invocado createAccessCardToUser()")
    void shouldReturnAccessCard() {
        var input = createInputUserDto();
        var user = createUserBuild();
        var accessCard = createAccessCard(user);
        var roles = createListRoles();

        when(roleService.verifyAndGetRoleBy(eq(input.getType()), eq(input.getAuthorities()))).thenReturn(roles);

        user.setAccessCard(accessCard);

        var result = accessCardFactory.createAccessCardToUser(input, user);

        assertNotEquals(accessCard, result);
        assertEquals(MOCK_USERNAME_COM_BR, result.getUsername());
        assertTrue(result.isAccountNonExpired());
        assertTrue(result.isEnabled());
        assertTrue(result.isCredentialsNonExpired());
        assertTrue(result.isAccountNonLocked());

        var rolesListResult = result.getRoles();
        assertThat(rolesListResult, hasSize(SIZE_3));
        assertThat(rolesListResult, containsInAnyOrder(
            hasProperty(ID_PROPERTY_NAME, is(ID_ROLE_ADMIN)),
            hasProperty(ID_PROPERTY_NAME, is(ID_ROLE_OPERATOR)),
            hasProperty(ID_PROPERTY_NAME, is(ID_ROLE_DRIVE))
        ));

        assertThat(rolesListResult, containsInAnyOrder(
            hasProperty(NAME_PROPERTY_NAME, is(ROLE_OPERATOR)),
            hasProperty(NAME_PROPERTY_NAME, is(ROLE_ADMIN)),
            hasProperty(NAME_PROPERTY_NAME, is(ROLE_DRIVER))
        ));

        assertThat(rolesListResult, containsInAnyOrder(
            hasProperty(DESCRIPTION_PROPERTY_NAME, is(DESCRIPTION_ROLE_ADMIN)),
            hasProperty(DESCRIPTION_PROPERTY_NAME, is(DESCRIPTION_ROLE_DRIVER)),
            hasProperty(DESCRIPTION_PROPERTY_NAME, is(DESCRIPTION_ROLE_OPERATOR))
        ));
    }

    @Test
    @DisplayName("Deve retonar AccessCard para Estudante")
    void shouldReturnAccessCardToStudent() {
        var user = createUserBuild();
        var driverRole = createRole(ID_ROLE_DRIVE, ROLE_DRIVER, DESCRIPTION_ROLE_DRIVER);

        when(roleService.verifyAndGetRoleBy(eq(TypeUserDto.STUDENTS), eq(List.of(1L)))).thenReturn(List.of(driverRole));

        var result = accessCardFactory.createAccessCardActiveStudentsRole(MOCK_USERNAME_COM_BR, PASSWORD, user);

        assertEquals(MOCK_USERNAME_COM_BR, result.getUsername());
        assertTrue(result.isAccountNonExpired());
        assertTrue(result.isEnabled());
        assertTrue(result.isCredentialsNonExpired());
        assertTrue(result.isAccountNonLocked());

        var rolesListResult = result.getRoles();
        assertThat(rolesListResult, hasSize(1));
        assertThat(rolesListResult, hasItem(
            hasProperty(ID_PROPERTY_NAME, is(ID_ROLE_DRIVE))
        ));

        assertThat(rolesListResult, hasItem(
            hasProperty(NAME_PROPERTY_NAME, is(ROLE_DRIVER))
        ));

        assertThat(rolesListResult, hasItem(
            hasProperty(DESCRIPTION_PROPERTY_NAME, is(DESCRIPTION_ROLE_DRIVER))
        ));
    }

    private User createUserBuild() {
        var mockUser = new User();
        mockUser.setName(MOCK_USER_TEST);
        mockUser.setId(ID);
        mockUser.setCreatedAt(DATE);
        mockUser.setUpdatedAt(DATE);
        mockUser.setTypeUser(TypeUser.SERVICE);
        mockUser.setAuthorisedAccess(Boolean.TRUE);
        mockUser.setNumberAccess(NUMBER_ACCESS);

        return mockUser;
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

    private InputUserDto createInputUserDto() {
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
}
