package br.com.tsi.utfpr.xenon.unit.domain.user.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.domain.security.entity.AccessCard;
import br.com.tsi.utfpr.xenon.domain.user.aggregator.EmailSenderAdapter;
import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.domain.user.exception.UsernameException;
import br.com.tsi.utfpr.xenon.domain.user.factory.UserFactory;
import br.com.tsi.utfpr.xenon.domain.user.repository.UserRepository;
import br.com.tsi.utfpr.xenon.domain.user.service.ValidatorUserRegistry;
import br.com.tsi.utfpr.xenon.domain.user.usecase.RegistryUser;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test - Unidade - RegistryUser")
class RegistryUserTest {

    @Mock
    private ValidatorUserRegistry validatorUserRegistry;

    @Mock
    private UserFactory userFactory;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailSenderAdapter emailSenderAdapter;

    @InjectMocks
    private RegistryUser registryUser;

    @Test
    @DisplayName("Deve retornar execption quando input não passou na validação")
    void shouldThrowsExceptionWhenUsernameExist() {
        var input = new InputUserDto();

        doThrow(UsernameException.class)
            .when(validatorUserRegistry)
            .validateToRegistry(input);

        assertThrows(UsernameException.class, () -> registryUser.save(input));

        verify(userFactory, never()).createUser(any());
        verify(userRepository, never()).save(any());
        verify(emailSenderAdapter, never())
            .sendMailPasswordToCompleteRegistry(anyString(), anyString());
    }

    @Test
    @DisplayName("Deve retornar salvar usuário com sucesso")
    void shouldHaveSaveUser() {
        var input = new InputUserDto();
        var user = new User();
        var accessCard = new AccessCard();

        user.setAccessCard(accessCard);

        doNothing()
            .when(validatorUserRegistry)
            .validateToRegistry(input);
        when(userFactory.createUser(input)).thenReturn(user);

        registryUser.save(input);

        verify(userFactory).createUser(argThat(args -> StringUtils.isNotEmpty(args.getPassword())));
        verify(userRepository).save(any(User.class));
        verify(emailSenderAdapter).sendMailPasswordToCompleteRegistry(anyString(), any());
    }
}