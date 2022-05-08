package com.qi.hospital.service;


import com.qi.hospital.dto.registrationFee.RegistrationFeeUpdateRequest;
import com.qi.hospital.model.dcotor.DoctorTitle;
import com.qi.hospital.model.registrationFee.RegistrationFee;
import com.qi.hospital.repository.RegistrationFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistrationFeeService {
    private final RegistrationFeeRepository registrationFeeRepository;

    @Transactional
    public List<RegistrationFee> getALlRegistrationFee() {
        initRegistrationFeeTable();
        return registrationFeeRepository.findAll();
    }

    public void updateRegistrationFee(RegistrationFeeUpdateRequest updateRegistrationFeeRequest) {
        registrationFeeRepository.save(RegistrationFee
                .builder()
                .doctorTitle(updateRegistrationFeeRequest.getDoctorTitle())
                .registrationFee(updateRegistrationFeeRequest.getRegistrationFee()).build());
    }

    //初始化挂号费的表,把所有没有在表里面的DoctorTitle 存到表里
    public void initRegistrationFeeTable() {
        List<DoctorTitle> allDoctorTitles = List.of(DoctorTitle.values());
        Set<DoctorTitle> allRegistrationFeeDoctorTitle = getAllRegistrationFeeDoctorTitle();
        allDoctorTitles.stream()
                .filter(doctorTitle -> !allRegistrationFeeDoctorTitle.contains(doctorTitle))
                .map(doctorTitle -> RegistrationFee.builder().doctorTitle(doctorTitle).build())
                .forEach(registrationFeeRepository::save);
    }

    private Set<DoctorTitle> getAllRegistrationFeeDoctorTitle() {
        return registrationFeeRepository.findAll()
                .stream()
                .map(RegistrationFee::getDoctorTitle)
                .collect(Collectors.toSet());
    }
}
