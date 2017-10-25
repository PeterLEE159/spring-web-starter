package com.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.mapper.UserMapper;
import com.spring.vo.User;

@Service
public class UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	public List<User> readAll() {
		return this.userMapper.readAll();
	}
	
	public void create(User user) {
		this.userMapper.create(user);
	}
	
	public User readByUsername(String username) {
		return this.userMapper.readByUsername(username);
	}
}
