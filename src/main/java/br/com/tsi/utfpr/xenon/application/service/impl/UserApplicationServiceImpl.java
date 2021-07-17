package br.com.tsi.utfpr.xenon.application.service.impl;

import br.com.tsi.utfpr.xenon.application.service.UserApplicationService;
import br.com.tsi.utfpr.xenon.domain.user.usecase.GetterCurrentUser;
import br.com.tsi.utfpr.xenon.domain.user.usecase.GetterFiles;
import br.com.tsi.utfpr.xenon.domain.user.usecase.GetterPageUser;
import br.com.tsi.utfpr.xenon.domain.user.usecase.RegistryUser;
import br.com.tsi.utfpr.xenon.domain.user.usecase.UpdateUser;
import br.com.tsi.utfpr.xenon.structure.dtos.UserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.ParamsSearchRequestDto;
import java.io.File;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserApplicationServiceImpl implements UserApplicationService {

    private final GetterPageUser getterPageUser;
    private final GetterFiles getterFiles;
    private final RegistryUser registryUser;
    private final UpdateUser updateUser;
    private final GetterCurrentUser getterCurrentUser;

    @Override
    public Page<UserDto> findAllPageableUsers(ParamsSearchRequestDto paramsSearchRequest) {
        return getterPageUser.getAllUserByFilter(paramsSearchRequest);
    }

    @Override
    public File getAvatar(Long id) {
        return getterFiles.getAvatar(id);
    }

    @Override
    public void saveNewUser(InputUserDto userDto) {
        registryUser.save(userDto);
    }

    @Override
    public InputUserDto findUserUpdate(Long id) {
        return updateUser.findUserUpdate(id);
    }

    @Override
    @Transactional
    public void updateUser(Long id, InputUserDto userDto) {
        updateUser.update(id, userDto);
    }

    @Override
    @Transactional
    public Boolean saveAvatar(MultipartFile avatar) {
        var currentUser = getterCurrentUser.currentUser();
        return updateUser.updateAvatar(currentUser.getId(), avatar);
    }

    @Override
    @Transactional
    public Boolean confirmDocument(Long id) {
        return updateUser.confirmDocument(id);
    }
}
