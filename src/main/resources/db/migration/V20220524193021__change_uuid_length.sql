ALTER TABLE t_advice MODIFY uid VARCHAR (36);
ALTER TABLE t_appointment MODIFY uid VARCHAR (36);
ALTER TABLE t_shift_schedule MODIFY uid VARCHAR (36);

ALTER TABLE t_doctor MODIFY uid VARCHAR (36);
ALTER TABLE t_shift MODIFY uid VARCHAR (36);

-- 清除外键
ALTER TABLE t_doctor DROP FOREIGN KEY t_doctor_ibfk_1;
ALTER TABLE t_section MODIFY uid VARCHAR (36);
ALTER TABLE t_doctor MODIFY section_id VARCHAR (36);
ALTER TABLE t_doctor ADD CONSTRAINT foreign key(section_id) REFERENCES t_section(uid) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- -- 清除外键
ALTER TABLE t_advice DROP FOREIGN KEY t_advice_ibfk_1;
ALTER TABLE t_appointment DROP FOREIGN KEY t_appointment_ibfk_1;
ALTER TABLE t_appointment MODIFY user_id VARCHAR (36);
ALTER TABLE t_advice MODIFY user_id VARCHAR (36);
ALTER TABLE t_user MODIFY uid VARCHAR (36);
ALTER TABLE t_advice ADD CONSTRAINT foreign key(user_id) REFERENCES t_user(uid) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE t_appointment ADD CONSTRAINT foreign key(user_id) REFERENCES t_user(uid) ON DELETE RESTRICT ON UPDATE RESTRICT;

