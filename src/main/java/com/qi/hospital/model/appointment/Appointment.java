package com.qi.hospital.model.appointment;

import com.qi.hospital.model.dcotor.DoctorTitle;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_appointment")
@Builder
public class Appointment {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "uid")
    private String id;
    private String userId;
    private String doctorJobNumber;
    @Column(name = "localdate")
    private LocalDate localDate;
    @Enumerated(EnumType.STRING)
    private AppointmentTime appointmentTime;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;
    private LocalDateTime payTime;

    // 医生相关信息需要保存在对应列，不能根据医生工号查询
    private String doctorName;
    private DoctorTitle doctorTitle;
    private String doctorIntro;
    private String sectionName;
    private Double registrationFee;
}
