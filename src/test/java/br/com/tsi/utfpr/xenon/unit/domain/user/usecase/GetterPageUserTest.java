package br.com.tsi.utfpr.xenon.unit.domain.user.usecase;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.domain.user.factory.UserFactory;
import br.com.tsi.utfpr.xenon.domain.user.repository.UserRepository;
import br.com.tsi.utfpr.xenon.domain.user.service.ParametersGetAllSpec;
import br.com.tsi.utfpr.xenon.domain.user.usecase.GetterPageUser;
import br.com.tsi.utfpr.xenon.structure.data.BasicSpecification;
import br.com.tsi.utfpr.xenon.structure.dtos.UserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.ParamsSearchRequestDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test - Unidade - GetterPageUser")
class GetterPageUserTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserFactory userFactory;

    @Mock
    private BasicSpecification<User, ParametersGetAllSpec> getterAllUserSpec;

    @InjectMocks
    private GetterPageUser getterPageUser;

    @Test
    @DisplayName("Deve retonar com suceseo Page com lista de usuários")
    void shouldReturnAllUserByFilter() {
        var mockSpecifications = mock(Specification.class);
        var params = new ParamsSearchRequestDto();
        var userPage = mock(Page.class);

        when(getterAllUserSpec.filterBy(any(ParametersGetAllSpec.class)))
            .thenReturn(mockSpecifications);
        when(userRepository.findAll(eq(mockSpecifications), any(Pageable.class)))
            .thenReturn(userPage);
        when(userPage.getContent()).thenReturn(List.of(new User(), new User()));
        when(userFactory.createUserDto(any(User.class))).thenReturn(UserDto.builder().build());

        var page = getterPageUser.getAllUserByFilter(params);
        assertNotNull(page);

        var verifyInOrder = inOrder(getterAllUserSpec, userRepository, userFactory);

        verifyInOrder.verify(getterAllUserSpec).filterBy(any(ParametersGetAllSpec.class));
        verifyInOrder.verify(userRepository).findAll(eq(mockSpecifications), any(Pageable.class));
        verifyInOrder.verify(userFactory, times(2)).createUserDto(any(User.class));

    }
}
