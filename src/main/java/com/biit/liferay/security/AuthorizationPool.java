package com.biit.liferay.security;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;

/**
 * Defines if an activity is authorized by an user or not.
 */
public class AuthorizationPool {

	private final static long EXPIRATION_TIME = 300000;// 300 seconds

	// user id -> time.
	private Map<User, Long> time;
	// Form, user id -> activity -> allowed.
	private Map<User, Map<IActivity, Boolean>> users;
	private Map<User, Map<Organization, Map<IActivity, Boolean>>> organizations;

	public AuthorizationPool() {
		reset();
	}

	public void reset() {
		time = new HashMap<User, Long>();
		users = new HashMap<User, Map<IActivity, Boolean>>();
		organizations = new HashMap<User, Map<Organization, Map<IActivity, Boolean>>>();
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
			Iterator<User> userEnum = new HashMap<>(time).keySet().iterator();
			while (userEnum.hasNext()) {
				userForm = userEnum.next();
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

	public Boolean isAuthorizedActivity(User user, Organization organization, IActivity activity) {
		long now = System.currentTimeMillis();
		User authorizedUser = null;

		if (time.size() > 0) {
			Iterator<User> userEnum = new HashMap<>(time).keySet().iterator();
			while (userEnum.hasNext()) {
				authorizedUser = userEnum.next();
				try {
					if (time.get(authorizedUser) != null && (now - time.get(authorizedUser)) > EXPIRATION_TIME) {
						// object has expired
						removeUser(authorizedUser);
						authorizedUser = null;
					} else if (user != null && user.equals(authorizedUser)) {
						if (organizations.get(user) != null && organizations.get(user).get(organization) != null
								&& activity != null) {
							return organizations.get(user).get(organization).get(activity);
						}
					}
				} catch (Exception except) {
					// Something is wrong. Considered as not cached.
				}
			}
		}
		return null;
	}

	public void addUser(User user, IActivity activity, Boolean authorized) {
		if (user != null && activity != null && authorized != null) {
			if (users.get(user) == null) {
				users.put(user, new Hashtable<IActivity, Boolean>());
			}

			time.put(user, System.currentTimeMillis());
			users.get(user).put(activity, authorized);
		}
	}

	public void addUser(User user, Organization organization, IActivity activity, Boolean authorized) {
		if (user != null && organization != null && activity != null && authorized != null) {
			if (organizations.get(user) == null) {
				organizations.put(user, new HashMap<Organization, Map<IActivity, Boolean>>());
			}

			if (organizations.get(user).get(organization) == null) {
				organizations.get(user).put(organization, new HashMap<IActivity, Boolean>());
			}

			organizations.get(user).get(organization).put(activity, authorized);
			time.put(user, System.currentTimeMillis());
		}
	}

	public void removeUser(User user) {
		if (user != null) {
			time.remove(user);
			users.remove(user);
			organizations.remove(user);
		}
	}
}
