CREATE TABLE if NOT EXISTS t_appointment
(
    uid              VARCHAR(50) COMMENT '预约id',
    user_id          VARCHAR(20) NOT NULL COMMENT '用户id',
    doctor_job_number VARCHAR(20) NOT NULL UNIQUE COMMENT '医生工号',
    localdate        DATE COMMENT '日期',
    appointment_time VARCHAR(20) NOT NULL COMMENT '预约时间',
    appointment_status VARCHAR(20) NOT NULL COMMENT '预约挂号状态',
    PRIMARY KEY (uid),
    FOREIGN KEY (user_id) REFERENCES t_user (uid) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;