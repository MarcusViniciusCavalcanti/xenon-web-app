package br.com.tsi.utfpr.xenon.unit.domain.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.tsi.utfpr.xenon.domain.config.property.FilesProperty;
import br.com.tsi.utfpr.xenon.domain.user.service.FileService;
import java.io.IOException;
import java.nio.file.Files;
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

    @Test
    @DisplayName("Deve retornar execption quando id esta nulo")
    void shouldThrowExceptionWhenIdIsNull() {
        assertThrows(IllegalStateException.class,
            () -> fileService.saveAvatar(null,
                this.getClass().getResourceAsStream("/test-file/avatar/100.png")));
    }

    @Test
    @DisplayName("Deve retornar execption quando id esta nulo")
    void shouldThrowExceptionWhenIdIsZero() {
        assertThrows(IllegalStateException.class,
            () -> fileService
                .saveAvatar(0L, this.getClass().getResourceAsStream("/test-file/avatar/100.png")));
    }

    @Test
    @DisplayName("Deve retornar execption quando byte do arquivo esta nulo")
    void shouldThrowExceptionWhenInputStreamIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> fileService.saveAvatar(100L, null));
    }

    @Test
    @DisplayName("Deve salvar avatar do usuário")
    void shouldHaveSaveAvatarUser() throws IOException {
        var avatar = this.getClass().getResourceAsStream("/test-file/avatar/100.png");

        var file = fileService.saveAvatar(299L, avatar);

        assertNotNull(avatar);
        assertEquals("299.png", file.getFileName().toFile().getName());
        assertTrue(Files.exists(file));
    }
}