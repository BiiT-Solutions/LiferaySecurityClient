package com.liferay.portal.model;

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

public enum RoleType {

    STANDARD(1),

    SITE(2),

    ORGANIZATION(3),

    UNKNOWN(100);

    private int liferayCode;

    RoleType(int liferayCode) {
        this.liferayCode = liferayCode;
    }

    public static RoleType getRoleType(int liferayCode) {
        for (RoleType roleType : RoleType.values()) {
            if (roleType.getLiferayCode() == liferayCode) {
                return roleType;
            }
        }
        return RoleType.UNKNOWN;
    }

    public int getLiferayCode() {
        return liferayCode;
    }
}
