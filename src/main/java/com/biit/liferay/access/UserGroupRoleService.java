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
		// Locate the RoleSoap service
		UserGroupRoleServiceSoapServiceLocator locatorRole = new UserGroupRoleServiceSoapServiceLocator();
		setServiceSoap(locatorRole.getPortal_UserGroupRoleService(AccessUtils.getLiferayUrl(loginUser, password,
				getServiceName())));
	}

	@Override
	public String getServiceName() {
		return SERVICE_USERGROUP_ROLE_NAME;
	}

	/**
	 * Add a list of roles to a UserSoap. For testing use only.
	 * 
	 * @param UserSoap
	 * @param roles
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public void addUserGroupRoles(User UserSoap, UserGroup UserGroupSoap, List<Role> roles) throws RemoteException,
			NotConnectedToWebServiceException {
		if (UserGroupSoap != null && roles != null && roles.size() > 0) {
			checkConnection();
			long rolesIds[] = new long[roles.size()];
			for (int i = 0; i < roles.size(); i++) {
				rolesIds[i] = roles.get(i).getRoleId();
			}
			((UserGroupRoleServiceSoap) getServiceSoap()).addUserGroupRoles(UserSoap.getUserId(),
					UserGroupSoap.getUserGroupId(), rolesIds);
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
	public void addUserGroupRole(User UserSoap, UserGroup UserGroupSoap, Role RoleSoap) throws RemoteException,
			NotConnectedToWebServiceException {
		List<Role> roles = new ArrayList<Role>();
		roles.add(RoleSoap);
		addUserGroupRoles(UserSoap, UserGroupSoap, roles);
	}

}
