package com.ljz;

import java.util.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.ljz.filter.HTTPBasicAuthorizeAttribute;
import com.ljz.filter.HTTPBearerAuthorizeAttribute;
import com.ljz.jwt.Audience;

@SpringBootApplication
@EnableConfigurationProperties(Audience.class)  
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
        urlPatterns.add("/user/getaaa");  
        registrationBean.setUrlPatterns(urlPatterns);  
        return registrationBean;  
    }
    @Bean  
    public FilterRegistrationBean jwtFilterRegistrationBean(){  
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();  
        HTTPBearerAuthorizeAttribute httpBearerFilter = new HTTPBearerAuthorizeAttribute();  
        registrationBean.setFilter(httpBearerFilter);  
        List<String> urlPatterns = new ArrayList<String>();  
        urlPatterns.add("/user/get");  
        registrationBean.setUrlPatterns(urlPatterns);  
        return registrationBean;  
    }  
}
