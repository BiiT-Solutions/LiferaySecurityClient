package com.biit.liferay.security;

import java.util.Enumeration;
import java.util.Hashtable;

import com.liferay.portal.model.User;

/**
 * Defines if an activity is authorized by an user or not.
 */
public class AuthorizationPool {

	private final static long EXPIRATION_TIME = 300000;// 300 seconds

	// user id -> time.
	private Hashtable<User, Long> time;
	// Form, user id -> activity -> allowed.
	private Hashtable<User, Hashtable<IActivity, Boolean>> users;

	public AuthorizationPool() {
		time = new Hashtable<User, Long>();
		users = new Hashtable<User, Hashtable<IActivity, Boolean>>();
	}

	/**
	 * Returns true or false if the activity is authorized and null if is not catched.
	 * 
	 * @param form
	 * @param user
	 * @param activity
	 * @return
	 */
	public Boolean isAuthorizedActivity(User user, IActivity activity) {
		long now = System.currentTimeMillis();
		User userForm = null;

		if (time.size() > 0) {
			Enumeration<User> userEnum = time.keys();
			while (userEnum.hasMoreElements()) {
				userForm = userEnum.nextElement();
				try {
					if (time.get(userForm) != null && (now - time.get(userForm)) > EXPIRATION_TIME) {
						// object has expired
						removeUser(userForm);
						userForm = null;
					} else if (user != null && user.equals(userForm)) {
						if (users.get(user) != null && activity != null) {
							return users.get(user).get(activity);
						}
					}
				} catch (Exception except) {
					// Something is wrong. Considered as not cached.
				}
			}
		}
		return null;
	}

	public void addUser(User user, IActivity activity, boolean allowed) {
		if (user != null && activity != null) {
			if (users.get(user) == null) {
				users.put(user, new Hashtable<IActivity, Boolean>());
			}

			time.put(user, System.currentTimeMillis());
			users.get(user).put(activity, allowed);
		}
	}

	public void removeUser(User user) {
		if (user != null) {
			time.remove(user);
			users.remove(user);
		}
	}
}
