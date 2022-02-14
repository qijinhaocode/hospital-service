package com.qi.hospital.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue
    private String id;

    private String userName;

    private String idNumber;

    private String phoneNumber;

    private String password;

    private Integer gender;

    private String address;
}
