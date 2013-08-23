package com.biit.liferay.access;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

public class UserRolesPool {

	private final static long EXPIRATION_TIME = 30000;// 30 seconds

	private Hashtable<Long, Long> userTime; // user id -> time.
	private Hashtable<Long, List<Role>> rolesByUser; // Roles by user.

	private Hashtable<String, Long> groupTime; // user id -> time.
	private Hashtable<String, List<Role>> rolesByGroup; // Roles by group.

	public UserRolesPool() {
		userTime = new Hashtable<Long, Long>();
		rolesByUser = new Hashtable<Long, List<Role>>();
		rolesByGroup = new Hashtable<String, List<Role>>();
	}

	public List<Role> getUserRoles(User user) {
		long now = System.currentTimeMillis();
		Long userId = null;
		if (userTime.size() > 0) {
			Enumeration<Long> e = userTime.keys();
			while (e.hasMoreElements()) {
				userId = e.nextElement();
				if ((now - userTime.get(userId)) > EXPIRATION_TIME) {
					// object has expired
					removeUserRoles(userId);
					userId = null;
				} else {
					if (user.getUserId() == userId) {
						return rolesByUser.get(userId);
					}
				}
			}
		}
		return null;
	}

	public List<Role> getGroupRoles(UserGroup group) {
		long now = System.currentTimeMillis();
		String groupId = null;
		if (groupTime.size() > 0) {
			Enumeration<String> e = groupTime.keys();
			while (e.hasMoreElements()) {
				groupId = e.nextElement();
				if ((now - groupTime.get(groupId)) > EXPIRATION_TIME) {
					// object has expired
					removeGroupRoles(groupId);
					groupId = null;
				} else {
					if (group.getName().equals(groupId)) {
						return rolesByGroup.get(groupId);
					}
				}
			}
		}
		return null;
	}

	public void setUserRoles(User user, List<Role> roles) {
		if (user != null && roles != null) {
			userTime.put(user.getUserId(), System.currentTimeMillis());
			rolesByUser.put(user.getUserId(), roles);
		}
	}

	public void addUserRoles(User user, List<Role> roles) {
		if (user != null && roles != null && roles.size() > 0) {
			userTime.put(user.getUserId(), System.currentTimeMillis());
			List<Role> userRoles = rolesByUser.get(user.getUserId());
			if (userRoles == null) {
				userRoles = new ArrayList<Role>();
				rolesByUser.put(user.getUserId(), userRoles);
			}

			for (Role role : roles) {
				if (!userRoles.contains(role)) {
					userRoles.add(role);
				}
			}
		}
	}

	public void addUserRole(User user, Role role) {
		if (user != null && role != null) {
			List<Role> roles = new ArrayList<Role>();
			roles.add(role);
			addUserRoles(user, roles);
		}
	}

	public void addUserGroupRoles(UserGroup group, List<Role> roles) {
		if (group != null && roles != null && roles.size() > 0) {
			groupTime.put(group.getName(), System.currentTimeMillis());
			List<Role> groupRoles = rolesByGroup.get(group.getName());
			if (groupRoles == null) {
				groupRoles = new ArrayList<Role>();
				rolesByGroup.put(group.getName(), groupRoles);
			}

			for (Role role : roles) {
				if (!groupRoles.contains(role)) {
					groupRoles.add(role);
				}
			}
		}
	}

	public void addUserGroupRole(UserGroup group, Role role) {
		if (group != null && role != null) {
			List<Role> roles = new ArrayList<Role>();
			roles.add(role);
			addUserGroupRoles(group, roles);
		}
	}

	public void removeUserRole(User user, Role role) {
		if (user != null && role != null) {
			List<Role> userRoles = rolesByUser.get(user.getUserId());
			if (userRoles != null) {
				userRoles.remove(role);
			}
		}
	}

	public void removeUserRoles(long userId) {
		userTime.remove(userId);
		rolesByUser.remove(userId);
	}

	public void removeUserRoles(User user) {
		if (user != null) {
			removeUserRoles(user.getUserId());
		}
	}

	public void removeGroupRoles(String groupName) {
		groupTime.remove(groupName);
		rolesByGroup.remove(groupName);
	}

	public void removeGroupRoles(UserGroup group) {
		if (group != null) {
			removeGroupRoles(group.getName());
		}
	}

	public void removeRoles(List<Role> roles) {
		for (Role role : roles) {
			for (List<Role> userRoles : rolesByUser.values()) {
				userRoles.remove(role);
			}
			for (List<Role> groupRoles : rolesByGroup.values()) {
				groupRoles.remove(role);
			}
		}
	}

	public void removeRole(Role role) {
		List<Role> roles = new ArrayList<Role>();
		roles.add(role);
		removeRoles(roles);
	}
}
