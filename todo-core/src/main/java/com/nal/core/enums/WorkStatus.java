package com.nal.core.enums;

public enum WorkStatus {
    PLANNING("Planning"),
    DOING("Doing"),
    COMPLETE("Complete");

    private String value;

    WorkStatus(String value) {
        this.value = value;
    }
}
