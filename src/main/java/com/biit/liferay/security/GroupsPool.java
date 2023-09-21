package com.biit.liferay.security;

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
