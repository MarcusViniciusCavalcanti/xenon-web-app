package br.com.tsi.utfpr.xenon.application.service.impl;

import br.com.tsi.utfpr.xenon.application.service.RegistryStudentsApplicationService;
import br.com.tsi.utfpr.xenon.domain.user.usecase.RegistryNewStudents;
import br.com.tsi.utfpr.xenon.domain.user.usecase.ValidateToken;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputNewStudent;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputValidateTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegistryStudentsApplicationServiceImpl implements RegistryStudentsApplicationService {

    private final RegistryNewStudents registryNewStudents;
    private final ValidateToken validateToken;

    @Override
    public void includeNewRegistry(String email) {
        registryNewStudents.includeNew(email);
    }

    @Override
    public String validateToken(InputValidateTokenDto inputToken) {
        return validateToken.validate(inputToken);
    }

    @Override
    public void registryNewStudent(InputNewStudent inputNewStudent) {
        registryNewStudents.registry(inputNewStudent);
    }
}
