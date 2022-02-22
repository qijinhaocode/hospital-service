package com.qi.hospital.controller;

import com.qi.hospital.dto.user.UserLoginRequest;
import com.qi.hospital.dto.user.UserRequest;
import com.qi.hospital.dto.user.UserResponse;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping( "/user")
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
    public boolean userLogin(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        return userService.userLogin(userLoginRequest);
    }

    @GetMapping
    public List<UserResponse> getAAllUsers(){
        return userService.getAllUsers();
    }

}
