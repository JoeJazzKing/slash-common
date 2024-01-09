package com.heycine.slash.common.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ::优雅编程，此刻做起！
 * ::Elegant programming, start now!
 *
 * @author zhiji.zhou
 * @date 2022/8/30 下午5:38
 */
@Data
@ApiModel("SmsCodeMessageDTO")
public class SmsCodeMessageDTO {

	@ApiModelProperty(value = "手机号")
	private String mobile;

	@ApiModelProperty(value = "验证码")
	private Integer code;

	/**
	 * 随机6位数的验证码
	 * @return
	 */
	public Integer getCode() {

		return ((int) ((Math.random() * 9 + 1) * 100000));
	}

}
