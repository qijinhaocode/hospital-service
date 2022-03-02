package com.qi.hospital.repository;

import com.qi.hospital.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
  Optional<User> findByUserName(String username);

  Optional<User> findByPhoneNumber(String phoneNumber);

  List<User> findAll();

  List<User> findAllByUserName(String userName);

  List<User> findAllByIdNumber(String idNumber);

  List<User> findAllByUserNameAndIdNumber(String userName, String idNumber);
}
