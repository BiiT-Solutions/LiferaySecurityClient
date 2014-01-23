package com.biit.liferay.access;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.liferay.portal.model.Organization;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

public class RolesPool {

	private final static long EXPIRATION_TIME = 300000;// 5 minutes

	private Hashtable<Long, Long> userTime; // user id -> time.
	private Hashtable<Long, List<Role>> rolesByUser; // Roles by user.

	private Hashtable<Long, Long> groupTime; // Group Id -> time.
	private Hashtable<Long, List<Role>> rolesByGroup; // Roles by group.

	public RolesPool() {
		userTime = new Hashtable<Long, Long>();
		groupTime = new Hashtable<Long, Long>();
		rolesByUser = new Hashtable<Long, List<Role>>();
		rolesByGroup = new Hashtable<Long, List<Role>>();
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

	public List<Role> getOrganizationRoles(Organization organization) {
		return getGroupRoles(organization.getOrganizationId());
	}

	public List<Role> getGroupRoles(UserGroup group) {
		return getGroupRoles(group.getUserGroupId());
	}

	public List<Role> getGroupRoles(long groupId) {
		long now = System.currentTimeMillis();
		Long nextGroupId = null;
		if (groupTime.size() > 0) {
			Enumeration<Long> e = groupTime.keys();
			while (e.hasMoreElements()) {
				nextGroupId = e.nextElement();
				if ((now - groupTime.get(nextGroupId)) > EXPIRATION_TIME) {
					// object has expired
					removeGroupRoles(nextGroupId);
					nextGroupId = null;
				} else {
					if (groupId == nextGroupId) {
						return rolesByGroup.get(nextGroupId);
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

	public void addOrganizationRoles(Organization organization, List<Role> roles) {
		addUserGroupRoles(organization.getOrganizationId(), roles);
	}

	public void addUserGroupRoles(UserGroup group, List<Role> roles) {
		addUserGroupRoles(group.getUserGroupId(), roles);
	}

	private void addUserGroupRoles(Long groupId, List<Role> roles) {
		if (groupId != null && roles != null) {
			groupTime.put(groupId, System.currentTimeMillis());
			List<Role> groupRoles = rolesByGroup.get(groupId);
			if (groupRoles == null) {
				groupRoles = new ArrayList<Role>();
				rolesByGroup.put(groupId, groupRoles);
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

	public void removeGroupRoles(Long groupId) {
		groupTime.remove(groupId);
		rolesByGroup.remove(groupId);
	}

	public void removeGroupRoles(UserGroup group) {
		if (group != null) {
			removeGroupRoles(group.getUserGroupId());
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
