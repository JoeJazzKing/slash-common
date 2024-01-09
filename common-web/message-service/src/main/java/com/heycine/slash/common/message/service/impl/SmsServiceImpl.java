package com.heycine.slash.common.message.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.heycine.slash.common.domain.dto.SmsCodeMessageDTO;
import com.heycine.slash.common.message.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 短信服务
 *
 * @author zzj
 */
@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

	@Autowired
	private IAcsClient iAcsClient;

	@Resource(name = "codeCommonRequest")
	private CommonRequest commonRequest;

	/**
	 * 发送短信
	 *
	 * @param smsCodeMessageDTO
	 * @return
	 */
	@Override
	public boolean sendSms(SmsCodeMessageDTO smsCodeMessageDTO) {
		commonRequest.putQueryParameter("PhoneNumbers", smsCodeMessageDTO.getMobile());
		commonRequest.putQueryParameter("TemplateParam", "{\"code\":\"" + smsCodeMessageDTO.getCode() + "\"}");

		try {
			CommonResponse commonResponse = iAcsClient.getCommonResponse(commonRequest);
			String resultData = commonResponse.getData();
			log.info("短信发送完毕，回调结果：{}", resultData);

			// 放入缓存，定时5分钟 TODO
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return false;
	}

}