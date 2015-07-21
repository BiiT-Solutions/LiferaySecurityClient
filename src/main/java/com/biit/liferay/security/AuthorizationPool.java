package com.biit.liferay.security;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.IActivity;

/**
 * Defines if an activity is authorized by an user or not.
 */
public class AuthorizationPool {

	private final static long EXPIRATION_TIME = 300000;// 300 seconds

	// user id -> time.
	private Map<IUser<Long>, Long> time;
	// Form, user id -> activity -> allowed.
	private Map<IUser<Long>, Map<IActivity, Boolean>> users;
	private Map<IUser<Long>, Map<IGroup<Long>, Map<IActivity, Boolean>>> organizations;

	public AuthorizationPool() {
		reset();
	}

	public void reset() {
		time = new HashMap<IUser<Long>, Long>();
		users = new HashMap<IUser<Long>, Map<IActivity, Boolean>>();
		organizations = new HashMap<IUser<Long>, Map<IGroup<Long>, Map<IActivity, Boolean>>>();
	}

	/**
	 * Returns true or false if the activity is authorized and null if is not catched.
	 * 
	 * @param form
	 * @param user
	 * @param activity
	 * @return
	 */
	public Boolean isAuthorizedActivity(IUser<Long> user, IActivity activity) {
		long now = System.currentTimeMillis();
		IUser<Long> userForm = null;

		if (time.size() > 0) {
			Iterator<IUser<Long>> userEnum = new HashMap<IUser<Long>, Long>(time).keySet().iterator();
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

	public Boolean isAuthorizedActivity(IUser<Long> user, IGroup<Long> organization, IActivity activity) {
		long now = System.currentTimeMillis();
		IUser<Long> authorizedUser = null;

		if (time.size() > 0) {
			Iterator<IUser<Long>> userEnum = new HashMap<IUser<Long>, Long>(time).keySet().iterator();
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

	public void addUser(IUser<Long> user, IActivity activity, Boolean authorized) {
		if (user != null && activity != null && authorized != null) {
			if (users.get(user) == null) {
				users.put(user, new Hashtable<IActivity, Boolean>());
			}

			time.put(user, System.currentTimeMillis());
			users.get(user).put(activity, authorized);
		}
	}

	public void addUser(IUser<Long> user, IGroup<Long> organization, IActivity activity, Boolean authorized) {
		if (user != null && organization != null && activity != null && authorized != null) {
			if (organizations.get(user) == null) {
				organizations.put(user, new HashMap<IGroup<Long>, Map<IActivity, Boolean>>());
			}

			if (organizations.get(user).get(organization) == null) {
				organizations.get(user).put(organization, new HashMap<IActivity, Boolean>());
			}

			organizations.get(user).get(organization).put(activity, authorized);
			time.put(user, System.currentTimeMillis());
		}
	}

	public void removeUser(IUser<Long> user) {
		if (user != null) {
			time.remove(user);
			users.remove(user);
			organizations.remove(user);
		}
	}
}
