package com.triplet.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.triplet.bean.UserInfo;
import com.triplet.model.Role;
import com.triplet.model.User;
import com.triplet.service.UserService;

public class UserServiceImpl extends BaseServiceImpl implements UserService {

	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<User> loadUsers() {
		try {
			return getUserDAO().loadUsers();
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	@Override
	public User findById(Serializable key) {
		try {
			return getUserDAO().findById(key);
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	@Override
	public User findByUsername(String username) {
		try {
			return getUserDAO().findByUsername(username);
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	@Override
	public User saveOrUpdate(User entity) {
		try {
			return getUserDAO().saveOrUpdate(entity);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	@Override
	public boolean delete(User entity) {
		try {
			getUserDAO().delete(entity);
			return true;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	@Override
	public boolean checkEmailExist(String email) {
		try {
			// true: exist
			return getUserDAO().checkEmailExist(email);
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	@Override
	public boolean checkUsernameExist(String username) {
		try {
			// true: exist
			return getUserDAO().checkUsernameExist(username);
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}

	@Override
	public boolean createUser(User user) {

		try {
			if (checkEmailExist(user.getEmail()) || checkUsernameExist(user.getUsername()))
				return false;
			Role role = new Role();
			role.setId(2);
			role.setCode("USER");
			List<Role> roles = new ArrayList<>();
			roles.add(role);
			user.setRoles(roles);
			if (user.getPassword() != null) {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
			}
			getUserDAO().saveOrUpdate(user);
			return true;
		} catch (Exception ex) {
			logger.error("Error in saveUserAfterRegister: " + ex.getMessage());
			throw ex;
		}
	}

	@Override
	public List<Integer> saveBatch(List<User> users) {
		List<Integer> linesError = new ArrayList<Integer>();
		try {
			linesError = getUserDAO().saveBatch(users);
		} catch (Exception e) {
			logger.error("Error in save batch of user: " + e.getMessage());
			throw e;
		}
		return linesError;
	}

	public List<User> convertToUsers(List<UserInfo> listUserInfo) {
		try {
			List<User> users = new ArrayList<User>();
			for (UserInfo userInfo : listUserInfo) {
				User user = userInfo.convertToUser();

				Role role = new Role();
				role.setId(2);
				role.setCode("USER");
				List<Role> roles = new ArrayList<>();
				roles.add(role);
				user.setRoles(roles);
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				users.add(user);
			}
			return users;
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	@Override
	public List<User> loadUsers(Role role) {
		try {
			return getUserDAO().loadUsers(role);
		} catch (Exception e) {
			logger.error(e);
			return Collections.emptyList();
		}
	}
}
