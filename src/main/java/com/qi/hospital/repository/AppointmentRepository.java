package com.qi.hospital.repository;

import com.qi.hospital.model.appointment.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String>, JpaSpecificationExecutor<Appointment> {
    Optional<Appointment> findByUserIdAndDoctorJobNumberAndLocalDate(String userId, String doctorNumber, LocalDate localDate);

    List<Appointment> findByUserId(String userId);

    List<Appointment> findByLocalDate(LocalDate localDate);
}
