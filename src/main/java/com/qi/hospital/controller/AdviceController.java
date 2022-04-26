package com.qi.hospital.controller;

import com.qi.hospital.dto.advice.AdviceRequest;
import com.qi.hospital.dto.advice.AdviceResponse;
import com.qi.hospital.dto.section.SectionRequest;
import com.qi.hospital.dto.section.SectionUpdateRequest;
import com.qi.hospital.model.advice.Advice;
import com.qi.hospital.model.section.Section;
import com.qi.hospital.service.AdviceService;
import com.qi.hospital.service.SectionService;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/advice")
@Validated
@CrossOrigin(origins = "*")
public class AdviceController {
    private final AdviceService adviceService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public AdviceResponse createAdvice(@RequestHeader("token") String token, @RequestBody @Valid AdviceRequest adviceRequest) {
        return adviceService.createAdvice(token, adviceRequest);
    }

    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public List<AdviceResponse> getAllAdvices() {
        return adviceService.getAllAdvices();
    }
}
