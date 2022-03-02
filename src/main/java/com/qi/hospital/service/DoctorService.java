package com.qi.hospital.service;


import com.qi.hospital.dto.doctor.DoctorRequest;
import com.qi.hospital.dto.doctor.DoctorResponse;
import com.qi.hospital.dto.doctor.DoctorUpdateRequest;
import com.qi.hospital.exception.BusinessException;
import com.qi.hospital.exception.CommonErrorCode;
import com.qi.hospital.mapper.DoctorMapper;
import com.qi.hospital.model.dcotor.Doctor;
import com.qi.hospital.model.section.Section;
import com.qi.hospital.repository.DoctorRepository;
import com.qi.hospital.util.JpaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final SectionService sectionService;

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
        if (!doctorOriginOptional.isPresent()) {
            throw new BusinessException(CommonErrorCode.E_100109);
        }
        Doctor doctorOrigin = doctorOriginOptional.get();
        Doctor doctorSrc = doctorMapper.toDoctor(doctorUpdateRequest);
        JpaUtil.copyNotNullProperties(doctorSrc, doctorOrigin);
        doctorRepository.save(doctorOrigin);
    }

    public List<DoctorResponse> getAllDoctors() {
        List<Section> sections = sectionService.getAllSection();
        Map<String, Section> sectionGroupById = sections.stream().collect(Collectors.toMap(Section::getId, Function.identity()));
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream().map(doctor -> {
            DoctorResponse doctorResponse = doctorMapper.toDoctorResponse(doctor);
            doctorResponse.setSection(sectionGroupById.get(doctor.getSectionId()));
            return doctorResponse;
        }).collect(Collectors.toList());
    }


}
