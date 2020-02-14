package pl.boardies.controllers;

import org.springframework.web.bind.annotation.*;

import pl.boardies.models.Hash;
import pl.boardies.models.Mail;
import pl.boardies.models.Session;
import pl.boardies.models.User;
import pl.boardies.repositories.SessionRepository;
import pl.boardies.repositories.UserRepository;
import pl.boardies.services.MailService;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import pl.boardies.controllers.ReturnCode;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
@RestController
public class UserPanelController {
	
	 @Autowired
	 public UserRepository userRepository;
	 @Autowired
	 public SessionRepository sessionRepository;
	 
	 @Autowired
	 public MailService mailService;
	 
	 @Autowired
	 private Environment env;

	 private Boolean fieldsEmpty(List<String> fields) {
		for(String s : fields) {
			if(s == null || s.isEmpty() || s.trim().length() == 0) {
				return true;
			}
		}
		return false;
	 }
	 
	 
	 
	 private User updateUser(User user, String password, String street, String postalCode, String city, String email, String phone, double balance) {
		 user.setPassword(password);
		 user.setStreet(street);
		 user.setPostalCode(postalCode);
		 user.setCity(city);
		 user.setEmail(email);
		 user.setPhone(phone);
		 user.setBalance(balance);
		 return user;
	 }
	//Temporary: Will be edited when front-end is implemented!
	 @PostMapping("/forgottenPassword")
     @ResponseBody
     public Boolean sendMail(@RequestParam String email) throws NoSuchAlgorithmException {
		 User user = userRepository.findByEmailIgnoreCase(email);
		 if(user == null)
			 return false;
		 Session session = new Session(-user.getId());
		 sessionRepository.save(session);
		 Mail mail = new Mail();
       	 mail.setMailFrom("boardieswebservice@gmail.com");
         mail.setMailTo(email);
         mail.setMailSubject("Boardies - zapomniane hasło");
         mail.setMailContent("Dostajesz tego maila, bo użyłeś formularza przypomnienia hasła\n\nOto link do ustawienia nowego hasla: http://"+env.getProperty("my-front")+"newPassword?i="+user.getId()+"&s="+session.getSessionId());
         mailService.sendEmail(mail);
         return true;
	 }
	 
	//Temporary: Will be edited when front-end is implemented!
	 /*
	 @GetMapping("/newPassword/{userId}-{sessionId}")
	 @ResponseBody
	 public String newPassword(@PathVariable int userId, @PathVariable String sessionId) throws NoSuchAlgorithmException {
		 if(sessionRepository.findBySessionId(sessionId) == null)
			 return "Zla sesja dla tego uzytkownika.";
		 Optional<User> userOpt = userRepository.findById(userId);
		 if(!userOpt.isPresent())
			 return "Nie ma takiego uzytkownika.";
		 return "Tutaj bedzie strona FrontEndowa gdzie w formularzu wpisze sie nowe haslo jego potwierdzenie po czym wywola setNewPassword().";
	 }
	 */
	 @PostMapping("/setNewPassword")
	 @ResponseBody
	 public ReturnCode setNewPassword(@RequestParam int userId, @RequestParam String sessionId, @RequestParam String newPassword, @RequestParam String confirmedNewPassword) throws NoSuchAlgorithmException {
		 if(sessionRepository.findBySessionId(sessionId) == null)
			 return ReturnCode.SESSION_ERROR;
		 if(!confirmedNewPassword.equals(newPassword))
			 return ReturnCode.MISMATCHING_PASSWORDS;
		 Optional<User> userOpt = userRepository.findById(userId);
		 if(!userOpt.isPresent())
			 return ReturnCode.DATABASE_ERROR;
		 User user = userOpt.get();
		 user.setPassword(Hash.hash(newPassword));
		 userRepository.save(user);
		 sessionRepository.deleteById(userId);
		 return ReturnCode.SUCCESS;
	 }
	 
	@PostMapping("/getUser")
	@ResponseBody
	public User getUser(@RequestParam String sessionId) {
    	int id = sessionRepository.findBySessionId(sessionId).getUserId();
    	if(userRepository.findById(id).isPresent())
    		return userRepository.findById(id).get();
    	return null;
    }
	 
	@PostMapping("/getUserById")
	@ResponseBody
	public User getUserById(@RequestParam int userId) {
   		return userRepository.findById(userId).get();
   }
	
	@PostMapping("/getMatchForSession")
	@ResponseBody
	public boolean getMatchForSession(@RequestParam int userId, @RequestParam String sessionId) {
		if(sessionRepository.findByUserIdAndSessionId(userId, sessionId) != null)
			return true;
		return false;
   }
	 
    @PostMapping("/getName")
    @ResponseBody
    public String getName(@RequestParam String sessionId) {
    	int id = sessionRepository.findBySessionId(sessionId).getUserId();
    	if(userRepository.findById(id).isPresent())
    		return userRepository.findById(id).get().getName();
    	return "";
    }
    
    @PostMapping("/getBalance")
    @ResponseBody
    public int getBalance(@RequestParam String sessionId) {
    	int id = sessionRepository.findBySessionId(sessionId).getUserId();
    	if(userRepository.findById(id).isPresent())
    		return (int)userRepository.findById(id).get().getBalance();
    	return 0;
    }
	 
     @PostMapping("/editProfile")
     @ResponseBody
     public ReturnCode editProfile(@RequestParam String sessionId, @RequestParam String oldPassword, @RequestParam String password, String passwordConfirmation, @RequestParam String street, 
    		@RequestParam String postalCode, @RequestParam String city, @RequestParam String email, @RequestParam String phone) throws NoSuchAlgorithmException {
    	Session temp = sessionRepository.findBySessionId(sessionId);
    	if(temp == null)
    		return ReturnCode.SESSION_ERROR;
    	int userId = temp.getUserId();
    	if(sessionRepository.findByUserIdAndSessionId(userId, sessionId) == null)
    		return ReturnCode.SESSION_ERROR;
    	User user = userRepository.findById(userId).get();
    	if (user == null || !user.getPassword().equals(Hash.hash(oldPassword)))
    		return ReturnCode.BAD_PASSWORD;
    	if(password.isEmpty() && passwordConfirmation.isEmpty()) {
    		password = oldPassword;
    		passwordConfirmation = oldPassword;
    	}
    	if(fieldsEmpty(Arrays.asList(oldPassword, password, passwordConfirmation, street, postalCode, city, email, phone)))
    		return ReturnCode.EMPTY_FIELDS;
    	
    	if(!password.equals(passwordConfirmation)) {return ReturnCode.MISMATCHING_PASSWORDS; }
    	if(!user.getEmail().toLowerCase().equals(email.toLowerCase()) && userRepository.findByEmailIgnoreCase(email) != null) return ReturnCode.DUPLICATE_EMAIL;
    	if(!user.getPhone().equals(phone) && userRepository.findByPhone(phone) != null) return ReturnCode.DUPLICATE_PHONE;
    	userRepository.save(updateUser(user, Hash.hash(password), street, postalCode, city, email, phone, user.getBalance()));
    	return ReturnCode.SUCCESS;
     }
}
