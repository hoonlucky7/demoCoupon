package com.example.demo.auth;

import com.example.demo.auth.dto.TokenDto;
import com.example.demo.auth.dto.TokenResult;
import com.example.demo.common.util.JwtTokenProvider;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import com.example.demo.user.UserService;
import com.example.demo.user.dto.UserDto;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserService userService;

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Override
	public TokenDto createToken(UserDto userVO) {
		if (!userService.loginUser(userVO)) {
			throw new RuntimeException("login fail");
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
