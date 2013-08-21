package com.biit.liferay.security;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.biit.liferay.access.GroupService;
import com.biit.liferay.access.RoleService;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.log.LiferayAuthenticationClientLogger;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

public abstract class AuthorizationService {

	public AuthorizationService() {
		try {
			if (RoleService.getInstance().isNotConnected()) {
				RoleService.getInstance().connectToWebService();
			}
			if (GroupService.getInstance().isNotConnected()) {
				GroupService.getInstance().connectToWebService();
			}
		} catch (ServiceException se) {
			LiferayAuthenticationClientLogger
					.fatal(AuthenticationService.class.getName(),
							"Cannot connect to RoleService and/or GroupService. Please, configure the file 'liferay.conf' correctly.");
		}
	}

	public boolean isAuthorizedActivity(User user, String activity) {
		if (getUserActivitiesAllowed(user).contains(activity)) {
			return true;
		}
		LiferayAuthenticationClientLogger.debug(AuthorizationService.class.getName(), "Activity " + activity + " not authorized to " + user.getEmailAddress());
		return false;
	}

	private List<String> getUserActivitiesAllowed(User currentUser) {
		List<Role> roles = getUserRoles(currentUser);
		List<UserGroup> userGroups = getUserGroups(currentUser);
		List<String> activities = new ArrayList<String>();

		// Add roles obtained by group.
		for (UserGroup group : userGroups) {
			roles.addAll(getUserGroupRoles(group));
		}

		// Activities by role.
		for (Role role : roles) {
			List<String> roleActivities = getRoleActivities(role);
			activities.addAll(roleActivities);
		}
		return activities;
	}

	public List<Role> getUserRoles(User user) {
		try {
			return RoleService.getInstance().getUserRoles(user);
		} catch (RemoteException e) {
			LiferayAuthenticationClientLogger.error(AuthorizationService.class.getName(),
					"Error retrieving the user's roles from '" + user.getEmailAddress() + "'");
			LiferayAuthenticationClientLogger.errorMessage(AuthorizationService.class.getName(), e);
		} catch (NotConnectedToWebServiceException e) {
			LiferayAuthenticationClientLogger.error(AuthorizationService.class.getName(),
					"Error retrieving the user's roles from '" + user.getEmailAddress() + "'");
			LiferayAuthenticationClientLogger.errorMessage(AuthorizationService.class.getName(), e);
		}
		return new ArrayList<Role>();
	}

	public List<UserGroup> getUserGroups(User user) {
		try {
			return GroupService.getInstance().getUserUserGroups(user);
		} catch (RemoteException e) {
			LiferayAuthenticationClientLogger.error(AuthorizationService.class.getName(),
					"Error retrieving the user's groups from " + user.getEmailAddress() + "'");
			LiferayAuthenticationClientLogger.errorMessage(AuthorizationService.class.getName(), e);
		} catch (NotConnectedToWebServiceException e) {
			LiferayAuthenticationClientLogger.error(AuthorizationService.class.getName(),
					"Error retrieving the user's groups from " + user.getEmailAddress() + "'");
			LiferayAuthenticationClientLogger.errorMessage(AuthorizationService.class.getName(), e);
		}
		return new ArrayList<UserGroup>();
	}

	public List<Role> getUserGroupRoles(UserGroup group) {
		try {
			return RoleService.getInstance().getGroupRoles(group);
		} catch (RemoteException e) {
			LiferayAuthenticationClientLogger.error(AuthorizationService.class.getName(),
					"Error retrieving the group's roles from " + group.getName() + "'");
			LiferayAuthenticationClientLogger.errorMessage(AuthorizationService.class.getName(), e);
		} catch (NotConnectedToWebServiceException e) {
			LiferayAuthenticationClientLogger.error(AuthorizationService.class.getName(),
					"Error retrieving the group's roles from " + group.getName() + "'");
			LiferayAuthenticationClientLogger.errorMessage(AuthorizationService.class.getName(), e);
		}
		return new ArrayList<Role>();
	}

	public abstract List<String> getRoleActivities(Role role);
}
