package com.example.Course.Registration.Util;

import java.util.List;

public class Util {

    public static String makeCSVString(List<String> list) {
        StringBuilder csvString = new StringBuilder();
        for (String string : list) {
            csvString.append(string).append(",");
        }
        return csvString.substring(0, csvString.length() - 1);
    }
}
