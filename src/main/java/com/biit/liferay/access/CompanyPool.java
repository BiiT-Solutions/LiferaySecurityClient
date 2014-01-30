package com.biit.liferay.access;

import java.util.Enumeration;
import java.util.Hashtable;

import com.liferay.portal.model.Company;

public class CompanyPool {

	private final static long EXPIRATION_TIME = 300000;// 5 minutes

	private Hashtable<Long, Long> byIdTime;
	private Hashtable<Long, Company> companyPoolById;

	private Hashtable<String, Long> byVirtualHostTime;
	private Hashtable<String, Company> companyPoolByVirtualHost;

	private Hashtable<String, Long> byWebIdTime;
	private Hashtable<String, Company> companyPoolByWebId;

	public CompanyPool() {
		byIdTime = new Hashtable<Long, Long>();
		companyPoolById = new Hashtable<Long, Company>();
		byVirtualHostTime = new Hashtable<String, Long>();
		companyPoolByVirtualHost = new Hashtable<String, Company>();
		byWebIdTime = new Hashtable<String, Long>();
		companyPoolByWebId = new Hashtable<String, Company>();
	}

	public void addCompany(Company company) {
		if (company != null) {
			long now = System.currentTimeMillis();

			companyPoolById.put(company.getCompanyId(), company);
			byIdTime.put(company.getCompanyId(), now);

			companyPoolByWebId.put(company.getWebId(), company);
			byWebIdTime.put(company.getWebId(), now);
		}
	}

	public Company getCompanyById(long companyId) {
		long now = System.currentTimeMillis();
		Long nextCompanyId = null;
		if (byIdTime.size() > 0) {
			Enumeration<Long> e = byIdTime.keys();
			while (e.hasMoreElements()) {
				nextCompanyId = e.nextElement();
				if ((now - byIdTime.get(nextCompanyId)) > EXPIRATION_TIME) {
					// object has expired
					removeCompanyById(nextCompanyId);
					nextCompanyId = null;
				} else {
					if (companyId == nextCompanyId) {
						return companyPoolById.get(nextCompanyId);
					}
				}
			}
		}
		return null;
	}

	private void removeCompanyById(Long companyId) {
		byIdTime.remove(companyId);
		companyPoolById.remove(companyId);
	}

	public Company getCompanyByWebId(String webId) {
		long now = System.currentTimeMillis();
		String nextCompanyWebId = null;
		if (byWebIdTime.size() > 0) {
			Enumeration<String> e = byWebIdTime.keys();
			while (e.hasMoreElements()) {
				nextCompanyWebId = e.nextElement();
				if ((now - byWebIdTime.get(nextCompanyWebId)) > EXPIRATION_TIME) {
					// object has expired
					removeCompanyByWebId(nextCompanyWebId);
					nextCompanyWebId = null;
				} else {
					if (webId == nextCompanyWebId) {
						return companyPoolByWebId.get(nextCompanyWebId);
					}
				}
			}
		}
		return null;
	}

	private void removeCompanyByWebId(String webId) {
		byWebIdTime.remove(webId);
		companyPoolByWebId.remove(webId);
	}

	public void addCompany(Company company, String virtualHost) {
		if (company != null) {
			addCompany(company);
			long now = System.currentTimeMillis();

			companyPoolByVirtualHost.put(virtualHost, company);
			byVirtualHostTime.put(virtualHost, now);
		}
	}

	public Company getCompanyByVirtualHostId(String virtualHost) {
		long now = System.currentTimeMillis();
		String nextCompanyVirtualHost = null;
		if (byVirtualHostTime.size() > 0) {
			Enumeration<String> e = byVirtualHostTime.keys();
			while (e.hasMoreElements()) {
				nextCompanyVirtualHost = e.nextElement();
				if ((now - byVirtualHostTime.get(nextCompanyVirtualHost)) > EXPIRATION_TIME) {
					// object has expired
					removeCompanyByVirtualHost(nextCompanyVirtualHost);
					nextCompanyVirtualHost = null;
				} else {
					if (virtualHost == nextCompanyVirtualHost) {
						return companyPoolByVirtualHost.get(nextCompanyVirtualHost);
					}
				}
			}
		}
		return null;
	}

	private void removeCompanyByVirtualHost(String webId) {
		byVirtualHostTime.remove(webId);
		companyPoolByVirtualHost.remove(webId);
	}

}
