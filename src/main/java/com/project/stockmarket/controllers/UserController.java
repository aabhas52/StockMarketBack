package com.project.stockmarket.controllers;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.stockmarket.entities.User;
import com.project.stockmarket.repositories.UserRepository;

//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "https://stock-market-front.herokuapp.com")
@RestController
public class  UserController {
	
	@Autowired
	UserRepository userrepo;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;
	
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
		final String password = "Dec@2021";

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
					"<h1><a href =\"https://stock-market-front.herokuapp.com/confirmuser/"+userid+"/\"> Click to confirm </a></h1>",
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

	@PostMapping("/editUser")
	public void editUser(@RequestBody Map<String, String> body) {
		long id = Long.parseLong((String)body.get("id"));
		User user = userrepo.findById(id);
		String username = (String)body.get("username");
		String password = bcryptEncoder.encode((String)body.get("password"));
		User newUser = new User(username, password, user.getEmail(), user.getMobile(), true, user.getRole());
		userrepo.delete(user);
		userrepo.save(newUser);
	}
	
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
