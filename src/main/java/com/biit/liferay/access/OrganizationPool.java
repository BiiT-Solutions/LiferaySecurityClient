package com.biit.liferay.access;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.liferay.portal.model.Company;
import com.liferay.portal.model.Organization;

public class OrganizationPool {

	private final static long EXPIRATION_TIME = 300000;// 5 minutes

	private Hashtable<Long, Long> time; // User id -> time.
	private Hashtable<Long, List<Organization>> organizations; // User id -> User.

	public OrganizationPool() {
		time = new Hashtable<Long, Long>();
		organizations = new Hashtable<Long, List<Organization>>();
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
					removeOrganizations(companyId);
					companyId = null;
				} else {
					if (company.getCompanyId() == companyId) {
						return organizations.get(companyId);
					}
				}
			}
		}
		return null;
	}

	public void addOrganization(Company company, Organization organization) {
		if (company != null && organization != null) {
			List<Organization> organizations = new ArrayList<Organization>();
			organizations.add(organization);
			addOrganizations(company, organizations);
		}
	}

	public void addOrganizations(Company company, List<Organization> organizationsToAdd) {
		if (company != null && organizationsToAdd != null) {
			time.put(company.getCompanyId(), System.currentTimeMillis());
			List<Organization> organizationsOfCompany = organizations.get(company.getCompanyId());
			if (organizationsOfCompany == null) {
				organizationsOfCompany = new ArrayList<Organization>();
				organizations.put(company.getCompanyId(), organizationsOfCompany);
			}

			for (Organization organization : organizationsToAdd) {
				if (!organizationsOfCompany.contains(organization)) {
					organizationsOfCompany.add(organization);
				}
			}
		}
	}

	public void removeOrganization(Company company, Organization organization) {
		List<Organization> organizationsOfCompany = new ArrayList<Organization>(organizations.get(company
				.getCompanyId()));
		for (Organization organizationOfCompany : organizationsOfCompany) {
			if (organizationOfCompany.getOrganizationId() == organization.getOrganizationId()) {
				organizations.get(company.getCompanyId()).remove(organizationOfCompany);
			}
		}
	}

	public void removeOrganizations(Company company) {
		if (company != null) {
			removeOrganizations(company.getCompanyId());
		}
	}

	public void removeOrganizations(Long companyId) {
		time.remove(companyId);
		organizations.remove(companyId);
	}

}
