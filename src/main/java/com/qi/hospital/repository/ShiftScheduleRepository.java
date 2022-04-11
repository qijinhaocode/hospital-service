package com.qi.hospital.repository;

import com.qi.hospital.model.shift.ShiftSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftScheduleRepository extends JpaRepository<ShiftSchedule,String>, JpaSpecificationExecutor<ShiftSchedule> {
    Optional<ShiftSchedule> findByLocalDateAndDoctorJobNumber(LocalDate localDate, String jonNumber);

    List<ShiftSchedule> findByLocalDateInOrderByLocalDate(List<LocalDate> localDates);

}
