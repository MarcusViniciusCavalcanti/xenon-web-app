package br.com.tsi.utfpr.xenon.unit.domain.user.usecase;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.domain.security.entity.AccessCard;
import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.domain.user.factory.UserFactory;
import br.com.tsi.utfpr.xenon.domain.user.repository.UserRepository;
import br.com.tsi.utfpr.xenon.domain.user.usecase.GetterCurrentUser;
import br.com.tsi.utfpr.xenon.structure.dtos.UserDto;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test - Unidade - GetterCurrentUser")
class GetterCurrentUserTest {

    @Mock
    private UserFactory userFactory;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetterCurrentUser getterCurrentUser;

    @Test
    @DisplayName("Deve retonar usuário logado")
    void shouldReturnCurrentUser() {
        var authentication = mock(Authentication.class);
        var securityContext = mock(SecurityContext.class);
        var accessCard = mock(AccessCard.class);
        var user = mock(User.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(accessCard);
        when(accessCard.getUser()).thenReturn(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userFactory.createUserDto(user)).thenReturn(UserDto.builder().build());

        SecurityContextHolder.setContext(securityContext);

        getterCurrentUser.currentUser();

        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
        verify(accessCard).getUser();
        verify(userFactory).createUserDto(user);
    }

    @Test
    @DisplayName("Deve lançar exception quando usuário não encontrado")
    void shouldThrowsException() {
        var authentication = mock(Authentication.class);
        var securityContext = mock(SecurityContext.class);
        var accessCard = mock(AccessCard.class);
        var user = mock(User.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(accessCard);
        when(accessCard.getUser()).thenReturn(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        SecurityContextHolder.setContext(securityContext);

        Assertions
            .assertThrows(EntityNotFoundException.class, () -> getterCurrentUser.currentUser());

        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
        verify(accessCard).getUser();
        verify(userFactory, never()).createUserDto(any());
    }
}