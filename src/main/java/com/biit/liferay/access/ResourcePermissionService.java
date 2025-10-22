package com.biit.liferay.access;

/*-
 * #%L
 * Liferay Authentication Client with Web Services
 * %%
 * Copyright (C) 2013 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.log.LiferayClientLogger;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.liferay.portal.model.ActionKey;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ResourcePermissionService extends ServiceAccess<String, String> {

    @Override
    public Set<String> decodeListFromJson(String json, Class<String> objectClass)
            throws JsonParseException, JsonMappingException, IOException {
        return null;
    }

    public boolean addResourcePermission(String resourceClass, long resourcePrimaryKey, long groupId, long companyId,
                                         Map<Long, ActionKey[]> roleIdsToActionIds) throws WebServiceAccessError,
            NotConnectedToWebServiceException, IOException, AuthenticationRequired {
        final Map<Long, String[]> translatedActions = new HashMap<Long, String[]>();
        for (Entry<Long, ActionKey[]> actionKeysByRole : roleIdsToActionIds.entrySet()) {
            final String[] actions = new String[actionKeysByRole.getValue().length];
            for (int i = 0; i < actionKeysByRole.getValue().length; i++) {
                actions[i] = actionKeysByRole.getValue()[i].getLiferayTag();
            }
            translatedActions.put(actionKeysByRole.getKey(), actions);
        }
        return addResourcePermission(groupId, companyId, resourceClass, resourcePrimaryKey, translatedActions);
    }

    private boolean addResourcePermission(long groupId, long companyId, String resourceClass, long resourcePrimaryKey,
                                          Map<Long, String[]> roleIdsToActionIds) throws WebServiceAccessError, NotConnectedToWebServiceException,
            IOException, AuthenticationRequired {
        checkConnection();
        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("groupId", Long.toString(groupId)));
        params.add(new BasicNameValuePair("companyId", Long.toString(companyId)));
        params.add(new BasicNameValuePair("name", resourceClass));
        params.add(new BasicNameValuePair("primKey", Long.toString(resourcePrimaryKey)));
        params.add(new BasicNameValuePair("roleIdsToActionIds", convertToJson(roleIdsToActionIds)));

        final String result = getHttpPostResponse("resourcepermission/set-individual-resource-permissions", params);
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
