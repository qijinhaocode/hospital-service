package com.qi.hospital.repository;

import com.qi.hospital.model.Section;
import com.qi.hospital.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section,String> {

  List<Section> findByNameContaining(String name);

  Optional<Section> findByName(String name);

  List<Section> findAll();

  void deleteByName(String name);

}
