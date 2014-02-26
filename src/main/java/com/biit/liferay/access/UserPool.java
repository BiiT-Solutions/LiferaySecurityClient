package com.biit.liferay.access;

import java.util.Enumeration;
import java.util.Hashtable;

import com.liferay.portal.model.User;

public class UserPool {

	private final static long EXPIRATION_TIME = 300000;// 5 minutes

	private Hashtable<Long, Long> time; // User id -> time.
	private Hashtable<Long, User> users; // User id -> User.

	private static UserPool instance = new UserPool();

	public static UserPool getInstance() {
		return instance;
	}

	private UserPool() {
		time = new Hashtable<Long, Long>();
		users = new Hashtable<Long, User>();
	}

	public void addUser(User user) {
		if (user != null) {
			time.put(user.getUserId(), System.currentTimeMillis());
			users.put(user.getUserId(), user);
		}
	}

	public User getUserByEmailAddress(String emailAddress) {
		long now = System.currentTimeMillis();
		Long userId = null;
		if (time.size() > 0) {
			Enumeration<Long> e = time.keys();
			while (e.hasMoreElements()) {
				userId = e.nextElement();
				if ((now - time.get(userId)) > EXPIRATION_TIME) {
					// object has expired
					removeUser(userId);
					userId = null;
				} else {
					if (users.get(userId) != null && users.get(userId).getEmailAddress().equals(emailAddress)) {
						return users.get(userId);
					}
				}
			}
		}
		return null;
	}

	public User getUserById(long userId) {
		long now = System.currentTimeMillis();
		Long storedObject = null;
		if (time.size() > 0) {
			Enumeration<Long> e = time.keys();
			while (e.hasMoreElements()) {
				storedObject = e.nextElement();
				if ((now - time.get(storedObject)) > EXPIRATION_TIME) {
					// object has expired
					removeUser(storedObject);
					storedObject = null;
				} else {
					if (users.get(storedObject) != null && users.get(storedObject).getUserId() == userId) {
						return users.get(userId);
					}
				}
			}
		}
		return null;
	}

	public User getUserByScreenName(String screenName) {
		long now = System.currentTimeMillis();
		Long userId = null;
		if (time.size() > 0) {
			Enumeration<Long> e = time.keys();
			while (e.hasMoreElements()) {
				userId = e.nextElement();
				if ((now - time.get(userId)) > EXPIRATION_TIME) {
					// object has expired
					removeUser(userId);
					userId = null;
				} else {
					if (users.get(userId) != null && users.get(userId).getScreenName().equals(screenName)) {
						return users.get(userId);
					}
				}
			}
		}
		return null;
	}

	public void removeUser(long userId) {
		time.remove(userId);
		users.remove(userId);
	}

	public void removeUser(User user) {
		if (user != null) {
			removeUser(user.getUserId());
		}
	}
}
