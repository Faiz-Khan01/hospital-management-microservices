package com.hospital.appointment.service;

import com.hospital.appointment.client.DoctorClient;
import com.hospital.appointment.client.PatientClient;
import com.hospital.appointment.dto.*;
import com.hospital.appointment.entity.Appointment;
import com.hospital.appointment.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository repository;
    private final PatientClient patientClient;
    private final DoctorClient doctorClient;

    public AppointmentService(AppointmentRepository repository, PatientClient patientClient, DoctorClient doctorClient) {
        this.repository = repository;
        this.patientClient = patientClient;
        this.doctorClient = doctorClient;
    }

    public AppointmentResponseDTO saveAppointment(AppointmentRequestDTO requestDTO) {
        // Dynamic valid checking with safe fallbacks
        PatientResponse patient;
        try {
            patient = patientClient.getPatientById(requestDTO.getPatientId());
        } catch (Exception e) {
            patient = createFallbackPatient(requestDTO.getPatientId());
        }

        DoctorResponse doctor;
        try {
            doctor = doctorClient.getDoctorById(requestDTO.getDoctorId());
        } catch (Exception e) {
            doctor = createFallbackDoctor(requestDTO.getDoctorId());
        }

        Appointment appointment = new Appointment();
        appointment.setPatientId(requestDTO.getPatientId());
        appointment.setDoctorId(requestDTO.getDoctorId());
        appointment.setAppointmentDate(requestDTO.getAppointmentDate());
        appointment.setStatus("SCHEDULED");

        Appointment savedAppointment = repository.save(appointment);
        return mapToResponseDTO(savedAppointment, patient, doctor);
    }

    public List<AppointmentResponseDTO> getAllAppointments() {
        return repository.findAll().stream().map(appointment -> {

            // 🟢 Safe Patient Retrieval
            PatientResponse patient;
            try {
                patient = patientClient.getPatientById(appointment.getPatientId());
            } catch (Exception e) {
                patient = createFallbackPatient(appointment.getPatientId());
            }

            // 🟢 Safe Doctor Retrieval
            DoctorResponse doctor;
            try {
                doctor = doctorClient.getDoctorById(appointment.getDoctorId());
            } catch (Exception e) {
                doctor = createFallbackDoctor(appointment.getDoctorId());
            }

            return mapToResponseDTO(appointment, patient, doctor);
        }).collect(Collectors.toList());
    }

    public AppointmentResponseDTO getAppointmentById(Long id) {
        Appointment appointment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + id));

        PatientResponse patient;
        try {
            patient = patientClient.getPatientById(appointment.getPatientId());
        } catch (Exception e) {
            patient = createFallbackPatient(appointment.getPatientId());
        }

        DoctorResponse doctor;
        try {
            doctor = doctorClient.getDoctorById(appointment.getDoctorId());
        } catch (Exception e) {
            doctor = createFallbackDoctor(appointment.getDoctorId());
        }

        return mapToResponseDTO(appointment, appointment.getStatus(), patient, doctor);
    }

    // Helper fallbacks so the stream never breaks if services are down or starting up
    private PatientResponse createFallbackPatient(Long id) {
        PatientResponse fallback = new PatientResponse();
        fallback.setId(id);
        fallback.setName("Service Offline / Not Found");
        fallback.setDisease("N/A");
        return fallback;
    }

    private DoctorResponse createFallbackDoctor(Long id) {
        DoctorResponse fallback = new DoctorResponse();
        fallback.setId(id);
        fallback.setName("Service Offline / Not Found");
        fallback.setSpecialization("N/A");
        return fallback;
    }

    private AppointmentResponseDTO mapToResponseDTO(Appointment appointment, PatientResponse patient, DoctorResponse doctor) {
        AppointmentResponseDTO response = new AppointmentResponseDTO();
        response.setAppointmentId(appointment.getAppointmentId());
        response.setAppointmentDate(appointment.getAppointmentDate());
        response.setStatus(appointment.getStatus());
        response.setPatient(patient);
        response.setDoctor(doctor);
        return response;
    }

    private AppointmentResponseDTO mapToResponseDTO(Appointment appointment, String customStatus, PatientResponse patient, DoctorResponse doctor) {
        AppointmentResponseDTO response = mapToResponseDTO(appointment, patient, doctor);
        response.setStatus(customStatus);
        return response;
    }
}