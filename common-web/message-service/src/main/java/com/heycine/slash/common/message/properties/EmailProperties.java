package com.heycine.slash.common.message.properties;

import cn.hutool.extra.mail.MailAccount;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.GeneralSecurityException;

/**
 * 发送消息队列
 *
 * @author zzj
 */
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "email")
@Data
public class EmailProperties {

	private String host;

	private Integer port;

	private Boolean auth;

	private String from;

	private String user;

	private String pass;

	@Bean("qqMailAccount")
	public MailAccount mailAccount () throws GeneralSecurityException {
		MailAccount mailAccount = new MailAccount();
		/*account.setHost("smtp.yeah.net");
		account.setPort("25");
		account.setAuth(true);
		account.setFrom("hutool@yeah.net");
		account.setUser("hutool");
		account.setPass("q1w2e3");*/

		mailAccount.setHost(host);
		mailAccount.setPort(port);
		mailAccount.setAuth(auth);
		mailAccount.setFrom(from);
		mailAccount.setUser(user);
		mailAccount.setPass(pass);

		// 解决SSL错误
		MailSSLSocketFactory sf = new MailSSLSocketFactory();
		sf.setTrustAllHosts(true);
		mailAccount.setCustomProperty("mail.smtp.ssl.socketFactory", sf);
		mailAccount.setSslEnable(true);

		return mailAccount;
	}

}
