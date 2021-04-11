package br.com.tsi.utfpr.xenon.domain.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.domain.security.entity.AccessCard;
import br.com.tsi.utfpr.xenon.domain.security.repository.AccessCardRepository;
import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@DisplayName("Test - Unidade UserDetails")
@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    public static final String BELTRANO = "beltrano";
    @Mock
    private AccessCardRepository accessCardRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException quando não encontrar usuário")
    void shouldThrowsUsernameNotFoundExceptionWhenUserNotFound() {
        when(accessCardRepository.findByUsername(eq(BELTRANO))).thenReturn(Optional.empty());

        var usernameNotFoundException = assertThrows(
            UsernameNotFoundException.class,
            () -> userDetailsService.loadUserByUsername(BELTRANO)
        );

        assertEquals("this username: 'beltrano' not found", usernameNotFoundException.getMessage());
    }

    @Test
    @DisplayName("Deve retonar AccessCard do usuário")
    void shouldReturnAccessCardToUserSuccessfully() {
        var accessCard = Mockito.mock(AccessCard.class);
        var user = mock(User.class);

        when(accessCardRepository.findByUsername(eq(BELTRANO))).thenReturn(Optional.of(accessCard));
        when(accessCard.getUser()).thenReturn(user);

        userDetailsService.loadUserByUsername(BELTRANO);

        verify(accessCardRepository).findByUsername(BELTRANO);
    }
}
