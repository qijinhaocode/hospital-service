package com.qi.hospital.dto.shift;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ShiftUpdateRequest {
    @NotBlank
    private String doctorJobNumber;
    @NotNull(message = "预约挂号数不能为空！")
    private Integer weekMondayMorning;
    @NotNull(message = "预约挂号数不能为空！")
    private Integer weekMondayAfternoon;
    @NotNull(message = "预约挂号数不能为空！")
    private Integer weekTuesdayMorning;
    @NotNull(message = "预约挂号数不能为空！")
    private Integer weekTuesdayAfternoon;
    @NotNull(message = "预约挂号数不能为空！")
    private Integer weekWednesdayMorning;
    @NotNull(message = "预约挂号数不能为空！")
    private Integer weekWednesdayAfternoon;
    @NotNull(message = "预约挂号数不能为空！")
    private Integer weekThursdayMorning;
    @NotNull(message = "预约挂号数不能为空！")
    private Integer weekThursdayAfternoon;
    @NotNull(message = "预约挂号数不能为空！")
    private Integer weekFridayMorning;
    @NotNull(message = "预约挂号数不能为空！")
    private Integer weekFridayAfternoon;
    @NotNull(message = "预约挂号数不能为空！")
    private Integer weekSaturdayMorning;
    @NotNull(message = "预约挂号数不能为空！")
    private Integer weekSaturdayAfternoon;
    @NotNull(message = "预约挂号数不能为空！")
    private Integer weekSundayMorning;
    @NotNull(message = "预约挂号数不能为空！")
    private Integer weekSundayAfternoon;
}
