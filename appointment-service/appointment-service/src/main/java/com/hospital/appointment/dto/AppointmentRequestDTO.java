package com.hospital.appointment.dto;

import lombok.Data;

@Data
public class AppointmentRequestDTO {

    private Long id;
    private Long patientId;
    private Long doctorId;
    private String appointmentDate;
    private String status;
}