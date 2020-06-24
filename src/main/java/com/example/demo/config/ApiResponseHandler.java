package com.example.demo.config;

import com.example.demo.common.ErrorCode;
import com.example.demo.coupon.dto.ApiErrorResponse;
import com.example.demo.coupon.dto.ApiResponse;
import com.example.demo.exception.ApiException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;


@ControllerAdvice(basePackages = {"com.example.demo.coupon"})
public class ApiResponseHandler implements ResponseBodyAdvice<Object> {
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@ExceptionHandler
	@ResponseBody
	public ApiErrorResponse handleException(final Exception e, final HttpServletResponse response) {
		e.printStackTrace();

		if (e instanceof ApiException) {
			final ApiException ae = (ApiException) e;
			response.setStatus(ae.getHttpStatus().value());

			return new ApiErrorResponse(ae);
		}
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ApiErrorResponse(ErrorCode.COUPON_UNKNOWN_ERROR);
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		HttpStatus httpStatus = HttpStatus.OK;

		if (body != null) {
			if (body instanceof ApiResponse) {
				ApiResponse res = (ApiResponse) body;
				try {
					httpStatus = HttpStatus.valueOf(res.getCode());
				} catch (IllegalArgumentException e) {
				}
				response.setStatusCode(httpStatus);
			}
		}

		if (body instanceof String || body instanceof ApiResponse) {
			return body;
		} else {
			for (Annotation a : returnType.getMethodAnnotations()) {
				if (a instanceof ResponseStatus) {
					ResponseStatus responseStatus = (ResponseStatus) a;
					httpStatus = responseStatus.value();
				}
			}
			return new ApiResponse(httpStatus, body);
		}
	}
}

