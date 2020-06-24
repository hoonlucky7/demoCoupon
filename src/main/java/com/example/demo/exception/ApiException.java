package com.example.demo.exception;

import com.example.demo.common.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = false)
public class ApiException extends RuntimeException {

    private ErrorCode code;
    private HttpStatus httpStatus;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode;
        this.httpStatus = errorCode.getHttpStatus();
    }

}
