package com.biit.liferay.access;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.service.http.UserGroupServiceSoap;
import com.liferay.portal.service.http.UserGroupServiceSoapServiceLocator;

/**
 * This class allows to manage group from Liferay portal.
 */
public class UserGroupService extends ServiceAccess {
	private final static String SERVICE_GROUP_NAME = "Portal_UserGroupService";
	private final static UserGroupService instance = new UserGroupService();
	private UserGroupsPool groupPool;

	private UserGroupService() {
		groupPool = new UserGroupsPool();
	}

	public static UserGroupService getInstance() {
		return instance;
	}

	public String getServiceName() {
		return SERVICE_GROUP_NAME;
	}

	@Override
	public void connectToWebService(String loginUser, String password) throws ServiceException {
		// Locate the Role service
		System.out.println(AccessUtils.getLiferayUrl(loginUser, password, getServiceName()));
		UserGroupServiceSoapServiceLocator locatorGroup = new UserGroupServiceSoapServiceLocator();
		setServiceSoap(locatorGroup.getPortal_UserGroupService(AccessUtils.getLiferayUrl(loginUser, password,
				getServiceName())));
	}

	/**
	 * Get group information using the group's name.
	 * 
	 * @param userGroupId
	 *            id of the group
	 * @return group information
	 * @throws RemoteException
	 *             if there is any communication problem.
	 * @throws NotConnectedToWebServiceException
	 */
	public UserGroup getUserGroup(long userGroupId) throws RemoteException, NotConnectedToWebServiceException {
		checkConnection();
		return ((UserGroupServiceSoap) getServiceSoap()).getUserGroup(userGroupId);
	}

	/**
	 * Get group information using the group's name.
	 * 
	 * @param name
	 *            name of the group
	 * @return group information
	 * @throws RemoteException
	 *             if there is any communication problem.
	 * @throws NotConnectedToWebServiceException
	 */
	public UserGroup getUserGroup(String name) throws RemoteException, NotConnectedToWebServiceException {
		if (name != null && name.length() > 0) {
			checkConnection();
			return ((UserGroupServiceSoap) getServiceSoap()).getUserGroup(name);
		}
		return null;
	}

	/**
	 * Creates a new group on Liferay. For testing use only.
	 * 
	 * @param name
	 *            name of the new group.
	 * @param description
	 *            description of the new group.
	 * @return
	 * @throws NotConnectedToWebServiceException
	 * @throws RemoteException
	 */
	public UserGroup addGroup(String name, String description) throws NotConnectedToWebServiceException,
			RemoteException {
		if (name != null && name.length() > 0) {
			checkConnection();
			return ((UserGroupServiceSoap) getServiceSoap()).addUserGroup(name, description);
		}
		return null;
	}

	/**
	 * Get a list of groups where the user belongs to.
	 * 
	 * @param user
	 * @return group information
	 * @throws RemoteException
	 *             if there is any communication problem.
	 * @throws NotConnectedToWebServiceException
	 */
	public List<UserGroup> getUserUserGroups(User user) throws RemoteException, NotConnectedToWebServiceException {
		List<UserGroup> groups = new ArrayList<UserGroup>();

		// Look up user in the pool.
		if (user != null) {
			List<UserGroup> usergroups = groupPool.getGroupByUser(user);
			if (usergroups != null) {
				return usergroups;
			}
			checkConnection();
			UserGroup[] arrayOfGroups = ((UserGroupServiceSoap) getServiceSoap()).getUserUserGroups(user.getUserId());
			for (int i = 0; i < arrayOfGroups.length; i++) {
				groups.add(arrayOfGroups[i]);
			}
			groupPool.addUserGroups(user, groups);
		}
		return groups;
	}
	
	/**
	 * Removes a group from Liferay portal. For testing use only.
	 * 
	 * @param group
	 * @throws NotConnectedToWebServiceException
	 * @throws RemoteException
	 */
	public void deleteGroup(UserGroup group) throws NotConnectedToWebServiceException, RemoteException {
		if (group != null) {
			checkConnection();
			((UserGroupServiceSoap) getServiceSoap()).deleteUserGroup(group.getUserGroupId());
			groupPool.removeUserGroup(group);
		}
	}

}
