package com.qi.hospital.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ParseString {
    public static void main(String[] args) {
        String s  = "    private String doctorJobNumber;\n" +
                "    private Integer weekMondayMorning;\n" +
                "    private Integer weekMondayAfternoon;\n" +
                "    private Integer weekTuesdayMorning;\n" +
                "    private Integer weekTuesdayAfternoon;\n" +
                "    private Integer weekWednesdayMorning;\n" +
                "    private Integer weekWednesdayAfternoon;\n" +
                "    private Integer weekThursdayMorning;\n" +
                "    private Integer weekThursdayAfternoon;\n" +
                "    private Integer weekFridayMorning;\n" +
                "    private Integer weekFridayAfternoon;\n" +
                "    private Integer weekSaturdayMorning;\n" +
                "    private Integer weekSaturdayAfternoon;\n" +
                "    private Integer weekSundayMorning;\n" +
                "    private Integer weekSundayAfternoon;";
       List<String> strings = List.of(s.split(";"));
        strings.stream().map(s1->{
            System.out.println("\""+s1.substring(21)+"\""+":"+"3"+",");
           return "+s1.substring(21)+";

        }).collect(Collectors.toList());

    }

}
