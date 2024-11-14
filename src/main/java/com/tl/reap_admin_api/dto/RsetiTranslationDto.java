package com.tl.reap_admin_api.dto;

public class RsetiTranslationDto {
    private Long id;
    private String languageCode;
    private String name;
    private String districtName;
    private String address;
    private String directorName;

    // Constructors, getters, and setters

    public RsetiTranslationDto() {
    }

    public RsetiTranslationDto(Long id, String languageCode, String name, String districtName, String address, String directorName) {
        this.id = id;
        this.languageCode = languageCode;
        this.name = name;
        this.districtName = districtName;
        this.address = address;
        this.directorName = directorName;
    }

    // Getters and setters for all fields

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }
}