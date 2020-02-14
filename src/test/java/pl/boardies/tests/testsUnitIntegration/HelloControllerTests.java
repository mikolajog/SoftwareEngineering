package pl.boardies.tests.testsUnitIntegration;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


public class HelloControllerTests extends AbstractTest {
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
	    MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void getProductsList() throws Exception {
	   String uri = "/gr";
	   
	   MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
	      .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
	   
	   String content = mvcResult.getResponse().getContentAsString();
	   
	   assertEquals("Greetings from Spring Boot!", content);
	}
}
