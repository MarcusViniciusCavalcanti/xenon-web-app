package br.com.tsi.utfpr.xenon.domain.user.usecase;

import br.com.tsi.utfpr.xenon.domain.user.config.bean.SpecificationConfiguration;
import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.domain.user.factory.UserFactory;
import br.com.tsi.utfpr.xenon.domain.user.repository.UserRepository;
import br.com.tsi.utfpr.xenon.domain.user.service.ParametersGetAllSpec;
import br.com.tsi.utfpr.xenon.structure.data.BasicSpecification;
import br.com.tsi.utfpr.xenon.structure.dtos.UserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.ParamsSearchRequestDto;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class GetterPageUser {

    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final BasicSpecification<User, ParametersGetAllSpec> getterAllUserSpec;

    public GetterPageUser(
        UserRepository userRepository,
        UserFactory userFactory,
        @Qualifier(SpecificationConfiguration.QUALIFIER_GET_ALL_SPEC)
            BasicSpecification<User, ParametersGetAllSpec> getterAllUserSpec) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.getterAllUserSpec = getterAllUserSpec;
    }

    public Page<UserDto> getAllUserByFilter(ParamsSearchRequestDto params) {
        var sort = Sort.by(Sort.Direction.fromString(params.getOrDefaultSortDirection()),
            params.getOrDefaultSort());
        var pageable = PageRequest.of(params.getOrDefaultPage(), params.getOrDefaultSize(), sort);
        var paramsSpec = ParametersGetAllSpec.builder()
            .name(params.getName())
            .enabled(params.getOrDefaultEnabled())
            .profile(params.getProfile())
            .type(params.getType())
            .build();

        var spec = getterAllUserSpec.filterBy(paramsSpec);

        var userPage = userRepository.findAll(spec, pageable);
        var users = userPage.getContent().stream()
            .map(userFactory::createUserDto)
            .collect(Collectors.toList());

        return new PageImpl<>(users, pageable, userPage.getTotalElements());
    }
}
