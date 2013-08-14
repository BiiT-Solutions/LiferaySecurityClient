package com.biit.liferay.access;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.liferay.portal.model.UserGroupSoap;
import com.liferay.portal.model.UserSoap;
import com.liferay.portal.service.http.UserGroupServiceSoap;
import com.liferay.portal.service.http.UserGroupServiceSoapServiceLocator;

public class GroupService {
	private final static String SERVICE_GROUP_NAME = "Portal_UserGroupService";
	private UserGroupServiceSoap userGroupSoap;

	public GroupService(String loginUser, String password) throws ServiceException {
		// Locate the UserGroup service
		UserGroupServiceSoapServiceLocator locator = new UserGroupServiceSoapServiceLocator();
		userGroupSoap = locator.getPortal_UserGroupService(AccessUtils.getLiferayUrl(loginUser, password,
				SERVICE_GROUP_NAME));
	}

	/**
	 * Get group information using the group's name.
	 * 
	 * @param userGroupId
	 *            id of the group
	 * @return group information
	 * @throws RemoteException
	 *             if there is any communication problem.
	 */
	public UserGroupSoap getUserGroup(long userGroupId) throws RemoteException {
		return userGroupSoap.getUserGroup(userGroupId);
	}

	/**
	 * Get group information using the group's name.
	 * 
	 * @param name
	 *            name of the group
	 * @return group information
	 * @throws RemoteException
	 *             if there is any communication problem.
	 */
	public UserGroupSoap getUserGroup(String name) throws RemoteException {
		return userGroupSoap.getUserGroup(name);
	}

	/**
	 * Get a list of groups where the user belongs to.
	 * 
	 * @param user
	 * @return group information
	 * @throws RemoteException
	 *             if there is any communication problem.
	 */
	public List<UserGroupSoap> getUserUserGroups(UserSoap user) throws RemoteException {
		List<UserGroupSoap> groups = new ArrayList<UserGroupSoap>();
		UserGroupSoap[] arrayOfGroups = userGroupSoap.getUserUserGroups(user.getUserId());
		for (int i = 0; i < arrayOfGroups.length; i++) {
			groups.add(arrayOfGroups[i]);
		}
		return groups;
	}

}
