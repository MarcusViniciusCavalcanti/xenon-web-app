package br.com.tsi.utfpr.xenon.domain.user.service;

import br.com.tsi.utfpr.xenon.domain.config.property.FilesProperty;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileService {

    public static final String PATTER_FILE = "%s/%d.png";
    private final FilesProperty filesProperty;

    public File getAvatar(Long id) {
        var pathAvatar = Path.of(String.format(PATTER_FILE, filesProperty.getAvatarUrl(), id));
        if (Files.exists(pathAvatar)) {
            return pathAvatar.toFile();
        } else {
            return new File(String.format(PATTER_FILE, filesProperty.getAvatarUrl(), 0));
        }
    }
}
