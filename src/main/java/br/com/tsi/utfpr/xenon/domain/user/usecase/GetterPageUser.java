package br.com.tsi.utfpr.xenon.domain.user.usecase;

import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.domain.user.factory.UserFactory;
import br.com.tsi.utfpr.xenon.domain.user.repository.UserRepository;
import br.com.tsi.utfpr.xenon.domain.user.service.ParametersGetAllSpec;
import br.com.tsi.utfpr.xenon.domain.user.service.UserSpecifications;
import br.com.tsi.utfpr.xenon.structure.data.BasicSpecification;
import br.com.tsi.utfpr.xenon.structure.dtos.UserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.ParamsSearchRequestDTO;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GetterPageUser {

    private final UserRepository userRepository;
    private final UserFactory userFactory;

    @Qualifier(UserSpecifications.QUALIFIER_GET_ALL_SPEC)
    private final BasicSpecification<User, ParametersGetAllSpec> getterAllUserSpec;

    public Page<UserDto> getAllUserByFilter(ParamsSearchRequestDTO params) {
        var sort = Sort.by(Sort.Direction.fromString(params.getOrDefaultSortDirection()), params.getOrDefaultSort());
        var pageable = PageRequest.of(params.getOrDefaultPage(), params.getOrDefaultSize(), sort);
        var paramsSpec = ParametersGetAllSpec.builder()
            .name(params.getName())
            .enabled(params.getOrDefaultEnabled())
            .profile(params.getProfile())
            .type(params.getType())
            .build();

        var spec = getterAllUserSpec.filterBy(paramsSpec);

        var userPage = userRepository.findAll(spec, pageable);
        var userDTOS = userPage.getContent().stream()
            .map(userFactory::createUserDto)
            .collect(Collectors.toList());

        return new PageImpl<>(userDTOS, pageable, userPage.getTotalElements());
    }
}
