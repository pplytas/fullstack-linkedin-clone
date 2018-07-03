package server.endpoints.AuthControllerTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import javax.servlet.ServletContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import server.endpoints.AuthorizedTests;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration
@WebAppConfiguration
public class LoginTests extends AuthorizedTests {

	@Before
	public void setup() throws Exception {
		setupMockMvc();
	}
	
	@Test
	public void verifyWacLoaded() {
		ServletContext serContext = wac.getServletContext();
		Assert.assertNotNull(serContext);
		Assert.assertTrue(serContext instanceof MockServletContext);
	}
	
	@Test
	public void sequentiallyLoginAndLogoutExistingUsers() {
		try {
			MockHttpServletResponse result = login("a@a.a", "a");
			assertEquals(302, result.getStatus());
			assertEquals(200, getSimpleUserView().getStatus());
			result = logout();
			assertEquals(200, result.getStatus());
			assertEquals(403, getSimpleUserView().getStatus());
			
			result = login("f@f.f", "f");
			assertEquals(302, result.getStatus());
			assertEquals(200, getSimpleUserView().getStatus());
			result = logout();
			assertEquals(200, result.getStatus());
			assertEquals(403, getSimpleUserView().getStatus());
			
			result = login("k@k.k", "k");
			assertEquals(302, result.getStatus());
			assertEquals(200, getSimpleUserView().getStatus());
			result = logout();
			assertEquals(200, result.getStatus());
			assertEquals(403, getSimpleUserView().getStatus());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void loginWithNonExistingAccount() {
		try {
			MockHttpServletResponse result = login("nonexist@nonexist.nan", "none");
			assertEquals(404, result.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void logoutWithoutLogin() {
		try {
			MockHttpServletResponse result = logout();
			assertEquals(200, result.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	private MockHttpServletResponse getSimpleUserView() throws Exception {
		MvcResult result;
		if (session == null)
			result = mockMvc.perform(get("/user/simple")).andDo(print()).andReturn();
		else
			result = mockMvc.perform(get("/user/simple").session(session)).andDo(print()).andReturn();
		return result.getResponse();
	}
	
}
