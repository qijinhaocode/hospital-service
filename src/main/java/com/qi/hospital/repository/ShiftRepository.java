package com.qi.hospital.repository;

import com.qi.hospital.model.dcotor.Doctor;
import com.qi.hospital.model.dcotor.DoctorTitle;
import com.qi.hospital.model.shift.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<Shift,String>, JpaSpecificationExecutor<Shift> {
    Optional<Shift> findByDoctorJobNumber(String doctorJobNumber);

    Optional<Shift> deleteByDoctorJobNumber(String doctorJobNumber);

    List<Shift> findByDoctorJobNumberIn(List<String> jobNumbers);
}
