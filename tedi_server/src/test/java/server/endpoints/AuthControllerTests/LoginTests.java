package server.endpoints.AuthControllerTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.web.FilterChainProxy;
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
	
	@Resource
	private FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private SecurityService secService;
	
	private MockMvc mockMvc;
	
	private MockHttpSession session;
	
	@Before
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(springSecurityFilterChain).build();
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
			assertEquals(null, secService.currentUser());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	 protected MockHttpServletResponse login(String email, String password) throws Exception {
		MvcResult result = mockMvc
							.perform(post("/login")
							.contentType(MediaType.APPLICATION_FORM_URLENCODED)
							.param("email", email)
							.param("password", password))
							.andReturn();
		session = (MockHttpSession)result.getRequest().getSession();
		return result.getResponse();
	}
	
	private MockHttpServletResponse logout() throws Exception {
		MvcResult result = mockMvc.perform(get("/logout")).andReturn();
		session = null;
		return result.getResponse();
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
