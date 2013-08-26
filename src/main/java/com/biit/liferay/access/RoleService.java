package com.biit.liferay.access;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.log.LiferayAuthenticationClientLogger;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.service.http.RoleServiceSoap;
import com.liferay.portal.service.http.RoleServiceSoapServiceLocator;
import com.liferay.portal.service.http.UserServiceSoap;

/**
 * This class allows to manage roles from Liferay portal.
 */
public class RoleService extends ServiceAccess {
	private final static String SERVICE_ROLE_NAME = "Portal_RoleService";
	private final static RoleService instance = new RoleService();
	private RolesPool rolePool;

	private RoleService() {
		rolePool = new RolesPool();
	}

	public static RoleService getInstance() {
		return instance;
	}

	public String getServiceName() {
		return SERVICE_ROLE_NAME;
	}

	@Override
	public void connectToWebService(String loginUser, String password) throws ServiceException {
		// Locate the Role service
		RoleServiceSoapServiceLocator locatorRole = new RoleServiceSoapServiceLocator();
		setServiceSoap(locatorRole.getPortal_RoleService(AccessUtils.getLiferayUrl(loginUser, password,
				getServiceName())));
	}

	/**
	 * Get the list of roles for a user.
	 * 
	 * @param user
	 * @return
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public List<Role> getUserRoles(User user) throws RemoteException, NotConnectedToWebServiceException {
		List<Role> roles = new ArrayList<Role>();
		if (user != null) {
			List<Role> userRoles = rolePool.getUserRoles(user);
			if (userRoles != null) {
				return userRoles;
			}
			checkConnection();
			Role[] arrayOfRoles = ((RoleServiceSoap) getServiceSoap()).getUserRoles(user.getUserId());
			for (int i = 0; i < arrayOfRoles.length; i++) {
				roles.add(arrayOfRoles[i]);
			}
			rolePool.addUserRoles(user, roles);
		}
		return roles;
	}

	/**
	 * Get a list of roles for a group.
	 * 
	 * @param group
	 * @return
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public List<Role> getGroupRoles(UserGroup group) throws RemoteException, NotConnectedToWebServiceException {
		List<Role> roles = new ArrayList<Role>();
		if (group != null) {
			List<Role> groupRoles = rolePool.getGroupRoles(group);
			if (groupRoles != null) {
				return groupRoles;
			}
			checkConnection();
			if (group != null) {
				Role[] arrayOfRoles = ((RoleServiceSoap) getServiceSoap()).getGroupRoles(group.getUserGroupId());
				for (int i = 0; i < arrayOfRoles.length; i++) {
					roles.add(arrayOfRoles[i]);
				}
				rolePool.addUserGroupRoles(group, roles);
			}
		}
		return roles;
	}

	/**
	 * Creates a new role on Liferay. For testing use only.
	 * 
	 * @param name
	 *            name of the new role.
	 * @return
	 * @throws NotConnectedToWebServiceException
	 * @throws RemoteException
	 */
	public Role addRole(String name) throws NotConnectedToWebServiceException, RemoteException {
		if (name != null && name.length() > 0) {
			checkConnection();
			return ((RoleServiceSoap) getServiceSoap()).addRole(name, new String[0], new String[0], new String[0],
					new String[0], 1);
		}
		return null;
	}

	/**
	 * Removes a role from Liferay portal. For testing use only.
	 * 
	 * @param role
	 * @throws NotConnectedToWebServiceException
	 * @throws RemoteException
	 */
	public void deleteRole(Role role) throws NotConnectedToWebServiceException, RemoteException {
		if (role != null) {
			checkConnection();
			((RoleServiceSoap) getServiceSoap()).deleteRole(role.getRoleId());
			rolePool.removeRole(role);
		}
	}

	/**
	 * Add a list of roles to a user. For testing use only.
	 * 
	 * @param user
	 * @param roles
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public void addUserRoles(User user, List<Role> roles) throws RemoteException, NotConnectedToWebServiceException {
		if (user != null && roles != null && roles.size() > 0) {
			checkConnection();
			long rolesIds[] = new long[roles.size()];
			for (int i = 0; i < roles.size(); i++) {
				rolesIds[i] = roles.get(i).getRoleId();
			}
			((RoleServiceSoap) getServiceSoap()).addUserRoles(user.getUserId(), rolesIds);
			rolePool.addUserRoles(user, roles);
		}
	}

	/**
	 * Add a role to a user. For testing use only.
	 * 
	 * @param user
	 * @param role
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public void addUserRole(User user, Role role) throws RemoteException, NotConnectedToWebServiceException {
		List<Role> roles = new ArrayList<Role>();
		roles.add(role);
		addUserRoles(user, roles);
	}

	/**
	 * Removes the role from the user. For testing use only.
	 * 
	 * @param role
	 * @param user
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public void removeUserRole(User user, Role role) throws RemoteException, NotConnectedToWebServiceException {
		checkConnection();
		((UserServiceSoap) getServiceSoap()).deleteRoleUser(role.getRoleId(), user.getUserId());
		rolePool.removeUserRole(user, role);
		LiferayAuthenticationClientLogger.info(this.getClass().getName(), "Role '" + role.getName() + "' for user '"
				+ user.getScreenName() + "' deleted.");
	}
}
