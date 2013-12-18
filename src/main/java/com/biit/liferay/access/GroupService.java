package com.biit.liferay.access;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.UserGroupDoesNotExistException;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.service.http.GroupServiceSoap;
import com.liferay.portal.service.http.GroupServiceSoapServiceLocator;
import com.liferay.portal.service.http.UserGroupServiceSoap;

public class GroupService extends ServiceAccess {
	private final static String SERVICE_GROUP_NAME = "Portal_GroupService";
	private final static GroupService instance = new GroupService();
	private GroupPool groupPool;

	private GroupService() {
		groupPool = new GroupPool();
	}

	public static GroupService getInstance() {
		return instance;
	}

	@Override
	public void connectToWebService(String loginUser, String password) throws ServiceException {
		// Locate the RoleSoap service
		GroupServiceSoapServiceLocator locatorRole = new GroupServiceSoapServiceLocator();
		setServiceSoap(locatorRole.getPortal_GroupService(AccessUtils.getLiferayUrl(loginUser, password,
				getServiceName())));
	}

	@Override
	public String getServiceName() {
		return SERVICE_GROUP_NAME;
	}

	/**
	 * Get group information using the group's primary key.
	 * 
	 * @param groupId
	 *            group's primary key.
	 * @return a group.
	 * @throws RemoteException
	 *             if there is any communication problem.
	 * @throws NotConnectedToWebServiceException
	 */
	public UserGroup getUserGroupById(long groupId) throws RemoteException, NotConnectedToWebServiceException,
			UserGroupDoesNotExistException {
		if (groupId >= 0) {
			// Look up UserSoap in the pool.
			UserGroup group = groupPool.getGroupById(groupId);
			if (group != null) {
				return group;
			}

			// Read from Liferay.
			checkConnection();
			try {
				group = ((UserGroupServiceSoap) getServiceSoap()).getUserGroup(groupId);
				groupPool.addGroup(group);
				return group;
			} catch (RemoteException re) {
				if (re.getLocalizedMessage().contains("No Group exists with the primary key")) {
					throw new UserGroupDoesNotExistException("Group with id '" + groupId + "' does not exists.");
				} else {
					re.printStackTrace();
					throw re;
				}
			}
		}
		return null;
	}

	/**
	 * Add a RoleSoap to a list of groups. For testing only.
	 * 
	 * @param RoleSoap
	 * @param userGroups
	 * @throws NotConnectedToWebServiceException
	 * @throws RemoteException
	 */
	public void addRoleGroups(Role RoleSoap, List<UserGroup> userGroups) throws NotConnectedToWebServiceException,
			RemoteException {
		if (userGroups != null && RoleSoap != null && userGroups.size() > 0) {
			checkConnection();
			long userGroupsIds[] = new long[userGroups.size()];
			for (int i = 0; i < userGroups.size(); i++) {
				userGroupsIds[i] = userGroups.get(i).getUserGroupId();
			}
			((GroupServiceSoap) getServiceSoap()).addRoleGroups(RoleSoap.getRoleId(), userGroupsIds);
		}
	}

	/**
	 * Add a RoleSoap to a group. For testing only.
	 * 
	 * @param RoleSoap
	 * @param UserGroupSoap
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public void addRoleGroup(Role RoleSoap, UserGroup UserGroupSoap) throws RemoteException, NotConnectedToWebServiceException {
		List<UserGroup> groups = new ArrayList<UserGroup>();
		groups.add(UserGroupSoap);
		addRoleGroups(RoleSoap, groups);
	}
}
