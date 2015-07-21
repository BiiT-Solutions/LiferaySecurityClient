package com.biit.liferay.security;

import java.util.Set;

import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.UserManagementException;

public interface IAuthorizationService extends com.biit.usermanager.security.IAuthorizationService<Long, Long, Long> {

	Set<IGroup<Long>> getUserOrganizations(IUser<Long> user, IGroup<Long> site) throws UserManagementException;

}
