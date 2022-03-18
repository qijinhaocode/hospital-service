package com.qi.hospital.repository;

import com.qi.hospital.model.shift.Shift;
import com.qi.hospital.model.shift.ShiftSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftScheduleRepository extends JpaRepository<ShiftSchedule,String>, JpaSpecificationExecutor<Shift> {
    Optional<Shift> findByDoctorJobNumber(String doctorJobNumber);
    Optional<Shift> deleteByDoctorJobNumber(String doctorJobNumber);
    List<Shift> findByDoctorJobNumberIn(List<String> jobNumbers);


}
