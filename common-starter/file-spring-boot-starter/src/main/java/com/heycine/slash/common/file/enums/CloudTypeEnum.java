package com.heycine.slash.common.file.enums;

/**
 * 错误码枚举
 * @author zzj
 */
public enum CloudTypeEnum {
	/**
	 * 阿里云
	 */
	QINIU(1, "阿里云"),
	/**
	 * 腾讯云
	 */
	ALIYUN(2, "腾讯云"),
	/**
	 * 七牛云
	 */
	QCLOUD(3, "七牛云"),
	/**
	 * MinIO
	 */
	MINIO(4, "MinIO"),
	/**
	 * FASTDFS
	 */
	FASTDFS(5, "FASTDFS"),
	/**
	 * 本地
	 */
	LOCAL(6, "本地"),
	;

	private final Integer code;
	private final String info;

	CloudTypeEnum(Integer code, String info) {
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
