package com.qi.hospital.repository;

import com.qi.hospital.model.dcotor.Doctor;
import com.qi.hospital.model.dcotor.DoctorTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,String>, JpaSpecificationExecutor<Doctor> {


  Optional<Doctor> findByJobNumber(String jobNumber);

  void deleteByJobNumber(String jobNumber);

  List<Doctor> findByJobNumberContaining(String jobNumber);

  List<Doctor> findByNameContaining(String name);

  List<Doctor> findByTitle(DoctorTitle doctorTitle);

  List<Doctor> findAll();

}
