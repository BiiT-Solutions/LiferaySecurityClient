package com.biit.liferay.security;

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

import com.biit.usermanager.entity.IGroup;
import com.biit.utils.pool.CollectionPool;

import javax.inject.Named;
import java.util.Collection;

@Named
public class GroupsPool extends CollectionPool<String, Long, IGroup<Long>> {
    private static final long EXPIRATION_TIME = 10 * 60 * 1000;

    @Override
    public long getExpirationTime() {
        return EXPIRATION_TIME;
    }

    @Override
    public boolean isDirty(Collection<IGroup<Long>> element) {
        return false;
    }

}
