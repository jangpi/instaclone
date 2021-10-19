package com.jin.insta.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
	
	@GetMapping("/test/home")
	public String testHome() {
		return "home";
	}
	
	@GetMapping("/test/login")
	public String login() {
		return "test/login";
	}
	
	@GetMapping("/test/join")
	public String join() {
		return "test/join";
	}
	
	@GetMapping("/test/profile")
	public String profile() {
		return "test/join";
	}
}
