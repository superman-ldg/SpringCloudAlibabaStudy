package com.ldg.cloud.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.stereotype.Component;

@Component
public class SmsDemo {

    public static String product="Dysmsapi";
    public static String damain="dysmsapi.aliyuncs.com";
    public static String accessKeyId="KeyId";
    public static String accessKeySecret="secret";

    //发送送短信
    public static void sendSms(String phone,String name,String code,String param)  {

        try {
            //可自助调节超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            //初始化acsClient
            IClientProfile profile =
                    DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);

            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, damain);

            DefaultAcsClient acsClient = new DefaultAcsClient(profile);
            SendSmsRequest request = new SendSmsRequest();
            request.setPhoneNumbers(phone);
            request.setSignName(name);
            request.setTemplateCode(code);
            request.setTemplateParam(param);
            request.setOutId("yourOutId");
            SendSmsResponse smsResponse = acsClient.getAcsResponse(request);
            if (!"ok".equals(smsResponse.getCode())) {
                throw new RuntimeException(smsResponse.getMessage());
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("短信发送失败！");
        }
    }




}
