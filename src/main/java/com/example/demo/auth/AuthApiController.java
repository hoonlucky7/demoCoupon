package com.example.demo.auth;

import com.example.demo.auth.dto.TokenDto;
import com.example.demo.auth.dto.TokenResult;
import com.example.demo.user.UserService;
import com.example.demo.user.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthApiController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthService authService;

	@PostMapping("/create/token")
	public TokenDto createToken(@RequestBody UserDto userDto) {
		log.info("createToken");
		return authService.createToken(userDto);
	}
}
