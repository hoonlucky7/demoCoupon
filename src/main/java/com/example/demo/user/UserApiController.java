package com.example.demo.user;

import com.example.demo.user.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserApiController {

	@Autowired
    UserService userService;

	@PostMapping("/signup")
	public void signup(@RequestBody UserDto userDto) {
		log.info("user signup");
		userService.create(userDto.getEmail(), userDto.getPassword());
	}
}
