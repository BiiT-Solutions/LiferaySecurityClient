package com.biit.liferay.access;

import java.util.Enumeration;
import java.util.Hashtable;

import com.liferay.portal.model.User;

public class UserPool {

	private final static long EXPIRATION_TIME = 30000;// 30 seconds

	private Hashtable<User, Long> users;

	public UserPool() {
		users = new Hashtable<User, Long>();
	}

	public User getUserByEmailAddress(String emailAddress) {
		long now = System.currentTimeMillis();
		User storedObject = null;
		if (users.size() > 0) {
			Enumeration<User> e = users.keys();
			while (e.hasMoreElements()) {
				storedObject = e.nextElement();
				if ((now - users.get(storedObject)) > EXPIRATION_TIME) {
					// object has expired
					users.remove(storedObject);
					storedObject = null;
				} else {
					if (storedObject.getEmailAddress().equals(emailAddress)) {
						return storedObject;
					}
				}
			}
		}
		return null;
	}

	public User getUserById(long userId) {
		long now = System.currentTimeMillis();
		User storedObject = null;
		if (users.size() > 0) {
			Enumeration<User> e = users.keys();
			while (e.hasMoreElements()) {
				storedObject = e.nextElement();
				if ((now - users.get(storedObject)) > EXPIRATION_TIME) {
					// object has expired
					users.remove(storedObject);
					storedObject = null;
				} else {
					if (storedObject.getUserId() == userId) {
						return storedObject;
					}
				}
			}
		}
		return null;
	}

	public User getUserByScreenName(String screenName) {
		long now = System.currentTimeMillis();
		User storedObject = null;
		if (users.size() > 0) {
			Enumeration<User> e = users.keys();
			while (e.hasMoreElements()) {
				storedObject = e.nextElement();
				if ((now - users.get(storedObject)) > EXPIRATION_TIME) {
					// object has expired
					users.remove(storedObject);
					storedObject = null;
				} else {
					if (storedObject.getScreenName().equals(screenName)) {
						return storedObject;
					}
				}
			}
		}
		return null;
	}

	public void addUser(User user) {
		users.put(user, System.currentTimeMillis());
	}

	public void deleteUser(User user) {
		// User object could be modified. Look up by id.
		users.remove(getUserById(user.getUserId()));
	}
}
