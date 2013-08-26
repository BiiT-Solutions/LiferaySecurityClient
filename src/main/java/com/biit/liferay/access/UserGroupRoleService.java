package com.biit.liferay.access;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.service.http.UserGroupRoleServiceSoap;
import com.liferay.portal.service.http.UserGroupRoleServiceSoapServiceLocator;

public class UserGroupRoleService extends ServiceAccess {
	private final static String SERVICE_USERGROUP_ROLE_NAME = "Portal_UserGroupRoleService";
	private final static UserGroupRoleService instance = new UserGroupRoleService();

	private UserGroupRoleService() {

	}

	public static UserGroupRoleService getInstance() {
		return instance;
	}

	@Override
	public void connectToWebService(String loginUser, String password) throws ServiceException {
		// Locate the Role service
		UserGroupRoleServiceSoapServiceLocator locatorRole = new UserGroupRoleServiceSoapServiceLocator();
		setServiceSoap(locatorRole.getPortal_UserGroupRoleService(AccessUtils.getLiferayUrl(loginUser, password,
				getServiceName())));
	}

	@Override
	public String getServiceName() {
		return SERVICE_USERGROUP_ROLE_NAME;
	}

	/**
	 * Add a list of roles to a user. For testing use only.
	 * 
	 * @param user
	 * @param roles
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public void addUserGroupRoles(User user, UserGroup usergroup, List<Role> roles) throws RemoteException,
			NotConnectedToWebServiceException {
		if (usergroup != null && roles != null && roles.size() > 0) {
			checkConnection();
			Long rolesIds[] = new Long[roles.size()];
			for (int i = 0; i < roles.size(); i++) {
				rolesIds[i] = roles.get(i).getRoleId();
			}
			((UserGroupRoleServiceSoap) getServiceSoap()).addUserGroupRoles(user.getUserId(),
					usergroup.getUserGroupId(), rolesIds);
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
	public void addUserGroupRole(User user, UserGroup usergroup, Role role) throws RemoteException,
			NotConnectedToWebServiceException {
		List<Role> roles = new ArrayList<Role>();
		roles.add(role);
		addUserGroupRoles(user, usergroup, roles);
	}

}
