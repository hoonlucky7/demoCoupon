package com.example.demo.coupon.dto;

import com.example.demo.common.ErrorCode;
import com.example.demo.exception.ApiException;
import lombok.Data;

@Data
public class ApiErrorResponse {

    String code;
    String message;

    public ApiErrorResponse(ApiException ae) {
        this.code = ae.getCode().getCode();
        this.message = ae.getCode().getMessage();
    }

    public ApiErrorResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
