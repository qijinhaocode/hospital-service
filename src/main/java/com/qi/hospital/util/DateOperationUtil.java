package com.qi.hospital.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DateOperationUtil {
    public static List<String> collectTimeFrame(LocalDate start, LocalDate end) {
        // 用起始时间作为流的源头，按照每次加一天的方式创建一个无限流
        return Stream.iterate(start, localDate -> localDate.plusDays(1))
                // 截断无限流，长度为起始时间和结束时间的差+1个
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                // 由于最后要的是字符串，所以map转换一下
                .map(LocalDate::toString)
                // 把流收集为List
                .collect(Collectors.toList());
    }
    //Datetime transfer to String
    public static String LocalDate2String(LocalDate localDate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDate.format(dateTimeFormatter);
    }

    //String to Localdate
    public static LocalDate String2LocalDate(String localDateString){
        return LocalDate.parse(localDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    // localDate to Week
    public static DayOfWeek LocalDate2Week(LocalDate localDate){
        return localDate.getDayOfWeek();
    }
}
