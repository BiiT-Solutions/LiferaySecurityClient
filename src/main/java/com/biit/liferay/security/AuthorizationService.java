package com.biit.liferay.security;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.biit.liferay.access.OrganizationService;
import com.biit.liferay.access.RoleService;
import com.biit.liferay.access.UserGroupService;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.PortletNotInstalledException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.log.LiferayClientLogger;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.Site;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

public abstract class AuthorizationService {
	private AuthorizationPool authorizationPool;
	private RoleService roleService = new RoleService();
	private UserGroupService userGroupService = new UserGroupService();
	private OrganizationService organizationService = new OrganizationService();

	public AuthorizationService() {
		authorizationPool = new AuthorizationPool();
		roleService.serverConnection();
		userGroupService.serverConnection();
		organizationService.serverConnection();
	}

	private List<IActivity> getUserActivitiesAllowed(User user, Organization organization) throws IOException,
			AuthenticationRequired {
		List<IActivity> organizationActivities = new ArrayList<IActivity>();
		if (user != null && organization != null) {
			// Add roles obtained by organization.
			for (Role role : getUserRoles(user, organization)) {
				for (IActivity activity : getRoleActivities(role)) {
					if (!organizationActivities.contains(activity)) {
						organizationActivities.add(activity);
					}
				}
			}
		}

		return organizationActivities;
	}

	private List<IActivity> getUserActivitiesAllowed(User user) throws IOException, AuthenticationRequired {
		List<IActivity> activities = new ArrayList<IActivity>();
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

			// Activities by role.
			for (Role role : roles) {
				List<IActivity> roleActivities = getRoleActivities(role);
				for (IActivity roleActivity : roleActivities) {
					if (!activities.contains(roleActivity)) {
						activities.add(roleActivity);
					}
				}
			}
		}
		return activities;
	}

	/**
	 * Return all user organization of the application. An organization is in
	 * the application if it has a role that exists in the application.
	 * 
	 * @param user
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws AuthenticationRequired
	 */
	public List<Organization> getUserOrganizations(User user) throws ClientProtocolException, IOException,
			AuthenticationRequired {
		try {
			List<Organization> organizations = organizationService.getUserOrganizations(user);
			List<Organization> applicationOrganizations = new ArrayList<Organization>();
			for (Organization organization : organizations) {
				if (!getUserRoles(user, organization).isEmpty()) {
					applicationOrganizations.add(organization);
				}
			}
			return applicationOrganizations;
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

	public List<Organization> getUserOrganizations(User user, Site site) throws ClientProtocolException, IOException,
			AuthenticationRequired, PortletNotInstalledException {
		try {
			List<Organization> organizations = organizationService.getOrganizations(site, user);
			List<Organization> applicationOrganizations = new ArrayList<Organization>();
			for (Organization organization : organizations) {
				if (!getUserRoles(user, organization).isEmpty()) {
					applicationOrganizations.add(organization);
				}
			}
			return applicationOrganizations;
		} catch (NotConnectedToWebServiceException e) {
			LiferayClientLogger.error(AuthorizationService.class.getName(),
					"Error retrieving the user's organizations for '" + user.getEmailAddress() + "'");
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
		}
		return new ArrayList<Organization>();
	}

	public List<Role> getUserRoles(User user, Organization organization) throws IOException, AuthenticationRequired {
		if (user != null && organization != null) {
			try {
				return roleService.getUserRolesOfOrganization(user, organization);
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
				return roleService.getUserRoles(user);
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
				return userGroupService.getUserUserGroups(user);
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
				return roleService.getGroupRoles(group);
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
		authorized = getUserActivitiesAllowed(user).contains(activity);
		authorizationPool.addUser(user, activity, authorized);
		return authorized;
	}

	public boolean isAuthorizedActivity(User user, Organization organization, IActivity activity) throws IOException,
			AuthenticationRequired {
		if (user != null) {
			// If user has the permission... no need to check the organization.
			if (isAuthorizedActivity(user, activity)) {
				return true;
			}

			// Is it in the pool?
			Boolean authorized = authorizationPool.isAuthorizedActivity(user, organization, activity);
			if (authorized != null) {
				return authorized;
			}

			// Calculate authorization.
			authorized = getUserActivitiesAllowed(user, organization).contains(activity);
			authorizationPool.addUser(user, organization, activity, authorized);
			return authorized;
		}
		return false;
	}

	public abstract List<IActivity> getRoleActivities(Role role);
}
