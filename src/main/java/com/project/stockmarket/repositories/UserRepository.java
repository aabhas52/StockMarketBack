package com.project.stockmarket.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.stockmarket.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	public User findById(long id);

	public Optional<User> findByUsernameAndPassword(String username, String password);
	
	public User findByUsername(String username);
}
