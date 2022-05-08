package com.qi.hospital.controller;

import com.qi.hospital.dto.registrationFee.RegistrationFeeUpdateRequest;
import com.qi.hospital.model.registrationFee.RegistrationFee;
import com.qi.hospital.service.RegistrationFeeService;
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
@RequestMapping("/registration_fee")
@Validated
@CrossOrigin(origins = "*")
public class RegistrationFeeController {
    private final RegistrationFeeService registrationFeeService;

    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public List<RegistrationFee> getRegistrationFee() {
        return registrationFeeService.getALlRegistrationFee();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateRegistrationFee(@RequestBody @Valid RegistrationFeeUpdateRequest registrationFeeUpdateRequest) {
        registrationFeeService.updateRegistrationFee(registrationFeeUpdateRequest);
    }

    @PostMapping(value = "init_registration_fee")
    @ResponseStatus(HttpStatus.OK)
    public void initRegistrationFee() {
        registrationFeeService.initRegistrationFeeTable();
    }
}
