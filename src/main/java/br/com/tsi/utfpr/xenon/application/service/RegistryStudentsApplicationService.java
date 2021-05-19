package br.com.tsi.utfpr.xenon.application.service;

import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputNewStudent;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputValidateTokenDto;
import org.springframework.stereotype.Service;

@Service
public interface RegistryStudentsApplicationService {

    void includeNewRegistry(String email);

    String validateToken(InputValidateTokenDto inputToken);

    void registryNewStudent(InputNewStudent inputNewStudent);
}
