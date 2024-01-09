package com.heycine.slash.common.message.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.heycine.slash.common.domain.dto.EmailMessageDTO;
import com.heycine.slash.common.message.service.EmailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * 邮件服务
 *
 * @author zzj
 */
@Service
public class EmailServiceImpl implements EmailService {

	@Resource(name = "qqMailAccount")
	private MailAccount qqMailAccount;

	/**
	 * 发送邮件
	 *
	 * @param emailMessageDTO
	 * @return
	 */
	@Override
	public boolean sendEmail(EmailMessageDTO emailMessageDTO) {
		try {
			// 携带附件
			if (ArrayUtil.isNotEmpty(emailMessageDTO.getFiles())) {
				MailUtil.send(
						Arrays.asList(emailMessageDTO.getTos()),
						emailMessageDTO.getSubject(),
						emailMessageDTO.getContent(),
						emailMessageDTO.getIsHtml(),
						emailMessageDTO.getFiles()
				);
			} else {
				// 不携带附件
				MailUtil.send(
						Arrays.asList(emailMessageDTO.getTos()),
						emailMessageDTO.getSubject(),
						emailMessageDTO.getContent(),
						emailMessageDTO.getIsHtml()
				);
			}

			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 发送邮件 -根据自定义账户
	 *
	 * @param emailMessageDTO
	 * @return
	 */
	@Override
	public boolean sendEmailForAccount(EmailMessageDTO emailMessageDTO) {
		try {

			if (ArrayUtil.isNotEmpty(emailMessageDTO.getFiles())) {
				// 携带附件
				MailUtil.send(
						qqMailAccount,
						Arrays.asList(emailMessageDTO.getTos()),
						emailMessageDTO.getSubject(),
						emailMessageDTO.getContent(),
						emailMessageDTO.getIsHtml(),
						emailMessageDTO.getFiles()
				);
				Mail.create(qqMailAccount)
						.setTos(emailMessageDTO.getTos())
						.setTitle(emailMessageDTO.getSubject())
						.setContent(emailMessageDTO.getContent())
						.setHtml(emailMessageDTO.getIsHtml())
						.setAttachments(emailMessageDTO.getDataSources())
						.send();
			} else {
				// 不携带附件
				MailUtil.send(
						qqMailAccount,
						Arrays.asList(emailMessageDTO.getTos()),
						emailMessageDTO.getSubject(),
						emailMessageDTO.getContent(),
						emailMessageDTO.getIsHtml()
				);
				Mail.create(qqMailAccount)
						.setTos(emailMessageDTO.getTos())
						.setTitle(emailMessageDTO.getSubject())
						.setContent(emailMessageDTO.getContent())
						.setHtml(emailMessageDTO.getIsHtml())
						.send();
			}

			return true;
		} catch (Exception ex) {
			return false;
		}
	}

}