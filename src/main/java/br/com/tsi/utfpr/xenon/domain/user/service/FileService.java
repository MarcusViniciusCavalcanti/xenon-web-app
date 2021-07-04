package br.com.tsi.utfpr.xenon.domain.user.service;

import br.com.tsi.utfpr.xenon.domain.config.property.FilesProperty;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileService {

    public static final String PATTER_FILE = "%s/%d.png";
    private final FilesProperty filesProperty;

    public File getAvatar(Long id) {
        var pathAvatar = buildPathAvatar(id);
        if (Files.exists(pathAvatar)) {
            return pathAvatar.toFile();
        } else {
            return new File(String.format(PATTER_FILE, filesProperty.getAvatarUrl(), 0));
        }
    }

    public Path saveAvatar(Long id, InputStream avatar) throws IOException {
        Assert.notNull(avatar, "Multipart is null check params");
        Assert.state(Objects.nonNull(id) && id > 0, "Multipart is null check params");

        var tmpImage = ImageIO.read(avatar);

        var tmp = tmpImage.getScaledInstance(300, 300, Image.SCALE_FAST);
        int width = tmp.getWidth(null);
        int height = tmp.getHeight(null);

        var bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        bufferedImage.getGraphics().drawImage(tmp, 0, 0, null);
        var outInout = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", outInout);
        var input = new ByteArrayInputStream(outInout.toByteArray());

        var pathAvatar = buildPathAvatar(id);
        Files.copy(input, pathAvatar, StandardCopyOption.REPLACE_EXISTING);

        return pathAvatar;
    }

    private Path buildPathAvatar(Long id) {
        return Path.of(String.format(PATTER_FILE, filesProperty.getAvatarUrl(), id));
    }
}
