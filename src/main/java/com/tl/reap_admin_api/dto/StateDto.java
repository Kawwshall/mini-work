package com.tl.reap_admin_api.dto;

public class StateDto {
    private Integer extId;
    private String name;
    private String languageCode;

    // Default constructor
    public StateDto() {}

    // Constructor with all fields
    public StateDto(Integer extId, String name, String languageCode) {
        this.extId = extId;
        this.name = name;
        this.languageCode = languageCode;
    }

    // Getters and setters

    public Integer getExtId() {
        return extId;
    }

    public void setExtId(Integer extId) {
        this.extId = extId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}