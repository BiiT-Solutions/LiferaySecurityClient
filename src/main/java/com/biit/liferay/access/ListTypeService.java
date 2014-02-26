package com.biit.liferay.access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.portal.model.ListType;

public class ListTypeService extends ServiceAccess<ListType> {
	private final static String FULL_MEMBER_TAG = "full-member";
	private final static String FULL_MEMBER_TYPE = "com.liferay.portal.model.Organization.status";

	public ListTypeService() {
	}

	@Override
	public List<ListType> decodeListFromJson(String json, Class<ListType> objectClass) throws JsonParseException,
			JsonMappingException, IOException {
		List<ListType> myObjects = new ObjectMapper().readValue(json, new TypeReference<List<ListType>>() {
		});
		return myObjects;
	}

	public int getFullMemberStatus() throws NotConnectedToWebServiceException, ClientProtocolException, IOException,
			AuthenticationRequired, WebServiceAccessError {
		// Look up user in the liferay.
		checkConnection();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type", FULL_MEMBER_TYPE));

		String result = getHttpResponse("listtype/get-list-types", params);
		if (result != null) {
			// A Simple JSON Response Read
			List<ListType> listTypes = decodeListFromJson(result, ListType.class);

			for (ListType listType : listTypes) {
				if (listType.getName().equals(FULL_MEMBER_TAG)) {
					return listType.getListTypeId().intValue();
				}
			}
		}
		return -1;
	}

}
