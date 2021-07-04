package br.com.tsi.utfpr.xenon.unit.domain.user.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.tsi.utfpr.xenon.domain.user.service.FileService;
import br.com.tsi.utfpr.xenon.domain.user.usecase.GetterFiles;
import java.io.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test - Unidade - GetterFiles")
class GetterFilesTest {

    @Mock
    private FileService fileService;

    @InjectMocks
    private GetterFiles getterFiles;

    @Test
    @DisplayName("Deve retonar o avatar do usuário")
    void shouldReturnAvatar() {
        var id = 20L;
        var file = new File("/test-file/100.png");

        when(fileService.getAvatar(id)).thenReturn(file);

        var result = getterFiles.getAvatar(id);

        assertEquals(file, result);
        verify(fileService).getAvatar(id);
    }
}