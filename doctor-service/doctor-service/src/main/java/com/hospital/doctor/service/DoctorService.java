package com.hospital.doctor.service;

import com.hospital.doctor.dto.DoctorDto;
import com.hospital.doctor.entity.Doctor;
import com.hospital.doctor.exception.ResourceNotFoundException; // Import custom exception
import com.hospital.doctor.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository repository;

    public DoctorService(DoctorRepository repository) {
        this.repository = repository;
    }

    public DoctorDto save(DoctorDto dto) {
        Doctor doctor = convertToEntity(dto);
        Doctor savedDoctor = repository.save(doctor);
        return convertToDto(savedDoctor);
    }

    public List<DoctorDto> getAll() {
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public DoctorDto getById(Long id) {
        // Updated to use ResourceNotFoundException
        Doctor doctor = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with ID " + id + " not found"));
        return convertToDto(doctor);
    }

    // Helper: Convert Entity to DTO
    private DoctorDto convertToDto(Doctor doctor) {
        return DoctorDto.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .specialization(doctor.getSpecialization())
                .contactNumber(doctor.getContactNumber())
                .email(doctor.getEmail())
                .experience(doctor.getExperience())
                .build();
    }

    // Helper: Convert DTO to Entity
    private Doctor convertToEntity(DoctorDto dto) {
        return Doctor.builder()
                .id(dto.getId())
                .name(dto.getName())
                .specialization(dto.getSpecialization())
                .contactNumber(dto.getContactNumber())
                .email(dto.getEmail())
                .experience(dto.getExperience())
                .build();
    }
}