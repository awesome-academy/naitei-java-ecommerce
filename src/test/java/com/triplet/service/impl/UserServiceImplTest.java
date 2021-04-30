package com.triplet.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.triplet.bean.UserInfo;
import com.triplet.dao.UserDAO;
import com.triplet.model.User;

class UserServiceImplTest {

	@Mock
	private UserDAO userDAO;

	@InjectMocks
	private UserServiceImpl userService;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testLoadUsers() {
		List<User> users = new ArrayList<>();
		when(userDAO.loadUsers()).thenReturn(users);
		assertEquals(users, userService.loadUsers());
	}

	@Test
	public void testFindById() {
		User user = new User();
		when(userDAO.findById(anyInt())).thenReturn(user);
		assertEquals(user, userService.findById(anyInt()));
	}

	@Test
	void testFindByUsername() {
		User user = new User();
		String name = "ngocanh";
		when(userDAO.findByUsername(name)).thenReturn(user);
		assertEquals(user, userService.findByUsername(name));
	}

	@Test
	public void testSaveOrUpdate() {
		User user = new User();
		when(userDAO.saveOrUpdate(any(User.class))).thenReturn(new User());
		assertNotNull(userService.saveOrUpdate(user));
	}

	@Test
	public void testDelete() {
		doNothing().when(userDAO).delete(any(User.class));
		assertTrue(userService.delete(any(User.class)));
	}

	@Test
	void testCheckEmailExist() {
		when(userDAO.checkEmailExist(anyString())).thenReturn(true);
		assertTrue(userService.checkEmailExist(anyString()));
	}

	@Test
	void testCheckUsernameExist() {
		when(userDAO.checkUsernameExist(anyString())).thenReturn(true);
		assertTrue(userService.checkUsernameExist(anyString()));
	}

	@Test
	void testCreateUser() {
		User user = new User();
		when(userDAO.saveOrUpdate(any(User.class))).thenReturn(new User());
		assertTrue(userService.createUser(user));
	}

	@Test
	void testSaveBatch() {
		List<User> users = new ArrayList<>();
		when(userDAO.saveBatch(users)).thenReturn(true);
		assertTrue(userService.saveBatch(users));
	}

	@Test
	void testConvertToUsers() {
		List<User> users = new ArrayList<>();
		List<UserInfo> listUserInfo = new ArrayList<>();
		assertEquals(users, userService.convertToUsers(listUserInfo));
	}

}
