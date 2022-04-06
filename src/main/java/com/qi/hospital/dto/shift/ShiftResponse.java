package com.qi.hospital.dto.shift;

import com.qi.hospital.dto.doctor.DoctorResponse;
import lombok.Data;

@Data
public class ShiftResponse {
    private String doctorJobNumber;
    private Integer weekMondayMorning;
    private Integer weekMondayAfternoon;
    private Integer weekTuesdayMorning;
    private Integer weekTuesdayAfternoon;
    private Integer weekWednesdayMorning;
    private Integer weekWednesdayAfternoon;
    private Integer weekThursdayMorning;
    private Integer weekThursdayAfternoon;
    private Integer weekFridayMorning;
    private Integer weekFridayAfternoon;
    private Integer weekSaturdayMorning;
    private Integer weekSaturdayAfternoon;
    private Integer weekSundayMorning;
    private Integer weekSundayAfternoon;
    private DoctorResponse doctorResponse;
}
