package com.heycine.slash.common.socket.test;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("TPMSecondStatisticsDTO")
public class TPMSecondStatisticsDTO implements Serializable {

    @ApiModelProperty("统计列表")
    private List<TPMSecondDTO> infoList;

    @Data
    @ApiModel("TPMSecondDTO")
    public static class TPMSecondDTO implements Serializable {

        @ApiModelProperty("大区")
        private String areaName;

        @ApiModelProperty("办事处")
        private String officeName;

        @ApiModelProperty("门店code")
        private String storeNum;

        @ApiModelProperty("初审结果")
        private String firstAuditResult;

        @ApiModelProperty("终审结果")
        private String finallyAuditResult;

        @ApiModelProperty("TA号")
        private String taNum;

        @ApiModelProperty("费用小项")
        private String expenseItem;

        @ApiModelProperty("执行状态")
        private String executionStatus;

        @ApiModelProperty("执行率")
        private String executionRate;


    }

}
