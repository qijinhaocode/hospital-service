CREATE TABLE t_doctor (
                        uid VARCHAR(50)  COMMENT '医生id',
                        name VARCHAR(20) NOT NULL COMMENT '医生姓名',
                        job_number VARCHAR(20) NOT NULL UNIQUE COMMENT '电话号码',
                        title VARCHAR(20) NOT NULL  COMMENT '医生职称',
                        intro VARCHAR(20) NOT NULL  COMMENT '医生简介',
                        section_id VARCHAR(50) COMMENT '科室id',
                        PRIMARY KEY (uid),
                        FOREIGN KEY (section_id) REFERENCES t_section (uid) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;