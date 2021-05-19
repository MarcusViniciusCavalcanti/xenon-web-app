package br.com.tsi.utfpr.xenon.domain.security.service;

import br.com.tsi.utfpr.xenon.domain.security.entity.Role;
import br.com.tsi.utfpr.xenon.domain.security.exception.AuthoritiesNotAllowedException;
import br.com.tsi.utfpr.xenon.domain.security.repository.RoleRepository;
import br.com.tsi.utfpr.xenon.domain.user.entity.TypeUser;
import br.com.tsi.utfpr.xenon.structure.dtos.TypeUserDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleService {

    private final RoleRepository roleRepository;

    public List<Role> verifyAndGetRoleBy(TypeUserDto typeUser, List<Long> authorities) {
        if (typeUser == TypeUserDto.STUDENTS || typeUser == TypeUserDto.SPEAKER) {
            var allowedProfiles = TypeUser.valueOf(typeUser.name()).getAllowedProfiles();

            var size = authorities.stream()
                .filter(value -> !allowedProfiles.contains(value))
                .count();

            if (size != 0) {
                throw new AuthoritiesNotAllowedException(typeUser.getTranslaterName());
            }
        }

        return roleRepository.findAllById(authorities);
    }
}
