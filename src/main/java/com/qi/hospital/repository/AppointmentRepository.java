package com.qi.hospital.repository;

import com.qi.hospital.model.appointment.Appointment;
import com.qi.hospital.model.appointment.AppointmentStatus;
import com.qi.hospital.model.appointment.AppointmentTime;
import com.qi.hospital.model.shift.ShiftSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String>, JpaSpecificationExecutor<Appointment> {
    Optional<Appointment> findByUserIdAndDoctorJobNumberAndLocalDate(String userId, String doctorNumber, LocalDate localDate);

    Optional<Appointment> findByUserIdAndDoctorJobNumberAndLocalDateAndAppointmentStatus(String userId, String doctorNumber, LocalDate localDate, AppointmentStatus appointmentStatus);

    List<Appointment> findByUserIdOrderByPayTimeDesc(String userId);

    List<Appointment> findByLocalDateInOrderByPayTimeDesc(List<LocalDate> localDates);

    List<Appointment> findByDoctorJobNumberAndLocalDate(String doctorNumber, LocalDate localDate);
}
