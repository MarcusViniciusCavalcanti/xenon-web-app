package br.com.tsi.utfpr.xenon.application.service;

import org.springframework.stereotype.Service;

@Service
public interface RegistryStudentsApplicationService {

    void includeNewRegistry(String email);
}
