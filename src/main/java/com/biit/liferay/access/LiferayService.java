package com.biit.liferay.access;

import javax.xml.rpc.ServiceException;

import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;

public interface LiferayService {

	/**
	 * Check if the user and password has been successfully inserted before.
	 * 
	 * @return
	 */
	boolean isNotConnected();

	/**
	 * Connects to Liferay webservice using the provided user and password.
	 * 
	 * @param loginUser
	 * @param password
	 * @throws ServiceException
	 */
	void connectToWebService(String loginUser, String password) throws ServiceException;

	/**
	 * Connects to Liferay webservice using the liferay.conf file.
	 * 
	 * @throws ServiceException
	 */
	void connectToWebService() throws ServiceException;

	/**
	 * Checks if a connection has been established.
	 * 
	 * @throws NotConnectedToWebServiceException
	 */
	void checkConnection() throws NotConnectedToWebServiceException;

	/**
	 * Finish the connection.
	 */
	void disconnect();
}
