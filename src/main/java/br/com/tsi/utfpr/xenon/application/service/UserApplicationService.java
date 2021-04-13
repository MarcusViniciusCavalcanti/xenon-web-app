package br.com.tsi.utfpr.xenon.application.service;

import br.com.tsi.utfpr.xenon.structure.dtos.UserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.ParamsSearchRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface UserApplicationService {

    Page<UserDto> findAllPageableUsers(ParamsSearchRequestDTO paramsSearchRequest);

}
