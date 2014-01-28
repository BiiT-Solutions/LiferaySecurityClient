package com.biit.liferay.security;

import java.util.List;

import com.liferay.portal.model.Organization;

public class ActivitiesPerOrganization {
	private List<IActivity> activities;
	private Organization organization;

	public ActivitiesPerOrganization(List<IActivity> activities, Organization organization) {
		this.activities = activities;
		this.organization = organization;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activities == null) ? 0 : activities.hashCode());
		result = prime * result + ((organization == null) ? 0 : organization.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActivitiesPerOrganization other = (ActivitiesPerOrganization) obj;
		if (activities == null) {
			if (other.activities != null)
				return false;
		} else if (!activities.equals(other.activities))
			return false;
		if (organization == null) {
			if (other.organization != null)
				return false;
		} else if (!organization.equals(other.organization))
			return false;
		return true;
	}
}
