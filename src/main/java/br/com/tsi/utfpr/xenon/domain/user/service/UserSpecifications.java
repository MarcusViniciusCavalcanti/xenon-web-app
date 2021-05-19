package br.com.tsi.utfpr.xenon.domain.user.service;

import br.com.tsi.utfpr.xenon.domain.security.entity.AccessCard_;
import br.com.tsi.utfpr.xenon.domain.security.entity.Role_;
import br.com.tsi.utfpr.xenon.domain.user.entity.TypeUser;
import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.domain.user.entity.User_;
import java.util.Locale;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public final class UserSpecifications {

    private static final String WILD_CARD = "%";

    private UserSpecifications() {}

    public static Specification<User> nameOrUsernameContains(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        return (root, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.or(
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(User_.name)),
                    criteriaBuilder
                        .lower(criteriaBuilder.literal(concatenateKeyValueWithWildCard(value)))
                ),
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(User_.accessCard).get(AccessCard_.username)),
                    criteriaBuilder
                        .lower(criteriaBuilder.literal(concatenateKeyValueWithWildCard(value)))
                )
            );
    }

    public static Specification<User> enabled(Boolean value) {
        if (Objects.isNull(value)) {
            return null;
        }

        return (root, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.equal(root.get(User_.accessCard).get(AccessCard_.enabled), value);
    }

    public static Specification<User> type(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        var type = TypeUser.valueOf(value);
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
            .equal(root.get(User_.typeUser), type);
    }

    public static Specification<User> profile(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        return (root, criteriaQuery, criteriaBuilder) -> {
            var join = root.join(User_.accessCard).join(AccessCard_.roles);

            return criteriaBuilder.like(
                criteriaBuilder.lower(join.get(Role_.name)),
                criteriaBuilder
                    .lower(criteriaBuilder.literal(concatenateKeyValueWithWildCard(value)))
            );
        };
    }

    private static String concatenateKeyValueWithWildCard(String value) {
        return WILD_CARD + value.toLowerCase(Locale.getDefault()) + WILD_CARD;
    }
}
