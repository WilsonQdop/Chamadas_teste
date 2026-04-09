package com.WilsonQdop.Chamadas.enums;

public enum RolesType {
    ADM(1),
    TECH(2),
    USER(3);

    private final long roleId;

    RolesType(long roleId) {
        this.roleId = roleId;
    }

    public long getRoleId() {
        return roleId;
    }
}
