package com.qi.hospital.model.dcotor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum DoctorTitle {
    EXPERT("专家医师"),
    CHIEF("主任医师"),
    ASSOCIATE_CHIEF("副主任医师"),
    ORDINARY("普通医师");

    private String description;
    }
