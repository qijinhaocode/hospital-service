package com.qi.hospital.service;


import com.qi.hospital.dto.user.UserLoginRequest;
import com.qi.hospital.dto.user.UserRequest;
import com.qi.hospital.exception.BusinessException;
import com.qi.hospital.exception.CommonErrorCode;
import com.qi.hospital.mapper.UserMapper;
import com.qi.hospital.model.User;
import com.qi.hospital.repository.UserRepository;
import com.qi.hospital.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void userRegister(UserRequest userRequest) {
        Optional<User> user = userRepository.findByUserName(userRequest.getUserName());
        if (user.isPresent()) {
            throw new BusinessException(CommonErrorCode.E_100101);
        }
        if (user.get().equals(Constants.ADMIN_USERNAME)) {
            throw new BusinessException(CommonErrorCode.E_100102);
        }
        userRepository.save(userMapper.toUser(userRequest));
    }

    public boolean userLogin(UserLoginRequest userRequest) {
        //judge is admin
        if (isMatchAdminUsernameAndPassword(userRequest)){
            return true;
        }
        // judge is normal user
        Optional<User> user = userRepository.findByUserName(userRequest.getUserName());
        if(user.isPresent() && isMatchNormalUsernameAndPassword(userRequest, user.get())){
            return true;
        }
        //username and password not match
        throw new BusinessException(CommonErrorCode.E_100103);
    }

    private boolean isMatchNormalUsernameAndPassword(UserLoginRequest userLoginRequest, User user) {
        return user.getUserName().equals(userLoginRequest.getUserName()) &&
                user.getPassword().equals((userLoginRequest.getPassword()));
    }

    private boolean isMatchAdminUsernameAndPassword(UserLoginRequest userLoginRequest) {
        return userLoginRequest.getUserName().equals(Constants.ADMIN_USERNAME) &&
                userLoginRequest.getPassword().equals((Constants.ADMIN_PASSWORD));
    }
}
