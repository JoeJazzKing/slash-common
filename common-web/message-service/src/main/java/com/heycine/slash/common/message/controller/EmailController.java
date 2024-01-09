package com.heycine.slash.common.message.controller;

import com.heycine.slash.common.api.ExampleFeignClient;
import com.heycine.slash.common.basic.http.R;
import com.heycine.slash.common.domain.dto.EmailMessageDTO;
import com.heycine.slash.common.domain.dto.ExampleDTO;
import com.heycine.slash.common.message.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 邮件服务
 *
 * @author zzj
 */
@RestController
@RequestMapping("/email")
@Api(tags = "邮件服务")
public class EmailController {

	@Autowired
	private ExampleFeignClient exampleFeignClient;

	@Autowired
	private EmailService emailService;

	@PostMapping("/send")
	@ApiOperation("发送邮件")
	public R<?> sendEmail(@RequestBody EmailMessageDTO emailMessageDTO) {
		boolean isSuccess = emailService.sendEmail(emailMessageDTO);
		if (isSuccess) {
			return R.ok();
		} else {
			return R.fail();
		}
	}


	@GetMapping({"/example/detail"})
	@ApiOperation("测试feign超时")
	R<ExampleDTO> example(@RequestParam("id") String id) {
		R<ExampleDTO> example = exampleFeignClient.example(id);
		return R.ok(example.getData());
	}

}