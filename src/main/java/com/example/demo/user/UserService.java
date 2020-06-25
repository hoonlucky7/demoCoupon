package com.example.demo.user;

import com.example.demo.user.dto.UserDto;

public interface UserService {
	void create(String email, String password);
	Boolean checkUser(UserDto user);
}
