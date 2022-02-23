package com.qi.hospital.service;


import com.qi.hospital.dto.user.AdminLoginRequest;
import com.qi.hospital.dto.user.UserLoginRequest;
import com.qi.hospital.dto.user.UserRequest;
import com.qi.hospital.dto.user.UserResponse;
import com.qi.hospital.dto.user.UserUpdateRequest;
import com.qi.hospital.exception.BusinessException;
import com.qi.hospital.exception.CommonErrorCode;
import com.qi.hospital.mapper.UserMapper;
import com.qi.hospital.model.User;
import com.qi.hospital.repository.UserRepository;
import com.qi.hospital.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
        if (userRequest.getUserName().equals(Constants.ADMIN_USERNAME)) {
            throw new BusinessException(CommonErrorCode.E_100102);
        }
        userRepository.save(userMapper.toUser(userRequest));
    }

    public boolean userLogin(UserLoginRequest userRequest) {
        // judge is normal user
        Optional<User> user = userRepository.findByPhoneNumber(userRequest.getPhoneNumber());
        if(user.isPresent() && isMatchNormalPhoneNumberAndPassword(userRequest, user.get())){
            return true;
        }
        //username and password not match
        throw new BusinessException(CommonErrorCode.E_100103);
    }

    public boolean adminLogin(AdminLoginRequest adminLoginRequest) {
        //judge is admin
        if (isMatchAdminUsernameAndPassword(adminLoginRequest)){
            return true;
        }

        //username and password not match
        throw new BusinessException(CommonErrorCode.E_100103);
    }
    private boolean isMatchNormalPhoneNumberAndPassword(UserLoginRequest userLoginRequest, User user) {
        return user.getUserName().equals(userLoginRequest.getPhoneNumber()) &&
                user.getPassword().equals((userLoginRequest.getPassword()));
    }

    private boolean isMatchAdminUsernameAndPassword(AdminLoginRequest adminLoginRequest) {
        return adminLoginRequest.getUserName().equals(Constants.ADMIN_USERNAME) &&
                adminLoginRequest.getPassword().equals((Constants.ADMIN_PASSWORD));
    }

    public List<UserResponse> getAllUsers() {
        return userMapper.toResponses(userRepository.findAll());
    }
}
