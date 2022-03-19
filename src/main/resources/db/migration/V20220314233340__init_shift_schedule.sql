CREATE TABLE if NOT EXISTS t_shift_schedule (
                         uid VARCHAR(50)  COMMENT '排班id',
                         doctor_job_number VARCHAR(20) NOT NULL COMMENT '医生工号',
                         localdate DATE COMMENT '日期',
                         morning INTEGER (20) COMMENT '上午限约数',
                         afternoon INTEGER (20) COMMENT '下午限约数',
                         PRIMARY KEY (uid),
                         FOREIGN KEY (doctor_job_number) REFERENCES t_doctor (job_number) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;