package com.biit.liferay.access;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.liferay.portal.model.Company;
import com.liferay.portal.service.http.CompanyServiceSoap;
import com.liferay.portal.service.http.CompanyServiceSoapServiceLocator;

/**
 * This class allows to obtain a liferay portal instance.
 */
public class CompanyService extends ServiceAccess {
	private final static String SERVICE_COMPANY_NAME = "Portal_CompanyService";
	private final static CompanyService instance = new CompanyService();

	private CompanyService() {

	}

	public static CompanyService getInstance() {
		return instance;
	}

	@Override
	public String getServiceName() {
		return SERVICE_COMPANY_NAME;
	}

	@Override
	public void connectToWebService(String loginUser, String password) throws ServiceException {
		// Locate the User service
		CompanyServiceSoapServiceLocator locatorCompany = new CompanyServiceSoapServiceLocator();
		setServiceSoap(locatorCompany.getPortal_CompanyService(AccessUtils.getLiferayUrl(loginUser, password,
				SERVICE_COMPANY_NAME)));
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
	public Company getCompanyByVirtualHost(String virtualHost) throws RemoteException {
		return ((CompanyServiceSoap) getServiceSoap()).getCompanyByVirtualHost(virtualHost);
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
	public Company getCompanyById(long companyId) throws RemoteException {
		return ((CompanyServiceSoap) getServiceSoap()).getCompanyById(companyId);
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
	public Company getCompanyByWebId(String webId) throws RemoteException {
		return ((CompanyServiceSoap) getServiceSoap()).getCompanyByWebId(webId);
	}

}
