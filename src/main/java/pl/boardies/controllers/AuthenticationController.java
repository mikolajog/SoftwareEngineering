package pl.boardies.controllers;

import org.springframework.web.bind.annotation.*;

import pl.boardies.models.Hash;
import pl.boardies.models.Session;
import pl.boardies.models.User;
import pl.boardies.repositories.SessionRepository;
import pl.boardies.repositories.UserRepository;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;

// TODO: Cascade relations between entities
enum ReturnCode {
	  DATABASE_ERROR(0, "A database error has occured."),
	  SUCCESS(1, "User registered/logged successfully!"),
	  DUPLICATE_USERNAME(-1, "User with such username already exists."),
	  DUPLICATE_EMAIL(-2, "User with such email address already exists."),
	  DUPLICATE_PHONE(-3, "User with such phone number already exists."),
	  MISMATCHING_PASSWORDS(-4, "Passwords don't match."),
	  EMPTY_FIELDS(-5, "Some fields are empty."),
	  LOGIN_ERROR(-9, "Wrong log in details."),
	  SESSION_ERROR(-10, "Wrong session ID."),
	  BAD_PASSWORD(-11, "Bad password.");

	  private final int code;
	  private final String description;

	  private ReturnCode(int code, String description) {
	    this.code = code;
	    this.description = description;
	  }

	  public String getDescription() {
	     return description;
	  }

	  public int getCode() {
	     return code;
	  }

	  @Override
	  public String toString() {
	    return code + ": " + description;
	  }
	}

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
@RestController
public class AuthenticationController {

    @Autowired
    public UserRepository userRepository;
    
    @Autowired
    public SessionRepository sessionRepository;
	
	private void deleteSession(int userId) {
		Optional<Session> optSession = sessionRepository.findById(userId);
		if (optSession.isPresent()) {
			sessionRepository.delete(optSession.get());
		}
	}
	
	private void deleteSession(int userId, String sessionId) {
		Session session = sessionRepository.findByUserIdAndSessionId(userId, sessionId);
		if (session != null) {
			sessionRepository.delete(session);
		}
	}
	
	public String addSession(int userId) throws NoSuchAlgorithmException {
		deleteSession(userId);
		Session ses = new Session(userId);
		sessionRepository.save(ses);
		return ses.getSessionId();
	}

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestParam String username, @RequestParam String password) throws NoSuchAlgorithmException {
    	User user = userRepository.findByUsernameIgnoreCase(username);
    	if (user == null || !user.getPassword().equals(Hash.hash(password)))
    		return "0";
    	String resultSession = addSession(user.getId());
    	return resultSession;
    }
    
    @PostMapping("/getUserId")
    @ResponseBody
    public int getUserId(@RequestParam String sessionId) throws NoSuchAlgorithmException {
    	Session ses = sessionRepository.findBySessionId(sessionId);
    	if(ses == null)
    		return 0;
    	return ses.getUserId();
    }
    
    @PostMapping("/logout")
    @ResponseBody
    public ReturnCode logout(@RequestParam int userId, @RequestParam String sessionId) throws NoSuchAlgorithmException {
    	Optional<User> user = userRepository.findById(userId);
    	if (!user.isPresent())
    		return ReturnCode.DATABASE_ERROR;
    	deleteSession(userId, sessionId);
    	return ReturnCode.SUCCESS;
    }
    
    @PostMapping("/register")
    @ResponseBody
    public ReturnCode register(@RequestParam String username, @RequestParam String password, String passwordConfirmation, 
    		@RequestParam String name, @RequestParam String surname, @RequestParam String street, 
    		@RequestParam String postalCode, @RequestParam String city, @RequestParam String email, @RequestParam String phone) throws NoSuchAlgorithmException {
    	
    	if(fieldsEmpty(Arrays.asList(username, password, passwordConfirmation, name, surname, street, postalCode, city, email, phone)))
    		return ReturnCode.EMPTY_FIELDS;
    	
    	if(!password.equals(passwordConfirmation)) {return ReturnCode.MISMATCHING_PASSWORDS; }
    	if(userRepository.findByUsernameIgnoreCase(username) != null) return ReturnCode.DUPLICATE_USERNAME;
    	if(userRepository.findByEmailIgnoreCase(email) != null) return ReturnCode.DUPLICATE_EMAIL;
    	if(userRepository.findByPhone(phone) != null) return ReturnCode.DUPLICATE_PHONE;
    	
    	userRepository.save(new User(username, Hash.hash(password), name, surname, street, postalCode, city, email, phone, 100));
    	return ReturnCode.SUCCESS;
    	
    }

    private Boolean fieldsEmpty(List<String> fields) {
    	for(String s : fields) {
    		if(s == null || s.isEmpty() || s.trim().length() == 0) {
    			return true;
    		}
    	}
    	return false;
    }

}

