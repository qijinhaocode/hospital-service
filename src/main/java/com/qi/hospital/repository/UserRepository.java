package com.qi.hospital.repository;

import com.qi.hospital.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
  Optional<User> findByUserName(String username);
}
