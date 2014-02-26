package com.biit.liferay.access;

import java.util.Enumeration;
import java.util.Hashtable;

import com.liferay.portal.model.UserGroup;

public class UserGroupPool {

	private final static long EXPIRATION_TIME = 300000;// 5 minutes

	private Hashtable<Long, Long> time; // group id -> time.
	private Hashtable<Long, UserGroup> groups; // groupid -> UserGroup.

	private static UserGroupPool instance = new UserGroupPool();

	public static UserGroupPool getInstance() {
		return instance;
	}

	private UserGroupPool() {
		time = new Hashtable<Long, Long>();
		groups = new Hashtable<Long, UserGroup>();
	}

	public void addGroup(UserGroup group) {
		if (group != null) {
			time.put(group.getUserGroupId(), System.currentTimeMillis());
			groups.put(group.getUserGroupId(), group);
		}
	}

	public UserGroup getGroup(long groupId) {
		long now = System.currentTimeMillis();
		Long storedObject = null;
		if (time.size() > 0) {
			Enumeration<Long> e = time.keys();
			while (e.hasMoreElements()) {
				storedObject = e.nextElement();
				if ((now - time.get(storedObject)) > EXPIRATION_TIME) {
					// object has expired
					removeGroup(storedObject);
					storedObject = null;
				} else {
					if (groups.get(storedObject) != null && groups.get(storedObject).getUserGroupId() == groupId) {
						return groups.get(groupId);
					}
				}
			}
		}
		return null;
	}

	public UserGroup getGroup(String name) {
		long now = System.currentTimeMillis();
		Long storedObject = null;
		if (time.size() > 0) {
			Enumeration<Long> e = time.keys();
			while (e.hasMoreElements()) {
				storedObject = e.nextElement();
				if ((now - time.get(storedObject)) > EXPIRATION_TIME) {
					// object has expired
					removeGroup(storedObject);
					storedObject = null;
				} else {
					if (groups.get(storedObject) != null && groups.get(storedObject).getName().equals(name)) {
						return groups.get(storedObject);
					}
				}
			}
		}
		return null;
	}

	public void removeGroup(long groupId) {
		time.remove(groupId);
		groups.remove(groupId);
	}

	public void removeGroup(UserGroup group) {
		if (group != null) {
			removeGroup(group.getUserGroupId());
		}
	}
}
