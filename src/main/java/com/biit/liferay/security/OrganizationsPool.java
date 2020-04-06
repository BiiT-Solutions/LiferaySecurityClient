package com.biit.liferay.security;

import java.util.Collection;

import javax.inject.Named;

import com.biit.usermanager.entity.IGroup;
import com.biit.utils.pool.CollectionPool;

@Named
public class OrganizationsPool extends CollectionPool<String, Long, IGroup<Long>> {
	private final static long EXPIRATION_TIME = 10 * 60 * 1000;

	@Override
	public long getExpirationTime() {
		return EXPIRATION_TIME;
	}

	@Override
	public boolean isDirty(Collection<IGroup<Long>> element) {
		return false;
	}

}
