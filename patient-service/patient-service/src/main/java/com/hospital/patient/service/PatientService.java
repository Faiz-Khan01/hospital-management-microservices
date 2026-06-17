package com.hospital.patient.service;

import com.hospital.patient.dto.PatientDto;
import com.hospital.patient.entity.Patient;
import com.hospital.patient.exception.PatientNotFoundException;
import com.hospital.patient.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository repository;

    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    // FIX: Change parameter from 'Patient dto' to 'PatientDto dto'
    public PatientDto save(PatientDto dto) {
        Patient patient = convertToEntity(dto); // Ab ye bilkul sahi chalega
        Patient savedPatient = repository.save(patient);
        return convertToDto(savedPatient);
    }

    public List<PatientDto> getAll() {
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PatientDto getById(Long id) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient with ID " + id + " does not exist."));
        return convertToDto(patient);
    }

    // Helper method for DTO conversion
    private PatientDto convertToDto(Patient patient) {
        return PatientDto.builder()
                .id(patient.getId())
                .name(patient.getName())
                .age(patient.getAge())
                .gender(patient.getGender())
                .contactNumber(patient.getContactNumber())
                .address(patient.getAddress())
                .disease(patient.getDisease())
                .build();
    }

    // Helper method for Entity conversion
    private Patient convertToEntity(PatientDto dto) {
        return Patient.builder()
                .id(dto.getId())
                .name(dto.getName())
                .age(dto.getAge())
                .gender(dto.getGender())
                .contactNumber(dto.getContactNumber())
                .address(dto.getAddress())
                .disease(dto.getDisease())
                .build();
    }
}