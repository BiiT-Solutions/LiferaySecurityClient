package com.biit.liferay.access;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.log.LiferayClientLogger;
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
		// Locate the RoleSoap service
		RoleServiceSoapServiceLocator locatorRole = new RoleServiceSoapServiceLocator();
		setServiceSoap(locatorRole.getPortal_RoleService(AccessUtils.getLiferayUrl(loginUser, password,
				getServiceName())));
	}

	/**
	 * Get the list of roles for a UserSoap.
	 * 
	 * @param UserSoap
	 * @return
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public List<Role> getUserRoles(User UserSoap) throws RemoteException, NotConnectedToWebServiceException {
		List<Role> roles = new ArrayList<Role>();
		if (UserSoap != null) {
			List<Role> userRoles = rolePool.getUserRoles(UserSoap);
			if (userRoles != null) {
				return userRoles;
			}
			checkConnection();
			Role[] arrayOfRoles = ((RoleServiceSoap) getServiceSoap()).getUserRoles(UserSoap.getUserId());
			for (int i = 0; i < arrayOfRoles.length; i++) {
				roles.add(arrayOfRoles[i]);
			}
			rolePool.addUserRoles(UserSoap, roles);
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
	 * Creates a new RoleSoap on Liferay. For testing use only.
	 * 
	 * @param name
	 *            name of the new RoleSoap.
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
	 * Removes a RoleSoap from Liferay portal. For testing use only.
	 * 
	 * @param RoleSoap
	 * @throws NotConnectedToWebServiceException
	 * @throws RemoteException
	 */
	public void deleteRole(Role RoleSoap) throws NotConnectedToWebServiceException, RemoteException {
		if (RoleSoap != null) {
			checkConnection();
			((RoleServiceSoap) getServiceSoap()).deleteRole(RoleSoap.getRoleId());
			rolePool.removeRole(RoleSoap);
		}
	}

	/**
	 * Add a list of roles to a UserSoap. For testing use only.
	 * 
	 * @param UserSoap
	 * @param roles
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public void addUserRoles(User UserSoap, List<Role> roles) throws RemoteException, NotConnectedToWebServiceException {
		if (UserSoap != null && roles != null && roles.size() > 0) {
			checkConnection();
			long rolesIds[] = new long[roles.size()];
			for (int i = 0; i < roles.size(); i++) {
				rolesIds[i] = roles.get(i).getRoleId();
			}
			((RoleServiceSoap) getServiceSoap()).addUserRoles(UserSoap.getUserId(), rolesIds);
			rolePool.addUserRoles(UserSoap, roles);
		}
	}

	/**
	 * Add a RoleSoap to a UserSoap. For testing use only.
	 * 
	 * @param UserSoap
	 * @param RoleSoap
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public void addUserRole(User UserSoap, Role RoleSoap) throws RemoteException, NotConnectedToWebServiceException {
		List<Role> roles = new ArrayList<Role>();
		roles.add(RoleSoap);
		addUserRoles(UserSoap, roles);
	}

	/**
	 * Removes the RoleSoap from the UserSoap. For testing use only.
	 * 
	 * @param RoleSoap
	 * @param UserSoap
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public void removeUserRole(User UserSoap, Role RoleSoap) throws RemoteException, NotConnectedToWebServiceException {
		checkConnection();
		((UserServiceSoap) getServiceSoap()).deleteRoleUser(RoleSoap.getRoleId(), UserSoap.getUserId());
		rolePool.removeUserRole(UserSoap, RoleSoap);
		LiferayClientLogger.info(this.getClass().getName(), "Role '" + RoleSoap.getName() + "' for User '"
				+ UserSoap.getScreenName() + "' deleted.");
	}
}
