package com.qi.hospital.controller;

import com.qi.hospital.dto.shift.ShiftScheduleRequest;
import com.qi.hospital.dto.shift.ShiftScheduleResponse;
import com.qi.hospital.service.ShiftScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/shift_schedule")
@Validated
@CrossOrigin(origins = "*")
public class ShiftScheduleController {
    private final ShiftScheduleService shiftScheduleService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public List<ShiftScheduleResponse> createShiftSchedule(@RequestBody @Valid ShiftScheduleRequest shiftScheduleRequest) {
        return shiftScheduleService.createShiftSchedule(shiftScheduleRequest);
    }

    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public List<ShiftScheduleResponse> getAllDoctorsShiftSchedules() {
        return shiftScheduleService.getAllShiftSchedule();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ShiftScheduleResponse> getShiftScheduleByCondition(@RequestParam(value = "startDate") String startDate,
                                                                   @RequestParam(value = "endDate") String endDate) {
        return shiftScheduleService.getShiftScheduleByCondition(startDate, endDate);
    }

}
