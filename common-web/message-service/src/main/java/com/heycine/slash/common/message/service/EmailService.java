package com.heycine.slash.common.message.service;

import com.heycine.slash.common.domain.dto.EmailMessageDTO;

/**
 * 邮件服务
 *
 * @author zzj
 */
public interface EmailService {

	/**
	 * 发送邮件
	 * @param emailMessageDTO
	 * @return
	 */
	boolean sendEmail(EmailMessageDTO emailMessageDTO);

	/**
	 * 发送邮件 -根据自定义账户
	 * @param emailMessageDTO
	 * @return
	 */
	boolean sendEmailForAccount(EmailMessageDTO emailMessageDTO);

}