package com.qi.hospital.service;


import com.qi.hospital.dto.registrationFee.RegistrationFeeUpdateRequest;
import com.qi.hospital.mapper.RegistrationFeeMapper;
import com.qi.hospital.model.dcotor.DoctorTitle;
import com.qi.hospital.model.registrationFee.RegistrationFee;
import com.qi.hospital.repository.RegistrationFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistrationFeeService {
    private final RegistrationFeeRepository registrationFeeRepository;
    private final RegistrationFeeMapper registrationFeeMapper;

    //初始化挂号费的表
    public void initRegistrationFee() {
        List<DoctorTitle> allDoctorTitles = List.of(DoctorTitle.values());
        //get all doctorTitle from db
        Set<DoctorTitle> allRegistrationFeeDoctorTitle = getAllRegistrationFeeDoctorTitle();
        allDoctorTitles.forEach(doctorTitle -> {
            if (!allRegistrationFeeDoctorTitle.contains(doctorTitle)) {
                registrationFeeRepository.save(RegistrationFee
                        .builder()
                        .doctorTitle(doctorTitle).build());
            }
        });
    }

    private Set<DoctorTitle> getAllRegistrationFeeDoctorTitle() {
        return registrationFeeRepository.findAll()
                .stream()
                .map(RegistrationFee::getDoctorTitle)
                .collect(Collectors.toSet());
    }

    public void updateRegistrationFee(RegistrationFeeUpdateRequest updateRegistrationFeeRequest){
        registrationFeeRepository.save(RegistrationFee
                .builder()
                .doctorTitle(updateRegistrationFeeRequest.getDoctorTitle())
                .registrationFee(updateRegistrationFeeRequest.getRegistrationFee()).build());
    }

    public List<RegistrationFee> getALlRegistrationFee(){
        return registrationFeeRepository.findAll();
    }

}
