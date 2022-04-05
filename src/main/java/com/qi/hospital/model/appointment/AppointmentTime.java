package com.qi.hospital.model.appointment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AppointmentTime {
    MORNING("上午"),
    AFTERNOON("下午");

    private String description;
}
