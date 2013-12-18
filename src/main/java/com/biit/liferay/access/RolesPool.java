package com.biit.liferay.access;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

public class RolesPool {

	private final static long EXPIRATION_TIME = 300000;// 5 minutes

	private Hashtable<Long, Long> userTime; // UserSoap id -> time.
	private Hashtable<Long, List<Role>> rolesByUser; // Roles by UserSoap.

	private Hashtable<String, Long> groupTime; // UserSoap id -> time.
	private Hashtable<String, List<Role>> rolesByGroup; // Roles by group.

	public RolesPool() {
		userTime = new Hashtable<Long, Long>();
		groupTime = new Hashtable<String, Long>();
		rolesByUser = new Hashtable<Long, List<Role>>();
		rolesByGroup = new Hashtable<String, List<Role>>();
	}

	public List<Role> getUserRoles(User UserSoap) {
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
					if (UserSoap.getUserId() == userId) {
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

	public void setUserRoles(User UserSoap, List<Role> roles) {
		if (UserSoap != null && roles != null) {
			userTime.put(UserSoap.getUserId(), System.currentTimeMillis());
			rolesByUser.put(UserSoap.getUserId(), roles);
		}
	}

	public void addUserRoles(User UserSoap, List<Role> roles) {
		if (UserSoap != null && roles != null && roles.size() > 0) {
			userTime.put(UserSoap.getUserId(), System.currentTimeMillis());
			List<Role> userRoles = rolesByUser.get(UserSoap.getUserId());
			if (userRoles == null) {
				userRoles = new ArrayList<Role>();
				rolesByUser.put(UserSoap.getUserId(), userRoles);
			}

			for (Role RoleSoap : roles) {
				if (!userRoles.contains(RoleSoap)) {
					userRoles.add(RoleSoap);
				}
			}
		}
	}

	public void addUserRole(User UserSoap, Role RoleSoap) {
		if (UserSoap != null && RoleSoap != null) {
			List<Role> roles = new ArrayList<Role>();
			roles.add(RoleSoap);
			addUserRoles(UserSoap, roles);
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

			for (Role RoleSoap : roles) {
				if (!groupRoles.contains(RoleSoap)) {
					groupRoles.add(RoleSoap);
				}
			}
		}
	}

	public void addUserGroupRole(UserGroup group, Role RoleSoap) {
		if (group != null && RoleSoap != null) {
			List<Role> roles = new ArrayList<Role>();
			roles.add(RoleSoap);
			addUserGroupRoles(group, roles);
		}
	}

	public void removeUserRole(User UserSoap, Role RoleSoap) {
		if (UserSoap != null && RoleSoap != null) {
			List<Role> userRoles = rolesByUser.get(UserSoap.getUserId());
			if (userRoles != null) {
				userRoles.remove(RoleSoap);
			}
		}
	}

	public void removeUserRoles(long userId) {
		userTime.remove(userId);
		rolesByUser.remove(userId);
	}

	public void removeUserRoles(User UserSoap) {
		if (UserSoap != null) {
			removeUserRoles(UserSoap.getUserId());
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
		for (Role RoleSoap : roles) {
			for (List<Role> userRoles : rolesByUser.values()) {
				userRoles.remove(RoleSoap);
			}
			for (List<Role> groupRoles : rolesByGroup.values()) {
				groupRoles.remove(RoleSoap);
			}
		}
	}

	public void removeRole(Role RoleSoap) {
		List<Role> roles = new ArrayList<Role>();
		roles.add(RoleSoap);
		removeRoles(roles);
	}
}
