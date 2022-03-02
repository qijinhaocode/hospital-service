package com.qi.hospital.controller;

import com.qi.hospital.dto.doctor.DoctorQueryCriteria;
import com.qi.hospital.dto.doctor.DoctorRequest;
import com.qi.hospital.dto.doctor.DoctorResponse;
import com.qi.hospital.dto.doctor.DoctorUpdateRequest;
import com.qi.hospital.model.dcotor.Doctor;
import com.qi.hospital.model.dcotor.DoctorTitle;
import com.qi.hospital.model.section.Section;
import com.qi.hospital.service.DoctorService;
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
@RequestMapping( "/doctor")
@Validated
@CrossOrigin(origins = "*")
public class DoctorController {
    private final DoctorService doctorService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Doctor createDoctor(@RequestBody @Valid DoctorRequest doctorRequest) {
        return doctorService.createDoctor(doctorRequest);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.OK)
    public void deleteSection(@RequestParam(value = "jobNumber") String jobNumber) {
        doctorService.deleteDoctor(jobNumber);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateSection(@RequestBody @Valid DoctorUpdateRequest doctorUpdateRequest){
        doctorService.updateSection(doctorUpdateRequest);
    }

    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public List<DoctorResponse> getAllDoctors(){
        return doctorService.getAllDoctors();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DoctorResponse> getDoctorsByCondition(@RequestParam(value = "name", required = false) String name,
                                                      @RequestParam(value = "jobNumber" ,required = false) String jobNumber,
                                                      @RequestParam(value = "title" ,required = false) DoctorTitle title,
                                                      @RequestParam(value = "sectionId" ,required = false) String sectionId){
        return doctorService.getDoctorsByCondition(DoctorQueryCriteria.builder().name(name).sectionId(sectionId).title(title).jobNumber(jobNumber).build());
    }
}
