package com.spring.web;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.service.SQLService;


@Controller
public class MainController {
	@Autowired
	private SQLService service;
	
	@RequestMapping(value={"/", "/index"})
	public String home(Model model) {
		return "index";
	}
	
	@RequestMapping("/sql")
	public String sql(Model model) throws ParseException {
		
		return "sql";
	}
	
}
