package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.repository.*;
import com.example.pojo.*;
@Controller
@EnableAutoConfiguration
public class UserController {
	@Autowired
	private UserRepository userRepository;
	
	@Modifying
	@RequestMapping("add")
	@ResponseBody 
	public String addUser(@RequestBody User user){
		userRepository.save(user);
		return "Add successfully!";
	}
	
	@Modifying
	@RequestMapping("delete")
	@ResponseBody 
	public String deleteUser(@RequestParam Long id){
		userRepository.delete(id);
		return "Delete successfully!";
	}
	
	@Modifying
	@RequestMapping("update")
	@ResponseBody 
	public String updateUser(@RequestBody User user){
		User userget = userRepository.findUserById(user.getId());
		if(userget!=null){
			userget.setName(user.getName());
			userget.setAge(user.getAge());
			return "Update successfully!";
		}
		else return "User not found!";
	}
	
	@RequestMapping("get")
	@ResponseBody 
	public String findUser(@RequestParam Long id){
		User userget = userRepository.findUserById(id);
		if(userget!=null)	return "User exists!";
		else return "User not found!";
	}
}
