package com.qi.hospital.controller;

import com.qi.hospital.dto.doctor.DoctorQueryCriteria;
import com.qi.hospital.dto.doctor.DoctorRequest;
import com.qi.hospital.dto.doctor.DoctorResponse;
import com.qi.hospital.dto.doctor.DoctorUpdateRequest;
import com.qi.hospital.dto.shift.ShiftRequest;
import com.qi.hospital.dto.shift.ShiftResponse;
import com.qi.hospital.dto.shift.ShiftUpdateRequest;
import com.qi.hospital.model.dcotor.Doctor;
import com.qi.hospital.model.dcotor.DoctorTitle;
import com.qi.hospital.model.shift.Shift;
import com.qi.hospital.service.DoctorService;
import com.qi.hospital.service.ShiftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping( "/shift")
@Validated
@CrossOrigin(origins = "*")
public class ShiftController {
    private final ShiftService shiftService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Shift createDoctor(@RequestBody @Valid ShiftRequest shiftRequest) {
        return shiftService.createShift(shiftRequest);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.OK)
    public void deleteShift(@RequestParam(value = "jobNumber") String jobNumber) {
        shiftService.deleteShift(jobNumber);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateShift(@RequestBody @Valid ShiftUpdateRequest shiftUpdateRequest){
        shiftService.updateShift(shiftUpdateRequest);
    }

    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public List<ShiftResponse> getAllDoctorsShifts(){
        return shiftService.getAllDoctorsShifts();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ShiftResponse> getDoctorsByCondition(@RequestParam(value = "name", required = false) String name,
                                                      @RequestParam(value = "jobNumber" ,required = false) String jobNumber,
                                                      @RequestParam(value = "title" ,required = false)  DoctorTitle title,
                                                      @RequestParam(value = "sectionId" ,required = false) String sectionId){
        return shiftService.getShiftsByCondition(DoctorQueryCriteria.builder().name(name).sectionId(sectionId).title(title).jobNumber(jobNumber).build());
    }
}
