package com.qi.hospital.model.shift;

import com.qi.hospital.model.dcotor.DoctorTitle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_shift_schedule")
@Builder
public class ShiftSchedule {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "uid")
    private String id;
    private String doctorJobNumber;
    @Column(name = "localdate")
    private LocalDate localDate;
    private Integer morning;
    private Integer afternoon;

    //不绑定外键
    private String doctorName;
    private DoctorTitle doctorTitle;
    private String doctorIntro;
    private String sectionName;
    private Double registrationFee;
}
