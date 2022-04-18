ALTER  TABLE t_shift_schedule ADD COLUMN doctor_title VARCHAR(20) NOT NULL  COMMENT '医生职称';
ALTER  TABLE t_shift_schedule ADD COLUMN doctor_intro VARCHAR(20) NOT NULL  COMMENT '医生简介';
ALTER  TABLE t_shift_schedule ADD COLUMN doctor_name VARCHAR(20) NOT NULL COMMENT '医生姓名';
ALTER  TABLE t_shift_schedule ADD COLUMN section_name VARCHAR(20) NOT NULL COMMENT '科室名称';
ALTER  TABLE t_shift_schedule ADD COLUMN registration_fee DOUBLE NOT NULL COMMENT '挂号费';