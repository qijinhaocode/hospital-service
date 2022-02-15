package com.qi.hospital.service;


import com.qi.hospital.dto.user.UserRequest;
import com.qi.hospital.exception.BusinessException;
import com.qi.hospital.exception.CommonErrorCode;
import com.qi.hospital.mapper.UserMapper;
import com.qi.hospital.model.User;
import com.qi.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

  public void userRegister(UserRequest userRequest){
    Optional<User> user = userRepository.findByUserName(userRequest.getUserName());
    if (user.isPresent()){
        throw new BusinessException(CommonErrorCode.E_100101);
    }
    userRepository.save(userMapper.toUser(userRequest));
  }
}
