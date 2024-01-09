package com.heycine.slash.common.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.heycine.slash.common.basic.BaseDTO;
import com.heycine.slash.common.basic.Group;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * ::优雅编程，此刻做起！
 * ::Elegant programming, start now!
 *
 * @author zhiji.zhou
 * @date 2022/8/30 下午5:38
 */
@Data
@ApiModel("MqMessageDTO")
public class MqMessageDTO<T> {

	@ApiModelProperty(value = "主题")
	private String topic;

	@ApiModelProperty(value = "消息内容")
	private T content;

}
