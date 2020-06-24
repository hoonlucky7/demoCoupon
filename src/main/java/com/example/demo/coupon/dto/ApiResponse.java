package com.example.demo.coupon.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiResponse<T> {
    int code;

    String status;

    T data;

    @JsonIgnore
    HttpStatus httpStatus;

    public ApiResponse(HttpStatus httpStatus, T data) {
        this.httpStatus = httpStatus;
        this.data = data;
        this.code = httpStatus.value();
        this.status = this.getHttpMessage(httpStatus);
    }

    private String getHttpMessage(HttpStatus httpStatus) {
        if (httpStatus.is2xxSuccessful()) {
            return "success";
        } else if (httpStatus.is4xxClientError()) {
            return "error";
        } else if (httpStatus.is5xxServerError()) {
            return "fail";
        } else {
            return "unknown";
        }
    }
}
