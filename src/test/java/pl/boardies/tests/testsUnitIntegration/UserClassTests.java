package pl.boardies.tests.testsUnitIntegration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import pl.boardies.models.User;

public class UserClassTests {
	
	@Test
    public void createdUserHasProperUsername() 
    {
		User user = new User("user", "password", "Name", "Surname", "Street", "22-200", "City", "Email@emails.com", "666555444", 100.0); 
		assertEquals("user",(user.getUsername()));
		assertEquals("password",(user.getPassword()));
		assertEquals("Name",(user.getName()));
		assertEquals("Surname",(user.getSurname()));
		assertEquals("Street",(user.getStreet()));
		assertEquals("22-200",(user.getPostalCode()));
		assertEquals("City",(user.getCity()));
		assertEquals("Email@emails.com",(user.getEmail()));
		assertEquals("666555444",(user.getPhone()));
		assertEquals(100.0,(user.getBalance()), 0);
    }

}
