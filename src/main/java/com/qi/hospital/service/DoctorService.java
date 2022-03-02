package com.qi.hospital.service;


import com.qi.hospital.dto.doctor.DoctorRequest;
import com.qi.hospital.dto.doctor.DoctorUpdateRequest;
import com.qi.hospital.exception.BusinessException;
import com.qi.hospital.exception.CommonErrorCode;
import com.qi.hospital.mapper.DoctorMapper;
import com.qi.hospital.model.dcotor.Doctor;
import com.qi.hospital.repository.DoctorRepository;
import com.qi.hospital.util.JpaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    public Doctor createDoctor(DoctorRequest doctorRequest) {
        Optional<Doctor> doctor = doctorRepository.findByJobNumber(doctorRequest.getJobNumber());
        if (doctor.isPresent()) {
            throw new BusinessException(CommonErrorCode.E_100107);
        }
        return doctorRepository.save(doctorMapper.toDoctor(doctorRequest));
    }

    @Transactional
    public void deleteDoctor(String jobNumber) {
        Optional<Doctor> doctor = doctorRepository.findByJobNumber(jobNumber);
        if (doctor.isPresent()) {
            doctorRepository.deleteByJobNumber(jobNumber);
            return;
        }
        throw new BusinessException(CommonErrorCode.E_100110);
    }

    @Transactional
    public void updateSection(DoctorUpdateRequest doctorUpdateRequest) {
        Optional<Doctor> doctorOriginOptional = doctorRepository.findByJobNumber(doctorUpdateRequest.getJobNumber());
        if (!doctorOriginOptional.isPresent()){
            throw new BusinessException(CommonErrorCode.E_100109);
        }
        Doctor doctorOrigin = doctorOriginOptional.get();
        Doctor doctorSrc = doctorMapper.toDoctor(doctorUpdateRequest);
        JpaUtil.copyNotNullProperties(doctorSrc, doctorOrigin);
        doctorRepository.save(doctorOrigin);
    }

    public List<Doctor> getAllDoctors(){
        return doctorRepository.findAll();
    }


}
