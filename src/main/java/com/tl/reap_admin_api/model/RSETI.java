package com.tl.reap_admin_api.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "rsetis")
public class RSETI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;

    @Column(name = "ext_id", nullable = false)
    private String extId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(name = "contact_no", nullable = false)
    private String contactNo;

    @Column(name = "district_name", nullable = false)
    private String districtName;

    @Column(nullable = false)
    private String address;

    @Column(name = "director_name", nullable = false)
    private String directorName;

    @Column(name = "director_contact_no", nullable = false)
    private String directorContactNo;

    @Column(name = "state_id", nullable = false)
    private Integer stateId;

    @Column(name = "bank_id", nullable = false)
    private UUID bankId;

    @OneToMany(mappedBy = "rseti", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RsetiTranslation> translations = new ArrayList<>();
    
    @OneToMany(mappedBy = "rseti", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RsetiCourse> rsetiCourses = new ArrayList<>();

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<RsetiTranslation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<RsetiTranslation> translations) {
        this.translations = translations;
    }

    public void addTranslation(RsetiTranslation translation) {
        translations.add(translation);
        translation.setRseti(this);
    }

    public void removeTranslation(RsetiTranslation translation) {
        translations.remove(translation);
        translation.setRseti(null);
    }
    
    public List<RsetiCourse> getRsetiCourses() {
        return rsetiCourses;
    }

    public void setRsetiCourses(List<RsetiCourse> rsetiCourses) {
        this.rsetiCourses = rsetiCourses;
    }

    public void addRsetiCourse(RsetiCourse rsetiCourse) {
        rsetiCourses.add(rsetiCourse);
        rsetiCourse.setRseti(this);
    }

    public void removeRsetiCourse(RsetiCourse rsetiCourse) {
        rsetiCourses.remove(rsetiCourse);
        rsetiCourse.setRseti(null);
    }

	@Override
	public String toString() {
		return "RSETI [id=" + id + ", uuid=" + uuid + ", extId=" + extId + ", name=" + name + ", email=" + email
				+ ", contactNo=" + contactNo + ", districtName=" + districtName + ", address=" + address
				+ ", directorName=" + directorName + ", directorContactNo=" + directorContactNo + ", stateId=" + stateId
				+ ", bankId=" + bankId + ", translations=" + translations + ", rsetiCourses=" + rsetiCourses
				+ ", getId()=" + getId() + ", getUuid()=" + getUuid() + ", getExtId()=" + getExtId() + ", getName()="
				+ getName() + ", getEmail()=" + getEmail() + ", getContactNo()=" + getContactNo()
				+ ", getDistrictName()=" + getDistrictName() + ", getAddress()=" + getAddress() + ", getDirectorName()="
				+ getDirectorName() + ", getDirectorContactNo()=" + getDirectorContactNo() + ", getStateId()="
				+ getStateId() + ", getBankId()=" + getBankId() + ", getTranslations()=" + getTranslations()
				+ ", getRsetiCourses()=" + getRsetiCourses() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
    
    
}