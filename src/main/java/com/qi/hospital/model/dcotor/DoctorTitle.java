package com.qi.hospital.model.dcotor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum DoctorTitle {
    EXPERT(0, "专家医师"),
    CHIEF(1, "主任医师"),
    ASSOCIATE_CHIEF(2, "副主任医师"),
    ORDINARY(3, "普通医师");

    private int sequence;
    private String description;
}
