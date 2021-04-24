package br.com.tsi.utfpr.xenon.application.service.impl;

import br.com.tsi.utfpr.xenon.application.service.RegistryStudentsApplicationService;
import br.com.tsi.utfpr.xenon.domain.user.usecase.RegistryNewStudents;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegistryStudentsApplicationServiceImpl implements RegistryStudentsApplicationService {

    private final RegistryNewStudents registryNewStudents;

    @Override
    public void includeNewRegistry(String email) {
        registryNewStudents.includeNew(email);
    }
}
