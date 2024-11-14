package com.tl.reap_admin_api.dto;

import java.time.LocalDate;
import java.util.UUID;

public class TraineeRsetiDto {
    private UUID uuid;
    private String enrollId;
    private LocalDate enrolledOn;
    private UUID rsetiUuid;
    private UUID traineeProfileUuid;
    private UUID courseUuid;
    private String status;

    // Constructors
    public TraineeRsetiDto() {}

    public TraineeRsetiDto(UUID uuid, String enrollId, LocalDate enrolledOn, UUID rsetiUuid, UUID traineeProfileUuid, UUID courseUuid, String status) {
        this.uuid = uuid;
        this.enrollId = enrollId;
        this.enrolledOn = enrolledOn;
        this.rsetiUuid = rsetiUuid;
        this.traineeProfileUuid = traineeProfileUuid;
        this.courseUuid = courseUuid;
        this.status = status;
    }

    // Getters and setters for all fields

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getEnrollId() {
        return enrollId;
    }

    public void setEnrollId(String enrollId) {
        this.enrollId = enrollId;
    }

    public LocalDate getEnrolledOn() {
        return enrolledOn;
    }

    public void setEnrolledOn(LocalDate enrolledOn) {
        this.enrolledOn = enrolledOn;
    }

    public UUID getRsetiUuid() {
        return rsetiUuid;
    }

    public void setRsetiUuid(UUID rsetiUuid) {
        this.rsetiUuid = rsetiUuid;
    }

    public UUID getTraineeProfileUuid() {
        return traineeProfileUuid;
    }

    public void setTraineeProfileUuid(UUID traineeProfileUuid) {
        this.traineeProfileUuid = traineeProfileUuid;
    }

    public UUID getCourseUuid() {
        return courseUuid;
    }

    public void setCourseUuid(UUID courseUuid) {
        this.courseUuid = courseUuid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}