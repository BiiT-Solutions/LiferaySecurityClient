package com.biit.liferay.security;

/*-
 * #%L
 * Liferay Authentication Client with Web Services
 * %%
 * Copyright (C) 2013 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.liferay.access.CompanyService;
import com.biit.liferay.access.ContactService;
import com.biit.liferay.access.ICompanyService;
import com.biit.liferay.access.IContactService;
import com.biit.liferay.access.IPasswordService;
import com.biit.liferay.access.IUserGroupService;
import com.biit.liferay.access.IUserService;
import com.biit.liferay.access.PasswordService;
import com.biit.liferay.access.UserGroupService;
import com.biit.liferay.access.UserService;
import com.biit.liferay.access.VerificationService;
import com.biit.liferay.access.exceptions.DuplicatedLiferayElement;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.configuration.LiferayConfigurationReader;
import com.biit.liferay.log.LiferayClientLogger;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.IAuthenticationService;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.liferay.portal.log.SecurityLogger;
import com.liferay.portal.model.User;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Set;

/**
 * Implements Liferay service access for authentication.
 */
@Named
public class AuthenticationService implements IAuthenticationService<Long, Long> {
    private static final int DEFAULT_YEAR = 1970;
    private IGroup<Long> company = null;

    @Inject
    private IUserService userService;

    @Inject
    private ICompanyService companyService;

    @Inject
    private IUserGroupService userGroupService;

    @Inject
    private IContactService contactService;

    @Inject
    private IPasswordService passwordService;

    @PostConstruct
    public void connect() {
        userService.serverConnection();
        companyService.serverConnection();
        userGroupService.serverConnection();
        contactService.serverConnection();
        passwordService.serverConnection();
    }

    /**
     * If Spring is not available, create the required services. Be carefull, if
     * created on this way, each service uses its own pool and is not shared with
     * other different beans.
     */
    @Override
    public void createBeans() {
        userService = new UserService();
        companyService = new CompanyService();
        userGroupService = new UserGroupService();
        contactService = new ContactService();
        passwordService = new PasswordService();

        connect();
    }

    /**
     * Obtains a liferay user by the mail and the password.
     *
     * @param userMail
     * @param password
     * @return
     */
    @Override
    public IUser<Long> authenticate(String userMail, String password) throws UserManagementException,
            AuthenticationRequired, InvalidCredentialsException, UserDoesNotExistException {
        // Login fails if either the username or password is null
        if (userMail == null || password == null) {
            throw new InvalidCredentialsException("No fields filled up.");
        }

        // Check password.
        try {
            try {
                VerificationService.getInstance().testConnection(getCompany(), userMail, password);
            } catch (AuthenticationRequired ar) {
                // The error is with the user or password.
                SecurityLogger.info(this.getClass().getName(), "Invalid password '" + password + "' for user '" + userMail + "'.");
                throw new InvalidCredentialsException("Invalid password '" + password + "' for user '" + userMail + "'.");
            }
            // Get user information.
            final IUser<Long> user;
            user = userService.getUserByEmailAddress(getCompany(), userMail);
            if (user == null) {
                SecurityLogger.debug(this.getClass().getName(), "Invalid password '" + password + "' for user '" + userMail + "'.");
                throw new InvalidCredentialsException("Invalid password for user '" + userMail + "' on company '" + getCompany() + "'.");
            }
            SecurityLogger.info(this.getClass().getName(), "Access granted to user '" + userMail + "'.");
            return user;
        } catch (NotConnectedToWebServiceException | WebServiceAccessError | IOException e) {
            LiferayClientLogger.errorMessage(this.getClass().getName(), e);
            throw new UserManagementException(
                    "Error connecting to Liferay service with '" + LiferayConfigurationReader.getInstance().getUser()
                            + " at " + LiferayConfigurationReader.getInstance().getHost() + ":"
                            + LiferayConfigurationReader.getInstance().getConnectionPort()
                            + "'. Check configuration at 'liferay.conf' file.");
        }
    }

    private IGroup<Long> getCompany() throws NotConnectedToWebServiceException {
        try {
            if (company == null) {
                company = companyService
                        .getCompanyByVirtualHost(LiferayConfigurationReader.getInstance().getVirtualHost());
                SecurityLogger.debug(this.getClass().getName(), "Company found '" + company + "' for '"
                        + LiferayConfigurationReader.getInstance().getVirtualHost() + "'.");
            }
            return company;
        } catch (Exception ar) {
            LiferayClientLogger.errorMessage(this.getClass().getName(), ar);
            throw new NotConnectedToWebServiceException(
                    "Error connecting to Liferay service with '" + LiferayConfigurationReader.getInstance().getUser()
                            + " at " + LiferayConfigurationReader.getInstance().getHost() + ":"
                            + LiferayConfigurationReader.getInstance().getConnectionPort()
                            + "'.\n Check configuration at 'liferay.conf' file.");
        }
    }

    /**
     * Get first group of user.
     *
     * @param user
     * @return
     * @throws UserManagementException
     */
    @Override
    public IGroup<Long> getDefaultGroup(IUser<Long> user) throws UserManagementException {
        if (user != null) {
            final Set<IGroup<Long>> userGroups;
            try {
                userGroups = userGroupService.getUserUserGroups(user);
            } catch (NotConnectedToWebServiceException | IOException | AuthenticationRequired e) {
                LiferayClientLogger.errorMessage(this.getClass().getName(), e);
                throw new UserManagementException(e.getMessage());
            }
            if (userGroups != null && userGroups.size() > 0) {
                return userGroups.iterator().next();
            }
        }
        return null;
    }

