package com.example.demo.auth.dto;

import lombok.Data;

@Data
public class TokenDto {
    private String accessToken;
    private String expirationDate;
}
