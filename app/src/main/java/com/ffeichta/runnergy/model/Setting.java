package com.ffeichta.runnergy.model;

/**
 * Created by Fabian on 30.11.2015.
 */
public class Setting {
    private String key = null;
    private String value = null;

    public Setting() {
    }

    public Setting(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
