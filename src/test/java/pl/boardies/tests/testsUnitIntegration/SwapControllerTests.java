package pl.boardies.tests.testsUnitIntegration;

import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import pl.boardies.models.Boardies;
import pl.boardies.models.Game;
import pl.boardies.models.Session;
import pl.boardies.models.Swap;
import pl.boardies.models.User;
import pl.boardies.repositories.BoardiesRepository;
import pl.boardies.repositories.GameRepository;
import pl.boardies.repositories.SessionRepository;
import pl.boardies.repositories.SwapRepository;
import pl.boardies.repositories.UserRepository;

public class SwapControllerTests extends AbstractTest {

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
    @Autowired
    public SwapRepository swapRepository;
    
 
    @Test
   	public void testCenPrepareSwap() throws Exception {
   	    gameRepository.deleteAll();
   		userRepository.deleteAll();
   		sessionRepository.deleteAll();
   		boardiesRepository.deleteAll();
   		Game game = new Game("Gra planszowa.", "Opis gry.", "/img/picture.jpeg");
   		game = gameRepository.save(game);
   		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0);
   		user = userRepository.save(user); 
   		Session session = new Session(user.getId()); 
   		session = sessionRepository.save(session);
   		Boardies boardie = new Boardies(user, game, 50.0, 1);
   		boardie = boardiesRepository.save(boardie); 
   		String uri = "/swap/prepare"; 
   		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("boardieId", Integer.toString(boardie.getId())).param("userId", Integer.toString(user.getId())).param("sessionId", session.getSessionId()).accept(MediaType.APPLICATION_JSON)).andReturn();
   		
   		String content = mvcResult.getResponse().getContentAsString();
   		Boolean result = super.mapFromJson(content, Boolean.class); 
   		
   		assertTrue(result);	  
   	}
    
    @Test
   	public void testCenPrepareForSession() throws Exception {
   	    gameRepository.deleteAll();
   		userRepository.deleteAll();
   		sessionRepository.deleteAll();
   		boardiesRepository.deleteAll();
   		Game game = new Game("Gra planszowa.", "Opis gry.", "/img/picture.jpeg");
   		game = gameRepository.save(game);
   		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0);
   		user = userRepository.save(user); 
   		Session session = new Session(user.getId()); 
   		session = sessionRepository.save(session);
   		Boardies boardie = new Boardies(user, game, 50.0, 1);
   		boardie = boardiesRepository.save(boardie); 
   		String uri = "/swap/prepareForSession"; 
   		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("boardieId", Integer.toString(boardie.getId())).param("sessionId", session.getSessionId()).accept(MediaType.APPLICATION_JSON)).andReturn();
   		
   		String content = mvcResult.getResponse().getContentAsString();
   		Boolean result = super.mapFromJson(content, Boolean.class); 
   		
   		assertTrue(result);	  
   	}
    
    @Test
   	public void testSwapConfirm() throws Exception {
   	    gameRepository.deleteAll();
   		userRepository.deleteAll();
   		sessionRepository.deleteAll();
   		boardiesRepository.deleteAll();
   		Game game = new Game("Gra planszowa.", "Opis gry.", "/img/picture.jpeg");
   		game = gameRepository.save(game);
   		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0);
   		user = userRepository.save(user); 
   		Session session = new Session(user.getId()); 
   		session = sessionRepository.save(session);
   		Boardies boardie = new Boardies(user, game, 50.0, 1);
   		boardie = boardiesRepository.save(boardie); 
   		String uri = "/swap/prepareForSession"; 
   		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("boardieId", Integer.toString(boardie.getId())).param("userId", Integer.toString(user.getId())).param("sessionId", session.getSessionId()).accept(MediaType.APPLICATION_JSON)).andReturn();
   		
   		String content = mvcResult.getResponse().getContentAsString();
   		Boolean result = super.mapFromJson(content, Boolean.class); 
   		
   		assertTrue(result);	  
   	}
    
	
 @Test
	public void testCanDeleteSwap() throws Exception {
	    gameRepository.deleteAll();
		userRepository.deleteAll();
		sessionRepository.deleteAll();
		boardiesRepository.deleteAll();
		swapRepository.deleteAll(); 
		Game game = new Game("Gra planszowa.", "Opis gry.", "/img/picture.jpeg");
		game = gameRepository.save(game);
		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0); 
		user = userRepository.save(user); 
		Session session = new Session(user.getId()); 
		session = sessionRepository.save(session);
		
		Boardies boardie = new Boardies(user, game, 50.0, 1);
		boardie = boardiesRepository.save(boardie); 
	    Swap swap = new Swap(user, new Timestamp(1000), new Timestamp(1200), boardie);
	    swap = swapRepository.save(swap);
	    
	    String uri = "/swap/delete/"+swap.getId()+"/"+user.getId()+"/"+session.getSessionId(); 
	    
	    MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		Boolean result = super.mapFromJson(content, Boolean.class); 
		
		assertTrue(result);	   
	}
	
 @Test
	public void testCanCancelSwap() throws Exception {
	    gameRepository.deleteAll();
		userRepository.deleteAll();
		sessionRepository.deleteAll();
		boardiesRepository.deleteAll();
		swapRepository.deleteAll(); 
		Game game = new Game("Gra planszowa.", "Opis gry.", "/img/picture.jpeg");
		game = gameRepository.save(game);
		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0); 
		user = userRepository.save(user); 
		Session session = new Session(user.getId()); 
		session = sessionRepository.save(session);
		Boardies boardie = new Boardies(user, game, 50.0, 1);
		boardie = boardiesRepository.save(boardie); 
	    Swap swap = new Swap(user, new Timestamp(1000), new Timestamp(1200), boardie);
	    swap = swapRepository.save(swap);
	    
	    String uri = "/swap/cancel"; 
	    MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("swapId", Integer.toString(swap.getId())).param("userId", Integer.toString(user.getId())).param("sessionId", session.getSessionId()).accept(MediaType.APPLICATION_JSON)).andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		Boolean result = super.mapFromJson(content, Boolean.class); 
		
		assertTrue(result);	    
	}
	
	
}
