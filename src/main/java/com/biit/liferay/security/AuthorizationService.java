package com.biit.liferay.security;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;

import com.biit.liferay.access.CompanyService;
import com.biit.liferay.access.GroupService;
import com.biit.liferay.access.OrganizationService;
import com.biit.liferay.access.RoleService;
import com.biit.liferay.access.ServiceAccess;
import com.biit.liferay.access.UserGroupService;
import com.biit.liferay.access.UserService;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.PortletNotInstalledException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.configuration.LiferayConfigurationReader;
import com.biit.liferay.log.LiferayClientLogger;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IRole;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.entity.pool.ActivityAuthorizationPool;
import com.biit.usermanager.security.IActivity;
import com.biit.usermanager.security.IAuthorizationService;
import com.biit.usermanager.security.IRoleActivities;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class AuthorizationService implements IAuthorizationService<Long, Long, Long> {
	private ActivityAuthorizationPool authorizationPool;
	private RoleService roleService = new RoleService();
	private UserGroupService userGroupService = new UserGroupService();
	private GroupService groupService = new GroupService();
	private OrganizationService organizationService = new OrganizationService();
	private CompanyService companyService = new CompanyService();
	private UserService userService = new UserService();

	private IRoleActivities roleActivities;

	public AuthorizationService() {
		authorizationPool = new ActivityAuthorizationPool();
		roleService.serverConnection();
		groupService.serverConnection();
		userGroupService.serverConnection();
		organizationService.serverConnection();
		companyService.serverConnection();
		userService.serverConnection();
	}

	@Override
	public Set<IUser<Long>> getAllUsers() throws UserManagementException {
		Set<IUser<Long>> users = new HashSet<IUser<Long>>();
		try {
			IGroup<Long> company = companyService
					.getCompanyByVirtualHost(LiferayConfigurationReader.getInstance().getVirtualHost());
			return userService.getCompanyUsers(company);
		} catch (IOException e) {

		} catch (NotConnectedToWebServiceException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException("Error retrieving all users.");
		} catch (AuthenticationRequired e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException("Error retrieving all users.");
		} catch (WebServiceAccessError e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException("Error retrieving all users.");
		}
		return users;
	}

	@Override
	public Set<IUser<Long>> getAllUsers(IGroup<Long> organization) throws UserManagementException {
		try {
			return organizationService.getOrganizationUsers(organization);
		} catch (IOException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException(
					"Error retrieving the users from organization with id '" + organization.getId() + "'.");
		} catch (NotConnectedToWebServiceException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException(
					"Error retrieving the users from organization with id '" + organization.getId() + "'.");
		} catch (AuthenticationRequired e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException(
					"Error retrieving the users from organization with id '" + organization.getId() + "'.");
		}
	}

	@Override
	public IGroup<Long> getOrganization(Long organizationId) throws UserManagementException {
		if (organizationId == null) {
			return null;
		}
		try {
			return organizationService.getOrganization(organizationId);
		} catch (NotConnectedToWebServiceException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException("Error retrieving the organization with id '" + organizationId + "'.");
		} catch (WebServiceAccessError e) {
			LiferayClientLogger.warning(AuthorizationService.class.getName(), e.getMessage());
			throw new UserManagementException("Error retrieving the organization with id '" + organizationId + "'.");
		} catch (JsonParseException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException("Error retrieving the organization with id '" + organizationId + "'.");
		} catch (JsonMappingException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException("Error retrieving the organization with id '" + organizationId + "'.");
		} catch (IOException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException("Error retrieving the organization with id '" + organizationId + "'.");
		} catch (AuthenticationRequired e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException("Error retrieving the organization with id '" + organizationId + "'.");
		}
	}

	/**
	 * Gets the organization from its name. Returns a group not an organization.
	 * 
	 * @param organizationId
	 * @return
	 * @throws UserManagementException
	 */
	@Override
	public IGroup<Long> getOrganization(String organizationName) throws UserManagementException {
		if (organizationName == null) {
			return null;
		}
		try {
			IGroup<Long> company = companyService
					.getCompanyByVirtualHost(LiferayConfigurationReader.getInstance().getVirtualHost());
			IGroup<Long> organizationGroup = groupService.getGroup(company.getId(),
					organizationName + ServiceAccess.LIFERAY_ORGANIZATION_GROUP_SUFIX);
			// Id of organization is 1 less than its group.
			return organizationService.getOrganization(organizationGroup.getId() - 1);
		} catch (NotConnectedToWebServiceException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException("Error retrieving the organization '" + organizationName + "'.");
		} catch (WebServiceAccessError e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException("Error retrieving the organization '" + organizationName + "'.");
		} catch (JsonParseException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException("Error retrieving the organization '" + organizationName + "'.");
		} catch (JsonMappingException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException("Error retrieving the organization '" + organizationName + "'.");
		} catch (IOException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException("Error retrieving the organization '" + organizationName + "'.");
		} catch (AuthenticationRequired e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException("Error retrieving the organization '" + organizationName + "'.");
		}
	}

	/**
	 * Gets all the organizations of a company
	 * 
	 * @param organizationId
	 * @return
	 * @throws UserManagementException
	 */
	public Set<IGroup<Long>> getAllAvailableOrganizations() throws UserManagementException {
		try {
			IGroup<Long> company = companyService
					.getCompanyByVirtualHost(LiferayConfigurationReader.getInstance().getVirtualHost());
			return organizationService.getOrganizations(company);
		} catch (NotConnectedToWebServiceException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException("Error retrieving all organizations of a company.");
		} catch (WebServiceAccessError e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException("Error retrieving all organizations of a company.");
		} catch (JsonParseException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException("Error retrieving all organizations of a company.");
		} catch (JsonMappingException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException("Error retrieving all organizations of a company.");
		} catch (IOException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException("Error retrieving all organizations of a company.");
		} catch (AuthenticationRequired e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException("Error retrieving all organizations of a company.");
		}
	}

	@Override
	public IRole<Long> getRole(Long roleId) throws UserManagementException {
		if (roleId != null) {
			try {
				return roleService.getRole(roleId);
			} catch (RemoteException e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException("Error retrieving the role '" + roleId + "'");
			} catch (NotConnectedToWebServiceException e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException("Error retrieving the role '" + roleId + "'");
			} catch (WebServiceAccessError e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException("Error retrieving the role '" + roleId + "'");
			} catch (ClientProtocolException e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException(e.getMessage());
			} catch (IOException e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException(e.getMessage());
			} catch (AuthenticationRequired e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException(e.getMessage());
			}
		}
		return null;
	}

	@Override
	public IRole<Long> getRole(String roleName) throws UserManagementException {
		if (roleName != null) {
			try {
				IGroup<Long> company = companyService
						.getCompanyByVirtualHost(LiferayConfigurationReader.getInstance().getVirtualHost());
				return roleService.getRole(roleName, company.getId());
			} catch (RemoteException e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException("Error retrieving the role '" + roleName + "'");
			} catch (NotConnectedToWebServiceException e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException("Error retrieving the role '" + roleName + "'");
			} catch (WebServiceAccessError e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException("Error retrieving the role '" + roleName + "'");
			} catch (JsonParseException e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException(e.getMessage());
			} catch (JsonMappingException e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException(e.getMessage());
			} catch (IOException e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException(e.getMessage());
			} catch (AuthenticationRequired e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException(e.getMessage());
			}
		}
		return null;
	}

	private Set<IActivity> getUserActivitiesAllowed(IUser<Long> user) throws UserManagementException {
		Set<IActivity> activities = new HashSet<IActivity>();
		if (user != null) {
			Set<IRole<Long>> roles = getUserRoles(user);

			// Add roles obtained by group.
			Set<IGroup<Long>> userGroups = getUserGroups(user);
			for (IGroup<Long> group : userGroups) {
				for (IRole<Long> role : getUserGroupRoles(group)) {
					if (!roles.contains(role)) {
						roles.add(role);
					}
				}
			}

			// Activities by role.
			for (IRole<Long> role : roles) {
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

	private Set<IActivity> getUserActivitiesAllowed(IUser<Long> user, IGroup<Long> organization)
			throws UserManagementException {
		Set<IActivity> organizationActivities = new HashSet<IActivity>();
		if (user != null && organization != null) {
			// Add roles obtained by organization.
			for (IRole<Long> role : getUserRoles(user, organization)) {
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
	public Set<IRole<Long>> getUserGroupRoles(IGroup<Long> group) throws UserManagementException {
		if (group != null) {
			try {
				return new HashSet<IRole<Long>>(roleService.getGroupRoles(group));
			} catch (RemoteException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the group's roles for " + group.getUniqueName() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			} catch (NotConnectedToWebServiceException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the group's roles for " + group.getUniqueName() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			} catch (ClientProtocolException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the group's roles for " + group.getUniqueName() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			} catch (IOException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the group's roles for " + group.getUniqueName() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			} catch (AuthenticationRequired e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the group's roles for " + group.getUniqueName() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			}
		}
		return new HashSet<IRole<Long>>();
	}

	@Override
	public Set<IGroup<Long>> getUserGroups(IUser<Long> user) throws UserManagementException {
		if (user != null) {
			try {
				return new HashSet<IGroup<Long>>(userGroupService.getUserUserGroups(user));
			} catch (RemoteException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the user's groups for " + user.getEmailAddress() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			} catch (NotConnectedToWebServiceException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the user's groups for " + user.getEmailAddress() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			} catch (ClientProtocolException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the user's groups for " + user.getEmailAddress() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			} catch (IOException e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the user's groups for " + user.getEmailAddress() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			} catch (AuthenticationRequired e) {
				LiferayClientLogger.error(AuthorizationService.class.getName(),
						"Error retrieving the user's groups for " + user.getEmailAddress() + "'");
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			}
		}
		return new HashSet<IGroup<Long>>();
	}

	/**
	 * Return all user organization of the application. An organization is in
	 * the application if it has a role that exists in the application.
	 * 
	 * @param user
	 * @return
	 * @throws UserManagementException
	 */
	@Override
	public Set<IGroup<Long>> getUserOrganizations(IUser<Long> user) throws UserManagementException {
		try {
			Set<IGroup<Long>> organizations = new HashSet<IGroup<Long>>(organizationService.getUserOrganizations(user));
			Set<IGroup<Long>> applicationOrganizations = new HashSet<IGroup<Long>>();
			for (IGroup<Long> organization : organizations) {
				if (!getUserRoles(user, organization).isEmpty()) {
					applicationOrganizations.add(organization);
				}
			}
			return applicationOrganizations;
		} catch (NotConnectedToWebServiceException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException(
					"Error retrieving the user's organizations for '" + user.getEmailAddress() + "'");
		} catch (WebServiceAccessError e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException(
					"Error retrieving the user's organizations for '" + user.getEmailAddress() + "'");
		} catch (ClientProtocolException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException(
					"Error retrieving the user's organizations for '" + user.getEmailAddress() + "'");
		} catch (IOException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException(
					"Error retrieving the user's organizations for '" + user.getEmailAddress() + "'");
		} catch (AuthenticationRequired e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException(
					"Error retrieving the user's organizations for '" + user.getEmailAddress() + "'");
		}
	}

	@Override
	public Set<IGroup<Long>> getUserOrganizations(IUser<Long> user, IGroup<Long> site) throws UserManagementException {
		try {
			Set<IGroup<Long>> organizations = new HashSet<IGroup<Long>>(
					organizationService.getOrganizations(site, user));
			Set<IGroup<Long>> applicationOrganizations = new HashSet<IGroup<Long>>();
			for (IGroup<Long> organization : organizations) {
				if (!getUserRoles(user, organization).isEmpty()) {
					applicationOrganizations.add(organization);
				}
			}
			return applicationOrganizations;
		} catch (NotConnectedToWebServiceException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException(
					"Error retrieving the user's organizations for '" + user.getEmailAddress() + "'");
		} catch (ClientProtocolException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException(
					"Error retrieving the user's organizations for '" + user.getEmailAddress() + "'");
		} catch (IOException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException(
					"Error retrieving the user's organizations for '" + user.getEmailAddress() + "'");
		} catch (AuthenticationRequired e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException(
					"Error retrieving the user's organizations for '" + user.getEmailAddress() + "'");
		} catch (PortletNotInstalledException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException(
					"Error retrieving the user's organizations for '" + user.getEmailAddress() + "'");
		}
	}

	public Set<IGroup<Long>> getUserOrganizationsWhereIsAuthorized(IUser<Long> user, IActivity... activities)
			throws UserManagementException {
		Set<IGroup<Long>> organizations = new HashSet<IGroup<Long>>();
		organizations = getUserOrganizations(user);
		Iterator<IGroup<Long>> itr = organizations.iterator();
		while (itr.hasNext()) {
			IGroup<Long> organization = itr.next();
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
		return organizations;
	}

	@Override
	public Set<IRole<Long>> getUserRoles(IUser<Long> user) throws UserManagementException {
		if (user != null) {
			try {
				return new HashSet<IRole<Long>>(roleService.getUserRoles(user));
			} catch (RemoteException e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException(
						"Error retrieving the user's roles for '" + user.getEmailAddress() + "'");
			} catch (NotConnectedToWebServiceException e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException(
						"Error retrieving the user's roles for '" + user.getEmailAddress() + "'");
			} catch (ClientProtocolException e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException(
						"Error retrieving the user's roles for '" + user.getEmailAddress() + "'");
			} catch (IOException e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException(
						"Error retrieving the user's roles for '" + user.getEmailAddress() + "'");
			} catch (AuthenticationRequired e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException(
						"Error retrieving the user's roles for '" + user.getEmailAddress() + "'");
			}
		}
		return new HashSet<IRole<Long>>();
	}

	@Override
	public Set<IRole<Long>> getUserRoles(IUser<Long> user, IGroup<Long> organization) throws UserManagementException {
		if (user != null && organization != null) {
			try {
				return new HashSet<IRole<Long>>(roleService.getUserRolesOfOrganization(user, organization));
			} catch (NotConnectedToWebServiceException e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException(
						"Error retrieving the user's roles for '" + user.getEmailAddress() + "'");
			} catch (WebServiceAccessError e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException(
						"Error retrieving the user's roles for '" + user.getEmailAddress() + "'");
			} catch (ClientProtocolException e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException(
						"Error retrieving the user's roles for '" + user.getEmailAddress() + "'");
			} catch (IOException e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException(
						"Error retrieving the user's roles for '" + user.getEmailAddress() + "'");
			} catch (AuthenticationRequired e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException(
						"Error retrieving the user's roles for '" + user.getEmailAddress() + "'");
			}
		}
		return new HashSet<IRole<Long>>();
	}

	@Override
	public Set<IRole<Long>> getAllRoles(IGroup<Long> organization) throws UserManagementException {
		try {
			return new HashSet<IRole<Long>>(roleService.getOrganizationRoles(organization));
		} catch (ClientProtocolException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException(
					"Error retrieving the organization's roles for '" + organization.getUniqueName() + "'");
		} catch (NotConnectedToWebServiceException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException(
					"Error retrieving the organization's roles for '" + organization.getUniqueName() + "'");
		} catch (IOException e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException(
					"Error retrieving the organization's roles for '" + organization.getUniqueName() + "'");
		} catch (AuthenticationRequired e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException(
					"Error retrieving the organization's roles for '" + organization.getUniqueName() + "'");
		} catch (WebServiceAccessError e) {
			LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
			throw new UserManagementException(
					"Error retrieving the organization's roles for '" + organization.getUniqueName() + "'");
		}
	}

	/**
	 * Get users from a role in an organization.
	 * 
	 * @param role
	 * @param organization
	 * @return
	 */
	@Override
	public Set<IUser<Long>> getUsers(IRole<Long> role, IGroup<Long> organization) throws UserManagementException {
		if (role != null && organization != null) {
			try {
				return roleService.getUsers(role, organization);
			} catch (ClientProtocolException e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException(
						"Error retrieving the user's for the role '" + role.getUniqueName() + "'.");
			} catch (IOException e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException(
						"Error retrieving the user's for the role '" + role.getUniqueName() + "'.");
			} catch (NotConnectedToWebServiceException e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException(
						"Error retrieving the user's for the role '" + role.getUniqueName() + "'.");
			} catch (AuthenticationRequired e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException(
						"Error retrieving the user's for the role '" + role.getUniqueName() + "'.");
			} catch (WebServiceAccessError e) {
				LiferayClientLogger.errorMessage(AuthorizationService.class.getName(), e);
				throw new UserManagementException(
						"Error retrieving the user's for the role '" + role.getUniqueName() + "'.");
			}
		}
		return null;
	}

	/**
	 * IUser<Long> is allowed to do an activity.
	 * 
	 * @param user
	 * @param activity
	 * @return
	 * @throws UserManagementException
	 */
	@Override
	public boolean isAuthorizedActivity(IUser<Long> user, IActivity activity) throws UserManagementException {
		if (user == null) {
			return false;
		}
		// Is it in the pool?
		Boolean authorized = authorizationPool.isAuthorizedActivity(user, activity);
		if (authorized != null) {
			return authorized;
		}

		authorized = getUserActivitiesAllowed(user).contains(activity);
		authorizationPool.addUser(user, activity, authorized);
		return authorized;
	}

	@Override
	public boolean isAuthorizedActivity(IUser<Long> user, IGroup<Long> organization, IActivity activity)
			throws UserManagementException {
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

			authorized = getUserActivitiesAllowed(user, organization).contains(activity);
			authorizationPool.addUser(user, organization, activity, authorized);
			return authorized;
		}
		return false;
	}

	@Override
	public void reset() {
		authorizationPool.reset();
		roleService.reset();
		userGroupService.reset();
		organizationService.reset();
		companyService.reset();
		userService.reset();
		groupService.reset();
	}

	@Override
	public Set<IActivity> getRoleActivities(IRole<Long> role) {
		return roleActivities.getRoleActivities(role);
	}

	public IRoleActivities getRoleActivities() {
		return roleActivities;
	}

	public void setRoleActivities(IRoleActivities roleActivities) {
		this.roleActivities = roleActivities;
	}

}
