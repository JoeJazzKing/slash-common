package com.heycine.slash.common.socket.test;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("TPMFirstStatisticsDTO")
public class TPMFirstStatisticsDTO implements Serializable {

    @ApiModelProperty("统计列表")
    private List<TPMFirstDTO> infoList;

    @Data
    @ApiModel("TPMFirstDTO")
    public static class TPMFirstDTO implements Serializable {
        @ApiModelProperty("大区")
        private String areaName;

        @ApiModelProperty("办事处")
        private String officeName;

        @ApiModelProperty("门店code")
        private String storeNum;

        @ApiModelProperty("MP号")
        private String mpNum;

    }

}
