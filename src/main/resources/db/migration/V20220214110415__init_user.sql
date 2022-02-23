CREATE TABLE t_user (
                        uid VARCHAR(50)  COMMENT '用户id',
                        username VARCHAR(20) NOT NULL COMMENT '姓名',
                        id_number VARCHAR(20) NOT NULL UNIQUE COMMENT '身份证号',
                        password CHAR(32) NOT NULL COMMENT '密码',
                        phone VARCHAR(20) NOT NULL UNIQUE COMMENT '电话号码',
                        gender INT COMMENT '性别:0-女，1-男',
                        address VARCHAR(50) COMMENT '家庭住址',
                        PRIMARY KEY (uid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;