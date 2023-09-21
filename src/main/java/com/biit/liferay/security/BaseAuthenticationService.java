package com.biit.liferay.security;

import com.biit.liferay.log.LiferayClientLogger;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.IActivity;
import com.biit.usermanager.security.IAuthenticationService;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.liferay.portal.model.Role;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Common methods for Liferay based authentication services.
 */
public class BaseAuthenticationService extends AuthorizationService {

    @Inject
    private IAuthenticationService<Long, Long> authenticationService;

    protected BaseAuthenticationService() {
        super();
    }

    protected BaseAuthenticationService(IAuthenticationService<Long, Long> authenticationService) {
        super();
        this.authenticationService = authenticationService;
    }

    public IAuthenticationService<Long, Long> getAuthenticationService() {
        return authenticationService;
    }

    public IGroup<Long> getDefaultGroup(IUser<Long> user) {
        try {
            return authenticationService.getDefaultGroup(user);
        } catch (UserManagementException | UserDoesNotExistException | InvalidCredentialsException e) {
            LiferayClientLogger.errorMessage(this.getClass().getName(), e);
        }
        return null;
    }

    public Set<IActivity> getActivitiesOfRoles(List<Role> roles) {
        final Set<IActivity> activities = new HashSet<>();
        for (Role role : roles) {
            activities.addAll(getRoleActivities(role));
        }
        return activities;
    }

    public boolean isUserAuthorizedInAnyOrganization(IUser<Long> user, IActivity activity) throws UserManagementException {

        // Check isUserAuthorizedActivity (own permissions)
        if (isAuthorizedActivity(user, activity)) {
            return true;
        }
        // Get all organizations of user
        final Set<IGroup<Long>> organizations = getUserOrganizations(user);
        for (IGroup<Long> organization : organizations) {
            if (isAuthorizedActivity(user, organization, activity)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAuthorizedActivity(IUser<Long> user, Long organizationId, IActivity activity) {
        if (organizationId == null) {
            return false;
        }
        final IGroup<Long> organization = getOrganization(user, organizationId);
        if (organization == null) {
            return false;
        }
        try {
            return isAuthorizedActivity(user, organization, activity);
        } catch (UserManagementException e) {
            LiferayClientLogger.errorMessage(this.getClass().getName(), e);
            // For security
            return false;
        }
    }

    private IGroup<Long> getOrganization(IUser<Long> user, Long organizationId) {
        try {
            final Set<IGroup<Long>> organizations = getUserOrganizations(user);
            for (IGroup<Long> organization : organizations) {
                if (organization.getUniqueId().equals(organizationId)) {
                    return organization;
                }
            }
        } catch (UserManagementException e) {
            LiferayClientLogger.errorMessage(this.getClass().getName(), e);
        }

        return null;
    }

    public Set<IGroup<Long>> getUserOrganizationsWhereIsAuthorized(IUser<Long> user, IActivity activity) {
        Set<IGroup<Long>> organizations = new HashSet<>();
        try {
            organizations = getUserOrganizations(user);
            final Iterator<IGroup<Long>> itr = organizations.iterator();
            while (itr.hasNext()) {
                final IGroup<Long> organization = itr.next();
                if (!isAuthorizedActivity(user, organization, activity)) {
                    itr.remove();
                }
            }
        } catch (UserManagementException e) {
            LiferayClientLogger.errorMessage(this.getClass().getName(), e);
        }
        return organizations;
    }

}
