package com.biit.liferay.security;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;

import com.biit.liferay.access.CompanyService;
import com.biit.liferay.access.OrganizationService;
import com.biit.liferay.access.RoleService;
import com.biit.liferay.access.UserGroupService;
import com.biit.liferay.access.UserService;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.PortletNotInstalledException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.configuration.ConfigurationReader;
import com.biit.liferay.log.LiferayClientLogger;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.Site;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

public abstract class AuthorizationService implements IAuthorizationService {
	private AuthorizationPool authorizationPool;
	private RoleService roleService = new RoleService();
	private UserGroupService userGroupService = new UserGroupService();
	private OrganizationService organizationService = new OrganizationService();
	private CompanyService companyService = new CompanyService();
	private UserService userService = new UserService();

	public AuthorizationService() {
		authorizationPool = new AuthorizationPool();
		roleService.serverConnection();
		userGroupService.serverConnection();
		organizationService.serverConnection();
		companyService.serverConnection();
		userService.serverConnection();
	}

	@Override
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();
		try {
			Company company = companyService
					.getCompanyByVirtualHost(ConfigurationReader.getInstance().getVirtualHost());
			return userService.getCompanyUsers(company.getCompanyId());
		} catch (IOException | AuthenticationRequired | NotConnectedToWebServiceException | WebServiceAccessError e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
		}
		return users;
	}

	@Override
	public Organization getOrganization(long organizationId) throws IOException, AuthenticationRequired {
		try {
			Organization organization = organizationService.getOrganization(organizationId);
			return organization;
		} catch (NotConnectedToWebServiceException e) {
			LiferayClientLogger.error(AuthorizationService.class.getName(),
					"Error retrieving the organization with id '" + organizationId + "'.");
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
		} catch (WebServiceAccessError e) {
			LiferayClientLogger.error(AuthorizationService.class.getName(),
					"Error retrieving the organization with id '" + organizationId + "'.");
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
		}
		return null;
	}

	private Set<IActivity> getUserActivitiesAllowed(User user) throws IOException, AuthenticationRequired {
		Set<IActivity> activities = new HashSet<IActivity>();
		if (user != null) {
			Set<Role> roles = getUserRoles(user);

			// Add roles obtained by group.
			Set<UserGroup> userGroups = getUserGroups(user);
			for (UserGroup group : userGroups) {
				for (Role role : getUserGroupRoles(group)) {
					if (!roles.contains(role)) {
						roles.add(role);
					}
				}
			}

			// Activities by role.
			for (Role role : roles) {
				Set<IActivity> roleActivities = getRoleActivities(role);
				for (IActivity roleActivity : roleActivities) {
					if (!activities.contains(roleActivity)) {
						activities.add(roleActivity);
					}
				}
			}
		}
		return activities;
	}

	private Set<IActivity> getUserActivitiesAllowed(User user, Organization organization) throws IOException,
			AuthenticationRequired {
		Set<IActivity> organizationActivities = new HashSet<IActivity>();
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

	@Override
	public Set<Role> getUserGroupRoles(UserGroup group) throws ClientProtocolException, IOException,
			AuthenticationRequired {
		if (group != null) {
			try {
				return new HashSet<Role>(roleService.getGroupRoles(group));
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
		return new HashSet<Role>();
	}

	@Override
	public Set<UserGroup> getUserGroups(User user) throws ClientProtocolException, IOException, AuthenticationRequired {
		if (user != null) {
			try {
				return new HashSet<UserGroup>(userGroupService.getUserUserGroups(user));
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
		return new HashSet<UserGroup>();
	}

	/**
	 * Return all user organization of the application. An organization is in the application if it has a role that
	 * exists in the application.
	 * 
	 * @param user
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws AuthenticationRequired
	 */
	@Override
	public Set<Organization> getUserOrganizations(User user) throws ClientProtocolException, IOException,
			AuthenticationRequired {
		try {
			Set<Organization> organizations = new HashSet<Organization>(organizationService.getUserOrganizations(user));
			Set<Organization> applicationOrganizations = new HashSet<Organization>();
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
		return new HashSet<Organization>();
	}

	@Override
	public Set<Organization> getUserOrganizations(User user, Site site) throws ClientProtocolException, IOException,
			AuthenticationRequired, PortletNotInstalledException {
		try {
			Set<Organization> organizations = new HashSet<Organization>(
					organizationService.getOrganizations(site, user));
			Set<Organization> applicationOrganizations = new HashSet<Organization>();
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
		return new HashSet<Organization>();
	}

	@Override
	public Set<Role> getUserRoles(User user) throws IOException, AuthenticationRequired {
		if (user != null) {
			try {
				return new HashSet<Role>(roleService.getUserRoles(user));
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
		return new HashSet<Role>();
	}

	@Override
	public Set<Role> getUserRoles(User user, Organization organization) throws IOException, AuthenticationRequired {
		if (user != null && organization != null) {
			try {
				return new HashSet<Role>(roleService.getUserRolesOfOrganization(user, organization));
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
		return new HashSet<Role>();
	}

	@Override
	public Role getRole(long roleId) throws IOException, AuthenticationRequired {
		try {
			return roleService.getRole(roleId);
		} catch (RemoteException e) {
			LiferayClientLogger.error(AuthorizationService.class.getName(), "Error retrieving the role '" + roleId
					+ "'");
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
		} catch (NotConnectedToWebServiceException e) {
			LiferayClientLogger.error(AuthorizationService.class.getName(), "Error retrieving the role '" + roleId
					+ "'");
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
		} catch (WebServiceAccessError e) {
			LiferayClientLogger.error(AuthorizationService.class.getName(), "Error retrieving the role '" + roleId
					+ "'");
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
		}
		return null;
	}

	@Override
	public Role getRole(String roleName) throws IOException, AuthenticationRequired {
		if (roleName != null) {
			try {
				Company company = companyService.getCompanyByVirtualHost(ConfigurationReader.getInstance()
						.getVirtualHost());
				return roleService.getRole(roleName, company.getCompanyId());
			} catch (RemoteException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(), "Error retrieving the role '"
						+ roleName + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			} catch (NotConnectedToWebServiceException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(), "Error retrieving the role '"
						+ roleName + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			} catch (WebServiceAccessError e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(), "Error retrieving the role '"
						+ roleName + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			}
		}
		return null;
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
	@Override
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

	@Override
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

	public Set<Organization> getUserOrganizationsWhereIsAuthorized(User user, IActivity... activities) {
		Set<Organization> organizations = new HashSet<>();
		// Is it in the pool?
		try {
			organizations = getUserOrganizations(user);
			Iterator<Organization> itr = organizations.iterator();
			while (itr.hasNext()) {
				Organization organization = itr.next();
				boolean remove = true;
				for (IActivity activity : activities) {
					if (isAuthorizedActivity(user, organization, activity)) {
						remove = false;
						break;
					}
				}
				if (remove) {
					itr.remove();
				}
			}
		} catch (IOException | AuthenticationRequired e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
		}
		return organizations;
	}

	public void reset() {
		authorizationPool.reset();
		roleService.reset();
		userGroupService.reset();
		organizationService.reset();
		companyService.reset();
		userService.reset();
	}
}
