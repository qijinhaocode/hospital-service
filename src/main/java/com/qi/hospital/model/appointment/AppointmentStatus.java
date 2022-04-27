package com.qi.hospital.model.appointment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AppointmentStatus {
    PROCESSING("处理中"),
    CANCEL("取消"),
    DONE("挂号完成"),
    UNKNOWN("未知状态");

    private String description;
}
