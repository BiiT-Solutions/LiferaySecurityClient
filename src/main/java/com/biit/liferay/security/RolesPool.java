package com.biit.liferay.security;

import java.util.Collection;

import javax.inject.Named;

import com.biit.usermanager.entity.IRole;
import com.biit.utils.pool.CollectionPool;

@Named
public class RolesPool extends CollectionPool<String, Long, IRole<Long>> {
	private final static long EXPIRATION_TIME = 10 * 60 * 1000;

	@Override
	public long getExpirationTime() {
		return EXPIRATION_TIME;
	}

	@Override
	public boolean isDirty(Collection<IRole<Long>> element) {
		return false;
	}

}
