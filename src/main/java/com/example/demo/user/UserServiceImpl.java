package com.example.demo.user;

import com.example.demo.user.dto.UserDto;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
    UserRepository userRepository;

	@Override
	@Transactional
	public void create(String email, String password) {
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		userRepository.save(user);
	}

	@Override
	public Boolean checkUser(UserDto user) {
		User dbUser = userRepository.findOneByEmail(user.getEmail());
		if (BCrypt.checkpw(user.getPassword(), dbUser.getPassword())) {
			return true;
		}
		return false;
	}
}
