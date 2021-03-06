package com.qi.hospital.service;


import com.qi.hospital.dto.doctor.DoctorQueryCriteria;
import com.qi.hospital.dto.doctor.DoctorResponse;
import com.qi.hospital.dto.shift.ShiftRequest;
import com.qi.hospital.dto.shift.ShiftResponse;
import com.qi.hospital.dto.shift.ShiftUpdateRequest;
import com.qi.hospital.exception.BusinessException;
import com.qi.hospital.exception.CommonErrorCode;
import com.qi.hospital.mapper.ShiftMapper;
import com.qi.hospital.model.shift.Shift;
import com.qi.hospital.repository.ShiftRepository;
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
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;
    private final DoctorService doctorService;

    public Shift createShift(ShiftRequest shiftRequest) {
        Optional<Shift> shift = shiftRepository.findByDoctorJobNumber(shiftRequest.getDoctorJobNumber());
        if (shift.isPresent()) {
            throw new BusinessException(CommonErrorCode.E_100107);
        }
        return shiftRepository.save(shiftMapper.toShift(shiftRequest));
    }

    @Transactional
    public void deleteShift(String doctorJobNumber) {
        Optional<Shift> shift = shiftRepository.findByDoctorJobNumber(doctorJobNumber);
        if (shift.isPresent()) {
            shiftRepository.deleteByDoctorJobNumber(doctorJobNumber);
            return;
        }
        throw new BusinessException(CommonErrorCode.E_100112);
    }

    @Transactional
    public void updateShift(ShiftUpdateRequest shiftUpdateRequest) {
        Optional<Shift> shiftOptional = shiftRepository.findByDoctorJobNumber(shiftUpdateRequest.getDoctorJobNumber());
        if (shiftOptional.isEmpty()) {
            throw new BusinessException(CommonErrorCode.E_100109);
        }
        Shift shiftOrigin = shiftOptional.get();
        Shift shiftSrc = shiftMapper.toShift(shiftUpdateRequest);
        JpaUtil.copyNotNullProperties(shiftSrc, shiftOrigin);
        shiftRepository.save(shiftOrigin);
    }

    public List<ShiftResponse> getAllDoctorsShifts() {
        Map<String, DoctorResponse> doctorResponseGroupByJobNumber = doctorService.getDoctorJobNumberDoctorResponseMap();
        List<Shift> shifts = shiftRepository.findAll();
        return shifts.stream().map(shift -> {
            ShiftResponse shiftResponse = shiftMapper.toResponse(shift);
            shiftResponse.setDoctorResponse(doctorResponseGroupByJobNumber.get(shift.getDoctorJobNumber()));
            return shiftResponse;
        }).collect(Collectors.toList());
    }

    public List<ShiftResponse> getShiftsByCondition(DoctorQueryCriteria doctorQueryCriteria) {
        List<DoctorResponse> doctorResponses = doctorService.getDoctorsByCondition(doctorQueryCriteria);
        Map<String, DoctorResponse> doctorResponseGroupByJobNumber = doctorResponses.stream().collect(Collectors.toMap(DoctorResponse::getJobNumber, Function.identity()));
        List<String> jobNumbers = doctorResponses.stream().map(DoctorResponse::getJobNumber).collect(Collectors.toList());
        List<Shift> shifts = shiftRepository.findByDoctorJobNumberIn(jobNumbers);
        return shifts.stream().map(shift -> {
            ShiftResponse shiftResponse = shiftMapper.toResponse(shift);
            shiftResponse.setDoctorResponse(doctorResponseGroupByJobNumber.get(shift.getDoctorJobNumber()));
            return shiftResponse;
        }).collect(Collectors.toList());
    }
}
