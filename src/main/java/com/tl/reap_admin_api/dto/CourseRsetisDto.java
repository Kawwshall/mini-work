package com.tl.reap_admin_api.dto;

import java.util.List;
import java.util.UUID;

public class CourseRsetisDto {
    private List<UUID> rsetis;

    // Constructor
    public CourseRsetisDto() {}

    public CourseRsetisDto(List<UUID> rsetis) {
        this.rsetis = rsetis;
    }

    // Getter and setter
    public List<UUID> getRsetis() {
        return rsetis;
    }

    public void setRsetis(List<UUID> rsetis) {
        this.rsetis = rsetis;
    }
}