package com.spring.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
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
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
		this.userService.create(user);
		
		return "redirect:/user/index";
	}
	
	@RequestMapping("/index")
	public String list(Model model) {
		List<User> userList = this.userService.readAll();
		model.addAttribute("users", userList);
		return "user/index";
		
	}
}
