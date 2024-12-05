package com.mayab.quality.logginunittest.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.model.User;
import com.mayab.quality.loginunittest.service.LoginService;

class LoginServiceTest {
	
	private IDAOUser dao;
	private User user;
	private LoginService login;
	
	@BeforeEach
	void setUp() throws Exception {
		
		dao = mock(IDAOUser.class);
		user = mock(User.class);
		login = new LoginService(dao);
	}

	@Test
	void testLoginSucces() {
		
		String email = "hola";
		String password = "123";
		
		when(dao.findByUserName(anyString())).thenReturn(user);
		when(user.getPassword()).thenReturn("123");
		
		boolean result = login.login(email, password);
		boolean expected = true;
		
		assertThat(expected, is(result));
	}
	
	@Test
	void testLoginFail() {
		
		String email = "hola";
		String password = "12345 xd";
		
		when(dao.findByUserName(anyString())).thenReturn(user);
		when(user.getPassword()).thenReturn("123");
		
		boolean result = login.login(email, password);
		boolean expected = true;
		
		assertThat(result, is(expected));
	}

}