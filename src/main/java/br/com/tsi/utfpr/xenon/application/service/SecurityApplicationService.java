package br.com.tsi.utfpr.xenon.application.service;

import br.com.tsi.utfpr.xenon.structure.dtos.RoleDTO;
import br.com.tsi.utfpr.xenon.structure.dtos.UserDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface SecurityApplicationService {

    UserDto currentUser();

    List<RoleDTO> getAllRoles();
}
