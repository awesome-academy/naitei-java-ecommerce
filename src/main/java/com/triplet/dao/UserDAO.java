package com.triplet.dao;

import java.util.List;

import com.triplet.model.Role;
import com.triplet.model.User;

public interface UserDAO extends BaseDAO<Integer, User> {

	List<User> loadUsers();

	User findByUsername(String username);

	boolean checkEmailExist(String email);

	boolean checkUsernameExist(String username);

	List<Integer> saveBatch(List<User> users);

	List<User> loadUsers(Role role);
}
