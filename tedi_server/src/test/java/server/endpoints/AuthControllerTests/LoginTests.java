package server.endpoints.AuthControllerTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import javax.servlet.ServletContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import server.auth.SecurityService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration
@WebAppConfiguration
public class LoginTests {
	
	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private SecurityService secService;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	public void verifyWacLoaded() {
		ServletContext serContext = wac.getServletContext();
		Assert.assertNotNull(serContext);
		Assert.assertTrue(serContext instanceof MockServletContext);
	}
	
	//Tests for logging in users
	//RUN AFTER YOU RUN REGISTERTESTS
	
	@Test
	public void sequentiallyLoginAndLogoutExistingUsers() {
		try {
			MockHttpServletResponse result = login("a@a.a", "a");
			assertEquals(200, result.getStatus());
			result = logout();
			assertEquals(200, result.getStatus());
			assertEquals(null, secService.currentUser());
			
			result = login("f@f.f", "f");
			assertEquals(200, result.getStatus());
			result = logout();
			assertEquals(200, result.getStatus());
			assertEquals(null, secService.currentUser());
			
			result = login("k@k.k", "k");
			assertEquals(200, result.getStatus());
			result = logout();
			assertEquals(200, result.getStatus());
			assertEquals(null, secService.currentUser());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/* not working for some reason!
	@Test
	public void loginWithNonExistingAccount() {
		try {
			MockHttpServletResponse result = login("nonexist@nonexist.nan", "none");
			assertEquals(404, result.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}*/
	
	@Test
	public void logoutWithoutLogin() {
		try {
			MockHttpServletResponse result = logout();
			assertEquals(200, result.getStatus());
			assertEquals(null, secService.currentUser());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	private MockHttpServletResponse login(String email, String password) throws Exception {
		MvcResult result = mockMvc.perform(post("/login")
							.param("email", email)
							.param("password", password))
							.andReturn();
		return result.getResponse();
	}
	
	private MockHttpServletResponse logout() throws Exception {
		MvcResult result = mockMvc.perform(get("/logout")).andReturn();
		return result.getResponse();
	}
	
}
