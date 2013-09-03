package com.biit.liferay.security;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.biit.liferay.access.RoleService;
import com.biit.liferay.access.UserGroupService;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.log.LiferayClientLogger;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

public abstract class AuthorizationService {

	public AuthorizationService() {
		try {
			if (RoleService.getInstance().isNotConnected()) {
				RoleService.getInstance().connectToWebService();
			}
			if (UserGroupService.getInstance().isNotConnected()) {
				UserGroupService.getInstance().connectToWebService();
			}
		} catch (ServiceException se) {
			LiferayClientLogger
					.fatal(AuthenticationService.class.getName(),
							"Cannot connect to RoleService and/or GroupService. Please, configure the file 'liferay.conf' correctly.");
		}
	}

	public boolean isAuthorizedActivity(User user, String activity) {
		if (user != null) {
			if (getUserActivitiesAllowed(user).contains(activity)) {
				return true;
			}
		}
		return false;
	}

	private List<String> getUserActivitiesAllowed(User user) {
		List<String> activities = new ArrayList<String>();
		if (user != null) {
			List<Role> roles = getUserRoles(user);
			List<UserGroup> userGroups = getUserGroups(user);

			// Add roles obtained by group.
			for (UserGroup group : userGroups) {
				roles.addAll(getUserGroupRoles(group));
			}

			// Activities by role.
			for (Role role : roles) {
				List<String> roleActivities = getRoleActivities(role);
				activities.addAll(roleActivities);
			}
		}
		return activities;
	}

	public List<Role> getUserRoles(User user) {
		if (user != null) {
			try {
				return RoleService.getInstance().getUserRoles(user);
			} catch (RemoteException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the user's roles from '" + user.getEmailAddress() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			} catch (NotConnectedToWebServiceException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the user's roles from '" + user.getEmailAddress() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			}
		}
		return new ArrayList<Role>();
	}

	public List<UserGroup> getUserGroups(User user) {
		if (user != null) {
			try {
				return UserGroupService.getInstance().getUserUserGroups(user);
			} catch (RemoteException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the user's groups from " + user.getEmailAddress() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			} catch (NotConnectedToWebServiceException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the user's groups from " + user.getEmailAddress() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			}
		}
		return new ArrayList<UserGroup>();
	}

	public List<Role> getUserGroupRoles(UserGroup group) {
		if (group != null) {
			try {
				return RoleService.getInstance().getGroupRoles(group);
			} catch (RemoteException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the group's roles from " + group.getName() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			} catch (NotConnectedToWebServiceException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the group's roles from " + group.getName() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			}
		}
		return new ArrayList<Role>();
	}

	public abstract List<String> getRoleActivities(Role role);
}
