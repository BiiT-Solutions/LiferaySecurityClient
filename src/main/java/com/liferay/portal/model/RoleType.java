package com.liferay.portal.model;

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
