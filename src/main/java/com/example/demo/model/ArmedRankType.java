package com.example.demo.model;

public enum ArmedRankType {
    /**
     * 军长
     */
    COMMANDER(0),
    /**
     * 旅长
     */
    BRIGADIER(1);

    private int value = 0;

    private ArmedRankType(int value) {
        this.value = value;
    }

    public static ArmedRankType adopt(int value) {
        switch (value) {
            case 0:
                return COMMANDER;
            case 1:
                return BRIGADIER;
        }
        return null;
    }

    public int value() {
        return this.value;
    }
}
