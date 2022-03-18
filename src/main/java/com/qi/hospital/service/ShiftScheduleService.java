package com.qi.hospital.service;

import com.qi.hospital.dto.doctor.DoctorResponse;
import com.qi.hospital.dto.shift.ShiftResponse;
import com.qi.hospital.dto.shift.ShiftScheduleRequest;
import com.qi.hospital.dto.shift.ShiftScheduleResponse;
import com.qi.hospital.exception.BusinessException;
import com.qi.hospital.exception.CommonErrorCode;
import com.qi.hospital.mapper.ShiftScheduleMapper;
import com.qi.hospital.model.shift.Shift;
import com.qi.hospital.model.shift.ShiftSchedule;
import com.qi.hospital.repository.ShiftRepository;
import com.qi.hospital.repository.ShiftScheduleRepository;
import com.qi.hospital.util.DateOperationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftScheduleService {

    private final ShiftRepository shiftRepository;
    private final ShiftService shiftService;
    private final ShiftScheduleRepository shiftScheduleRepository;
    private final DoctorService doctorService;
    private final ShiftScheduleMapper shiftScheduleMapper;

    //create ShiftSchedule by start date and end date (generate)
    public List<ShiftSchedule> createShiftSchedule(ShiftScheduleRequest shiftScheduleRequest) {
        //日期转星期，遍历这个日期之间所有，生成号表。
        //遍历日期之间
        List<String> strings = DateOperationUtil.collectTimeFrame(shiftScheduleRequest.getStartDate(), shiftScheduleRequest.getEndDate());
        List<LocalDate> localDates = strings
                .stream()
                .map(string -> DateOperationUtil.String2LocalDate(string))
                .collect(Collectors.toList());
        List<ShiftSchedule> shiftSchedules = new LinkedList<>();
        //get all ShiftSchedule
        List<ShiftResponse> allDoctorsShifts = shiftService.getAllDoctorsShifts();
        allDoctorsShifts.forEach(doctorsShift -> localDates.forEach(localDate -> {
            Integer[] morningAndAfternoonReservationNumberFromDate = getMorningAndAfternoonReservationNumberFromDate(localDate, doctorsShift.getDoctorJobNumber());
            ShiftSchedule build = ShiftSchedule.builder().doctorJobNumber(doctorsShift
                    .getDoctorJobNumber())
                    .localDate(localDate)
                    .morning(morningAndAfternoonReservationNumberFromDate[0])
                    .afternoon(morningAndAfternoonReservationNumberFromDate[1]).build();
            shiftSchedules.add(build);
            shiftScheduleRepository.save(build);
        }));
        return shiftSchedules;
    }

    //返回所有号表信息
    public List<ShiftScheduleResponse> getAllShiftSchedule() {
        List<DoctorResponse> doctorResponses = doctorService.getAllDoctors();
        Map<String, DoctorResponse> doctorResponseGroupByJobNumber = doctorResponses.stream().collect(Collectors.toMap(DoctorResponse::getJobNumber, Function.identity()));
        List<ShiftSchedule> shiftSchedules = shiftScheduleRepository.findAll();
        return shiftSchedules.stream().map(shiftSchedule -> {
            ShiftScheduleResponse shiftScheduleResponse = shiftScheduleMapper.toResponse(shiftSchedule);
            shiftScheduleResponse.setDoctorResponse(doctorResponseGroupByJobNumber.get(shiftScheduleResponse.getDoctorJobNumber()));
            return shiftScheduleResponse;
        }).collect(Collectors.toList());
    }

    // from date to get Morning and Afternoon
    private Integer[] getMorningAndAfternoonReservationNumberFromDate(LocalDate localDate, String jobNumber) {
        Integer[] shift = new Integer[2];
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        Optional<Shift> byDoctorJobNumber = shiftRepository.findByDoctorJobNumber(jobNumber);
        if (!byDoctorJobNumber.isPresent()) {
            throw new BusinessException(CommonErrorCode.E_100114);
        }
        if (dayOfWeek.equals(DayOfWeek.MONDAY)) {
            shift[0] = byDoctorJobNumber.get().getWeekMondayMorning();
            shift[1] = byDoctorJobNumber.get().getWeekMondayAfternoon();
        }
        if (dayOfWeek.equals(DayOfWeek.TUESDAY)) {
            shift[0] = byDoctorJobNumber.get().getWeekTuesdayMorning();
            shift[1] = byDoctorJobNumber.get().getWeekTuesdayAfternoon();
        }
        if (dayOfWeek.equals(DayOfWeek.WEDNESDAY)) {
            shift[0] = byDoctorJobNumber.get().getWeekWednesdayMorning();
            shift[1] = byDoctorJobNumber.get().getWeekWednesdayAfternoon();
        }
        if (dayOfWeek.equals(DayOfWeek.THURSDAY)) {
            shift[0] = byDoctorJobNumber.get().getWeekThursdayMorning();
            shift[1] = byDoctorJobNumber.get().getWeekThursdayAfternoon();
        }
        if (dayOfWeek.equals(DayOfWeek.FRIDAY)) {
            shift[0] = byDoctorJobNumber.get().getWeekFridayMorning();
            shift[1] = byDoctorJobNumber.get().getWeekFridayAfternoon();
        }
        if (dayOfWeek.equals(DayOfWeek.SATURDAY)) {
            shift[0] = byDoctorJobNumber.get().getWeekSaturdayMorning();
            shift[1] = byDoctorJobNumber.get().getWeekSaturdayAfternoon();
        }
        if (dayOfWeek.equals(DayOfWeek.SUNDAY)) {
            shift[0] = byDoctorJobNumber.get().getWeekSundayMorning();
            shift[1] = byDoctorJobNumber.get().getWeekSundayAfternoon();
        }

        return shift;
    }
}