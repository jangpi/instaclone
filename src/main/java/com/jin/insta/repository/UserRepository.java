package com.jin.insta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jin.insta.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByUsername(String username);

}
