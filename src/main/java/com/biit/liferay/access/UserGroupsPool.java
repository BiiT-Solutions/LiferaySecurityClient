package com.biit.liferay.access;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

public class UserGroupsPool {

	private final static long EXPIRATION_TIME = 300000;// 5 minutes

	private Hashtable<Long, Long> userTime; // group name -> time.
	private Hashtable<Long, List<UserGroup>> groupsByUser; // UserSoap id -> UserSoap.

	public UserGroupsPool() {
		userTime = new Hashtable<Long, Long>();
		groupsByUser = new Hashtable<Long, List<UserGroup>>();
	}

	public List<UserGroup> getGroupByUser(User UserSoap) {
		long now = System.currentTimeMillis();
		Long userId = null;
		if (userTime.size() > 0) {
			Enumeration<Long> e = userTime.keys();
			while (e.hasMoreElements()) {
				userId = e.nextElement();
				if ((now - userTime.get(userId)) > EXPIRATION_TIME) {
					// object has expired
					removeUserGroups(userId);
					userId = null;
				} else {
					if (UserSoap.getUserId() == userId) {
						return groupsByUser.get(userId);
					}
				}
			}
		}
		return null;
	}

	public void removeUserGroups(long userId) {
		userTime.remove(userId);
		groupsByUser.remove(userId);
	}

	public void removeUserGroups(User UserSoap) {
		if (UserSoap != null) {
			removeUserGroups(UserSoap.getUserId());
		}
	}

	public void addUserGroups(User UserSoap, List<UserGroup> groups) {
		if (UserSoap != null && groups != null && groups.size() > 0) {
			userTime.put(UserSoap.getUserId(), System.currentTimeMillis());
			List<UserGroup> userGroups = groupsByUser.get(UserSoap.getUserId());
			if (userGroups == null) {
				userGroups = new ArrayList<UserGroup>();
				groupsByUser.put(UserSoap.getUserId(), userGroups);
			}

			for (UserGroup group : groups) {
				if (!userGroups.contains(group)) {
					userGroups.add(group);
				}
			}
		}
	}

	public void removeUserGroups(List<UserGroup> groups) {
		for (UserGroup group : groups) {
			for (List<UserGroup> userGroups : groupsByUser.values()) {
				userGroups.remove(group);
			}
		}
	}

	public void removeUserGroup(UserGroup group) {
		List<UserGroup> groups = new ArrayList<UserGroup>();
		groups.add(group);
		removeUserGroups(groups);
	}
}
