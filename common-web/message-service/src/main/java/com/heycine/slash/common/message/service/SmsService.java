package com.heycine.slash.common.message.service;

import com.heycine.slash.common.domain.dto.SmsCodeMessageDTO;

/**
 * 短信服务
 *
 * @author zzj
 */
public interface SmsService {

	/**
	 * 发送短信
	 * @param smsCodeMessageDTO
	 * @return
	 */
	boolean sendSms(SmsCodeMessageDTO smsCodeMessageDTO);
}