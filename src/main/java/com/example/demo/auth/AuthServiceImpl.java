package com.example.demo.auth;

import com.example.demo.auth.dto.TokenDto;
import com.example.demo.auth.dto.TokenResult;
import com.example.demo.common.ErrorCode;
import com.example.demo.common.util.JwtTokenProvider;
import com.example.demo.exception.ApiException;
import com.example.demo.user.UserService;
import com.example.demo.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserService userService;

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Override
	public TokenDto createToken(UserDto userVO) {
		if (!userService.checkUser(userVO)) {
			throw new ApiException(ErrorCode.USER_NOT_FOUND_CODE);
		}
		TokenDto tokenDto = new TokenDto();
		tokenDto.setAccessToken(jwtTokenProvider.generateToken(userVO.getEmail()));
		tokenDto.setExpirationDate("" + new Date().getTime()
				+ jwtTokenProvider.jwtExpirationInMs);
		return tokenDto;
	}

	@Override
	public TokenResult verifyToken(String accessToken) {
		return null;
	}
}
