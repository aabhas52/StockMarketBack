package com.project.stockmarket.jwt;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.stockmarket.controllers.UserController;
import com.project.stockmarket.entities.User;


@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	@Autowired
	private UserController userController;
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	// without dto below public ResponseEntity<?> saveUser(@RequestBody UserDto user) throws Exception {
	public ResponseEntity<?> saveUser(@RequestBody User user) throws Exception {
//USer1 is your user pojo entity 
		User newUser = userDetailsService.save(user);
		userController.sendemail(newUser.getId());
		return ResponseEntity.ok(newUser);
	}
//if not using dto ,then for register name and not user name
	//user username for authenticate but
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST,headers = "Accept=application/json")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		User user = userDetailsService.getUser(authenticationRequest.getUsername());
		
		final String role = user.getRole();
		
		final boolean confirmed = user.isConfirmed();

		if(!confirmed) {
			return new ResponseEntity<String>("Email not confirmed", HttpStatus.BAD_REQUEST);
		}
		
		Map<String, String> response = new HashMap<String, String>();
		response.put("role", role);
		response.put("token", token);
		response.put("username", authenticationRequest.getUsername());
		response.put("password", authenticationRequest.getPassword());
		response.put("id", Long.toString(user.getId()));
		return new ResponseEntity<Map<String, String>>(response, HttpStatus.OK);
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
