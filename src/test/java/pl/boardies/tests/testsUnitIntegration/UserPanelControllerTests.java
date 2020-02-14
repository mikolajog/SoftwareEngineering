package pl.boardies.tests.testsUnitIntegration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import pl.boardies.models.Hash;
import pl.boardies.models.Session;
import pl.boardies.models.User;
import pl.boardies.repositories.BoardiesRepository;
import pl.boardies.repositories.GameRepository;
import pl.boardies.repositories.SessionRepository;
import pl.boardies.repositories.UserRepository;

public class UserPanelControllerTests extends AbstractTest {
	
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
	public void testSetNewPassword() throws Exception {		
		String uri = "/setNewPassword";
		
		sessionRepository.deleteAll();
		userRepository.deleteAll();
		User user = new User("user", "user", "user", "user", "user", "user", "user", "user", "user", 200.0); 
		user.setPassword(Hash.hash("user"));
		user = userRepository.save(user); 
		Session session = new Session(user.getId()); 
		session= sessionRepository.save(session);
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("userId", Integer.toString(user.getId())).param("sessionId", session.getSessionId()).param("newPassword","new").param("confirmedNewPassword", "new").accept(MediaType.APPLICATION_JSON)).andReturn();
				
		String content = mvcResult.getResponse().getContentAsString();
		ReturnCode result = super.mapFromJson(content, ReturnCode.class); 
		
		assertEquals(result.getCode(), ReturnCode.SUCCESS.getCode()); 
	}
   
	
    @Test
	public void testCanEditProfileWithSuccess() throws Exception {
    	String uri = "/editProfile";
    	
		sessionRepository.deleteAll();
		userRepository.deleteAll();
		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0); 
		user.setPassword(Hash.hash("user"));
		user = userRepository.save(user); 
		Session session = new Session(user.getId()); 
		session= sessionRepository.save(session);
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("sessionId", session.getSessionId()).param("oldPassword", "user").param("password", "new").param("passwordConfirmation", "new").param("street", "new").param("postalCode", "new").param("city", "new").param("email", "new").param("phone", "new")
				.accept(MediaType.APPLICATION_JSON)).andReturn();
				
		String content = mvcResult.getResponse().getContentAsString();
		ReturnCode result = super.mapFromJson(content, ReturnCode.class); 
		
		assertEquals(result.getCode(), ReturnCode.SUCCESS.getCode()); 
	}
    
    @Test
   	public void testCanEditProfileWithSessionError() throws Exception {
       	String uri = "/editProfile";
       	
   		sessionRepository.deleteAll();
   		userRepository.deleteAll();
   		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0); 
   		user.setPassword(Hash.hash("user"));
   		user = userRepository.save(user); 
   		Session session = new Session(user.getId()); 
   		session= sessionRepository.save(session);
   		
   		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("sessionId", "").param("oldPassword", "user").param("password", "new").param("passwordConfirmation", "new").param("street", "new").param("postalCode", "new").param("city", "new").param("email", "new").param("phone", "new")
   				.accept(MediaType.APPLICATION_JSON)).andReturn();
   				
   		String content = mvcResult.getResponse().getContentAsString();
   		ReturnCode result = super.mapFromJson(content, ReturnCode.class); 
   		
   		assertEquals(result.getCode(), ReturnCode.SESSION_ERROR.getCode()); 
   	}
    
    @Test
   	public void testCanEditProfileThrowsErrorGivenDifferentPasswords() throws Exception {
       	String uri = "/editProfile";
       	
   		sessionRepository.deleteAll();
   		userRepository.deleteAll();
   		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0); 
   		user.setPassword(Hash.hash("user"));
   		user = userRepository.save(user); 
   		Session session = new Session(user.getId()); 
   		session= sessionRepository.save(session);
   		
   		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("sessionId", "").param("oldPassword", "topsecret123").param("password", "new1").param("passwordConfirmation", "new").param("street", "new").param("postalCode", "new").param("city", "new").param("email", "new").param("phone", "new")
   				.accept(MediaType.APPLICATION_JSON)).andReturn();
   				
   		String content = mvcResult.getResponse().getContentAsString();
   		ReturnCode result = super.mapFromJson(content, ReturnCode.class); 
   		
   		assertEquals(result.getCode(), ReturnCode.SESSION_ERROR.getCode()); 
   	}
    
    @Test
	public void testCanGetName() throws Exception {
    	String uri = "/getName";
    	
		sessionRepository.deleteAll();
		userRepository.deleteAll();
		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0); 
		user = userRepository.save(user); 
		Session session = new Session(user.getId()); 
		session= sessionRepository.save(session);
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("sessionId", session.getSessionId())
				.accept(MediaType.APPLICATION_JSON)).andReturn();
				
		String content = mvcResult.getResponse().getContentAsString();
		
		assertEquals(content, user.getName()); 
	}
    
    @Test
	public void testCanGetMatchForSession() throws Exception {
    	String uri = "/getMatchForSession";
    	
		sessionRepository.deleteAll();
		userRepository.deleteAll();
		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0);
		user = userRepository.save(user); 
		Session session = new Session(user.getId()); 
		session= sessionRepository.save(session);
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("sessionId", session.getSessionId()).param("userId", Integer.toString(user.getId()))
				.accept(MediaType.APPLICATION_JSON)).andReturn();
				
		String content = mvcResult.getResponse().getContentAsString();
		boolean result = super.mapFromJson(content, boolean.class); 
		
		assertTrue(result); 
	}
	
	@Test
	public void testCanGetUser() throws Exception {
		String uri = "/getUser";
		
		sessionRepository.deleteAll();
		userRepository.deleteAll();
		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0);
		user = userRepository.save(user); 
		Session session = new Session(user.getId()); 
		session= sessionRepository.save(session);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("sessionId", session.getSessionId())
				.accept(MediaType.APPLICATION_JSON)).andReturn();
				
		String content = mvcResult.getResponse().getContentAsString();	
		
		assertNotNull(content); 
	}
	
	@Test
	public void testCanGetUserById() throws Exception {
		String uri = "/getUserById";
		
		userRepository.deleteAll();
		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0); 
		user = userRepository.save(user); 
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("userId", Integer.toString(user.getId()))
				.accept(MediaType.APPLICATION_JSON)).andReturn();
				
		String content = mvcResult.getResponse().getContentAsString();	
		
		assertNotNull(content); 
	}
}
