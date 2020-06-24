package com.example.demo.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

	COUPON_NOT_FOUND("COUPON1", HttpStatus.NOT_FOUND, "COUPON CODE를 찾을 수 없습니다. "),
	COUPON_INVALID_EMAIL("COUPON2", HttpStatus.BAD_REQUEST, "잘못된 이메일 주소 형식 입니다. "),
	COUPON_ALREADY_USE_COUPON("COUPON3", HttpStatus.BAD_REQUEST, "이미 사용된 쿠폰 입니다."),
	COUPON_EXPIRED_CODE("COUPON4", HttpStatus.BAD_REQUEST, "만기일이 지난 쿠폰 입니다."),
	COUPON_UNKNOWN_ERROR("COUPON9", HttpStatus.INTERNAL_SERVER_ERROR, "알수없는 오류 발생");

	private final String code;
	private final HttpStatus httpStatus;
	private final String message;

	public static ErrorCode getByCode(final String code) {
		for (final ErrorCode e : values()) {
			if (e.code.equals(code))
				return e;
		}
		return ErrorCode.COUPON_UNKNOWN_ERROR;
	}

}
