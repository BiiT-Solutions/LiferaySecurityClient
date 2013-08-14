package com.liferay.portal.service.http;

public class CompanyServiceSoapProxy implements com.liferay.portal.service.http.CompanyServiceSoap {
  private String _endpoint = null;
  private com.liferay.portal.service.http.CompanyServiceSoap companyServiceSoap = null;
  
  public CompanyServiceSoapProxy() {
    _initCompanyServiceSoapProxy();
  }
  
  public CompanyServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initCompanyServiceSoapProxy();
  }
  
  private void _initCompanyServiceSoapProxy() {
    try {
      companyServiceSoap = (new com.liferay.portal.service.http.CompanyServiceSoapServiceLocator()).getPortal_CompanyService();
      if (companyServiceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)companyServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)companyServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (companyServiceSoap != null)
      ((javax.xml.rpc.Stub)companyServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.liferay.portal.service.http.CompanyServiceSoap getCompanyServiceSoap() {
    if (companyServiceSoap == null)
      _initCompanyServiceSoapProxy();
    return companyServiceSoap;
  }
  
  public com.liferay.portal.model.CompanySoap addCompany(java.lang.String webId, java.lang.String virtualHost, java.lang.String mx, java.lang.String shardName, boolean system, int maxUsers, boolean active) throws java.rmi.RemoteException{
    if (companyServiceSoap == null)
      _initCompanyServiceSoapProxy();
    return companyServiceSoap.addCompany(webId, virtualHost, mx, shardName, system, maxUsers, active);
  }
  
  public void deleteLogo(long companyId) throws java.rmi.RemoteException{
    if (companyServiceSoap == null)
      _initCompanyServiceSoapProxy();
    companyServiceSoap.deleteLogo(companyId);
  }
  
  public com.liferay.portal.model.CompanySoap getCompanyById(long companyId) throws java.rmi.RemoteException{
    if (companyServiceSoap == null)
      _initCompanyServiceSoapProxy();
    return companyServiceSoap.getCompanyById(companyId);
  }
  
  public com.liferay.portal.model.CompanySoap getCompanyByLogoId(long logoId) throws java.rmi.RemoteException{
    if (companyServiceSoap == null)
      _initCompanyServiceSoapProxy();
    return companyServiceSoap.getCompanyByLogoId(logoId);
  }
  
  public com.liferay.portal.model.CompanySoap getCompanyByMx(java.lang.String mx) throws java.rmi.RemoteException{
    if (companyServiceSoap == null)
      _initCompanyServiceSoapProxy();
    return companyServiceSoap.getCompanyByMx(mx);
  }
  
  public com.liferay.portal.model.CompanySoap getCompanyByVirtualHost(java.lang.String virtualHost) throws java.rmi.RemoteException{
    if (companyServiceSoap == null)
      _initCompanyServiceSoapProxy();
    return companyServiceSoap.getCompanyByVirtualHost(virtualHost);
  }
  
  public com.liferay.portal.model.CompanySoap getCompanyByWebId(java.lang.String webId) throws java.rmi.RemoteException{
    if (companyServiceSoap == null)
      _initCompanyServiceSoapProxy();
    return companyServiceSoap.getCompanyByWebId(webId);
  }
  
  public void removePreferences(long companyId, java.lang.String[] keys) throws java.rmi.RemoteException{
    if (companyServiceSoap == null)
      _initCompanyServiceSoapProxy();
    companyServiceSoap.removePreferences(companyId, keys);
  }
  
  public com.liferay.portal.model.CompanySoap updateCompany(long companyId, java.lang.String virtualHost, java.lang.String mx, int maxUsers, boolean active) throws java.rmi.RemoteException{
    if (companyServiceSoap == null)
      _initCompanyServiceSoapProxy();
    return companyServiceSoap.updateCompany(companyId, virtualHost, mx, maxUsers, active);
  }
  
  public com.liferay.portal.model.CompanySoap updateCompany(long companyId, java.lang.String virtualHost, java.lang.String mx, java.lang.String homeURL, java.lang.String name, java.lang.String legalName, java.lang.String legalId, java.lang.String legalType, java.lang.String sicCode, java.lang.String tickerSymbol, java.lang.String industry, java.lang.String type, java.lang.String size) throws java.rmi.RemoteException{
    if (companyServiceSoap == null)
      _initCompanyServiceSoapProxy();
    return companyServiceSoap.updateCompany(companyId, virtualHost, mx, homeURL, name, legalName, legalId, legalType, sicCode, tickerSymbol, industry, type, size);
  }
  
  public void updateDisplay(long companyId, java.lang.String languageId, java.lang.String timeZoneId) throws java.rmi.RemoteException{
    if (companyServiceSoap == null)
      _initCompanyServiceSoapProxy();
    companyServiceSoap.updateDisplay(companyId, languageId, timeZoneId);
  }
  
  public void updateSecurity(long companyId, java.lang.String authType, boolean autoLogin, boolean sendPassword, boolean strangers, boolean strangersWithMx, boolean strangersVerify, boolean siteLogo) throws java.rmi.RemoteException{
    if (companyServiceSoap == null)
      _initCompanyServiceSoapProxy();
    companyServiceSoap.updateSecurity(companyId, authType, autoLogin, sendPassword, strangers, strangersWithMx, strangersVerify, siteLogo);
  }
  
  
}