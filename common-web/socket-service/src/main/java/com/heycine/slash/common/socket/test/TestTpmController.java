package com.heycine.slash.common.socket.test;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.heycine.slash.common.basic.http.*;

/**
 * ::优雅编程，此刻做起！
 * ::Elegant programming, start now!
 *
 * @author zhiji.zhou
 * @date 2022/10/20 下午2:16
 */
@RestController
@RequestMapping("/tpm/push")
@Api(tags = "TPM推送测试")
@Slf4j
public class TestTpmController {

	@PostMapping("/all")
	@ApiOperation("推送TPM结果-聚合3种接口")
	public R<?> pushAll(@RequestBody TpmAllStatisticsDTO allDTO) {
		log.info("TPM-All接收到推送消息：{}", JSONObject.toJSONString(allDTO));
		return R.ok(JSONObject.toJSONString(allDTO));
	}

	@PostMapping("/mp")
	@ApiOperation("推送专项结果")
	public R<?> pushMp(@RequestBody TPMFirstStatisticsDTO mpDTO) {
		log.info("TPM-MP接收到推送消息：{}", JSONObject.toJSONString(mpDTO));
		return R.ok();
	}
	@PostMapping("/mp/add")
	@ApiOperation("推送专项结果(新增)")
	public R<?> pushMpAdd(@RequestBody TPMFirstStatisticsDTO mpAddDTO) {
		log.info("TPM-MP接收到推送消息(新增)：{}", JSONObject.toJSONString(mpAddDTO));
		return R.ok();
	}
	@PostMapping("/mp/update")
	@ApiOperation("推送专项结果(修改)")
	public R<?> pushMpUpdate(@RequestBody TPMFirstStatisticsDTO mpUpdateDTO) {
		log.info("TPM-MP接收到推送消息(修改)：{}", JSONObject.toJSONString(mpUpdateDTO));
		return R.ok();
	}

	@PostMapping("/auditResult")
	@ApiOperation("推送审核结果")
	public R<?> pushAuditResult(@RequestBody TPMSecondStatisticsDTO resultDTO) {
		log.info("TPM-RESULT接收到推送消息：{}", JSONObject.toJSONString(resultDTO));
		return R.ok();
	}
	@PostMapping("/auditResult/add")
	@ApiOperation("推送审核结果(新增)")
	public R<?> pushAuditResultAdd(@RequestBody TPMSecondStatisticsDTO resultAddDTO) {
		log.info("TPM-RESULT接收到推送消息(新增)：{}", JSONObject.toJSONString(resultAddDTO));
		return R.ok();
	}
	@PostMapping("/auditResult/update")
	@ApiOperation("推送审核结果(修改)")
	public R<?> pushAuditResultUpdate(@RequestBody TPMSecondStatisticsDTO resultUpdateDTO) {
		log.info("TPM-RESULT接收到推送消息(修改)：{}", JSONObject.toJSONString(resultUpdateDTO));
		return R.ok();
	}
}
