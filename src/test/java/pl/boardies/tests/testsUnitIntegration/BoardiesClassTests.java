package pl.boardies.tests.testsUnitIntegration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pl.boardies.models.Boardies;
import pl.boardies.models.Game;
import pl.boardies.models.User;

public class BoardiesClassTests {
	
	@Test
	public void createdBoardiehasProperFields() {
		Game game = new Game("Title", "Description", "img");
		User user = new User("username","password","email","Name","Surname","Street","PostalCode","City","Phone",100);
		
		Boardies boardie = new Boardies(user, game, 20.0, 1); 

		assertEquals(user.getUsername(), boardie.getUser().getUsername()); 
		assertEquals(game, boardie.getGame()); 
		assertEquals(20.0, boardie.getPrice(), 0); 
		assertEquals(1, boardie.getStatus()); 
	}
}





