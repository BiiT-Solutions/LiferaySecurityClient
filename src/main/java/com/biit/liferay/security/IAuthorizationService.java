package com.biit.liferay.security;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;

import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.PortletNotInstalledException;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.Site;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

public interface IAuthorizationService {

	List<User> getAllUsers();

	Organization getOrganization(long organizationId) throws IOException, AuthenticationRequired;

	Set<IActivity> getRoleActivities(Role role);

	Set<Role> getUserGroupRoles(UserGroup group) throws ClientProtocolException, IOException, AuthenticationRequired;

	Set<UserGroup> getUserGroups(User user) throws ClientProtocolException, IOException, AuthenticationRequired;

	Set<Organization> getUserOrganizations(User user) throws ClientProtocolException, IOException,
			AuthenticationRequired;

	Set<Organization> getUserOrganizations(User user, Site site) throws ClientProtocolException, IOException,
			AuthenticationRequired, PortletNotInstalledException;

	Set<Role> getUserRoles(User user) throws IOException, AuthenticationRequired;

	Set<Role> getUserRoles(User user, Organization organization) throws IOException, AuthenticationRequired;

	boolean isAuthorizedActivity(User user, IActivity activity) throws IOException, AuthenticationRequired;

	boolean isAuthorizedActivity(User user, Organization organization, IActivity activity) throws IOException,
			AuthenticationRequired;

	Role getRole(String roleName) throws IOException, AuthenticationRequired;

	Role getRole(long roleId) throws IOException, AuthenticationRequired;

}
