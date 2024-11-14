package com.tl.reap_admin_api.mapper;

import com.tl.reap_admin_api.dto.TraineeProfileDto;
import com.tl.reap_admin_api.model.TraineeProfile;
import org.springframework.stereotype.Component;

@Component
public class TraineeProfileMapper {

    public TraineeProfileDto toDTO(TraineeProfile traineeProfile) {
        if (traineeProfile == null) {
            return null;
        }
        return new TraineeProfileDto(
            traineeProfile.getUuid(),
            traineeProfile.getEnrollId(),
            traineeProfile.getEnrolledOn(),
            traineeProfile.getStatus(),
            traineeProfile.getBatchNo(),
            traineeProfile.getCandidateName(),
            traineeProfile.getFatherNameOrHusbandName(),
            traineeProfile.getMaritalStatus(),
            traineeProfile.getAge(),
            traineeProfile.getReligion(),
            traineeProfile.getCaste(),
            traineeProfile.getEducation(),
            traineeProfile.getPersonWithDisability(),
            traineeProfile.getSex(),
            traineeProfile.getPovertyLine(),
            traineeProfile.getPovertyLineNumberOrRationCardNumber(),
            traineeProfile.getSecc(),
            traineeProfile.getSeccNo(),
            traineeProfile.getPanNumber(),
            traineeProfile.getResidential(),
            traineeProfile.getDateOfBirth(),
            traineeProfile.getAadharCardNo(),
            traineeProfile.getLandlineStd(),
            traineeProfile.getLandlineNumber(),
            traineeProfile.getMobileNumber1(),
            traineeProfile.getMobileNumber2(),
            traineeProfile.getSgsyCandidate(),
            traineeProfile.getFamilyOccupation(),
            traineeProfile.getCandidatePresentOccupation(),
            traineeProfile.getNativityArea(),
            traineeProfile.getCandidateAddress(),
            traineeProfile.getVillage(),
            traineeProfile.getHobli(),
            traineeProfile.getDistrict(),
            traineeProfile.getTaluk(),
            traineeProfile.getPincode(),
            traineeProfile.getCandidateSponsoredByBank(),
            traineeProfile.getSponsoredBankName(),
            traineeProfile.getSponsoredBankBranch(),
            traineeProfile.getSponsoredBankCity(),
            traineeProfile.getSponsorName(),
            traineeProfile.getRelevantExperience(),
            traineeProfile.getNameOfShg(),
            traineeProfile.getFamilyMember(),
            traineeProfile.getEmail(),
            traineeProfile.getMnergaCardNo()
        );
    }

    public TraineeProfile toEntity(TraineeProfileDto dto) {
        if (dto == null) {
            return null;
        }
        TraineeProfile traineeProfile = new TraineeProfile();
        updateEntityFromDTO(dto, traineeProfile);
        return traineeProfile;
    }

    public void updateEntityFromDTO(TraineeProfileDto dto, TraineeProfile traineeProfile) {
        if (dto == null || traineeProfile == null) {
            return;
        }
        traineeProfile.setUuid(dto.getUuid());
        traineeProfile.setEnrollId(dto.getEnrollId());
        traineeProfile.setEnrolledOn(dto.getEnrolledOn());
        traineeProfile.setStatus(dto.getStatus());
        traineeProfile.setBatchNo(dto.getBatchNo());
        traineeProfile.setCandidateName(dto.getCandidateName());
        traineeProfile.setFatherNameOrHusbandName(dto.getFatherNameOrHusbandName());
        traineeProfile.setMaritalStatus(dto.getMaritalStatus());
        traineeProfile.setAge(dto.getAge());
        traineeProfile.setReligion(dto.getReligion());
        traineeProfile.setCaste(dto.getCaste());
        traineeProfile.setEducation(dto.getEducation());
        traineeProfile.setPersonWithDisability(dto.getPersonWithDisability());
        traineeProfile.setSex(dto.getSex());
        traineeProfile.setPovertyLine(dto.getPovertyLine());
        traineeProfile.setPovertyLineNumberOrRationCardNumber(dto.getPovertyLineNumberOrRationCardNumber());
        traineeProfile.setSecc(dto.getSecc());
        traineeProfile.setSeccNo(dto.getSeccNo());
        traineeProfile.setPanNumber(dto.getPanNumber());
        traineeProfile.setResidential(dto.getResidential());
        traineeProfile.setDateOfBirth(dto.getDateOfBirth());
        traineeProfile.setAadharCardNo(dto.getAadharCardNo());
        traineeProfile.setLandlineStd(dto.getLandlineStd());
        traineeProfile.setLandlineNumber(dto.getLandlineNumber());
        traineeProfile.setMobileNumber1(dto.getMobileNumber1());
        traineeProfile.setMobileNumber2(dto.getMobileNumber2());
        traineeProfile.setSgsyCandidate(dto.getSgsyCandidate());
        traineeProfile.setFamilyOccupation(dto.getFamilyOccupation());
        traineeProfile.setCandidatePresentOccupation(dto.getCandidatePresentOccupation());
        traineeProfile.setNativityArea(dto.getNativityArea());
        traineeProfile.setCandidateAddress(dto.getCandidateAddress());
        traineeProfile.setVillage(dto.getVillage());
        traineeProfile.setHobli(dto.getHobli());
        traineeProfile.setDistrict(dto.getDistrict());
        traineeProfile.setTaluk(dto.getTaluk());
        traineeProfile.setPincode(dto.getPincode());
        traineeProfile.setCandidateSponsoredByBank(dto.getCandidateSponsoredByBank());
        traineeProfile.setSponsoredBankName(dto.getSponsoredBankName());
        traineeProfile.setSponsoredBankBranch(dto.getSponsoredBankBranch());
        traineeProfile.setSponsoredBankCity(dto.getSponsoredBankCity());
        traineeProfile.setSponsorName(dto.getSponsorName());
        traineeProfile.setRelevantExperience(dto.getRelevantExperience());
        traineeProfile.setNameOfShg(dto.getNameOfShg());
        traineeProfile.setFamilyMember(dto.getFamilyMember());
        traineeProfile.setEmail(dto.getEmail());
        traineeProfile.setMnergaCardNo(dto.getMnergaCardNo());
    }
}