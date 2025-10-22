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

import org.testng.annotations.Test;

import com.biit.liferay.access.exceptions.NotValidPasswordException;
import com.biit.liferay.security.AccessUtils;

public class AccessUtilsTest {

	@Test(groups = { "password" }, expectedExceptions = NotValidPasswordException.class)
	public void notValidPassword() throws NotValidPasswordException {
		AccessUtils.checkPassword("111@22");
	}
	
	@Test(groups = { "password" }, expectedExceptions = NotValidPasswordException.class)
	public void notValidPassword2() throws NotValidPasswordException {
		AccessUtils.checkPassword("asd¡");
	}

	@Test(groups = { "password" })
	public void validPassword() throws NotValidPasswordException {
		AccessUtils.checkPassword("A!Sa(dfgH*))!120");
	}
}
