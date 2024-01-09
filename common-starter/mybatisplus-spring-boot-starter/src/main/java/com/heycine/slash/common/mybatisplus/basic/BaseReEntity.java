package com.heycine.slash.common.mybatisplus.basic;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类，所有实体都需要继承
 *
 * @author zhiji.zhou
 */
@Data
public abstract class BaseReEntity implements Serializable {

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

	/**
	 * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
