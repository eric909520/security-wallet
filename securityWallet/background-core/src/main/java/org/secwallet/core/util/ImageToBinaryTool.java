package org.secwallet.core.util;

import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageToBinaryTool {

    public static String getImageBinary(String imageFileName) {
//        BASE64Encoder encoder = new BASE64Encoder();
        File imageFile = new File(imageFileName);
        String suffix = imageFileName.substring(imageFileName.lastIndexOf(".") + 1);
        try {
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, suffix, baos);
            byte[] bytes = baos.toByteArray();
//            return encoder.encodeBuffer(bytes).trim();
            return Base64.encodeBase64String(bytes).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void base64StringToImage(String base64String, String imageFileName, String suffix) {
        try {
//            BASE64Decoder decoder = new BASE64Decoder();
//            byte[] bytes1 = decoder.decodeBuffer(base64String);
            byte[] bytes1 = Base64.decodeBase64(base64String);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            BufferedImage bufferedImage = ImageIO.read(bais);
            File imageFile = new File(imageFileName);
            ImageIO.write(bufferedImage, suffix, imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
