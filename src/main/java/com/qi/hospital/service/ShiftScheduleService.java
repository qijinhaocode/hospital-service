package com.qi.hospital.service;

import com.qi.hospital.dto.doctor.DoctorResponse;
import com.qi.hospital.dto.shift.ShiftResponse;
import com.qi.hospital.dto.shift.ShiftScheduleRequest;
import com.qi.hospital.dto.shift.ShiftScheduleResponse;
import com.qi.hospital.dto.shift.ShiftScheduleUpdateRequest;
import com.qi.hospital.exception.BusinessException;
import com.qi.hospital.exception.CommonErrorCode;
import com.qi.hospital.mapper.ShiftScheduleMapper;
import com.qi.hospital.model.appointment.AppointmentTime;
import com.qi.hospital.model.section.Section;
import com.qi.hospital.model.shift.Shift;
import com.qi.hospital.model.shift.ShiftSchedule;
import com.qi.hospital.repository.SectionRepository;
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
    private final SectionRepository sectionRepository;

    //create ShiftSchedule by start date and end date (generate)
    public List<ShiftScheduleResponse> createShiftSchedule(ShiftScheduleRequest shiftScheduleRequest) {
        //日期转星期，遍历这个日期之间所有，生成号表。
        List<String> strings = DateOperationUtil.collectTimeFrame(shiftScheduleRequest.getStartDate(), shiftScheduleRequest.getEndDate());
        List<LocalDate> localDates = strings
                .stream()
                .map(string -> DateOperationUtil.String2LocalDate(string))
                .collect(Collectors.toList());
        List<ShiftSchedule> shiftSchedules = new LinkedList<>();

        //get all ShiftSchedule
        List<ShiftResponse> allDoctorsShifts = shiftService.getAllDoctorsShifts();
        allDoctorsShifts.forEach(doctorsShift -> localDates.forEach(localDate -> {
            if (localDate.isAfter(LocalDate.now())) {
                Optional<ShiftSchedule> byLocalDateAndDoctorJobNumber = shiftScheduleRepository.findByLocalDateAndDoctorJobNumber(localDate, doctorsShift.getDoctorJobNumber());
                if (byLocalDateAndDoctorJobNumber.isPresent()) {
                    Integer[] morningAndAfternoonReservationNumberFromDate = getMorningAndAfternoonReservationNumberFromDate(localDate, doctorsShift.getDoctorJobNumber());
                    ShiftSchedule build = ShiftSchedule.builder()
                            .id(byLocalDateAndDoctorJobNumber.get().getId())
                            .doctorJobNumber(doctorsShift.getDoctorJobNumber())
                            .localDate(localDate)
                            .morning(morningAndAfternoonReservationNumberFromDate[0])
                            .afternoon(morningAndAfternoonReservationNumberFromDate[1]).build();
                    if (isShiftScheduleVisible(build.getMorning(), build.getAfternoon())) {
                        shiftSchedules.add(build);
                    }
                    shiftScheduleRepository.save(build);
                } else {
                    Integer[] morningAndAfternoonReservationNumberFromDate = getMorningAndAfternoonReservationNumberFromDate(localDate, doctorsShift.getDoctorJobNumber());
                    ShiftSchedule build = ShiftSchedule.builder()
                            .doctorJobNumber(doctorsShift.getDoctorJobNumber())
                            .localDate(localDate)
                            .morning(morningAndAfternoonReservationNumberFromDate[0])
                            .afternoon(morningAndAfternoonReservationNumberFromDate[1]).build();
                    if (isShiftScheduleVisible(build.getMorning(), build.getAfternoon())) {
                        shiftSchedules.add(build);
                    }
                    shiftScheduleRepository.save(build);
                }
            }
        }));

        Map<String, DoctorResponse> doctorResponseGroupByJobNumber = getJobNumberDoctorResponseMap();

        return shiftSchedules.stream().map(shiftSchedule -> {
            ShiftScheduleResponse shiftScheduleResponse = shiftScheduleMapper.toResponse(shiftSchedule);
            shiftScheduleResponse.setDoctorResponse(doctorResponseGroupByJobNumber.get(shiftScheduleResponse.getDoctorJobNumber()));
            return shiftScheduleResponse;
        }).collect(Collectors.toList());
    }

    private boolean isShiftScheduleVisible(Integer morning, Integer afternoon) {
        return morning != null || afternoon != null;
    }

    //返回所有号表信息
    public List<ShiftScheduleResponse> getAllShiftSchedule() {
        List<DoctorResponse> doctorResponses = doctorService.getAllDoctors();
        Map<String, DoctorResponse> doctorResponseGroupByJobNumber = doctorResponses.stream().collect(Collectors.toMap(DoctorResponse::getJobNumber, Function.identity()));
        List<ShiftSchedule> shiftSchedules = shiftScheduleRepository.findAll();
        return shiftSchedules.stream()
                .filter(shiftSchedule -> isShiftScheduleVisible(shiftSchedule.getMorning(), shiftSchedule.getAfternoon()))
                .map(shiftSchedule -> {
                    ShiftScheduleResponse shiftScheduleResponse = shiftScheduleMapper.toResponse(shiftSchedule);
                    shiftScheduleResponse.setDoctorResponse(doctorResponseGroupByJobNumber.get(shiftScheduleResponse.getDoctorJobNumber()));
                    return shiftScheduleResponse;
                }).collect(Collectors.toList());
    }

    //according to the start date and end date query
    public List<ShiftScheduleResponse> getShiftScheduleByCondition(String startDate, String endDate, String sectionId) {
        //查找所有日期
        List<LocalDate> localDates = getLocalDatesFromStartDateAndEndDate(startDate, endDate);

        Map<String, DoctorResponse> doctorResponseGroupByJobNumber = getJobNumberDoctorResponseMap();

        List<ShiftSchedule> shiftSchedules = shiftScheduleRepository.findByLocalDateInOrderByLocalDate(localDates);
        List<ShiftScheduleResponse> collect = new LinkedList<>();
        if (sectionId != null) {
            collect = shiftSchedules.stream()
                    .filter(shiftSchedule -> isShiftScheduleVisible(shiftSchedule.getMorning(), shiftSchedule.getAfternoon()))
                    .filter(shiftSchedule -> doctorResponseGroupByJobNumber.get(shiftSchedule.getDoctorJobNumber()).getSection().getId().equals(sectionId))
                    .map(shiftSchedule -> {
                        ShiftScheduleResponse shiftScheduleResponse = shiftScheduleMapper.toResponse(shiftSchedule);
                        shiftScheduleResponse.setDoctorResponse(doctorResponseGroupByJobNumber.get(shiftScheduleResponse.getDoctorJobNumber()));
                        return shiftScheduleResponse;
                    }).collect(Collectors.toList());
        } else {
            collect = shiftSchedules.stream()
                    .filter(shiftSchedule -> isShiftScheduleVisible(shiftSchedule.getMorning(), shiftSchedule.getAfternoon()))
                    .map(shiftSchedule -> {
                        ShiftScheduleResponse shiftScheduleResponse = shiftScheduleMapper.toResponse(shiftSchedule);
                        shiftScheduleResponse.setDoctorResponse(doctorResponseGroupByJobNumber.get(shiftScheduleResponse.getDoctorJobNumber()));
                        return shiftScheduleResponse;
                    }).collect(Collectors.toList());
        }
        return collect;
    }

    //根据section ID 筛选号表
    public List<List<List<ShiftScheduleResponse>>> getShiftScheduleByConditionGroupBySectionId(String startDate, String endDate) {
        List<List<List<ShiftScheduleResponse>>> resultList = new LinkedList<>();
        //查找所有部门和医生，医生按照工号生成实体Map
        List<Section> allSections = sectionRepository.findAll();
        Map<String, DoctorResponse> doctorResponseGroupByJobNumber = getJobNumberDoctorResponseMap();

        allSections.forEach(
                section -> {
                    String sectionId = section.getId();
                    List<LocalDate> localDatesPeriod = getLocalDatesFromStartDateAndEndDate(startDate, endDate);


                    List<ShiftSchedule> shiftSchedules = shiftScheduleRepository.findByLocalDateInOrderByLocalDate(localDatesPeriod);
                    List<ShiftScheduleResponse> collect;
                    List<List<ShiftScheduleResponse>> list = new LinkedList<>();
                    if (sectionId != null) {
                        collect = shiftSchedules.stream()
                                .filter(shiftSchedule -> isShiftScheduleVisible(shiftSchedule.getMorning(), shiftSchedule.getAfternoon()))
                                .filter(shiftSchedule -> doctorResponseGroupByJobNumber.get(shiftSchedule.getDoctorJobNumber()).getSection().getId().equals(sectionId))
                                .map(shiftSchedule -> {
                                    ShiftScheduleResponse shiftScheduleResponse = shiftScheduleMapper.toResponse(shiftSchedule);
                                    shiftScheduleResponse.setSection(section);
                                    shiftScheduleResponse.setDoctorResponse(doctorResponseGroupByJobNumber.get(shiftScheduleResponse.getDoctorJobNumber()));
                                    return shiftScheduleResponse;
                                }).collect(Collectors.toList());
                        localDatesPeriod.forEach(date -> {
                            //找到同一sectionID 下面， 相同日期的排班，形成列表
                            List<ShiftScheduleResponse> collect1 = collect.stream()
                                    .filter(c -> c.getLocalDate().equals(date))
                                    .collect(Collectors.toList());
                            if (collect1.size() != 0) {
                                list.add(collect1);
                            }
                        });


                    } else {
                        collect = shiftSchedules.stream()
                                .filter(shiftSchedule -> isShiftScheduleVisible(shiftSchedule.getMorning(), shiftSchedule.getAfternoon()))
                                .map(shiftSchedule -> {
                                    ShiftScheduleResponse shiftScheduleResponse = shiftScheduleMapper.toResponse(shiftSchedule);
                                    shiftScheduleResponse.setSection(section);
                                    shiftScheduleResponse.setDoctorResponse(doctorResponseGroupByJobNumber.get(shiftScheduleResponse.getDoctorJobNumber()));
                                    return shiftScheduleResponse;
                                }).collect(Collectors.toList());

                        localDatesPeriod.forEach(date -> {
                            //找到同一sectionID 下面， 相同日期的排班，形成列表
                            List<ShiftScheduleResponse> collect1 = collect.stream()
                                    .filter(c -> c.getLocalDate().equals(date))
                                    .collect(Collectors.toList());
                            if (collect1.size() != 0) {
                                list.add(collect1);
                            }
                        });
                    }
                    if (list.size() != 0) {
                        resultList.add(list);
                    }

                }
        );
        return resultList;
    }

    private List<LocalDate> getLocalDatesFromStartDateAndEndDate(String startDate, String endDate) {
        LocalDate startDateLocal = DateOperationUtil.String2LocalDate(startDate);
        LocalDate endDateLocal = DateOperationUtil.String2LocalDate(endDate);
        List<String> strings = DateOperationUtil.collectTimeFrame(startDateLocal, endDateLocal);
        return strings
                .stream()
                .map(string -> DateOperationUtil.String2LocalDate(string))
                .collect(Collectors.toList());
    }

    private Map<String, DoctorResponse> getJobNumberDoctorResponseMap() {
        List<DoctorResponse> doctorResponses = doctorService.getAllDoctors();
        return doctorResponses.stream().collect(Collectors.toMap(DoctorResponse::getJobNumber, Function.identity()));
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

    // update shift schedule
    public void updateShiftSchedule(ShiftScheduleUpdateRequest shiftScheduleUpdateRequest) {
        ShiftSchedule shiftScheduleOriginInDB = shiftScheduleRepository.getById(shiftScheduleUpdateRequest.getId());
        if (shiftScheduleUpdateRequest.getAppointmentTime().equals(AppointmentTime.MORNING))
            shiftScheduleOriginInDB.setMorning(shiftScheduleOriginInDB.getMorning() - 1);
        if (shiftScheduleUpdateRequest.getAppointmentTime().equals(AppointmentTime.AFTERNOON))
            shiftScheduleOriginInDB.setAfternoon(shiftScheduleOriginInDB.getAfternoon() - 1);
        shiftScheduleRepository.save(shiftScheduleOriginInDB);
    }
}