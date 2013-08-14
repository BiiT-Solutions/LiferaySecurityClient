package com.biit.liferay.access;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.liferay.portal.model.CompanySoap;
import com.liferay.portal.service.http.CompanyServiceSoap;
import com.liferay.portal.service.http.CompanyServiceSoapServiceLocator;

/**
 * This class allows to obtain a liferay portal instance.
 */
public class CompanyService {
	private final static String SERVICE_COMPANY_NAME = "Portal_UserService";
	private CompanyServiceSoap soapCompany;

	public CompanyService(String loginUser, String password) throws ServiceException {
		CompanyServiceSoapServiceLocator locatorCompany = new CompanyServiceSoapServiceLocator();
		soapCompany = locatorCompany.getPortal_CompanyService(AccessUtils.getLiferayUrl(loginUser, password,
				SERVICE_COMPANY_NAME));
	}

	/**
	 * Returns the company with the virtual host name.
	 * 
	 * @param virtualHost
	 *            the company's virtual host name.
	 * @return Returns the company with the virtual host name.
	 * @throws RemoteException
	 *             if there is a communication problem
	 * 
	 */
	public CompanySoap getCompanyByVirtualHost(String virtualHost) throws RemoteException {
		return soapCompany.getCompanyByVirtualHost(virtualHost);
	}

	/**
	 * Returns the company with the virtual host name.
	 * 
	 * @param companyId
	 *            the primary key of the company
	 * @return Returns the company with the virtual host name.
	 * @throws RemoteException
	 *             if there is a communication problem
	 */
	public CompanySoap getCompanyById(long companyId) throws RemoteException {
		return soapCompany.getCompanyById(companyId);
	}

	/**
	 * Returns the company with the web domain.
	 * 
	 * @param webId
	 *            The company's web domain
	 * @return Returns the company with the virtual host name.
	 * @throws RemoteException
	 *             if there is a communication problem
	 */
	public CompanySoap getCompanyByWebId(String webId) throws RemoteException {
		return soapCompany.getCompanyByWebId(webId);
	}

}
