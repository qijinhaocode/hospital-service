package com.qi.hospital.repository;

import com.qi.hospital.model.advice.Advice;
import com.qi.hospital.model.section.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdviceRepository extends JpaRepository<Advice,String> {
    List<Advice> findAllByOrderByCreateDateTimeDesc();
}
