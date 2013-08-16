package com.biit.liferay.security;

import java.util.ArrayList;
import java.util.List;

import com.liferay.portal.model.UserGroupRole;
import com.liferay.portal.model.User;

public abstract class AuthorizationService {

	public void authorizeActivity(String activity) {
		User currentUser = getCurrentUser();

		if (getUserActivities(currentUser).contains(activity)) {
			// Authorized
		} else {
			// Unauthorized
		}
	}

	public List<String> getUserActivities(User currentUser) {
		List<UserGroupRole> roles = getUserRoles(currentUser);
		List<String> activities = new ArrayList<String>();

		for (UserGroupRole role : roles) {
			List<String> roleActivities = getRoleActivities(role);
			activities.addAll(roleActivities);
		}
		return activities;
	}

	public User getCurrentUser() {
		return null;
	}

	public List<UserGroupRole> getUserRoles(User user) {
		return null;
	}

	public List<String> getRoleActivities(UserGroupRole role) {
		return null;
	}
}
