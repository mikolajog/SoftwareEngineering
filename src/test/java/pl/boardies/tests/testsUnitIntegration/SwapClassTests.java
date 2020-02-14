package pl.boardies.tests.testsUnitIntegration;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;

import org.junit.Test;

import pl.boardies.models.Boardies;
import pl.boardies.models.Game;
import pl.boardies.models.Swap;
import pl.boardies.models.User;
import java.sql.Timestamp;

public class SwapClassTests {
	
	@Test
	public void createdSwapHasProperUser() {
		Timestamp timestamp1 = new Timestamp(1578672624); 
		Timestamp timestamp2 = new Timestamp(1578672624);
		User user = new User("username","password","email","Name","Surname","Street","PostalCode","City","Phone",100);
		
		Game game = new Game("Title", "Description", "img");
		
		Boardies boardie = new Boardies(user, game, 20.0, 1); 
		
		Swap swap = new Swap(user, timestamp1, timestamp2, boardie); 
		
		assertEquals(user, swap.getBorrower()); 
		assertEquals(timestamp1, swap.getSwapDate()); 
		assertEquals(timestamp2, swap.getExpirationDate()); 
		assertEquals(boardie, swap.getBoardie()); 
	}
	

}
