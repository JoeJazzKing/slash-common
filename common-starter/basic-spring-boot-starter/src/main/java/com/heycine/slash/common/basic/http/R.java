package com.heycine.slash.common.basic.http;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.Transient;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Map;

public class R<T> implements Serializable {
	private static final long serialVersionUID = 7231271344778259465L;

	public static final Integer SUCCESS = 200;
	public static final Integer FAIL = 500;

	@JsonProperty("code")
	private int code = SUCCESS;
	@JsonProperty("msg")
	private String msg = "";
	@JsonProperty("path")
	private String path = "";
	@JsonProperty("timestamp")
	private Long timestamp = DateUtil.current();
	@JsonProperty("traceId")
	private String traceId = IdUtil.fastSimpleUUID();
	@JsonProperty("data")
	private T data;
	@JsonProperty("ext")
	private Map<String, Object> ext = null;

	@JsonIgnore
	@Transient
	public boolean isOk() {
		return this.getCode() == SUCCESS;
	}

	public static <T> R<T> unauthorized(T data) {

		return restResult(data, 401, "请登录后访问!");
	}

	public static <T> R<T> forbidden(T data) {

		return restResult(data, 403, "无权限访问,请联系平台管理员授权!");
	}

	public static <T> R<T> ok() {

		return restResult(null, SUCCESS, null);
	}

	public static <T> R<T> ok(T data) {

		return restResult(data, SUCCESS, null);
	}

	public static <T> R<T> ok(T data, String msg) {

		return restResult(data, SUCCESS, msg);
	}

	public static <T> R<T> fail() {

		return restResult(null, FAIL, (String) null);
	}

	public static <T> R<T> fail(String msg) {

		return restResult(null, FAIL, msg);
	}

	public static <T> R<T> fail(int code, String msg) {

		return restResult(null, code, msg);
	}

	public static <T> R<T> fail(ErrorCode errorCode) {
		new MessageFormat(errorCode.getInfo());
		return restResult(null, errorCode.getCode(), errorCode.getInfo());
	}

	public static <T> R<T> fail(ErrorCode errorCode, Object... messages) {
		MessageFormat mf = new MessageFormat(errorCode.getInfo());
		String msg = mf.format(messages);
		return restResult(null, errorCode.getCode(), msg);
	}

	private static <T> R<T> restResult(T data, int code, String msg) {
		R<T> apiResult = new R<T>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		return apiResult;
	}

	public R() {
	}

	public R(int code, String msg, String path, Long timestamp, String traceId, T data, Map<String, Object> ext) {
		this.code = code;
		this.msg = msg;
		this.path = path;
		this.timestamp = timestamp;
		this.traceId = traceId;
		this.data = data;
		this.ext = ext;
	}


	public R<T> msg(String msg) {
		this.msg = msg;
		return this;
	}

	public R<T> msgAppend(String msg) {
		this.msg = this.msg + msg;
		return this;
	}

	public R<T> code(int code) {
		this.code = code;
		return this;
	}

	public R<T> data(T data) {
		this.data = data;
		return this;
	}

	public R<T> traceId(String traceId) {
		this.traceId = traceId;
		return this;
	}

	public R<T> path(String path) {
		this.path = path;
		return this;
	}

	public R put(String key, Object value) {
		this.ext.put(key, value);
		return this;
	}

	public R<T> timestamp(Long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public int getCode() {

		return this.code;
	}

	public void setCode(int code) {

		this.code = code;
	}

	public String getMsg() {

		return this.msg;
	}

	public void setMsg(String msg) {

		this.msg = msg;
	}

	public T getData() {

		return this.data;
	}

	public void setData(T data) {

		this.data = data;
	}

	public String getPath() {

		return this.path;
	}

	public void setPath(String path) {

		this.path = path;
	}

	public Long getTimestamp() {

		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {

		this.timestamp = timestamp;
	}

	public Map<String, Object> getExt() {

		return this.ext;
	}

	public void setExt(Map<String, Object> ext) {

		this.ext = ext;
	}

	public String getTraceId() {

		return this.traceId;
	}

	public void setTraceId(String traceId) {

		this.traceId = traceId;
	}

}
