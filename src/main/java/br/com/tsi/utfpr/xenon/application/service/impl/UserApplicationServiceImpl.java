package br.com.tsi.utfpr.xenon.application.service.impl;

import br.com.tsi.utfpr.xenon.application.service.UserApplicationService;
import br.com.tsi.utfpr.xenon.domain.user.usecase.GetterPageUser;
import br.com.tsi.utfpr.xenon.structure.dtos.UserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.ParamsSearchRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserApplicationServiceImpl implements UserApplicationService {

    private final GetterPageUser getterPageUser;

    @Override
    public Page<UserDto> findAllPageableUsers(ParamsSearchRequestDTO paramsSearchRequest) {
        return getterPageUser.getAllUserByFilter(paramsSearchRequest);
    }
}
