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
	private Hashtable<Long, List<UserGroup>> groupsByUser; // user id -> user.

	private static UserGroupsPool instance = new UserGroupsPool();

	public static UserGroupsPool getInstance() {
		return instance;
	}

	private UserGroupsPool() {
		userTime = new Hashtable<Long, Long>();
		groupsByUser = new Hashtable<Long, List<UserGroup>>();
	}

	public void addUserGroup(User user, UserGroup group) {
		if (user != null && group != null) {
			List<UserGroup> groups = new ArrayList<UserGroup>();
			groups.add(group);
			addUserGroups(user, groups);
		}
	}

	public void addUserGroups(User user, List<UserGroup> groups) {
		if (user != null && groups != null) {
			userTime.put(user.getUserId(), System.currentTimeMillis());
			List<UserGroup> userGroups = groupsByUser.get(user.getUserId());
			if (userGroups == null) {
				userGroups = new ArrayList<UserGroup>();
				groupsByUser.put(user.getUserId(), userGroups);
			}

			for (UserGroup group : groups) {
				if (!userGroups.contains(group)) {
					userGroups.add(group);
				}
			}
		}
	}

	public List<UserGroup> getGroupsByUser(User user) {
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
					if (user.getUserId() == userId) {
						return groupsByUser.get(userId);
					}
				}
			}
		}
		return null;
	}

	public void removeUserGroup(Long groupId) {
		List<Long> groups = new ArrayList<Long>();
		groups.add(groupId);
		removeUserGroupsById(groups);
	}

	public void removeUserGroup(UserGroup group) {
		List<UserGroup> groups = new ArrayList<UserGroup>();
		groups.add(group);
		removeUserGroups(groups);
	}

	public void removeUserGroups(List<UserGroup> groups) {
		for (UserGroup group : groups) {
			for (List<UserGroup> userGroups : groupsByUser.values()) {
				userGroups.remove(group);
			}
		}
	}

	public void removeUserGroups(long userId) {
		userTime.remove(userId);
		groupsByUser.remove(userId);
	}

	public void removeUserGroups(User user) {
		if (user != null) {
			removeUserGroups(user.getUserId());
		}
	}

	public void removeUserGroupsById(List<Long> groupsIds) {
		for (long groupId : groupsIds) {
			for (List<UserGroup> userGroups : groupsByUser.values()) {
				List<UserGroup> userGroupsCopy = new ArrayList<UserGroup>(userGroups);
				for (UserGroup userGroup : userGroupsCopy) {
					if (userGroup.getUserGroupId() == groupId) {
						userGroups.remove(userGroup);
					}
				}
			}
		}
	}
}
