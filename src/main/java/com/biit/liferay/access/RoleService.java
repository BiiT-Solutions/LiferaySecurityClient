package com.biit.liferay.access;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.service.http.RoleServiceSoap;
import com.liferay.portal.service.http.RoleServiceSoapServiceLocator;

/**
 * This class allows to manage roles from Liferay portal.
 */
public class RoleService extends ServiceAccess {
	private final static String SERVICE_ROLE_NAME = "Portal_RoleService";
	private final static RoleService instance = new RoleService();

	private RoleService() {
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
	 * Get a list of roles where the user is included.
	 * 
	 * @param user
	 * @return
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public List<Role> getUserRoles(User user) throws RemoteException, NotConnectedToWebServiceException {
		checkConnection();
		List<Role> roles = new ArrayList<Role>();
		try {
			Role[] arrayOfRoles = ((RoleServiceSoap) getServiceSoap()).getUserRoles(user.getUserId());
			for (int i = 0; i < arrayOfRoles.length; i++) {
				roles.add(arrayOfRoles[i]);
			}
		} catch (RemoteException re) {

		}
		return roles;
	}

	/**
	 * Get a list of roles where the group is included.
	 * 
	 * @param group
	 * @return
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public List<Role> getGroupRoles(UserGroup group) throws RemoteException, NotConnectedToWebServiceException {
		checkConnection();
		List<Role> roles = new ArrayList<Role>();
		Role[] arrayOfRoles = ((RoleServiceSoap) getServiceSoap()).getGroupRoles(group.getUserGroupId());
		for (int i = 0; i < arrayOfRoles.length; i++) {
			roles.add(arrayOfRoles[i]);
		}
		return roles;
	}

	/**
	 * Creates a new role on Liferay.
	 * 
	 * @param name
	 *            name of the new role.
	 * @return
	 * @throws NotConnectedToWebServiceException
	 * @throws RemoteException
	 */
	public Role addRole(String name) throws NotConnectedToWebServiceException, RemoteException {
		checkConnection();
		return ((RoleServiceSoap) getServiceSoap()).addRole(name, new String[0], new String[0], new String[0],
				new String[0], 0);
	}

	/**
	 * Removes a role from Liferay portal.
	 * 
	 * @param role
	 * @throws NotConnectedToWebServiceException
	 * @throws RemoteException
	 */
	public void deleteRole(Role role) throws NotConnectedToWebServiceException, RemoteException {
		checkConnection();
		((RoleServiceSoap) getServiceSoap()).deleteRole(role.getRoleId());
	}

	/**
	 * Add a list of roles to a user.
	 * 
	 * @param user
	 * @param roles
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public void addUserRoles(User user, List<Role> roles) throws RemoteException, NotConnectedToWebServiceException {
		checkConnection();
		long rolesIds[] = new long[roles.size()];
		for (int i = 0; i < roles.size(); i++) {
			rolesIds[i] = roles.get(i).getRoleId();
		}
		((RoleServiceSoap) getServiceSoap()).addUserRoles(user.getUserId(), rolesIds);
	}

	/**
	 * Add a role to a user.
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
}
