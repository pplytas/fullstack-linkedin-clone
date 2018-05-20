package server.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

import server.auth.handlers.CustomLogoutHandler;
import server.auth.handlers.LoginFailureHandler;
import server.auth.handlers.LoginSuccessHandler;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private LoginSuccessHandler successHandler;
	
	@Autowired
	private LoginFailureHandler failureHandler;
	
	@Autowired
	private CustomLogoutHandler customLogout;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint());
		
		http.cors().and().csrf().disable()
			.authorizeRequests()
			.antMatchers("/login",
						 "/register",
						 "/hello").permitAll()
			.antMatchers("/user/article",
						 "/user/comment",
						 "/user/upvote",
						 "/user/update").hasAuthority("USER")
			.antMatchers("/secret",
						 "/user/articles").hasAnyAuthority("USER", "ADMIN")
			.antMatchers("/admin",
						 "/admin/*").hasAuthority("ADMIN")
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.loginPage("/login").loginProcessingUrl("/login")
			.usernameParameter("email").passwordParameter("password")
			.permitAll()
			.successHandler(successHandler)
			.failureHandler(failureHandler).permitAll()
			.and()
			.logout()
			.logoutSuccessHandler(customLogout).permitAll();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
	    return super.authenticationManagerBean();
	}

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
	
}
