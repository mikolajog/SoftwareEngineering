package pl.boardies.tests.testsUnitIntegration;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;

import org.junit.Test;

import pl.boardies.models.Boardies;
import pl.boardies.models.Game;
import pl.boardies.models.Swap;
import pl.boardies.models.User;

public class GameClassTests {
	
	@Test
	public void createdGameHasProperFields() {
		Game game = new Game("Title", "Description", "img");
		
		assertEquals("Title", game.getTitle()); 
		assertEquals("Description", game.getDescription()); 
		assertEquals("img", game.getImg()); 
	}

}
