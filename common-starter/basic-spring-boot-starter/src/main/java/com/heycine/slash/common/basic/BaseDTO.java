package com.heycine.slash.common.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author zhiji.zhou
 * @date 2022/1/12
 */
@Data
public class BaseDTO {
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public final static String TIMEZONE = "GMT+8";

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = DATE_TIME_PATTERN, timezone = TIMEZONE)
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = DATE_TIME_PATTERN, timezone = TIMEZONE)
    private Date updateTime;
}
