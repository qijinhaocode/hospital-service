package com.qi.hospital.service;


import com.qi.hospital.dto.user.AdminLoginRequest;
import com.qi.hospital.dto.user.UserCriteria;
import com.qi.hospital.dto.user.UserLoginRequest;
import com.qi.hospital.dto.user.UserRequest;
import com.qi.hospital.dto.user.UserResponse;
import com.qi.hospital.dto.user.UserUpdateRequest;
import com.qi.hospital.exception.BusinessException;
import com.qi.hospital.exception.CommonErrorCode;
import com.qi.hospital.mapper.UserMapper;
import com.qi.hospital.model.user.User;
import com.qi.hospital.repository.UserRepository;
import com.qi.hospital.util.Constants;
import com.qi.hospital.util.JpaUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void userRegister(UserRequest userRequest) {
        Optional<User> user = userRepository.findByPhoneNumber(userRequest.getPhoneNumber());
        if (user.isPresent()) {
            throw new BusinessException(CommonErrorCode.E_100101);
        }
        if (userRequest.getUserName().equals(Constants.ADMIN_USERNAME)) {
            throw new BusinessException(CommonErrorCode.E_100102);
        }
        userRepository.save(userMapper.toUser(userRequest));
    }

    public UserResponse userLogin(UserLoginRequest userRequest) {
        // judge is normal user
        Optional<User> user = userRepository.findByPhoneNumber(userRequest.getPhoneNumber());
        if (user.isPresent() && isMatchNormalPhoneNumberAndPassword(userRequest, user.get())) {
            return userMapper.toResponse(user.get());
        }
        //username and password not match
        throw new BusinessException(CommonErrorCode.E_100103);
    }

    public boolean adminLogin(AdminLoginRequest adminLoginRequest) {
        //judge is admin
        if (isMatchAdminUsernameAndPassword(adminLoginRequest)) {
            return true;
        }

        //username and password not match
        throw new BusinessException(CommonErrorCode.E_100103);
    }

    private boolean isMatchNormalPhoneNumberAndPassword(UserLoginRequest userLoginRequest, User user) {
        return user.getPhoneNumber().equals(userLoginRequest.getPhoneNumber()) &&
                user.getPassword().equals((userLoginRequest.getPassword()));
    }

    private boolean isMatchAdminUsernameAndPassword(AdminLoginRequest adminLoginRequest) {
        return adminLoginRequest.getUserName().equals(Constants.ADMIN_USERNAME) &&
                adminLoginRequest.getPassword().equals((Constants.ADMIN_PASSWORD));
    }

    public List<UserResponse> getAllUsers() {
        return userMapper.toResponses(userRepository.findAll());
    }

    public List<UserResponse> getUsersByNameAndIdNumber(UserCriteria userCriteria) {
        if (userCriteria.getUserName().isEmpty() && userCriteria.getIdNumber().isEmpty()) {
            return Collections.emptyList();
        } else if (userCriteria.getUserName().isEmpty()) {
            return userMapper.toResponses(userRepository.findAllByIdNumber(userCriteria.getIdNumber()));
        } else if (userCriteria.getIdNumber().isEmpty()) {
            return userMapper.toResponses(userRepository.findAllByUserName(userCriteria.getUserName()));
        } else
            return userMapper.toResponses(userRepository.findAllByUserNameAndIdNumber(userCriteria.getUserName(), userCriteria.getIdNumber()));
    }

    public UserResponse getUserInfo(String token) {
        Optional<User> userOptional = userRepository.findByPhoneNumber(token);
        validateUserExist(userOptional);
        return userMapper.toResponse(userOptional.get());
    }

    private void validateUserExist(Optional<User> userOptional) {
        if (!userOptional.isPresent()) {
            throw new BusinessException(CommonErrorCode.E_100103);
        }
    }

    public UserResponse updateUserInfo(String token, UserUpdateRequest userUpdateRequest) {
        //find user in db
        Optional<User> userOptional = userRepository.findByPhoneNumber(token);

        //judge user is exist
        validateUserExist(userOptional);
        User userOriginInDB = userOptional.get();
        //change password
        if (StringUtils.isNotBlank(userUpdateRequest.getOriginPassword()) && StringUtils.isNotBlank(userUpdateRequest.getNewPassword())) {
            // 判断密码是否正确
            if (userOriginInDB.getPassword().equals(userUpdateRequest.getOriginPassword())) {
                User userSrcRequest = userMapper.toUser(userUpdateRequest);
                JpaUtil.copyNotNullProperties(userSrcRequest, userOptional.get());
                User userSaveInDB = userRepository.save(userOriginInDB);
                return userMapper.toResponse(userSaveInDB);
            }
            throw new BusinessException(CommonErrorCode.E_100116);
        }
        User userSrcRequest = userMapper.toUser(userUpdateRequest);
        JpaUtil.copyNotNullProperties(userSrcRequest, userOptional.get());
        User userSaveInDB = userRepository.save(userOriginInDB);
        return userMapper.toResponse(userSaveInDB);
    }
}
