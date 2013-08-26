package com.biit.liferay.access;

import java.util.Enumeration;
import java.util.Hashtable;

import com.liferay.portal.model.UserGroup;

public class GroupPool {

	private final static long EXPIRATION_TIME = 30000;// 30 seconds

	private Hashtable<Long, Long> time; // user id -> time.
	private Hashtable<Long, UserGroup> groups; // user id -> user.

	public GroupPool() {
		time = new Hashtable<Long, Long>();
		groups = new Hashtable<Long, UserGroup>();
	}

	public UserGroup getGroupById(long groupId) {
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
					if (groups.get(groupId) != null) {
						return groups.get(groupId);
					}
				}
			}
		}
		return null;
	}

	public void addGroup(UserGroup group) {
		if (group != null) {
			time.put(group.getUserGroupId(), System.currentTimeMillis());
			groups.put(group.getUserGroupId(), group);
		}
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
