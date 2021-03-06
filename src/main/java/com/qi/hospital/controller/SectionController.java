package com.qi.hospital.controller;

import com.qi.hospital.dto.section.SectionRequest;
import com.qi.hospital.dto.section.SectionUpdateRequest;
import com.qi.hospital.model.section.Section;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/section")
@Validated
@CrossOrigin(origins = "*")
public class SectionController {
    private final SectionService sectionService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Section createSection(@RequestBody @Valid SectionRequest sectionRequest) {
        return sectionService.createSection(sectionRequest);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.OK)
    public void deleteSection(@RequestParam(value = "name") String name) {
        sectionService.deleteSection(name);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateSection(@RequestBody @Valid SectionUpdateRequest sectionUpdateRequest) {
        sectionService.updateSection(sectionUpdateRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Section> querySectionFuzzy(@RequestParam(value = "name") String name) {
        return sectionService.querySection(name);
    }

    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public List<Section> getSections() {
        return sectionService.getAllSection();
    }
}
