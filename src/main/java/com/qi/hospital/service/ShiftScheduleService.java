package com.qi.hospital.service;

import com.qi.hospital.dto.doctor.DoctorQueryCriteria;
import com.qi.hospital.dto.doctor.DoctorResponse;
import com.qi.hospital.dto.shift.AppointmentNumberCount;
import com.qi.hospital.dto.shift.ManuallyCreateShiftScheduleRequest;
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
import com.qi.hospital.repository.AppointmentRepository;
import com.qi.hospital.repository.SectionRepository;
import com.qi.hospital.repository.ShiftRepository;
import com.qi.hospital.repository.ShiftScheduleRepository;
import com.qi.hospital.util.DateOperationUtil;
import com.qi.hospital.util.ExcelUtils;
import com.qi.hospital.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j
public class ShiftScheduleService {

    private final ShiftRepository shiftRepository;
    private final ShiftService shiftService;
    private final ShiftScheduleRepository shiftScheduleRepository;
    private final DoctorService doctorService;
    private final ShiftScheduleMapper shiftScheduleMapper;
    private final SectionRepository sectionRepository;
    private final AppointmentRepository appointmentRepository;

    //create ShiftSchedule by start date and end date (generate)
    //?????????????????????????????????????????????
    public List<ShiftScheduleResponse> createShiftSchedule(ShiftScheduleRequest shiftScheduleRequest) {

        //??????????????????????????????????????????????????????????????????
        List<LocalDate> localDates = getLocalDates(shiftScheduleRequest);
        List<ShiftSchedule> shiftSchedules = new LinkedList<>();

        //get all Shift
        List<ShiftResponse> allDoctorsShifts = shiftService.getAllDoctorsShifts();
        Set<String> doctorShiftSetByJobNumber = allDoctorsShifts.stream().map(ShiftResponse::getDoctorJobNumber).collect(Collectors.toSet());
        //??????????????????????????????????????????????????????????????????????????? ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        //get all shift schedule by date frame
        List<ShiftSchedule> shiftSchedulesFromStartDateToEndDate = shiftScheduleRepository.findByLocalDateInOrderByLocalDate(localDates);
        shiftSchedulesFromStartDateToEndDate.stream()
                .filter(shiftSchedule -> shiftSchedule.getLocalDate().isAfter(LocalDate.now()))
                .forEach(shiftSchedule -> {
                    if (!doctorShiftSetByJobNumber.contains(shiftSchedule.getDoctorJobNumber())) {
                        shiftScheduleRepository.delete(shiftSchedule);
                    }
                });
        // map ?????? ??????
        Map<String, DoctorResponse> doctorResponseGroupByJobNumber = getJobNumberDoctorResponseMap();

        allDoctorsShifts.forEach(doctorsShift -> localDates.forEach(localDate -> {
            if (localDate.isAfter(LocalDate.now())) {
                Optional<ShiftSchedule> shiftScheduleByLocalDateAndDoctorJobNumber = shiftScheduleRepository.findByLocalDateAndDoctorJobNumber(localDate, doctorsShift.getDoctorJobNumber());
                //?????????????????????????????????
                if (shiftScheduleByLocalDateAndDoctorJobNumber.isEmpty()) {
                    AppointmentNumberCount morningAndAfternoonReservationNumberFromDate = null;
                    try {
                        morningAndAfternoonReservationNumberFromDate = getMorningAndAfternoonAppointmentNumberFromDate(localDate, doctorsShift.getDoctorJobNumber());
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    DoctorResponse doctorResponseFromMap = doctorResponseGroupByJobNumber.get(doctorsShift.getDoctorJobNumber());
                    ShiftSchedule buildShiftSchedule = ShiftSchedule.builder()
                            .doctorJobNumber(doctorsShift.getDoctorJobNumber())
                            .localDate(localDate)
                            .morning(morningAndAfternoonReservationNumberFromDate.getMorningAppointmentNumber())
                            .afternoon(morningAndAfternoonReservationNumberFromDate.getAfternoonAppointmentNumber())
                            .doctorIntro(doctorResponseFromMap.getIntro())
                            .doctorName(doctorResponseFromMap.getName())
                            .doctorTitle(doctorResponseFromMap.getTitle())
                            .sectionName(doctorResponseFromMap.getSection().getName())
                            .registrationFee(doctorResponseFromMap.getRegistrationFee())
                            .build();
                    if (isShiftScheduleVisible(buildShiftSchedule.getMorning(), buildShiftSchedule.getAfternoon())) {
                        shiftSchedules.add(buildShiftSchedule);
                    }
                    shiftScheduleRepository.save(buildShiftSchedule);
                }
            }
        }));

        return shiftScheduleMapper.toResponses(shiftSchedules);
    }

    private List<LocalDate> getLocalDates(ShiftScheduleRequest shiftScheduleRequest) {
        List<String> strings = DateOperationUtil.collectTimeFrame(shiftScheduleRequest.getStartDate(), shiftScheduleRequest.getEndDate());
        List<LocalDate> localDates = strings
                .stream()
                .map(DateOperationUtil::String2LocalDate)
                .collect(Collectors.toList());
        return localDates;
    }

    private boolean isShiftScheduleVisible(Integer morning, Integer afternoon) {
        return morning != null || afternoon != null;
    }

    //????????????????????????
    public List<ShiftScheduleResponse> getAllShiftSchedule() {
        List<DoctorResponse> doctorResponses = doctorService.getAllDoctors();
        Map<String, DoctorResponse> doctorResponseGroupByJobNumber = doctorResponses.stream().collect(Collectors.toMap(DoctorResponse::getJobNumber, Function.identity()));
        List<ShiftSchedule> shiftSchedules = shiftScheduleRepository.findAll();
        return shiftSchedules.stream()
                .filter(shiftSchedule -> isShiftScheduleVisible(shiftSchedule.getMorning(), shiftSchedule.getAfternoon()))
                .map(shiftScheduleMapper::toResponse).collect(Collectors.toList());
    }

    //according to the start date and end date query
    public List<ShiftScheduleResponse> getShiftScheduleByCondition(String startDate, String endDate, String sectionName) {
        //??????????????????
        List<LocalDate> localDates = getLocalDatesFromStartDateAndEndDate(startDate, endDate);

        Map<String, DoctorResponse> doctorResponseGroupByJobNumber = getJobNumberDoctorResponseMap();

        List<ShiftSchedule> shiftSchedules = shiftScheduleRepository.findByLocalDateInOrderByLocalDate(localDates);
        List<ShiftScheduleResponse> collect;
        if (sectionName != null) {
            collect = shiftSchedules.stream()
                    .filter(shiftSchedule -> isShiftScheduleVisible(shiftSchedule.getMorning(), shiftSchedule.getAfternoon()))
                    .filter(shiftSchedule -> shiftSchedule.getSectionName().equals(sectionName))
                    .map(shiftScheduleMapper::toResponse).collect(Collectors.toList());
        } else {
            collect = shiftSchedules.stream()
                    .filter(shiftSchedule -> isShiftScheduleVisible(shiftSchedule.getMorning(), shiftSchedule.getAfternoon()))
                    .map(shiftScheduleMapper::toResponse).collect(Collectors.toList());
        }
        return collect;
    }

    //??????section ID ????????????
    public List<List<List<ShiftScheduleResponse>>> getShiftScheduleByConditionGroupBySectionId(String startDate, String endDate) {
        List<List<List<ShiftScheduleResponse>>> resultList = new LinkedList<>();
        //????????????????????????????????????????????????????????????Map
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
                                .map(shiftScheduleMapper::toResponse).collect(Collectors.toList());
                        localDatesPeriod.forEach(date -> {
                            //????????????sectionID ????????? ????????????????????????????????????
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
                                .map(shiftScheduleMapper::toResponse).collect(Collectors.toList());

                        localDatesPeriod.forEach(date -> {
                            //????????????sectionID ????????? ????????????????????????????????????
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
                .map(DateOperationUtil::String2LocalDate)
                .collect(Collectors.toList());
    }

    private Map<String, DoctorResponse> getJobNumberDoctorResponseMap() {
        List<DoctorResponse> doctorResponses = doctorService.getAllDoctors();
        return doctorResponses.stream().collect(Collectors.toMap(DoctorResponse::getJobNumber, Function.identity()));
    }

    // from date to get Morning and Afternoon
    public AppointmentNumberCount getMorningAndAfternoonAppointmentNumberFromDate(LocalDate localDate, String jobNumber) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        AppointmentNumberCount appointmentNumberCount = new AppointmentNumberCount();
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        Optional<Shift> byDoctorJobNumber = shiftRepository.findByDoctorJobNumber(jobNumber);
        if (byDoctorJobNumber.isEmpty()) {
            throw new BusinessException(CommonErrorCode.E_100114);
        }
        Method methodGetMorning = byDoctorJobNumber.get().getClass().getMethod("getWeek" + StringUtils.uppercase(dayOfWeek.toString()) + "Morning");
        Method methodGetAfternoon = byDoctorJobNumber.get().getClass().getMethod("getWeek" + StringUtils.uppercase(dayOfWeek.toString()) + "Afternoon");
        appointmentNumberCount.setMorningAppointmentNumber((Integer) methodGetMorning.invoke(byDoctorJobNumber.get()));
        appointmentNumberCount.setAfternoonAppointmentNumber((Integer) methodGetAfternoon.invoke(byDoctorJobNumber.get()));
        return appointmentNumberCount;
    }

    // update shift schedule
    public ShiftScheduleResponse updateShiftSchedule(ShiftScheduleUpdateRequest shiftScheduleUpdateRequest) {
        Optional<ShiftSchedule> shiftScheduleOriginInDB = shiftScheduleRepository.findById(shiftScheduleUpdateRequest.getId());
        if (shiftScheduleUpdateRequest.getMorningAppointmentCount() != null || shiftScheduleUpdateRequest.getAfternoonAppointmentCount() != null) {
            if (shiftScheduleUpdateRequest.getMorningAppointmentCount() != null) {
                shiftScheduleOriginInDB.get().setMorning(shiftScheduleUpdateRequest.getMorningAppointmentCount());
            }
            if (shiftScheduleUpdateRequest.getAfternoonAppointmentCount() != null) {
                shiftScheduleOriginInDB.get().setAfternoon(shiftScheduleUpdateRequest.getAfternoonAppointmentCount());
            }
            return shiftScheduleMapper.toResponse(shiftScheduleRepository.save(shiftScheduleOriginInDB.get()));
        }
        if (shiftScheduleUpdateRequest.getAppointmentTime() != null) {
            if (shiftScheduleUpdateRequest.getAppointmentTime().equals(AppointmentTime.MORNING)) {
                shiftScheduleOriginInDB.get().setMorning(shiftScheduleOriginInDB.get().getMorning() - 1);
            }
            if (shiftScheduleUpdateRequest.getAppointmentTime().equals(AppointmentTime.AFTERNOON)) {
                shiftScheduleOriginInDB.get().setAfternoon(shiftScheduleOriginInDB.get().getAfternoon() - 1);
            }
            return shiftScheduleMapper.toResponse(shiftScheduleRepository.save(shiftScheduleOriginInDB.get()));
        }
        return new ShiftScheduleResponse();
    }

    public void deleteShiftSchedule(String id) {
        Optional<ShiftSchedule> shiftScheduleOptional = shiftScheduleRepository.findById(id);
        if (shiftScheduleOptional.isPresent()) {
            //????????????????????????????????????????????????????????????????????????
            ShiftSchedule ss = shiftScheduleOptional.get();
            if (appointmentRepository.findByDoctorJobNumberAndLocalDate(ss.getDoctorJobNumber(), ss.getLocalDate()).size() > 0)
                throw new BusinessException(CommonErrorCode.E_100121);
            shiftScheduleRepository.deleteById(id);
            return;
        }
        throw new BusinessException(CommonErrorCode.E_100120);
    }

    public void createShiftScheduleExcelAccordingDateFrame(String startDate, String endDate, HttpServletResponse response) {
        List<ShiftScheduleResponse> shiftScheduleResponses = getShiftScheduleByConditionGroupBySectionId(startDate, endDate).stream()
                .flatMap(Collection::stream)
                .flatMap(Collection::stream)
                .peek(s -> s.setDoctorTitleDescription(s.getDoctorTitle().getDescription()))
                .collect(Collectors.toList());
        CopyOnWriteArrayList<ShiftScheduleResponse> shiftScheduleResponsesCopy = new CopyOnWriteArrayList<>(shiftScheduleResponses);
        try {
            ExcelUtils.writeExcel(startDate + "???" + endDate + "??????.xlsx", ShiftScheduleResponse.class, response, shiftScheduleResponsesCopy);
        } catch (Exception e) {
            log.error("??????excel????????????:", e);
        }
    }

    /**
     * ??????????????? ??????????????????
     * ?????????
     * startDate
     * endDate
     * doctorJobNumber
     * morningCount
     * afternoonCount
     * ??????????????????????????????????????????????????????????????????
     */

    public List<ShiftScheduleResponse> manuallyCreateShiftSchedule(ManuallyCreateShiftScheduleRequest manuallyCreateShiftScheduleRequest){
        if (manuallyCreateShiftScheduleRequest.getMorning()== null && manuallyCreateShiftScheduleRequest.getAfternoon()==null){
            throw new BusinessException(CommonErrorCode.E_100122);
        }
        List<DoctorResponse> doctorsByDoctorNumber = doctorService.getDoctorsByCondition(DoctorQueryCriteria.builder().jobNumber(manuallyCreateShiftScheduleRequest.getDoctorJobNumber()).build());
        if (doctorsByDoctorNumber.isEmpty()){
            throw new BusinessException(CommonErrorCode.E_100109);
        }
        List<LocalDate> localDates = getLocalDates(ShiftScheduleRequest.builder()
                .startDate(manuallyCreateShiftScheduleRequest.getStartDate())
                .endDate(manuallyCreateShiftScheduleRequest.getEndDate())
                .build());
        Map<String, DoctorResponse> doctorResponseGroupByJobNumber = getJobNumberDoctorResponseMap();
        DoctorResponse doctorResponseFromMap = doctorResponseGroupByJobNumber.get(manuallyCreateShiftScheduleRequest.getDoctorJobNumber());
        List<ShiftSchedule> shiftSchedules = new LinkedList<>();
        localDates.stream()
                .filter(date->shiftScheduleRepository.findByLocalDateAndDoctorJobNumber(date, manuallyCreateShiftScheduleRequest.getDoctorJobNumber()).isEmpty())
                .forEach(date->{
                    ShiftSchedule buildShiftSchedule = ShiftSchedule.builder()
                            .doctorJobNumber(manuallyCreateShiftScheduleRequest.getDoctorJobNumber())
                            .localDate(date)
                            .morning(manuallyCreateShiftScheduleRequest.getMorning())
                            .afternoon(manuallyCreateShiftScheduleRequest.getAfternoon())
                            .doctorIntro(doctorResponseFromMap.getIntro())
                            .doctorName(doctorResponseFromMap.getName())
                            .doctorTitle(doctorResponseFromMap.getTitle())
                            .sectionName(doctorResponseFromMap.getSection().getName())
                            .registrationFee(doctorResponseFromMap.getRegistrationFee())
                            .build();
                    if (isShiftScheduleVisible(buildShiftSchedule.getMorning(), buildShiftSchedule.getAfternoon())) {
                        shiftSchedules.add(buildShiftSchedule);
                    }
                    shiftScheduleRepository.save(buildShiftSchedule);
                });
        return shiftScheduleMapper.toResponses(shiftSchedules);
    }
}