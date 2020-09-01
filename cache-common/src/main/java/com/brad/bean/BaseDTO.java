package com.brad.bean;

/**
 * BaseDTO
 */
public class BaseDTO {
    private String value;
    private String name;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BaseDTO{" +
                "value='" + value + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
