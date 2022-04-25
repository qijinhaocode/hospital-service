package com.qi.hospital.dto.shift;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.qi.hospital.model.dcotor.DoctorTitle;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ShiftScheduleResponse extends BaseRowModel {
    private String id;
    @ExcelProperty(value = "日期", index = 0)
    private LocalDate localDate;
    @ExcelProperty(value = "科室", index = 1)
    private String sectionName;
    @ExcelProperty(value = "医生姓名", index = 2)
    private String doctorName;

    private DoctorTitle doctorTitle;
    @ExcelProperty(value = "挂号费", index = 4)
    private Double registrationFee;
    @ExcelProperty(value = "上午剩余号数", index = 5)
    private Integer morning;
    @ExcelProperty(value = "下午剩余号数", index = 6)
    private Integer afternoon;

    private String doctorJobNumber;

    private String doctorIntro;
    @ExcelProperty(value = "医生职称", index = 3)
    private String doctorTitleDescription;
}