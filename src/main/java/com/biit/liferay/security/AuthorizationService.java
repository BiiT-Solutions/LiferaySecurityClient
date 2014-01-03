package com.biit.liferay.security;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.biit.liferay.access.RoleService;
import com.biit.liferay.access.UserGroupService;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.log.LiferayClientLogger;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

public abstract class AuthorizationService {

	public AuthorizationService() {
		if (RoleService.getInstance().isNotConnected()) {
			RoleService.getInstance().serverConnection();
		}
		if (UserGroupService.getInstance().isNotConnected()) {
			UserGroupService.getInstance().serverConnection();
		}
		// LiferayClientLogger
		// .fatal(AuthenticationService.class.getName(),
		// "Cannot connect to RoleService and/or GroupService. Please, configure the file 'liferay.conf' correctly.");
	}

	public boolean isAuthorizedActivity(User UserSoap, String activity) throws ClientProtocolException, IOException, AuthenticationRequired {
		if (UserSoap != null) {
			if (getUserActivitiesAllowed(UserSoap).contains(activity)) {
				return true;
			}
		}
		return false;
	}

	private List<String> getUserActivitiesAllowed(User UserSoap) throws ClientProtocolException, IOException, AuthenticationRequired {
		List<String> activities = new ArrayList<String>();
		if (UserSoap != null) {
			List<Role> roles = getUserRoles(UserSoap);
			List<UserGroup> userGroups = getUserGroups(UserSoap);

			// Add roles obtained by group.
			for (UserGroup group : userGroups) {
				roles.addAll(getUserGroupRoles(group));
			}

			// Activities by RoleSoap.
			for (Role RoleSoap : roles) {
				List<String> roleActivities = getRoleActivities(RoleSoap);
				activities.addAll(roleActivities);
			}
		}
		return activities;
	}

	public List<Role> getUserRoles(User UserSoap) throws ClientProtocolException, IOException, AuthenticationRequired {
		if (UserSoap != null) {
			try {
				return RoleService.getInstance().getUserRoles(UserSoap);
			} catch (RemoteException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the UserSoap's roles from '" + UserSoap.getEmailAddress() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			} catch (NotConnectedToWebServiceException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the UserSoap's roles from '" + UserSoap.getEmailAddress() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			}
		}
		return new ArrayList<Role>();
	}

	public List<UserGroup> getUserGroups(User UserSoap) throws ClientProtocolException, IOException, AuthenticationRequired {
		if (UserSoap != null) {
			try {
				return UserGroupService.getInstance().getUserUserGroups(UserSoap);
			} catch (RemoteException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the UserSoap's groups from " + UserSoap.getEmailAddress() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			} catch (NotConnectedToWebServiceException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the UserSoap's groups from " + UserSoap.getEmailAddress() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			}
		}
		return new ArrayList<UserGroup>();
	}

	public List<Role> getUserGroupRoles(UserGroup group) throws ClientProtocolException, IOException, AuthenticationRequired {
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

	public abstract List<String> getRoleActivities(Role RoleSoap);
}
