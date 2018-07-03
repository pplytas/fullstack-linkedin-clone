package server.endpoints;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import server.endpoints.inputmodels.RegisterInputModel;

public abstract class AuthorizedTests {
	
	@Resource
	protected FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	protected WebApplicationContext wac;
	
	@Autowired
	protected ObjectMapper objectMapper;
	
	protected MockMvc mockMvc;
	
	protected MockHttpSession session;
	
	protected void setupMockMvc() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(springSecurityFilterChain).build();
	}
	
	protected MockHttpServletResponse register(RegisterInputModel input) throws Exception {
		MvcResult result = this.mockMvc.perform(post("/register")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(input)))
							.andReturn();
		return result.getResponse();
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

	protected MockHttpServletResponse logout() throws Exception {
		MvcResult result = mockMvc.perform(get("/logout")).andReturn();
		session = null;
		return result.getResponse();
	}
	
}
