CREATE TABLE if NOT EXISTS t_registration_fee (
    doctor_title VARCHAR(20) NOT NULL UNIQUE comment '医生职称',
    registration_fee DOUBLE comment '挂号费',
    PRIMARY KEY (doctor_title)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
