package com.heycine.slash.common.mybatisplus.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 自定义ID生成
 * @author alikes
 */
@Component
public class CustomIdGenerator implements IdentifierGenerator {

	@Value("${snowflake.workerId:1}")
	private Integer workerId;

	@Value("${snowflake.datacenterId:1}")
	private Integer datacenterId;

	@Override
	public Long nextId(Object entity) {
		Snowflake snowflake = IdUtil.getSnowflake(workerId, datacenterId);
		return snowflake.nextId();
	}

	@Override
	public String nextUUID(Object entity) {

		return IdUtil.fastUUID();
	}
}
