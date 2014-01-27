package com.biit.liferay.security;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.biit.liferay.access.CompanyService;
import com.biit.liferay.access.GroupService;
import com.biit.liferay.access.ListTypeService;
import com.biit.liferay.access.OrganizationService;
import com.biit.liferay.access.RoleService;
import com.biit.liferay.access.UserGroupService;
import com.biit.liferay.access.UserService;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.log.LiferayClientLogger;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

public abstract class AuthorizationService {
	private AuthorizationPool authorizationPool;

	public AuthorizationService() {
		authorizationPool = new AuthorizationPool();
		// Connect if not connected the fist time used.
		if (UserService.getInstance().isNotConnected()) {
			UserService.getInstance().serverConnection();
		}
		if (CompanyService.getInstance().isNotConnected()) {
			CompanyService.getInstance().serverConnection();
		}
		if (RoleService.getInstance().isNotConnected()) {
			RoleService.getInstance().serverConnection();
		}
		if (GroupService.getInstance().isNotConnected()) {
			GroupService.getInstance().serverConnection();
		}
		if (UserGroupService.getInstance().isNotConnected()) {
			UserGroupService.getInstance().serverConnection();
		}
		if (OrganizationService.getInstance().isNotConnected()) {
			OrganizationService.getInstance().serverConnection();
		}
		if (ListTypeService.getInstance().isNotConnected()) {
			ListTypeService.getInstance().serverConnection();
		}
	}

	public boolean isAuthorizedActivity(User user, String activity) throws IOException, AuthenticationRequired {
		if (user != null) {
			if (getUserActivitiesAllowed(user).contains(activity)) {
				return true;
			}
		}
		return false;
	}

	private List<String> getUserActivitiesAllowed(User user) throws IOException, AuthenticationRequired {
		List<String> activities = new ArrayList<String>();
		if (user != null) {
			List<Role> roles = getUserRoles(user);

			// Add roles obtained by group.
			List<UserGroup> userGroups = getUserGroups(user);
			for (UserGroup group : userGroups) {
				for (Role role : getUserGroupRoles(group)) {
					if (!roles.contains(role)) {
						roles.add(role);
					}
				}
			}

			// Add roles obtained by organization.
			List<Organization> userOrganizations = getUserOrganizations(user);
			for (Organization organization : userOrganizations) {
				for (Role role : getUserRoles(user, organization)) {
					if (!roles.contains(role)) {
						roles.add(role);
					}
				}
			}

			// Activities by role.
			for (Role role : roles) {
				List<String> roleActivities = getRoleActivities(role);
				activities.addAll(roleActivities);
			}
		}
		return activities;
	}

	public List<Organization> getUserOrganizations(User user) throws ClientProtocolException, IOException,
			AuthenticationRequired {
		try {
			return OrganizationService.getInstance().getUserOrganizations(user);
		} catch (NotConnectedToWebServiceException e) {
			LiferayClientLogger.error(AuthorizationService.class.getName(),
					"Error retrieving the user's organizations for '" + user.getEmailAddress() + "'");
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
		} catch (WebServiceAccessError e) {
			LiferayClientLogger.error(AuthorizationService.class.getName(),
					"Error retrieving the user's organizations for '" + user.getEmailAddress() + "'");
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
		}
		return new ArrayList<Organization>();
	}

	public List<Role> getUserRoles(User user, Organization organization) throws IOException, AuthenticationRequired {
		if (user != null && organization != null) {
			try {
				return RoleService.getInstance().getUserRolesOfOrganization(user, organization);
			} catch (NotConnectedToWebServiceException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the user's roles for '" + user.getEmailAddress() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			} catch (WebServiceAccessError e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the user's roles for '" + user.getEmailAddress() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			}
		}
		return new ArrayList<Role>();
	}

	public List<Role> getUserRoles(User user) throws IOException, AuthenticationRequired {
		if (user != null) {
			try {
				return RoleService.getInstance().getUserRoles(user);
			} catch (RemoteException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the user's roles for '" + user.getEmailAddress() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			} catch (NotConnectedToWebServiceException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the user's roles for '" + user.getEmailAddress() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			}
		}
		return new ArrayList<Role>();
	}

	public List<UserGroup> getUserGroups(User user) throws ClientProtocolException, IOException, AuthenticationRequired {
		if (user != null) {
			try {
				return UserGroupService.getInstance().getUserUserGroups(user);
			} catch (RemoteException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the user's groups for " + user.getEmailAddress() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			} catch (NotConnectedToWebServiceException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the user's groups for " + user.getEmailAddress() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			}
		}
		return new ArrayList<UserGroup>();
	}

	public List<Role> getUserGroupRoles(UserGroup group) throws ClientProtocolException, IOException,
			AuthenticationRequired {
		if (group != null) {
			try {
				return RoleService.getInstance().getGroupRoles(group);
			} catch (RemoteException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the group's roles for " + group.getName() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			} catch (NotConnectedToWebServiceException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the group's roles for " + group.getName() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			}
		}
		return new ArrayList<Role>();
	}

	/**
	 * User is allowed to do an activity.
	 * 
	 * @param user
	 * @param activity
	 * @return
	 * @throws AuthenticationRequired
	 * @throws IOException
	 */
	public boolean isAuthorizedActivity(User user, IActivity activity) throws IOException, AuthenticationRequired {
		if (user == null) {
			return false;
		}
		// Is it in the pool?
		Boolean authorized = authorizationPool.isAuthorizedActivity(user, activity);
		if (authorized != null) {
			return authorized;
		}

		// Calculate authorization.
		authorized = isAuthorizedActivity(user, activity.getTag());
		authorizationPool.addUser(user, activity, authorized);
		return authorized;
	}

	public abstract List<String> getRoleActivities(Role role);
}
