package com.biit.liferay.access;

import java.util.Enumeration;
import java.util.Hashtable;

import com.liferay.portal.model.User;

public class UserPool {

	private final static long EXPIRATION_TIME = 30000;// 30 seconds

	private Hashtable<Long, Long> time; // user id -> time.
	private Hashtable<Long, User> users; // user id -> user.

	public UserPool() {
		time = new Hashtable<Long, Long>();
		users = new Hashtable<Long, User>();
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
					deleteUser(userId);
					userId = null;
				} else {
					if (users.get(userId).getEmailAddress().equals(emailAddress)) {
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
					deleteUser(storedObject);
					storedObject = null;
				} else {
					if (users.get(userId) != null) {
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
					deleteUser(userId);
					userId = null;
				} else {
					if (users.get(userId).getScreenName().equals(screenName)) {
						return users.get(userId);
					}
				}
			}
		}
		return null;
	}

	public void addUser(User user) {
		if (user != null) {
			time.put(user.getUserId(), System.currentTimeMillis());
			users.put(user.getUserId(), user);
		}
	}

	public void deleteUser(long userId) {
		time.remove(userId);
		users.remove(userId);
	}

	public void deleteUser(User user) {
		if (user != null) {
			// User object could be modified. Look up by id.
			time.remove(user.getUserId());
			users.remove(user.getUserId());
		}
	}
}
