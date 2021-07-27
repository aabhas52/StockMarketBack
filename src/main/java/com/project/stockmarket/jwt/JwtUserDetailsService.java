package com.project.stockmarket.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.stockmarket.entities.User;
import com.project.stockmarket.repositories.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	@Autowired
	UserRepository userrepo;

	@Autowired
	UserRepository userrepo2;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;

	public Collection<? extends GrantedAuthority> getAuthorities() {

		com.project.stockmarket.entities.User user = new com.project.stockmarket.entities.User();

		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		authorities.add(new SimpleGrantedAuthority(user.getRole()));

		return authorities;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		com.project.stockmarket.entities.User user = userrepo2.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		// non dto code below //return new
		// org.springframework.security.core.userdetails.User(user.getname(),
		// user.getpassword(),
		// new ArrayList<>());
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				new ArrayList<>());// you have to implement userdetails if you dont want to use dto
	}

// implement without dto	public com.stockexchange.phase3.User1 save(UserDto user) {
	public com.project.stockmarket.entities.User save(com.project.stockmarket.entities.User user) {
		com.project.stockmarket.entities.User newUser = new com.project.stockmarket.entities.User();
		// newUser.setname(user.getUsername());
		// newUser.setpassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setEmail(user.getEmail());
		newUser.setRole("user");
		return userrepo.save(newUser);
	}
	
	public User getUser(String username) {
		return userrepo2.findByUsername(username);
	}
}