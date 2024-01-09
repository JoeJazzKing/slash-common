package com.heycine.slash.common.file.enums;

/**
 * 错误码枚举
 * @author zzj
 */
public enum ErrorCodeEnum  {
	/**
	 * 文件上传异常
	 */
	OSS_UPLOAD_FILE_ERROR(14501, "上传文件到OSS服务器发生异常!"),
	/**
	 * 是
	 */
	EXAMPLE_2(1, "已被冻结");

	private final Integer code;
	private final String info;

	ErrorCodeEnum(Integer code, String info) {
		this.code = code;
		this.info = info;
	}

	public Integer getCode() {
		return code;
	}

	public String getInfo() {
		return info;
	}
}
