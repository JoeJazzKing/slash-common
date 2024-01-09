package com.heycine.slash.common.business.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.heycine.slash.common.mybatisplus.basic.BaseReEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件上传
 * 
 * @author ming ming.fang@mwcare.cn
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("oss_upload_record")
public class OssUploadRecordEntity extends BaseReEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;

	/**
	 * URL地址
	 */
	private String url;

}