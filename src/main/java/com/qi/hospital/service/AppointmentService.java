package com.qi.hospital.service;


import com.qi.hospital.dto.appointment.AppointmentRequest;
import com.qi.hospital.dto.appointment.AppointmentResponse;
import com.qi.hospital.dto.appointment.AppointmentUpdateRequest;
import com.qi.hospital.dto.appointment.GetAppointmentResponse;
import com.qi.hospital.dto.doctor.DoctorResponse;
import com.qi.hospital.exception.BusinessException;
import com.qi.hospital.exception.CommonErrorCode;
import com.qi.hospital.mapper.AppointmentMapper;
import com.qi.hospital.model.appointment.Appointment;
import com.qi.hospital.model.appointment.AppointmentStatus;
import com.qi.hospital.model.user.User;
import com.qi.hospital.repository.AppointmentRepository;
import com.qi.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final AppointmentMapper appointmentMapper;
    private final AppointmentStatus initAppointmentStatus = AppointmentStatus.PROCESSING;
    private final DoctorService doctorService;

    public AppointmentResponse createAppointment(String token, AppointmentRequest appointmentRequest) {
        //判断还能不能挂号
        Optional<User> userOptional = userRepository.findByPhoneNumber(token);
        //校验用户是否存在
        validateUserExist(userOptional);
        String userId = userOptional.get().getId();
        appointmentRequest.setUserId(userId);
        //1.在同一自然日，同一医院，同一科室，同一就诊单元，同一就诊人，可以预约最多1个号源；
        Optional<Appointment> appointmentOptional = appointmentRepository.findByUserIdAndDoctorJobNumberAndLocalDate(userId,
                appointmentRequest.getDoctorJobNumber(),
                appointmentRequest.getLocalDate());
        if (appointmentOptional.isPresent()) {
            throw new BusinessException(CommonErrorCode.E_100117);
        }
        Appointment appointment = appointmentMapper.toDomain(appointmentRequest);
        //初始化预约状态
        appointment.setAppointmentStatus(initAppointmentStatus);
        AppointmentResponse appointmentResponse = appointmentMapper.toResponse(appointmentRepository.save(appointment));
        // 对应医生可挂号数量 减1

        return appointmentResponse;
    }

    private void validateUserExist(Optional<User> userOptional) {
        if (!userOptional.isPresent()) {
            throw new BusinessException(CommonErrorCode.E_100103);
        }
    }

    public void deleteAppointment(String token, String doctorJobNumber, LocalDate localDate) {
        //can delete the appointment when the status is processing
        Optional<User> userOptional = userRepository.findByPhoneNumber(token);
        //校验用户是否存在
        validateUserExist(userOptional);

        String userId = userOptional.get().getId();
        Optional<Appointment> appointmentOptional = appointmentRepository.findByUserIdAndDoctorJobNumberAndLocalDate(userId,
                doctorJobNumber,
                localDate);
        if (appointmentOptional.isPresent()) {
            appointmentRepository.delete(appointmentOptional.get());
        }
    }

    public AppointmentResponse updateAppointment(String token, AppointmentUpdateRequest appointmentUpdateRequest) {
        Optional<User> userOptional = userRepository.findByPhoneNumber(token);
        //校验用户是否存在
        validateUserExist(userOptional);
        String userId = userOptional.get().getId();
        String doctorNumber = appointmentUpdateRequest.getDoctorJobNumber();
        LocalDate date = appointmentUpdateRequest.getLocalDate();
        // find the appointment need to update
        Optional<Appointment> appointmentOptional = appointmentRepository.findByUserIdAndDoctorJobNumberAndLocalDate(userId, doctorNumber, date);
        Appointment appointment = new Appointment();
        if (appointmentOptional.isPresent()) {
            if (appointmentOptional.get().getAppointmentStatus().equals(AppointmentStatus.DONE)) {
                throw new BusinessException(CommonErrorCode.E_100118);
            }
            if (appointmentOptional.get().equals(AppointmentStatus.PROCESSING)
                    && appointmentUpdateRequest.getAppointmentStatus().equals(AppointmentStatus.DONE)) {
                appointmentOptional.get().setAppointmentStatus(appointmentUpdateRequest.getAppointmentStatus());
                appointment = appointmentRepository.save(appointmentOptional.get());
            }
            if (appointmentOptional.get().equals(AppointmentStatus.PROCESSING)
                    && appointmentUpdateRequest.getAppointmentStatus().equals(AppointmentStatus.CANCEL)) {
                appointmentOptional.get().setAppointmentStatus(appointmentUpdateRequest.getAppointmentStatus());
                appointment = appointmentRepository.save(appointmentOptional.get());
            }
        }
        return appointmentMapper.toResponse(appointment);
    }

    public List<GetAppointmentResponse> getAllAppointment(String token) {
        Optional<User> userOptional = userRepository.findByPhoneNumber(token);
        validateUserExist(userOptional);
        String userId = userOptional.get().getId();
        List<Appointment> appointmentList = appointmentRepository.findByUserId(userId);
        Map<String, DoctorResponse> doctorJobNumberDoctorResponseMap = doctorService.getDoctorJobNumberDoctorResponseMap();
        return appointmentList.stream().map(appointment -> {
            GetAppointmentResponse getAppointmentResponse = appointmentMapper.toGetResponse(appointment);
            getAppointmentResponse.setDoctorResponse(doctorJobNumberDoctorResponseMap.get(appointment.getDoctorJobNumber()));
            return getAppointmentResponse;
        }).collect(Collectors.toList());
    }

    public Double getIncomeByDate(LocalDate localDate) {
        // find all appointment order which is done and count
        List<Appointment> byLocalDate = appointmentRepository.findByLocalDate(localDate);
        Map<String, DoctorResponse> doctorJobNumberDoctorResponseMap = doctorService.getDoctorJobNumberDoctorResponseMap();
        List<Double> collect = byLocalDate.stream()
                .filter(appointment -> appointment.getAppointmentStatus().equals(AppointmentStatus.DONE))
                .map(a -> doctorJobNumberDoctorResponseMap.get(a.getDoctorJobNumber()).getRegistrationFee())
                .collect(Collectors.toList());
        return Double.parseDouble(String.valueOf(collect.stream().count()));

    }
}
