package com.biit.liferay.access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.log.LiferayClientLogger;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.liferay.portal.model.ActionKey;

public class ResourcePermissionService extends ServiceAccess<String, String> {

	@Override
	public Set<String> decodeListFromJson(String json, Class<String> objectClass)
			throws JsonParseException, JsonMappingException, IOException {
		return null;
	}

	public boolean addResourcePermission(String resourceClass, long resourcePrimaryKey, long groupId, long companyId,
			Map<Long, ActionKey[]> roleIdsToActionIds) throws ClientProtocolException, WebServiceAccessError,
			NotConnectedToWebServiceException, IOException, AuthenticationRequired {
		Map<Long, String[]> translatedActions = new HashMap<Long, String[]>();
		for (Entry<Long, ActionKey[]> actionKeysByRole : roleIdsToActionIds.entrySet()) {
			String[] actions = new String[actionKeysByRole.getValue().length];
			for (int i = 0; i < actionKeysByRole.getValue().length; i++) {
				actions[i] = actionKeysByRole.getValue()[i].getLiferayTag();
			}
			translatedActions.put(actionKeysByRole.getKey(), actions);
		}
		return addResourcePermission(groupId, companyId, resourceClass, resourcePrimaryKey, translatedActions);
	}

	private boolean addResourcePermission(long groupId, long companyId, String resourceClass, long resourcePrimaryKey,
			Map<Long, String[]> roleIdsToActionIds) throws WebServiceAccessError, NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired {
		checkConnection();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("groupId", Long.toString(groupId)));
		params.add(new BasicNameValuePair("companyId", Long.toString(companyId)));
		params.add(new BasicNameValuePair("name", resourceClass));
		params.add(new BasicNameValuePair("primKey", Long.toString(resourcePrimaryKey)));
		params.add(new BasicNameValuePair("roleIdsToActionIds", convertToJson(roleIdsToActionIds)));

		String result = getHttpPostResponse("resourcepermission/set-individual-resource-permissions", params);
		if (result != null) {
			LiferayClientLogger.info(this.getClass().getName(),
					"Resource permission changed for '" + resourceClass + "' with id '" + resourcePrimaryKey + "'.");
			return true;
		}

		return false;

	}

	@Override
	public void reset() {

	}

}
