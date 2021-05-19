package com.triplet.service;

import java.util.List;

import com.triplet.bean.UserInfo;
import com.triplet.model.Role;
import com.triplet.model.User;

public interface UserService extends BaseService<Integer, User> {

	List<User> loadUsers();

	User findByUsername(String username);

	public boolean checkEmailExist(String email);

	boolean createUser(User user);

	public boolean checkUsernameExist(String username);
	
	public List<Integer> saveBatch(List<User> users);

	public List<User> convertToUsers(List<UserInfo> listUserInfo);
	
	List<User> loadUsers(Role role);
}
