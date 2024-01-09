package com.heycine.slash.common.socket.test;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zzj
 */
@Data
@ApiModel("TpmAllStatisticsDTO")
public class TpmAllStatisticsDTO implements Serializable {
	private static final long serialVersionUID = 708483203642358934L;

	@ApiModelProperty("统计列表")
	private List<TpmDTO> infoList;

	@Data
	@ApiModel("TpmDTO")
	public static class TpmDTO implements Serializable {
		@ApiModelProperty("门店编码")
		private String storeNum;

		@ApiModelProperty("专项编码（MP号）")
		private String mpNum;

		@ApiModelProperty("活动编码（TA号）")
		private String taNum;

		@ApiModelProperty("费用项（费用小项）")
		private String expenseItem;

		@ApiModelProperty("最终结果（1=合格，0=不合格）")
		private Integer finallyResult;

		@ApiModelProperty("最终执行情况")
		private String executionStatus;

		@ApiModelProperty("合格率[保留3位小数]")
		private String executionRate;

		@ApiModelProperty("结案锁定状态（2=审计中，1=锁定，0=解锁）")
		private Integer lockedState;
	}

}
