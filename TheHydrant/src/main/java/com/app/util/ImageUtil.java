package com.app.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class ImageUtil {

    public static byte[] convertImageToByteArray(File file) throws IOException {
        byte[] bytes;
        BufferedImage bImage;

        Optional<String> extension = CommonUtil.getExtension(file.getName());
        bImage = ImageIO.read(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, extension.orElse("jpg"), bos);
        bytes = bos.toByteArray();

        return bytes;
    }
}
