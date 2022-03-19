package com.qi.hospital.service;


import com.qi.hospital.dto.doctor.DoctorQueryCriteria;
import com.qi.hospital.dto.doctor.DoctorRequest;
import com.qi.hospital.dto.doctor.DoctorResponse;
import com.qi.hospital.dto.doctor.DoctorUpdateRequest;
import com.qi.hospital.exception.BusinessException;
import com.qi.hospital.exception.CommonErrorCode;
import com.qi.hospital.mapper.DoctorMapper;
import com.qi.hospital.model.dcotor.Doctor;
import com.qi.hospital.model.dcotor.DoctorTitle;
import com.qi.hospital.model.registrationFee.RegistrationFee;
import com.qi.hospital.model.section.Section;
import com.qi.hospital.repository.DoctorRepository;
import com.qi.hospital.repository.RegistrationFeeRepository;
import com.qi.hospital.util.JpaUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final SectionService sectionService;
    private final RegistrationFeeRepository registrationFeeRepository;

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
        List<RegistrationFee> allRegistrationFee = registrationFeeRepository.findAll();
        Map<DoctorTitle, RegistrationFee> doctorTitleRegistrationFeeMap = allRegistrationFee
                .stream()
                .collect(Collectors.toMap(RegistrationFee::getDoctorTitle, Function.identity()));
        return doctors.stream().map(doctor -> {
            DoctorResponse doctorResponse = doctorMapper.toDoctorResponse(doctor);
            doctorResponse.setSection(sectionGroupById.get(doctor.getSectionId()));
            doctorResponse.setRegistrationFee(doctorTitleRegistrationFeeMap.get(doctor.getTitle()).getRegistrationFee());
            return doctorResponse;
        }).collect(Collectors.toList());
    }

    public List<DoctorResponse> getDoctorsByCondition(DoctorQueryCriteria doctorQueryCriteria) {
        Specification<Doctor> specification = new Specification<Doctor>() {
            @Override
            public Predicate toPredicate(Root<Doctor> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new LinkedList<>();
                if (StringUtils.isNotBlank(doctorQueryCriteria.getName())) {
                    predicates.add(cb.like(root.get("name"), "%" + doctorQueryCriteria.getName() + "%")); // 查询name
                }
                if (Objects.nonNull(doctorQueryCriteria.getTitle())) {
                    predicates.add(cb.equal(root.get("title"), doctorQueryCriteria.getTitle())); // 查询title
                }
                if (StringUtils.isNotBlank(doctorQueryCriteria.getJobNumber())) {
                    predicates.add(cb.equal(root.get("jobNumber"), doctorQueryCriteria.getJobNumber())); // 查询工号
                }
                if (StringUtils.isNotBlank(doctorQueryCriteria.getSectionId())) {
                    predicates.add(cb.equal(root.get("sectionId"), doctorQueryCriteria.getSectionId())); // 查询科室
                }

                return query.where(predicates.toArray(new Predicate[0])).getRestriction();
            }
        };
        List<Doctor> doctors = doctorRepository.findAll(specification, Sort.by(Sort.Direction.ASC, "jobNumber"));
        List<Section> sections = sectionService.getAllSection();
        Map<String, Section> sectionGroupById = sections.stream().collect(Collectors.toMap(Section::getId, Function.identity()));
        //get registration fee map
        List<RegistrationFee> allRegistrationFee = registrationFeeRepository.findAll();
        Map<DoctorTitle, RegistrationFee> doctorTitleRegistrationFeeMap = allRegistrationFee.stream().collect(Collectors.toMap(RegistrationFee::getDoctorTitle, Function.identity()));

        return doctors.stream().map(doctor -> {
            DoctorResponse doctorResponse = doctorMapper.toDoctorResponse(doctor);
            doctorResponse.setSection(sectionGroupById.get(doctor.getSectionId()));
            //todo Nullpointexception deal
            doctorResponse.setRegistrationFee(doctorTitleRegistrationFeeMap.get(doctor.getTitle()).getRegistrationFee());
            return doctorResponse;
        }).collect(Collectors.toList());
    }


}
