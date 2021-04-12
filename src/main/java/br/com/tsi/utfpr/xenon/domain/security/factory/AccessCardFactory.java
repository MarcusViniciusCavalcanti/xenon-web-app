package br.com.tsi.utfpr.xenon.domain.security.factory;

import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.structure.FactoryException;
import br.com.tsi.utfpr.xenon.structure.dtos.AccessCardDTO;
import br.com.tsi.utfpr.xenon.structure.dtos.RoleDTO;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class AccessCardFactory {

    public AccessCardDTO createAccessCardDto(User user) {
        var accessCard = user.getAccessCard();

        if (Objects.isNull(accessCard)) {
            throw new FactoryException("AccessCard", "is null");
        }

        if (Objects.isNull(accessCard.getRoles()) || accessCard.getRoles().isEmpty()) {
            throw new FactoryException("List Role", "is null or is empty");
        }

        var rolesDto = accessCard.getRoles().stream()
            .map(role -> RoleDTO.builder()
                .id(role.getId())
                .description(role.getDescription())
                .name(role.getName())
                .build())
            .collect(Collectors.toList());

        return AccessCardDTO.builder()
            .username(accessCard.getUsername())
            .accountNonExpired(accessCard.isAccountNonExpired())
            .accountNonLocked(accessCard.isAccountNonLocked())
            .credentialsNonExpired(accessCard.isCredentialsNonExpired())
            .enabled(accessCard.isEnabled())
            .roles(rolesDto)
            .build();
    }
}
