package pl.boardies.tests.testsUnitIntegration;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
import pl.boardies.models.User;
import pl.boardies.repositories.BoardiesRepository;
import pl.boardies.repositories.GameRepository;
import pl.boardies.repositories.SessionRepository;
import pl.boardies.repositories.UserRepository;

public class BoardiesControllerTests extends AbstractTest {
	
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
	public void testCanAddBoardie() throws Exception {
		String uri = "/boardies/add";
		
		gameRepository.deleteAll();
		userRepository.deleteAll();
		Game game = new Game("Gra planszowa.", "Opis gry.", "/img/picture.jpeg");
		int id_game = gameRepository.save(game).getId(); 
		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0);
		userRepository.save(user); 
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("id_game",Integer.toString(id_game)).param("id_user",Integer.toString(user.getId())).param("price",Integer.toString(100))
		.accept(MediaType.APPLICATION_JSON)).andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		Boolean result = super.mapFromJson(content, Boolean.class); 
		
		assertTrue(result);	   
	}
	
	@Test
	public void testCanDeleteBoardie() throws Exception {
		gameRepository.deleteAll();
		userRepository.deleteAll();
		sessionRepository.deleteAll();
		boardiesRepository.deleteAll();
		Game game = new Game("Gra planszowa.", "Opis gry.", "/img/picture.jpeg");
		int id_game = gameRepository.save(game).getId(); 
		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0);
		userRepository.save(user); 
		Session session = new Session(user.getId()); 
		sessionRepository.save(session);
		Boardies boardie = new Boardies(user, game, 100.0, 1);
		int id_boardie = boardiesRepository.save(boardie).getId();
		
		String uri = "/boardies/delete/"+Integer.toString(id_boardie)+"/"+Integer.toString(user.getId())+"/"+session.getSessionId(); 
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		Boolean result = super.mapFromJson(content, Boolean.class); 
		
		assertTrue(result);	   
	}
	
	
	@Test
	public void testCanChangeStatusOfBoardie() throws Exception {
		gameRepository.deleteAll();
		userRepository.deleteAll();
		sessionRepository.deleteAll();
		boardiesRepository.deleteAll();
		Game game = new Game("Gra planszowa.", "Opis gry.", "/img/picture.jpeg");
		int id_game = gameRepository.save(game).getId(); 
		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0);
		userRepository.save(user); 
		Session session = new Session(user.getId()); 
		sessionRepository.save(session);
		Boardies boardie = new Boardies(user, game, 100.0, 1);
		int id_boardie = boardiesRepository.save(boardie).getId();
	
		String uri = "/boardies/changeStatus/"+Integer.toString(id_boardie)+"/"+Integer.toString(user.getId())+"/"+session.getSessionId()+"/2"; 
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		Boolean result = super.mapFromJson(content, Boolean.class); 
		
		assertTrue(result);	   
	}
	
	@Test
	public void testCanGetCheapestPriceOfBoardie() throws Exception {
		gameRepository.deleteAll();
		userRepository.deleteAll();
		sessionRepository.deleteAll();
		boardiesRepository.deleteAll();
		Game game = new Game("Gra planszowa.", "Opis gry.", "/img/picture.jpeg");
		int id_game = gameRepository.save(game).getId(); 
		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0);
		userRepository.save(user); 
		Session session = new Session(user.getId()); 
		sessionRepository.save(session);
		Boardies boardie = new Boardies(user, game, 100.0, 1);
		int id_boardie = boardiesRepository.save(boardie).getId();
	
		String uri = "/boardies/getCheapestPrice/"+game.getId(); 
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		int result = super.mapFromJson(content, int.class); 
		
		assertTrue(result!=0);	   
	}
	
	@Test
	public void testCanGetIdsOfUsersBoardies() throws Exception {
		gameRepository.deleteAll();
		userRepository.deleteAll();
		sessionRepository.deleteAll();
		boardiesRepository.deleteAll();
		Game game = new Game("Gra planszowa.", "Opis gry.", "/img/picture.jpeg");
		int id_game = gameRepository.save(game).getId(); 
		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0);
		userRepository.save(user); 
		Session session = new Session(user.getId()); 
		sessionRepository.save(session);
		Boardies boardie = new Boardies(user, game, 100.0, 1);
		int id_boardie = boardiesRepository.save(boardie).getId();
	
		String uri = "/boardies/getIdsOfUsersBoardies/"+user.getId(); 
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		System.out.println(content);
		int[] result = super.mapFromJson(content, int[].class); 
		
		assertEquals(result[0], game.getId());	   
	}
	
	@Test
	public void testCanSearchBoardieByTitle() throws Exception {
		gameRepository.deleteAll();
		userRepository.deleteAll();
		sessionRepository.deleteAll();
		boardiesRepository.deleteAll();
		Game game = new Game("Gra planszowa.", "Opis gry.", "/img/picture.jpeg");
		int id_game = gameRepository.save(game).getId(); 
		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0);
		userRepository.save(user); 
		Session session = new Session(user.getId()); 
		sessionRepository.save(session);
		Boardies boardie = new Boardies(user, game, 100.0, 1);
		int id_boardie = boardiesRepository.save(boardie).getId();
	
		String uri = "/boardies/search/"+game.getTitle(); 
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		assertNotNull(content);	   
	}
	
	@Test
	public void testCanShowAllBoardies() throws Exception {
		gameRepository.deleteAll();
		userRepository.deleteAll();
		sessionRepository.deleteAll();
		boardiesRepository.deleteAll();
		Game game = new Game("Gra planszowa.", "Opis gry.", "/img/picture.jpeg");
		int id_game = gameRepository.save(game).getId(); 
		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0);
		userRepository.save(user); 
		Session session = new Session(user.getId()); 
		sessionRepository.save(session);
		Boardies boardie = new Boardies(user, game, 100.0, 1);
		int id_boardie = boardiesRepository.save(boardie).getId();
	
		String uri = "/boardies/showAll"; 
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		
		assertNotNull(content);	   
	}
	
	
	@Test
	public void testCanNotShowAllBoardiesAvailableAsNoAvailable() throws Exception {
		gameRepository.deleteAll();
		userRepository.deleteAll();
		sessionRepository.deleteAll();
		boardiesRepository.deleteAll();
		Game game = new Game("Gra planszowa.", "Opis gry.", "/img/picture.jpeg");
		int id_game = gameRepository.save(game).getId(); 
		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0);
		userRepository.save(user); 
		Session session = new Session(user.getId()); 
		sessionRepository.save(session);
		Boardies boardie = new Boardies(user, game, 100.0, 0);
		int id_boardie = boardiesRepository.save(boardie).getId();
	
		String uri = "/boardies/showAllAvailable"; 
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		
		assertEquals(content, "");	   
	}
	
	@Test
	public void testCanShowAllBoardiesAvailable() throws Exception {
		gameRepository.deleteAll();
		userRepository.deleteAll();
		sessionRepository.deleteAll();
		boardiesRepository.deleteAll();
		Game game = new Game("Gra planszowa.", "Opis gry.", "/img/picture.jpeg");
		int id_game = gameRepository.save(game).getId(); 
		User user = new User("jankowalski", "topsecret123", "Jan", "Kowalski", "Wierzbowa","22-220", "Krakow", "jankowalski@gmail.com", "555444333", 200.0);
		userRepository.save(user); 
		Session session = new Session(user.getId()); 
		sessionRepository.save(session);
		Boardies boardie = new Boardies(user, game, 100.0, 0);
		int id_boardie = boardiesRepository.save(boardie).getId();
	
		String uri = "/boardies/showAllAvailable"; 
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		
		assertNotNull(content);	   
	}
	
	
	
}
