package com.github.telvarost.betterscreenshots;

public enum IsometricAngleEnum {
    ANGLE_0_DEGREES("0 Degrees"),
    ANGLE_90_DEGREES("90 Degrees"),
    ANGLE_180_DEGREES("180 Degrees"),
    ANGLE_270_DEGREES("270 Degrees");

    final String stringValue;

    IsometricAngleEnum() {
        this.stringValue = "0 Degrees";
    }

    IsometricAngleEnum(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}