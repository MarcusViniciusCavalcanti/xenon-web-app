package br.com.tsi.utfpr.xenon.domain.user.service;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.domain.security.entity.AccessCard_;
import br.com.tsi.utfpr.xenon.domain.security.entity.Role_;
import br.com.tsi.utfpr.xenon.domain.user.entity.TypeUser;
import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.domain.user.entity.User_;
import java.util.Locale;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@DisplayName("Test - Unidade - UserSpecification")
@ExtendWith(MockitoExtension.class)
class UserSpecificationsTest {

    private static final String WILD_CARD = "%";

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private CriteriaQuery query;

    @Mock
    private Root<User> root;

    @Test
    @DisplayName("Deve retonar Specification de nomes ou username null quando não enviado o argumento")
    void shouldReturnSpecificationNullWhenValueIsNull() {
        var predicate = UserSpecifications.nameOrUsernameContains(null);
        assertNull(predicate);
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", ""})
    @DisplayName("Deve retonar Specification de nomes ou username null quando enviado o argumento é invalido")
    void shouldReturnSpecificationNullWhenValueIsInvalid(String value) {
        var predicate = UserSpecifications.nameOrUsernameContains(value);
        assertNull(predicate);
    }

    @Test
    @DisplayName("Deve retonar Specification com nome ou username")
    void shouldReturnSpecificationUserOrName() {
        var pathAccessCard = mock(Path.class);
        var pathUsername = mock(Path.class);
        var pathUser = mock(Path.class);

        when(root.get(User_.name)).thenReturn(pathUser);
        when(root.get(User_.accessCard)).thenReturn(pathAccessCard);
        when(pathAccessCard.get(AccessCard_.username)).thenReturn(pathUsername);

        var value = "name";
        UserSpecifications.nameOrUsernameContains(value).toPredicate(root, query, builder);

        verify(builder).lower(root.get(User_.name));
        verify(builder).lower(root.get(User_.accessCard).get(AccessCard_.username));
        verify(builder, times(2)).lower(builder.literal(concatenateKeyValueWithWildCard(value)));
        verify(builder).like(builder.lower(root.get(User_.name)),
            builder.lower(builder.literal(concatenateKeyValueWithWildCard(value))));
        verify(builder).like(builder.lower(root.get(User_.accessCard).get(AccessCard_.username)),
            builder.lower(builder.literal(concatenateKeyValueWithWildCard(value))));
        verify(builder).or(builder.like(builder.lower(root.get(User_.name)),
            builder.lower(builder.literal(concatenateKeyValueWithWildCard(value)))),
            builder.like(builder.lower(root.get(User_.accessCard).get(AccessCard_.username)),
                builder.lower(builder.literal(concatenateKeyValueWithWildCard(value)))
            ));
    }

    @Test
    @DisplayName("Deve retonar Specification de conta ativa null quando não enviado o argumento")
    void shouldReturnSpecificationAccountEnableNullWhenValueIsNull() {
        var predicate = UserSpecifications.enabled(null);
        assertNull(predicate);
    }

    @Test
    @DisplayName("Deve retonar Specification com conta ativa ou não")
    void shouldReturnSpecificationEnable() {
        var pathAccessCard = mock(Path.class);
        var pathAccountEnable = mock(Path.class);

        when(root.get(User_.accessCard)).thenReturn(pathAccessCard);
        when(pathAccessCard.get(AccessCard_.enabled)).thenReturn(pathAccountEnable);

        UserSpecifications.enabled(Boolean.TRUE).toPredicate(root, query, builder);

        verify(builder).equal(root.get(User_.accessCard).get(AccessCard_.enabled), Boolean.TRUE);
    }

    @Test
    @DisplayName("Deve retonar Specification de tipos de usuários null quando não enviado o argumento")
    void shouldReturnSpecificationTypeUserNullWhenValueIsNull() {
        var predicate = UserSpecifications.type(null);
        assertNull(predicate);
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", ""})
    @DisplayName("Deve retonar Specification de tipos de usuários null quando enviado o argumento é invalido")
    void shouldReturnSpecificationTypeUserNullWhenValueIsInvalid(String value) {
        var predicate = UserSpecifications.type(value);
        assertNull(predicate);
    }

    @Test
    @DisplayName("Deve retonar Specification de tipos de usuários")
    void shouldReturnSpecificationType() {
        var pathTypeUser = mock(Path.class);

        var typeUser = TypeUser.SERVICE;
        when(root.get(User_.typeUser)).thenReturn(pathTypeUser);

        UserSpecifications.type(typeUser.name()).toPredicate(root, query, builder);

        verify(builder).equal(root.get(User_.typeUser), typeUser);
    }

    @Test
    @DisplayName("Deve retonar Specification de perfil null quando não enviado o argumento")
    void shouldReturnSpecificationProfileNullWhenValueIsNull() {
        var predicate = UserSpecifications.profile(null);
        assertNull(predicate);
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", ""})
    @DisplayName("Deve retonar Specification de perfil null quando enviado o argumento é invalido")
    void shouldReturnSpecificationProfilerNullWhenValueIsInvalid(String value) {
        var predicate = UserSpecifications.profile(value);
        assertNull(predicate);
    }

    @Test
    @DisplayName("Deve retonar Specification de perfil")
    void shouldReturnSpecificationProfile() {
        var pathRole = mock(Path.class);
        var joinAccessCard = mock(Join.class);
        var joinRole = mock(ListJoin.class);
        var value = "ROLE_NAME";

        when(root.join(User_.accessCard)).thenReturn(joinAccessCard);
        when(joinAccessCard.join(AccessCard_.roles)).thenReturn(joinRole);
        when(joinRole.get(Role_.name)).thenReturn(pathRole);

        UserSpecifications.profile(value).toPredicate(root, query, builder);

        verify(builder).lower(joinRole.get(Role_.name));
        verify(builder).lower(builder.literal(concatenateKeyValueWithWildCard(value)));
        verify(builder).like(builder.lower(joinRole.get(Role_.name)),
            builder.lower(builder.literal(concatenateKeyValueWithWildCard(value))));
    }

    private String concatenateKeyValueWithWildCard(String value) {
        return WILD_CARD + value.toLowerCase(Locale.getDefault()) + WILD_CARD;
    }
}
