package com.heycine.slash.common.message.controller;

import com.heycine.slash.common.basic.http.R;
import com.heycine.slash.common.domain.dto.MqMessageDTO;
import com.heycine.slash.common.domain.dto.SmsCodeMessageDTO;
import com.heycine.slash.common.message.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信服务
 *
 * @author zzj
 */
@RestController
@RequestMapping("/sms")
@Api(tags = "短信服务")
public class SmsController {

	@Autowired
	private SmsService smsService;

	@PostMapping("/send")
	@ApiOperation(value = "发送短信-验证码")
	public R<?> sendSms(@RequestBody SmsCodeMessageDTO smsCodeMessageDTO) {
		boolean isSuccess = smsService.sendSms(smsCodeMessageDTO);
		if (isSuccess) {
			return R.ok();
		} else {
			return R.fail();
		}
	}

}