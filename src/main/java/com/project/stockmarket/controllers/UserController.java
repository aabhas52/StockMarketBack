package com.project.stockmarket.controllers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import org.apache.tomcat.jni.Buffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.stockmarket.entities.User;
import com.project.stockmarket.repositories.UserRepository;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class  UserController {
	
	@Autowired
	UserRepository userrepo;
	
	@CrossOrigin(origins ="http://localhost:3000")
	@RequestMapping(value = "/setuserapi",method=RequestMethod.POST)
	
	public String Stringreactuserapi(@RequestBody User user) throws AddressException, MessagingException {	
	
		user.setRole("user");
		userrepo.save(user);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Responded", "UserController");
		headers.add("Access-Control-Allow-Origin", "*");
		sendemail(user.getId()) ;
		
		return user.toString();
	}




	public void sendemail(Long userid) throws AddressException, MessagingException {

      User user = userrepo.getById(userid);	




		final String username = "aabhasasawa52@gmail.com";
		final String password = "";

		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true"); //TLS

		Session session = Session.getInstance(prop,
				new javax.mail.Authenticator() {
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("sftrainerram@gmail.com"));
			//message.setRecipients(
				//	Message.RecipientType.TO,
				//	InternetAddress.parse("sftrainerram@gmail.com")
				//	);
			message.setRecipients(
					Message.RecipientType.TO,
					InternetAddress.parse(user.getEmail())
					);
			message.setSubject("USer confirmation email");
			//     message.setText("Dear Mail Crawler,"
			//           + "\n\n Please do not spam my email!");
			message.setContent(
					"<h1><a href =\"http://127.0.0.1:8080/confirmuser/"+userid+"/\"> Click to confirm </a></h1>",
					"text/html");
			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}


	@RequestMapping(value="/confirmuser/{userid}", method=RequestMethod.GET)
	public String welcomepage(@PathVariable Long userid) {
//		Optional<User> userlist =   userrepo.findById(userid);
		//do a null check for home work
		User usr = userrepo.getById(userid);
		usr.setConfirmed(true);
		userrepo.save(usr);
		return "User confirmed " +usr.getUsername();
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/editUser")
	public void editUser(@RequestBody User user) {
		userrepo.save(user);
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/loginUser")
	public ResponseEntity<String> login(@RequestBody User user){
		String username = user.getUsername();
		String password = user.getPassword();
		Optional<User> validUser = userrepo.findByUsernameAndPassword(username, password);
		if(validUser.isEmpty()) {
			return new ResponseEntity<String>("Invalid credentials", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(validUser.get().getRole(), HttpStatus.OK);
	}
	
}
