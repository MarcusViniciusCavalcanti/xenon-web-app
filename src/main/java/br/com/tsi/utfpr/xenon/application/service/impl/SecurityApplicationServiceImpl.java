package br.com.tsi.utfpr.xenon.application.service.impl;

import br.com.tsi.utfpr.xenon.application.service.SecurityApplicationService;
import br.com.tsi.utfpr.xenon.domain.security.service.RoleService;
import br.com.tsi.utfpr.xenon.domain.user.usecase.GetterCurrentUser;
import br.com.tsi.utfpr.xenon.structure.dtos.RoleDTO;
import br.com.tsi.utfpr.xenon.structure.dtos.UserDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityApplicationServiceImpl implements SecurityApplicationService {

    private final GetterCurrentUser getterCurrentUser;
    private final RoleService roleService;

    @Override
    public UserDto currentUser() {
        return getterCurrentUser.currentUser();
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        return roleService.getAll();
    }
}
