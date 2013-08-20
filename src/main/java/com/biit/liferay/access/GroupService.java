package com.biit.liferay.access;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.service.http.RoleServiceSoapServiceLocator;
import com.liferay.portal.service.http.UserGroupServiceSoap;

/**
 * This class allows to manage group from Liferay portal.
 */
public class GroupService extends ServiceAccess {
	private final static String SERVICE_GROUP_NAME = "Portal_UserGroupService";
	private UserGroupServiceSoap userGroupSoap;
	private final static GroupService instance = new GroupService();

	private GroupService() {
	}

	public static GroupService getInstance() {
		return instance;
	}

	public String getServiceName() {
		return SERVICE_GROUP_NAME;
	}

	@Override
	public void connectToWebService(String loginUser, String password) throws ServiceException {
		// Locate the Role service
		RoleServiceSoapServiceLocator locatorRole = new RoleServiceSoapServiceLocator();
		setServiceSoap(locatorRole.getPortal_RoleService(AccessUtils.getLiferayUrl(loginUser, password,
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
	 * @throws NotConnectedToWebServiceException
	 */
	public UserGroup getUserGroup(String name) throws RemoteException, NotConnectedToWebServiceException {
		checkConnection();
		return userGroupSoap.getUserGroup(name);
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
		checkConnection();
		List<UserGroup> groups = new ArrayList<UserGroup>();
		UserGroup[] arrayOfGroups = userGroupSoap.getUserUserGroups(user.getUserId());
		for (int i = 0; i < arrayOfGroups.length; i++) {
			groups.add(arrayOfGroups[i]);
		}
		return groups;
	}

}
