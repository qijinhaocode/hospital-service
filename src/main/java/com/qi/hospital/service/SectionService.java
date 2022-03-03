package com.qi.hospital.service;


import com.qi.hospital.dto.section.SectionRequest;
import com.qi.hospital.dto.section.SectionUpdateRequest;
import com.qi.hospital.exception.BusinessException;
import com.qi.hospital.exception.CommonErrorCode;
import com.qi.hospital.mapper.SectionMapper;
import com.qi.hospital.model.section.Section;
import com.qi.hospital.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SectionService {
    private final SectionRepository sectionRepository;
    private final SectionMapper sectionMapper;

    public Section createSection(SectionRequest sectionRequest) {
        Optional<Section> section = sectionRepository.findByName(sectionRequest.getName());
        if (section.isPresent()) {
            throw new BusinessException(CommonErrorCode.E_100104);
        }
        return sectionRepository.save(sectionMapper.toSection(sectionRequest));
    }

    @Transactional
    public void deleteSection(String name) {
        Optional<Section> section = sectionRepository.findByName(name);
        if (section.isPresent()) {
            sectionRepository.deleteByName(name);
            return;
        }
        throw new BusinessException(CommonErrorCode.E_100105);
    }

    @Transactional
    public void updateSection(SectionUpdateRequest sectionUpdateRequest) {
        Optional<Section> originSection = sectionRepository.findById(sectionUpdateRequest.getId());
        if (!originSection.isPresent()) {
            throw new BusinessException(CommonErrorCode.E_100104);
        }
        Optional<Section> section = sectionRepository.findById(sectionUpdateRequest.getId());
        if (section.get().getName().equals(sectionUpdateRequest.getName())) {
            return; // 如果名称与原来一样，则直接返回不做保存处理。
        }
        Section sectionNew = Section.builder().id(sectionUpdateRequest.getId()).name(sectionUpdateRequest.getName()).build();
        sectionRepository.save(sectionNew);
    }

    public List<Section> querySection(String name){
        return sectionRepository.findByNameContaining(name);
    }

    public List<Section> getAllSection(){
        return sectionRepository.findAll();
    }
}
