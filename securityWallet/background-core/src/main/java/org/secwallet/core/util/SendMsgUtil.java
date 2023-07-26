package org.secwallet.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.secwallet.core.model.SendMSGDto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendMsgUtil {

    private static String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
    private static String report= "true";
    private static String account = "";
    //pwd
    private static String password  = "";
    /**
     *
     * @Description
     * @param path
     * @param postContent
     * @return String
     * @throws
     */
    public static String sendSmsByPost(String path, String postContent) {
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");// commit mode
            httpURLConnection.setConnectTimeout(10000);//
            httpURLConnection.setReadTimeout(10000);//
            // Sending a POST request must set the following two lines
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

//			PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
//			printWriter.write(postContent);
//			printWriter.flush();

            httpURLConnection.connect();
            OutputStream os=httpURLConnection.getOutputStream();
            os.write(postContent.getBytes("UTF-8"));
            os.flush();

            StringBuilder sb = new StringBuilder();
            int httpRspCode = httpURLConnection.getResponseCode();
            if (httpRspCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                return sb.toString();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Send SMS verification code
     * @param mobile
     * @return
     */
    public static Boolean  sendCheckCode(String mobile,String code){
        SendMSGDto smsSingleRequest = new SendMSGDto();
        smsSingleRequest.setAccount(account);
        smsSingleRequest.setMsg(""+code+"");
        smsSingleRequest.setPassword(password);
        smsSingleRequest.setPhone(mobile);
        smsSingleRequest.setReport(report);
        String requestJson = JSON.toJSONString(smsSingleRequest);
        System.out.println("before request string is: " + requestJson);
        String response = SendMsgUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
        JSONObject jsonObject = JSONObject.parseObject(response);
        if("0".equals(jsonObject.getString("code"))){

            return  true;
        }else {
            return  false;
        }
    }

}
