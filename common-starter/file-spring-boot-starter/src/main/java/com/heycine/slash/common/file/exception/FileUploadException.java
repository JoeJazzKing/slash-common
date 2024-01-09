package com.heycine.slash.common.file.exception;

import com.heycine.slash.common.file.enums.ErrorCodeEnum;

/**
 * 自定义异常
 *
 * @author zzj
 */
public class FileUploadException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private int code;
	private String msg;

	public FileUploadException(int code, String params, Throwable e) {
		super(e);
		this.code = code;
		this.msg = params;
	}

	public FileUploadException(ErrorCodeEnum codeEnum) {
		this.code = codeEnum.getCode();
		this.msg = codeEnum.getInfo();
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
