package com.qi.hospital.repository;

import com.qi.hospital.model.dcotor.DoctorTitle;
import com.qi.hospital.model.registrationFee.RegistrationFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationFeeRepository extends JpaRepository<RegistrationFee, DoctorTitle> {
    List<RegistrationFee> findAll();
}
