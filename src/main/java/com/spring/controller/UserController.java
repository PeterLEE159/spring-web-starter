package com.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.service.UserService;
import com.spring.vo.User;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/create_form")
	public String createForm() {
		return "user/form";
	}
	
	@RequestMapping("/create")
	public String create(User user) {
//		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
		System.out.println(user);
		this.userService.create(user);
		
		return "redirect:/index";
	}
	
}
