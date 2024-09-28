package mks.myworkspace.learna.service.impl;

import mks.myworkspace.learna.entity.User;
import mks.myworkspace.learna.repository.UserRepository;
import mks.myworkspace.learna.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}
	
	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

}
