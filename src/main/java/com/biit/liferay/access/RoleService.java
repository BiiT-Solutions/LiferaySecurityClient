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
		Role[] arrayOfRoles = ((RoleServiceSoap) getServiceSoap()).getUserRoles(user.getUserId());
		for (int i = 0; i < arrayOfRoles.length; i++) {
			roles.add(arrayOfRoles[i]);
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
}
