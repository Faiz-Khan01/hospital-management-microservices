package com.hospital.patient.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private int age;

    private String gender;

    @Column(name = "contact_number", nullable = false)
    private String contactNumber;

    private String disease;  // Added to fix getDisease() error

    private String address;  // Added to fix getAddress() error
}