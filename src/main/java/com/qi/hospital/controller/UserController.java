package com.qi.hospital.controller;

import com.qi.hospital.dto.user.AdminLoginRequest;
import com.qi.hospital.dto.user.UserCriteria;
import com.qi.hospital.dto.user.UserLoginRequest;
import com.qi.hospital.dto.user.UserRequest;
import com.qi.hospital.dto.user.UserResponse;
import com.qi.hospital.dto.user.UserUpdateRequest;
import com.qi.hospital.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Validated
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody @Valid UserRequest userRequest) {
        userService.userRegister(userRequest);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse userLogin(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        return userService.userLogin(userLoginRequest);
    }

    @PostMapping("/admin_login")
    @ResponseStatus(HttpStatus.OK)
    public boolean adminLogin(@RequestBody @Valid AdminLoginRequest adminLoginRequest) {
        return userService.adminLogin(adminLoginRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserInfo(@RequestHeader("token") String token) {
        return userService.getUserInfo(token);
    }

    @GetMapping("/query")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getSpecificUsers(@RequestParam(value = "userName", required = false) String userName, @RequestParam(value = "idNumber", required = false) String idNumber) {
        return userService.getUsersByNameAndIdNumber(UserCriteria.builder().userName(userName).idNumber(idNumber).build());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateUserInfo(@NotBlank @RequestHeader("token") String token, @RequestBody UserUpdateRequest userUpdateRequest) {
        return userService.updateUserInfo(token, userUpdateRequest);
    }

}
