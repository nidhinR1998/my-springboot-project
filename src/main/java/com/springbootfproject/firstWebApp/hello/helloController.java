package com.springbootfproject.firstWebApp.hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class helloController {
	
	@RequestMapping("hello-page")
	private String hellopage() {
		return "hellopage";
	}
	

}
