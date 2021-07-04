package br.com.tsi.utfpr.xenon.domain.security.service;

import br.com.tsi.utfpr.xenon.domain.security.entity.Role;
import br.com.tsi.utfpr.xenon.domain.security.exception.AuthoritiesNotAllowedException;
import br.com.tsi.utfpr.xenon.domain.security.repository.RoleRepository;
import br.com.tsi.utfpr.xenon.domain.user.entity.TypeUser;
import br.com.tsi.utfpr.xenon.structure.dtos.RoleDTO;
import br.com.tsi.utfpr.xenon.structure.dtos.TypeUserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleService {

    private final RoleRepository roleRepository;

    public List<Role> verifyAndGetRoleBy(TypeUserDto typeUser, List<Long> authorities) {
        if (Boolean.TRUE.equals(verify(typeUser, authorities))) {
            throw new AuthoritiesNotAllowedException(typeUser.getTranslaterName());
        }

        return roleRepository.findAllById(authorities);
    }

    public List<Role> verifyAndGetRoleBy(InputUserDto input) {
        if (Boolean.TRUE.equals(verify(input.getType(), input.getAuthorities()))) {
            throw new AuthoritiesNotAllowedException(input);
        }

        return roleRepository.findAllById(input.getAuthorities());
    }

    private Boolean verify(TypeUserDto typeUser, List<Long> authorities) {
        if (typeUser == TypeUserDto.STUDENTS || typeUser == TypeUserDto.SPEAKER) {
            var allowedProfiles = TypeUser.valueOf(typeUser.name()).getAllowedProfiles();

            var size = authorities.stream()
                .filter(value -> !allowedProfiles.contains(value))
                .count();

            if (size != 0) {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    public List<RoleDTO> getAll() {
        return roleRepository.findAll().stream()
            .map(buildRoleDto())
            .collect(Collectors.toList());
    }

    public static Function<Role, RoleDTO> buildRoleDto() {
        return role -> RoleDTO.builder()
            .id(role.getId())
            .name(role.getName())
            .description(role.getDescription())
            .build();
    }
}
