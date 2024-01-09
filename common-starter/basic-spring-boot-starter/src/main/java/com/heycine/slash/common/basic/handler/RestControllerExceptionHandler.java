package com.heycine.slash.common.basic.handler;

import com.heycine.slash.common.basic.http.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.UUID;

/**
 * 使用 @ControllerAdvice & ResponseBodyAdvice 拦截Controller方法默认返回参数，统一处理返回值/响应体
 *
 * @author alikes
 */
@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler {

	public static final String TRACE_ID = "traceId";

	/**
	 * 处理自定义的业务异常
	 *
	 * @param e       异常对象
	 * @param request request
	 * @return 错误结果
	 */

	@ExceptionHandler(Exception.class)
	public R<?> exceptionHandler(Exception e, HttpServletRequest request) {
		log.error("发生异常: {}", e.getMessage(), e);

		String headerTraceId = request.getHeader(TRACE_ID);
		String traceId = null != headerTraceId ? headerTraceId : request.getParameter(TRACE_ID);
		if (null == traceId || "".equals(traceId)) {
			traceId = "TRES-" + UUID.randomUUID();
		}

		return R.fail(500, e.getMessage())
				.path(request.getRequestURL().toString())
				.traceId(traceId)
				.data(null);
	}

	/**
	 * 处理Validated校验异常
	 * 注: 常见的ConstraintViolationException异常， 也属于ValidationException异常
	 *
	 * @param e v捕获到的异常
	 * @return 返回给前端的data
	 */
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class, ConstraintViolationException.class})
	public R<Object> handleParameterVerificationException(Exception e, HttpServletRequest request) {
		log.error(" handleParameterVerificationException has been invoked", e);

		String msg = null;
		if (e instanceof BindException) {
			FieldError fieldError = ((BindException) e).getFieldError();
			if (fieldError != null) {
				msg = fieldError.getDefaultMessage();
			}
		} else if (e instanceof MethodArgumentNotValidException) {
			BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
			FieldError fieldError = bindingResult.getFieldError();
			if (fieldError != null) {
				msg = fieldError.getDefaultMessage();
			}
		} else if (e instanceof ConstraintViolationException) {
			msg = e.getMessage();
			if (msg != null) {
				int lastIndex = msg.lastIndexOf(':');
				if (lastIndex >= 0) {
					msg = msg.substring(lastIndex + 1).trim();
				}
			}
		} else {
			msg = HttpStatus.BAD_REQUEST.getReasonPhrase();
		}
		return R.fail(HttpStatus.BAD_REQUEST.value(), msg)
				.path(request.getRequestURI());
	}

}
