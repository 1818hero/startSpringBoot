package com.example;

import java.util.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.ljz.filter.HTTPBasicAuthorizeAttribute;

@SpringBootApplication
public class StartSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(StartSpringBootApplication.class, args);
	}
    @Bean  
    public FilterRegistrationBean  filterRegistrationBean() {  
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();  
        HTTPBasicAuthorizeAttribute httpBasicFilter = new HTTPBasicAuthorizeAttribute();  
        registrationBean.setFilter(httpBasicFilter);  
        List<String> urlPatterns = new ArrayList<String>();  
        urlPatterns.add("/user/*");  
        registrationBean.setUrlPatterns(urlPatterns);  
        return registrationBean;  
    }
}
