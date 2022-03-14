package com.qi.hospital.model.registrationFee;

import com.qi.hospital.model.dcotor.DoctorTitle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_registration_fee")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationFee {
    @Enumerated(EnumType.STRING)
    @Id
    private DoctorTitle doctorTitle;
    private Double registrationFee;
}
