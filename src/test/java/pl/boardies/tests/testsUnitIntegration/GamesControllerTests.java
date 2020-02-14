package pl.boardies.tests.testsUnitIntegration;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import pl.boardies.models.Game;
import pl.boardies.repositories.GameRepository;


public class GamesControllerTests extends AbstractTest {
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
	    MockitoAnnotations.initMocks(this);
	}
	@Autowired
	public GameRepository gameRepository;

	
	@Test
	public void testAnyGameIsAdded() throws Exception {
	   String uri = "/games/showAll";
	   
	   gameRepository.deleteAll();
	   Game game = new Game("Gra planszowa.", "Opis gry.", "/img/picture.jpeg");
	   gameRepository.save(game);

	   MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
	      .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
	   
	   String content = mvcResult.getResponse().getContentAsString();
	   Game[] gamelist = super.mapFromJson(content, Game[].class);
	   
	   assertTrue(gamelist.length>0);
	}
	
	@Test
	public void testShowGameWhichHasBeenAdded() throws Exception {
	   String uri = "/games/showAll";
	   
	   gameRepository.deleteAll();
	   Game game = new Game("Gra planszowa.", "Opis gry.", "/img/picture.jpeg");
	   gameRepository.save(game);

	   MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
	      .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
	   
	   String content = mvcResult.getResponse().getContentAsString();
	   Game[] gamelist = super.mapFromJson(content, Game[].class);
	   assertEquals(gamelist[0].getTitle(), game.getTitle());
	}
	
	@Test
	public void testShowAllGamesAdded() throws Exception {
	   String uri = "/games/showAll";
	   
	   gameRepository.deleteAll();
	   Game game = new Game("Gra planszowa1.", "Opis gry1.", "/img/picture1.jpeg");
	   Game game1 = new Game("Gra planszowa2.", "Opis gry2.", "/img/picture2.jpeg");
	   Game game2 = new Game("Gra planszowa3.", "Opis gry3.", "/img/picture3.jpeg"); 
	   gameRepository.save(game);
	   gameRepository.save(game1);
	   gameRepository.save(game2);

	   MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
	      .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
	   
	   String content = mvcResult.getResponse().getContentAsString();
	   Game[] gamelist = super.mapFromJson(content, Game[].class);
	   
	   assertEquals(gamelist[2].getTitle(), game.getTitle());
	   assertEquals(gamelist[1].getTitle(), game1.getTitle());
	   assertEquals(gamelist[0].getTitle(), game2.getTitle());
	}
	
	@Test
	public void testSearchParticularTitle() throws Exception {
	   String uri = "/games/search/Gra planszowa.";
	   
	   gameRepository.deleteAll();
	   Game game = new Game("Gra planszowa.", "Opis gry.", "/img/picture.jpeg");
	   gameRepository.save(game);

	   MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
	      .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
	   
	   String content = mvcResult.getResponse().getContentAsString();
	   Game[] gamelist = super.mapFromJson(content, Game[].class);
	   
	   assertEquals(gamelist[0].getTitle(), game.getTitle());
	}
	
	@Test
	public void testAddGame() throws Exception {
	   String uri = "/games/add";
	   
	   gameRepository.deleteAll();
	   Game game = new Game("Gra planszowa.", "Opis gry.", "/img/picture.jpeg");

	
	   MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("title",game.getTitle()).param("img",game.getImg()).param("description",game.getDescription())
			      .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
	   
	   String content = mvcResult.getResponse().getContentAsString();
	   int id = gameRepository.findByTitleContainsIgnoreCase(game.getTitle()).get(0).getId(); 
	   
	   assertEquals(Integer.parseInt(content),id);
	}
}
	


