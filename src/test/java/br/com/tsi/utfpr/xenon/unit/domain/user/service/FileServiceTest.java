package br.com.tsi.utfpr.xenon.unit.domain.user.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.tsi.utfpr.xenon.domain.config.property.FilesProperty;
import br.com.tsi.utfpr.xenon.domain.user.service.FileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("Test - Unidade - FileService")
@SpringBootTest(classes = {FilesProperty.class, FileService.class})
@EnableConfigurationProperties(value = FilesProperty.class)
@ActiveProfiles("test")
class FileServiceTest {

    @Autowired
    private FileService fileService;

    @Test
    @DisplayName("Deve retonar avatar de usuário padrão")
    void shouldReturnAvatarDefault() {
        var file = fileService.getAvatar(1000L);
        assertTrue(file.getAbsolutePath().endsWith("0.png"));
    }

    @Test
    @DisplayName("Deve retonar avatar de usuário quando encontrado")
    void shouldReturnAvatarWhenFound() {
        var file = fileService.getAvatar(100L);
        assertTrue(file.getAbsolutePath().endsWith("100.png"));
    }
}