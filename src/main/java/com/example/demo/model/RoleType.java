package com.example.demo.model;

public enum RoleType {
    /**
     * 管理员
     */
    ADMIN(0),
    /**
     * 普通用户
     */
    USER(1);

    private int value = 0;

    private RoleType(int value) {
        this.value = value;
    }

    public static RoleType adopt(int value) {
        switch (value) {
            case 0:
                return ADMIN;
            case 1:
                return USER;
        }
        return null;
    }

    public int value() {
        return this.value;
    }
}
