package com.biit.liferay.access;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.liferay.portal.model.Company;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;

public class OrganizationPool {

	private final static long EXPIRATION_TIME = 300000;// 5 minutes

	private Hashtable<Long, Long> time; // Company id -> time.
	private Hashtable<Long, List<Organization>> organizationsByCompany; // Company id -> Organizations.

	private Hashtable<Long, Long> userTime; // user id -> time.
	private Hashtable<Long, List<Group>> organizationsGroupByUser; // Group by user.

	private static OrganizationPool instance = new OrganizationPool();

	public static OrganizationPool getInstance() {
		return instance;
	}

	private OrganizationPool() {
		time = new Hashtable<Long, Long>();
		organizationsByCompany = new Hashtable<Long, List<Organization>>();
		userTime = new Hashtable<Long, Long>();
		organizationsGroupByUser = new Hashtable<Long, List<Group>>();
	}

	public void addOrganization(Company company, Organization organization) {
		if (company != null && organization != null) {
			List<Organization> organizations = new ArrayList<Organization>();
			organizations.add(organization);
			addOrganizations(company, organizations);
		}
	}

	public void addOrganizationGroups(Long userId, List<Group> groups) {
		if (userId != null && groups != null) {
			userTime.put(userId, System.currentTimeMillis());
			List<Group> organizationGroups = organizationsGroupByUser.get(userId);
			if (organizationGroups == null) {
				organizationGroups = new ArrayList<Group>();
				organizationsGroupByUser.put(userId, organizationGroups);
			}

			for (Group group : groups) {
				if (!organizationGroups.contains(group)) {
					organizationGroups.add(group);
				}
			}
		}
	}

	public void addOrganizationGroup(User user, Group group) {
		if (user != null && group != null) {
			List<Group> groups = new ArrayList<Group>();
			groups.add(group);
			addOrganizationGroups(user, groups);
		}
	}

	public void addOrganizationGroups(User user, List<Group> groups) {
		if (user != null && groups != null && !groups.isEmpty()) {
			addOrganizationGroups(user.getUserId(), groups);
		}
	}

	public void addOrganizations(Company company, List<Organization> organizationsToAdd) {
		if (company != null && organizationsToAdd != null) {
			time.put(company.getCompanyId(), System.currentTimeMillis());
			List<Organization> organizationsOfCompany = organizationsByCompany.get(company.getCompanyId());
			if (organizationsOfCompany == null) {
				organizationsOfCompany = new ArrayList<Organization>();
				organizationsByCompany.put(company.getCompanyId(), organizationsOfCompany);
			}

			for (Organization organization : organizationsToAdd) {
				if (!organizationsOfCompany.contains(organization)) {
					organizationsOfCompany.add(organization);
				}
			}
		}
	}

	public List<Group> getOrganizationGroups(long userId) {
		long now = System.currentTimeMillis();
		Long nextUserId = null;
		if (userTime.size() > 0) {
			Enumeration<Long> e = userTime.keys();
			while (e.hasMoreElements()) {
				nextUserId = e.nextElement();
				if ((now - userTime.get(nextUserId)) > EXPIRATION_TIME) {
					// object has expired
					removeOrganizationGroupsOfUser(nextUserId);
					nextUserId = null;
				} else {
					if (userId == nextUserId) {
						return organizationsGroupByUser.get(nextUserId);
					}
				}
			}
		}
		return null;
	}

	public List<Group> getOrganizationGroups(User user) {
		if (user != null) {
			return getOrganizationGroups(user.getUserId());
		}
		return null;
	}

	public List<Organization> getOrganizations(Company company) {
		long now = System.currentTimeMillis();
		Long companyId = null;
		if (time.size() > 0) {
			Enumeration<Long> e = time.keys();
			while (e.hasMoreElements()) {
				companyId = e.nextElement();
				if ((now - time.get(companyId)) > EXPIRATION_TIME) {
					// object has expired
					removeOrganizationsOfCompany(companyId);
					companyId = null;
				} else {
					if (company.getCompanyId() == companyId) {
						return organizationsByCompany.get(companyId);
					}
				}
			}
		}
		return null;
	}

	public void removeOrganization(Company company, Organization organization) {
		List<Organization> organizationsOfCompany = new ArrayList<Organization>(organizationsByCompany.get(company
				.getCompanyId()));
		for (Organization organizationOfCompany : organizationsOfCompany) {
			if (organizationOfCompany.getOrganizationId() == organization.getOrganizationId()) {
				organizationsByCompany.get(company.getCompanyId()).remove(organizationOfCompany);
			}
		}
	}

	public void removeOrganizationGroups(User user) {
		if (user != null) {
			removeOrganizationGroupsOfUser(user.getUserId());
		}
	}

	private void removeOrganizationGroupsOfUser(Long userId) {
		organizationsGroupByUser.remove(userId);
		userTime.remove(userId);
	}

	public void removeOrganizations(Company company) {
		if (company != null) {
			removeOrganizationsOfCompany(company.getCompanyId());
		}
	}

	public void removeOrganizationsOfCompany(Long companyId) {
		time.remove(companyId);
		organizationsByCompany.remove(companyId);
	}

}
