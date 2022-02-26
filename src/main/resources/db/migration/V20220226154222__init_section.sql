CREATE TABLE t_section (
                        uid VARCHAR(50)  COMMENT '科室id',
                        name VARCHAR(20) NOT NULL UNIQUE COMMENT '科室名称',
                        PRIMARY KEY (uid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;