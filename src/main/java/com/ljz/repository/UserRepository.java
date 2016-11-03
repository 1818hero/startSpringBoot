package com.ljz.repository;

//import javax.persistence.Table;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ljz.pojo.User;

@Repository
public interface UserRepository extends CrudRepository<User,Long>{
	public User findUserById(Long id);
	
	public User findUserByName(String name);
	
	//@Query("select u from User u where u.name=:username")
	//public User findUserByName(@Param("username") String name);

}
