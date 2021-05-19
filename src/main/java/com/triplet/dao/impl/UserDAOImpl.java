package com.triplet.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.triplet.dao.GenericDAO;
import com.triplet.dao.UserDAO;
import com.triplet.model.Role;
import com.triplet.model.User;

public class UserDAOImpl extends GenericDAO<Integer, User> implements UserDAO {
	Logger logger = Logger.getLogger(UserDAOImpl.class);

	public UserDAOImpl() {
		super(User.class);
	}

	public UserDAOImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> loadUsers() {
		return getSession().createQuery("from User").getResultList();
	}

	@Override
	public User findByUsername(String username) {
		return (User) getSession().createQuery("from User where username=: username").setParameter("username", username)
				.uniqueResult();
	}

	@Override
	public boolean checkEmailExist(String email) {
		User user = (User) getSession().createQuery("from User where email=: email").setParameter("email", email)
				.uniqueResult();
		if (user != null)
			return true;
		return false;
	}

	@Override
	public boolean checkUsernameExist(String username) {
		User user = (User) getSession().createQuery("from User where username=: username")
				.setParameter("username", username).uniqueResult();
		if (user != null)
			return true;
		return false;
	}

	@Override
	public List<Integer> saveBatch(List<User> users) {
		int batchSize = 10;
		int listSize = users.size();
		Session session = getSession();
		List<Integer> linesError = new ArrayList<Integer>();
		for (int i = 0; i < listSize; i++) {
			if (i > 0 && i % batchSize == 0) {
				session.flush();
				session.clear();
			}
			if (checkEmailExist(users.get(i).getEmail()) || checkUsernameExist(users.get(i).getUsername())) {
				linesError.add(i);
			} else {
				session.persist(users.get(i));
			}
		}
		return linesError;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> loadUsers(Role role) {
		return getSession().createQuery("from User user join user.roles role where role.code =: code")
				.setParameter("code", role.getCode())
				.getResultList();
	}
}
