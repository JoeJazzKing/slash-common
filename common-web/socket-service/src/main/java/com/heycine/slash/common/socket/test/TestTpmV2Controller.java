package com.heycine.slash.common.socket.test;

import com.alibaba.fastjson.JSONObject;
import com.heycine.slash.common.basic.http.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ::优雅编程，此刻做起！
 * ::Elegant programming, start now!
 *
 * @author zhiji.zhou
 * @date 2022/10/20 下午2:16
 */
@RestController
@RequestMapping("/")
@Api(tags = "TPM推送测试")
@Slf4j
public class TestTpmV2Controller {

	@PostMapping("/IO/SetActivityShopStatus")
	@ApiOperation("推送TPM结果-聚合3种接口")
	public R<?> pushAll(@RequestBody TpmAllStatisticsDTO allDTO) {
		log.info("TPM-All接收到推送消息：{}", JSONObject.toJSONString(allDTO));
		return R.ok(JSONObject.toJSONString(allDTO));
	}

}
