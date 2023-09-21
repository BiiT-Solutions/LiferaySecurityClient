package com.biit.liferay.security;

import com.biit.usermanager.entity.IRole;
import com.biit.utils.pool.CollectionPool;

import javax.inject.Named;
import java.util.Collection;

@Named
public class RolesPool extends CollectionPool<String, Long, IRole<Long>> {
    private static final long EXPIRATION_TIME = 10 * 60 * 1000;

    @Override
    public long getExpirationTime() {
        return EXPIRATION_TIME;
    }

    @Override
    public boolean isDirty(Collection<IRole<Long>> element) {
        return false;
    }

}