    @Override
    public IUser<Long> getUserByEmail(String userEmail) throws UserManagementException, UserDoesNotExistException {
        try {
            return userService.getUserByEmailAddress(getCompany(), userEmail);
        } catch (NotConnectedToWebServiceException | AuthenticationRequired | WebServiceAccessError | IOException e) {
            LiferayClientLogger.errorMessage(this.getClass().getName(), e);
            throw new UserManagementException(e.getMessage());
        }
    }

    @Override
    public IUser<Long> getUserById(long userId) throws UserManagementException, UserDoesNotExistException {
        try {
            return userService.getUserById(userId);
        } catch (NotConnectedToWebServiceException | IOException | AuthenticationRequired
                 | WebServiceAccessError e) {
            LiferayClientLogger.errorMessage(this.getClass().getName(), e);
            throw new UserManagementException(e.getMessage());
        }
    }

    @Override
    public boolean isInGroup(IGroup<Long> group, IUser<Long> user) throws UserManagementException {
        if (group != null && user != null) {
            final Set<IGroup<Long>> userGroups;
            try {
                userGroups = userGroupService.getUserUserGroups(user);
            } catch (NotConnectedToWebServiceException | AuthenticationRequired | IOException e) {
                LiferayClientLogger.errorMessage(this.getClass().getName(), e);
                throw new UserManagementException(e.getMessage());
            }
            for (final IGroup<Long> userGroupSoap : userGroups) {
                if (userGroupSoap.getUniqueId().equals(group.getUniqueId())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void reset() {
        userGroupService.reset();
        companyService.reset();
        userService.reset();
        passwordService.reset();
        contactService.reset();
    }

    @Override
    public IUser<Long> updatePassword(IUser<Long> user, String plainTextPassword) throws UserManagementException, InvalidCredentialsException {
        try {
            return passwordService.updatePassword(user, plainTextPassword);
        } catch (JsonParseException e) {
            LiferayClientLogger.errorMessage(this.getClass().getName(), e);
            throw new UserManagementException(e.getMessage());
        } catch (JsonMappingException e) {
            LiferayClientLogger.errorMessage(this.getClass().getName(), e);
            throw new UserManagementException(e.getMessage());
        } catch (NotConnectedToWebServiceException | IOException | WebServiceAccessError e) {
            LiferayClientLogger.errorMessage(this.getClass().getName(), e);
            throw new UserManagementException(e.getMessage());
        } catch (AuthenticationRequired e) {
            throw new InvalidCredentialsException("Invalid credentials!");
        }
    }

    @Override
    public IUser<Long> updateUser(IUser<Long> user) throws UserManagementException {
        if (user instanceof User) {
            try {
                return userService.updateUser((User) user);
            } catch (NotConnectedToWebServiceException | IOException | AuthenticationRequired
                     | WebServiceAccessError e) {
                LiferayClientLogger.errorMessage(this.getClass().getName(), e);
                throw new UserManagementException(e.getMessage());
            }
        }
        LiferayClientLogger.error(this.getClass().getName(), "Invalid user " + user);
        return null;
    }

    @Override
    public IUser<Long> addUser(IGroup<Long> company, String password, String screenName, String emailAddress,
                               String locale, String firstName, String middleName, String lastName) throws UserManagementException {

        try {
            return userService.addUser(company, password, screenName, emailAddress, 0L, "", locale, firstName,
                    middleName, lastName, 0, 0, true, 1, 1, DEFAULT_YEAR, "", new long[0], new long[0], new long[0],
                    new long[0], false);

        } catch (NotConnectedToWebServiceException | IOException | AuthenticationRequired | WebServiceAccessError
                 | DuplicatedLiferayElement e) {
            LiferayClientLogger.errorMessage(this.getClass().getName(), e);
            throw new UserManagementException("User '" + emailAddress + "' not added correctly.");
        }

    }


    @Override
    public IUser<Long> addUser(IUser<Long> user) throws UserManagementException, InvalidCredentialsException {
        if (user != null) {
            try {
                return userService.addUser(null, user.getPassword(), user.getUniqueName(), user.getEmailAddress(),
                        0L, "", user.getLocale().toString(), user.getFirstName(),
                        null, user.getLastName(), 0, 0, true, 1, 1, DEFAULT_YEAR, "", new long[0], new long[0], new long[0],
                        new long[0], false);
            } catch (NotConnectedToWebServiceException | IOException | AuthenticationRequired | WebServiceAccessError
                     | DuplicatedLiferayElement e) {
                LiferayClientLogger.errorMessage(this.getClass().getName(), e);
                throw new UserManagementException("User '" + user.getEmailAddress() + "' not added correctly.");
            }
        }
        return null;
    }

    @Override
    public void deleteUser(IUser<Long> user) throws UserManagementException {
        try {
            userService.deleteUser(user);
            SecurityLogger.info(this.getClass().getName(), "User  '" + user + "' deleted.");
        } catch (NotConnectedToWebServiceException | IOException | AuthenticationRequired e) {
            LiferayClientLogger.errorMessage(this.getClass().getName(), e);
            throw new UserManagementException("User '" + user.getEmailAddress() + "' not removed correctly.");
        }
    }
}
