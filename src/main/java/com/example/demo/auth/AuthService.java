package com.example.demo.auth;

import com.example.demo.auth.dto.TokenDto;
import com.example.demo.auth.dto.TokenResult;
import com.example.demo.user.dto.UserDto;

public interface AuthService {
	TokenDto createToken(UserDto userVO);

	TokenResult verifyToken(String accessToken);
}
