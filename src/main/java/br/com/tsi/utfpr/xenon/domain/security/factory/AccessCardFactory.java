package br.com.tsi.utfpr.xenon.domain.security.factory;

import br.com.tsi.utfpr.xenon.domain.security.entity.AccessCard;
import br.com.tsi.utfpr.xenon.domain.security.service.RoleService;
import br.com.tsi.utfpr.xenon.domain.user.entity.User;
import br.com.tsi.utfpr.xenon.structure.FactoryException;
import br.com.tsi.utfpr.xenon.structure.dtos.AccessCardDto;
import br.com.tsi.utfpr.xenon.structure.dtos.RoleDTO;
import br.com.tsi.utfpr.xenon.structure.dtos.TypeUserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccessCardFactory {

    private final RoleService roleService;

    private final BCryptPasswordEncoder passwordEncoder;

    public AccessCardDto createAccessCardDto(User user) {
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

        return AccessCardDto.builder()
            .username(accessCard.getUsername())
            .accountNonExpired(accessCard.isAccountNonExpired())
            .accountNonLocked(accessCard.isAccountNonLocked())
            .credentialsNonExpired(accessCard.isCredentialsNonExpired())
            .enabled(accessCard.isEnabled())
            .roles(rolesDto)
            .build();
    }

    public AccessCard createAccessCardToUser(InputUserDto inputUserDto, User user) {
        var newAccessCard = createNewAccessCard(
            inputUserDto.getUsername(),
            inputUserDto.getPassword(),
            user
        );

        configureAccess(
            newAccessCard,
            inputUserDto.isAccountNonLocked(),
            inputUserDto.isAccountNonExpired(),
            inputUserDto.isEnabled(),
            inputUserDto.isCredentialsNonExpired()
        );

        var roles =
            roleService.verifyAndGetRoleBy(inputUserDto.getType(), inputUserDto.getAuthorities());
        newAccessCard.setRoles(roles);

        return newAccessCard;
    }

    public AccessCard createAccessCardActiveStudentsRole(String email, String pass, User user) {
        var newAccessCard = createNewAccessCard(email, pass, user);

        configureAccess(newAccessCard, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        var roles =
            roleService.verifyAndGetRoleBy(TypeUserDto.STUDENTS, List.of(1L));
        newAccessCard.setRoles(roles);

        return newAccessCard;
    }

    private AccessCard createNewAccessCard(String email, String pass, User user) {
        var newAccessCard = new AccessCard();

        newAccessCard.setUsername(email);
        newAccessCard.setPassword(passwordEncoder.encode(pass));
        newAccessCard.setUser(user);

        return newAccessCard;
    }

    private void configureAccess(AccessCard accessCard, boolean isAccountNonLocked,
        boolean isAccountNonExpired, boolean isEnabled, boolean isCredentialsNonExpired) {
        accessCard.setAccountNonLocked(isAccountNonLocked);
        accessCard.setAccountNonExpired(isAccountNonExpired);
        accessCard.setEnabled(isEnabled);
        accessCard.setCredentialsNonExpired(isCredentialsNonExpired);
    }
}
