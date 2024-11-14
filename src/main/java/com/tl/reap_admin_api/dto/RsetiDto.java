package com.tl.reap_admin_api.dto;

import java.util.List;
import java.util.UUID;

public class RsetiDto {
    private UUID uuid;
    private String extId;
    private String name;
    private String email;
    private String contactNo;
    private String districtName;
    private String address;
    private String directorName;
    private String directorContactNo;
    private Integer stateId;
    private UUID bankId;
    private List<RsetiTranslationDto> translations;

    // Constructors, getters, and setters

    public RsetiDto() {
    }

    public RsetiDto(UUID uuid, String extId, String name, String email, String contactNo, String districtName,
                    String address, String directorName, String directorContactNo, Integer stateId, UUID bankId) {
        this.uuid = uuid;
        this.extId = extId;
        this.name = name;
        this.email = email;
        this.contactNo = contactNo;
        this.districtName = districtName;
        this.address = address;
        this.directorName = directorName;
        this.directorContactNo = directorContactNo;
        this.stateId = stateId;
        this.bankId = bankId;
    }

    // Getters and setters for all fields

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
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

    public String getDirectorContactNo() {
        return directorContactNo;
    }

    public void setDirectorContactNo(String directorContactNo) {
        this.directorContactNo = directorContactNo;
    }


    public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public UUID getBankId() {
        return bankId;
    }

    public void setBankId(UUID bankId) {
        this.bankId = bankId;
    }

    public List<RsetiTranslationDto> getTranslations() {
        return translations;
    }

    public void setTranslations(List<RsetiTranslationDto> translations) {
        this.translations = translations;
    }
}