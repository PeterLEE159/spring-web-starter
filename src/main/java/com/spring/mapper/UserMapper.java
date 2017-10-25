package com.spring.mapper;

import java.util.List;

import com.spring.vo.User;

public interface UserMapper {
	void create(User user);
	List<User> readAll();
}
