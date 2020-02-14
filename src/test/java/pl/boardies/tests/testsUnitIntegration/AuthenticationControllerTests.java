package pl.boardies.tests.testsUnitIntegration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import pl.boardies.models.Session;
import pl.boardies.models.User;
import pl.boardies.repositories.BoardiesRepository;
import pl.boardies.repositories.GameRepository;
import pl.boardies.repositories.SessionRepository;
import pl.boardies.repositories.UserRepository;

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

public class AuthenticationControllerTests extends AbstractTest {
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
	    MockitoAnnotations.initMocks(this);
	}
	@Autowired
	public GameRepository gameRepository;
	@Autowired
	public BoardiesRepository boardiesRepository;
    @Autowired
    public SessionRepository sessionRepository;
    @Autowired
    public UserRepository userRepository;
	
	
	@Test
	public void testLogin() throws Exception {
		String uri = "/login";
		
		userRepository.deleteAll();
		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0);
		userRepository.save(user); 
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("username",user.getUsername()).param("password",user.getPassword())
		.accept(MediaType.APPLICATION_JSON)).andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		String result = super.mapFromJson(content, String.class); 
		
		assertTrue(result!="0");	 

}
	
	@Test
	public void testCanGetUserIdGivenSessionID() throws Exception {
		String uri = "/getUserId";
		
		sessionRepository.deleteAll();
		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0);
		Session session = new Session(12); 
		Session sessionRet = sessionRepository.save(session);
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("sessionId",sessionRet.getSessionId())
		.accept(MediaType.APPLICATION_JSON)).andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		String result = super.mapFromJson(content, String.class); 
		assertEquals(Integer.parseInt(result), 12);	 

}
	
	@Test
	public void testCantNoLogutUserGivenUserIdandSessionIdButUserNotInDB() throws Exception {
		String uri = "/logout";
		
		sessionRepository.deleteAll();
		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0);
		Session session = new Session(12); 
		Session sessionRet = sessionRepository.save(session);
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("userId", "12").param("sessionId",sessionRet.getSessionId())
		.accept(MediaType.APPLICATION_JSON)).andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		ReturnCode result = super.mapFromJson(content, ReturnCode.class); 
		assertEquals(result.getCode(), ReturnCode.DATABASE_ERROR.getCode());	 
}
	
	@Test
	public void testCanLogutUserGivenUserIdandSessionId() throws Exception {	
		String uri = "/logout";
		
		sessionRepository.deleteAll();
		userRepository.deleteAll();
		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0);
		user = userRepository.save(user); 
		Session session = new Session(user.getId()); 
		Session sessionRet = sessionRepository.save(session);
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("userId", Integer.toString(user.getId())).param("sessionId",sessionRet.getSessionId())
		.accept(MediaType.APPLICATION_JSON)).andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		ReturnCode result = super.mapFromJson(content, ReturnCode.class); 
		assertEquals(result.getCode(), ReturnCode.SUCCESS.getCode());	 

}
	
	@Test
	public void testCanRegisterUser() throws Exception {
		String uri = "/register";
		
		userRepository.deleteAll();
		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0);
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("username", user.getUsername()).param("password", user.getPassword()).param("passwordConfirmation", user.getPassword()).param("name", user.getName()).param("surname", user.getSurname()).param("street", user.getStreet()).param("postalCode", user.getPostalCode()).param("city", user.getCity()).param("email", user.getEmail()).param("phone", user.getPhone())
		.accept(MediaType.APPLICATION_JSON)).andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		ReturnCode result = super.mapFromJson(content, ReturnCode.class); 
		assertEquals(result.getCode(), ReturnCode.SUCCESS.getCode());	 

}

}

