package com.biit.liferay.access;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.liferay.portal.model.Group;
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

	private Hashtable<Long, Long> userRoleOfGroupTime; // user id -> time.
	private Hashtable<Long, Hashtable<Long, List<Role>>> userRolesOfGroup; // User->Group->Roles.

	public RolesPool() {
		userTime = new Hashtable<Long, Long>();
		groupTime = new Hashtable<Long, Long>();
		rolesByUser = new Hashtable<Long, List<Role>>();
		rolesByGroup = new Hashtable<Long, List<Role>>();
		userRoleOfGroupTime = new Hashtable<Long, Long>();
		userRolesOfGroup = new Hashtable<Long, Hashtable<Long, List<Role>>>();
	}

	public void addOrganizationRoles(Organization organization, List<Role> roles) {
		addUserGroupRoles(organization.getOrganizationId(), roles);
	}

	public void addUserGroupRole(UserGroup group, Role role) {
		if (group != null && role != null) {
			List<Role> roles = new ArrayList<Role>();
			roles.add(role);
			addUserGroupRoles(group, roles);
		}
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

	public void addUserGroupRoles(UserGroup group, List<Role> roles) {
		addUserGroupRoles(group.getUserGroupId(), roles);
	}

	public void addUserRole(User user, Role role) {
		if (user != null && role != null) {
			List<Role> roles = new ArrayList<Role>();
			roles.add(role);
			addUserRoles(user, roles);
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

	public void addUserRolesOfGroup(Long userId, Long groupId, List<Role> roles) {
		if (userId != null && groupId != null && roles != null) {
			userRoleOfGroupTime.put(userId, System.currentTimeMillis());

			Hashtable<Long, List<Role>> userAndGroupRoles = userRolesOfGroup.get(userId);
			if (userAndGroupRoles == null) {
				userAndGroupRoles = new Hashtable<Long, List<Role>>();
				userRolesOfGroup.put(userId, userAndGroupRoles);
			}

			List<Role> groupRoles = userAndGroupRoles.get(groupId);
			if (groupRoles == null) {
				groupRoles = new ArrayList<Role>();
				userAndGroupRoles.put(groupId, groupRoles);
			}

			for (Role role : roles) {
				if (!groupRoles.contains(role)) {
					groupRoles.add(role);
				}
			}
		}
	}

	public void addUserRolesOfGroup(User user, Group group, List<Role> roles) {
		if (user != null && group != null) {
			addUserRolesOfGroup(user.getUserId(), group.getGroupId(), roles);
		}
	}

	/**
	 * Get all roles of a group.
	 * 
	 * @param groupId
	 * @return
	 */
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

	/**
	 * Get all roles of a group.
	 * 
	 * @param group
	 * @return
	 */
	public List<Role> getGroupRoles(UserGroup group) {
		return getGroupRoles(group.getUserGroupId());
	}

	/**
	 * Get all roles of a organization.
	 * 
	 * @param organization
	 * @return
	 */
	public List<Role> getOrganizationRoles(Organization organization) {
		return getGroupRoles(organization.getOrganizationId());
	}

	public List<Role> getUserRoles(User user) {
		if (user != null) {
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
		}
		return null;
	}

	public List<Role> getUserRolesOfGroup(Long userId, Long groupId) {
		if (userId != null && groupId != null) {
			long now = System.currentTimeMillis();
			Long nextUserId = null;
			if (userRoleOfGroupTime.size() > 0) {
				Enumeration<Long> e = userRoleOfGroupTime.keys();
				while (e.hasMoreElements()) {
					nextUserId = e.nextElement();
					if ((now - userRoleOfGroupTime.get(nextUserId)) > EXPIRATION_TIME) {
						// object has expired
						removeUserRolesOfGroup(nextUserId);
						nextUserId = null;
					} else {
						if (userId == nextUserId) {
							Hashtable<Long, List<Role>> userAndGroupRoles = userRolesOfGroup.get(nextUserId);
							if (userAndGroupRoles != null) {
								return userAndGroupRoles.get(groupId);
							}
						}
					}
				}
			}
		}
		return null;
	}

	public List<Role> getUserRolesOfGroup(User user, Group group) {
		if (user != null && group != null) {
			return getUserRolesOfGroup(user.getUserId(), group.getGroupId());
		}
		return null;
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

	public void removeGroupRole(Role role, Group group) {
		removeGroupRole(role, group.getGroupId());
	}

	public void removeOrganizationRole(Role role, Organization organization) {
		removeGroupRole(role, organization.getOrganizationId());
	}

	public void removeGroupRole(Role role, Long groupId) {
		if (rolesByGroup.get(groupId) != null) {
			rolesByGroup.get(groupId).remove(role);
		}
	}

	public void removeOrganizationRoles(Long organizationId) {
		removeGroupRoles(organizationId);
	}

	public void removeOrganizationRoles(Organization organization) {
		if (organization != null) {
			removeGroupRoles(organization.getOrganizationId());
		}
	}

	public void removeRole(Role role) {
		List<Role> roles = new ArrayList<Role>();
		roles.add(role);
		removeRoles(roles);
	}

	public void removeRoles(List<Role> roles) {
		for (Role role : roles) {
			for (List<Role> userRoles : rolesByUser.values()) {
				userRoles.remove(role);
			}
			for (List<Role> groupRoles : rolesByGroup.values()) {
				groupRoles.remove(role);
			}
			for (Hashtable<Long, List<Role>> rolesByUserAndGroup : userRolesOfGroup.values()) {
				for (List<Role> userGroupRoles : rolesByUserAndGroup.values()) {
					userGroupRoles.remove(role);
				}
			}
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

	public void removeUserRolesOfGroup(long userId) {
		userRoleOfGroupTime.remove(userId);
		userRolesOfGroup.remove(userId);
	}

	public void setUserRoles(User user, List<Role> roles) {
		if (user != null && roles != null) {
			userTime.put(user.getUserId(), System.currentTimeMillis());
			rolesByUser.put(user.getUserId(), roles);
		}
	}
}
