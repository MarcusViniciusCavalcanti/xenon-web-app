package br.com.tsi.utfpr.xenon.domain.security.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.tsi.utfpr.xenon.domain.security.entity.AccessCard;
import br.com.tsi.utfpr.xenon.domain.security.entity.Role;
import br.com.tsi.utfpr.xenon.domain.user.entity.TypeUser;
import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.structure.FactoryException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {AccessCardFactory.class})
@DisplayName("Test - Unidade - AccessCardFactory")
class AccessCardFactoryTest {

    private static final String MOCK_USER_TEST = "Mock User Test";
    private static final long ID = 1L;
    private static final LocalDate DATE = LocalDate.now();
    private static final TypeUser TYPE_USER = TypeUser.SERVICE;
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

    @Autowired
    private AccessCardFactory accessCardFactory;

    @Test
    @DisplayName("Deve retonar uma AccessCard com sucesso")
    void shouldReturnAccessCardDto() {
        var mockUser = createUserBuild();
        var mockAccessCard = createAccessCard(mockUser);
        var mockListRoles = createListRoles();

        mockUser.setAccessCard(mockAccessCard);
        mockAccessCard.setUser(mockUser);
        mockAccessCard.setRoles(mockListRoles);

        var accessCardDto = accessCardFactory.createAccessCardDto(mockUser);
        var roles = accessCardDto.getRoles();

        assertNotNull(accessCardDto);
        assertNotNull(roles);

        assertEquals(MOCK_USERNAME_COM_BR, accessCardDto.getUsername());
        assertTrue(accessCardDto.isAccountNonExpired());
        assertTrue(accessCardDto.isEnabled());
        assertTrue(accessCardDto.isCredentialsNonExpired());
        assertTrue(accessCardDto.isAccountNonLocked());

        assertThat(roles, hasSize(3));
        assertThat(roles, containsInAnyOrder(
            hasProperty(ID_PROPERTY_NAME, is(ID_ROLE_ADMIN)),
            hasProperty(ID_PROPERTY_NAME, is(ID_ROLE_OPERATOR)),
            hasProperty(ID_PROPERTY_NAME, is(ID_ROLE_DRIVE))
        ));

        assertThat(roles, containsInAnyOrder(
            hasProperty(NAME_PROPERTY_NAME, is(ROLE_OPERATOR)),
            hasProperty(NAME_PROPERTY_NAME, is(ROLE_ADMIN)),
            hasProperty(NAME_PROPERTY_NAME, is(ROLE_DRIVER))
        ));

        assertThat(roles, containsInAnyOrder(
            hasProperty(DESCRIPTION_PROPERTY_NAME, is(DESCRIPTION_ROLE_ADMIN)),
            hasProperty(DESCRIPTION_PROPERTY_NAME, is(DESCRIPTION_ROLE_DRIVER)),
            hasProperty(DESCRIPTION_PROPERTY_NAME, is(DESCRIPTION_ROLE_OPERATOR))
        ));
    }

    @Test
    @DisplayName("Deve não deve retonar um AcessCardDto quando Roles é null")
    void shouldThrowsFactoryExceptionWhenRolesIsNull() {
        var mockUser = createUserBuild();
        var mockAccessCard = createAccessCard(mockUser);

        mockUser.setAccessCard(mockAccessCard);
        mockAccessCard.setUser(mockUser);

        var factoryException = assertThrows(FactoryException.class,
            () -> accessCardFactory.createAccessCardDto(mockUser));

        assertEquals(
            "Dto List Role cannot be created for this reason: is null or is empty",
            factoryException.getMessage());
    }

    @Test
    @DisplayName("Deve não deve retonar um AcessCardDto quando User é null")
    void shouldThrowsFactoryExceptionWhenUserIsNull() {
        var mockUser = createUserBuild();

        var factoryException = assertThrows(FactoryException.class,
            () -> accessCardFactory.createAccessCardDto(mockUser));

        assertEquals(
            "Dto AccessCard cannot be created for this reason: is null",
            factoryException.getMessage());
    }

    private User createUserBuild() {
        var mockUser = new User();
        mockUser.setName(MOCK_USER_TEST);
        mockUser.setId(ID);
        mockUser.setCreatedAt(DATE);
        mockUser.setUpdatedAt(DATE);
        mockUser.setTypeUser(TYPE_USER);
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
        var operatorRole = new Role();
        operatorRole.setId(ID_ROLE_OPERATOR);
        operatorRole.setName(ROLE_OPERATOR);
        operatorRole.setDescription(DESCRIPTION_ROLE_OPERATOR);

        var driverRole = new Role();
        driverRole.setId(ID_ROLE_DRIVE);
        driverRole.setName(ROLE_DRIVER);
        driverRole.setDescription(DESCRIPTION_ROLE_DRIVER);

        var adminRole = new Role();
        adminRole.setId(ID_ROLE_ADMIN);
        adminRole.setName(ROLE_ADMIN);
        adminRole.setDescription(DESCRIPTION_ROLE_ADMIN);

        return List.of(operatorRole, driverRole, adminRole);
    }
}
