package com.qi.hospital.model.shift;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_shift")
@Builder
public class Shift {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "uid")
    private String id;
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
}