package org.secwallet.core.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DetectionCheatUtil {


    /**
     * Read network pictures to distinguish whether they have been tampered with
     * @param url
     * @return
     */
    public static Boolean detection(String url) {
        String host = "https://aips.market.alicloudapi.com";
        String path = "/psdect";
        String method = "POST";
        String appcode = "7dc978e370cd4d25a687ddc8b44b0fcf";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        try {
            bodys.put("src", encodeToString(url));
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            String result = EntityUtils.toString(response.getEntity());
            log.info("================"+result+"===============");
            if(result == null || result.length() <= 0 || "".equals(result)){
                return false;
            }
            JSONObject jsonObject = JSONObject.parseObject(result);
            if(200 == jsonObject.getInteger("status")){
                if (jsonObject.getDouble("Threshold") > 0.8){
                    return true;
                }
                return false;
            }else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String encodeToString(String imagePath) throws IOException {
        URL url = new URL(imagePath);
        String type = StringUtils.substring(imagePath, imagePath.lastIndexOf(".") + 1);
        BufferedImage image = ImageIO.read(url);
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();
            imageString = Base64.getEncoder().encodeToString(imageBytes);
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  imageString;
    }
}
