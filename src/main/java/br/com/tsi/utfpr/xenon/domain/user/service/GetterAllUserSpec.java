package br.com.tsi.utfpr.xenon.domain.user.service;

import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.structure.data.BasicSpecification;
import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.domain.Specification;

public class GetterAllUserSpec implements BasicSpecification<User, ParametersGetAllSpec> {

    @Override
    public Specification<User> filterBy(@NotNull final ParametersGetAllSpec spec) {
        return (root, query, builder) -> {
            query.distinct(true);
            return Specification.where(
                UserSpecifications.nameOrUsernameContains(spec.getName()))
                .and(UserSpecifications.enabled(spec.getEnabled()))
                .and(UserSpecifications.type(spec.getType()))
                .and(UserSpecifications.profile(spec.getProfile()))
                .toPredicate(root, query, builder);
        };
    }
}
