alter table t_appointment add constraint FK_ID foreign key(doctor_job_number) REFERENCES t_doctor(job_number)