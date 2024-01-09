package com.heycine.slash.common.message.properties;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 发送消息队列
 *
 * @author zzj
 */
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "sms")
@Data
public class SmsProperties {

	@Value("${ali.accesskey:''}")
	private String accesskey;

	@Value("${ali.accesssecret:''}")
	private String accesssecret;

	@Bean
	public IAcsClient iAcsClient() {
		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accesskey, accesssecret);
		IAcsClient client = new DefaultAcsClient(profile);

		return client;
	}

	@Bean("codeCommonRequest")
	public CommonRequest commonRequest() {
		CommonRequest request = new CommonRequest();
		request.setSysMethod(MethodType.POST);
		request.setSysDomain("dysmsapi.aliyuncs.com");
		request.setSysVersion("2017-05-25");
		request.setSysAction("SendSms");
		request.putQueryParameter("RegionId", "cn-hangzhou");
		request.putQueryParameter("SignName", "天坛会");
		request.putQueryParameter("TemplateCode", "SMS_199795095");

		// 需要动态赋值
		request.putQueryParameter("PhoneNumbers", null);
		request.putQueryParameter("TemplateParam", "{\"code\":\"" + null + "\"}");

		return request;
	}

}
