package br.com.tsi.utfpr.xenon.application.service;

import br.com.tsi.utfpr.xenon.structure.dtos.UserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.ParamsSearchRequestDto;
import java.io.File;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserApplicationService {

    Page<UserDto> findAllPageableUsers(ParamsSearchRequestDto paramsSearchRequest);

    File getAvatar(Long id);

    void saveNewUser(InputUserDto userDto);

    InputUserDto findUserUpdate(Long id);

    void updateUser(Long id, InputUserDto userDto);

    Boolean saveAvatar(MultipartFile avatar);
}
