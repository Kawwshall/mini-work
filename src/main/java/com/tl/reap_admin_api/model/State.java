package com.tl.reap_admin_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "states", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ext_id", "language_code"})
})
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ext_id", nullable = false)
    private Integer extId;

    @Column(nullable = false)
    private String name;

    @Column(name = "language_code", nullable = false)
    private String languageCode;
    
 // Default constructor
    public State() {}

    // Constructor with name, extId, and languageCode
    public State(String name, Integer extId, String languageCode) {
        this.name = name;
        this.extId = extId;
        this.languageCode = languageCode;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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