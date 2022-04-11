CREATE TABLE if NOT EXISTS t_advice
(
    uid              VARCHAR(50) COMMENT '预约id',
    user_id          VARCHAR(50) NOT NULL COMMENT '用户id',
    advice           VARCHAR(300) NOT NULL COMMENT '意见反馈',
    create_date_time DATETIME NOT NULL COMMENT '创建时间',
    PRIMARY KEY (uid),
    FOREIGN KEY (user_id) REFERENCES t_user (uid) ON DELETE RESTRICT ON UPDATE RESTRICT
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8;