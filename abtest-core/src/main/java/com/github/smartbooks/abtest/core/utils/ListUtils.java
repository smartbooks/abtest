package com.github.smartbooks.abtest.core.utils;

import java.util.List;

public class ListUtils {

    public static String mkString(List<String> dataList, String separatorChar) {

        StringBuilder value = new StringBuilder();

        if (dataList.isEmpty() == false) {

            int len = dataList.size();

            for (int i = 0; i < len; i++) {
                if (i == len - 1) {
                    value.append(String.format("%s", dataList.get(i)));
                } else {
                    value.append(String.format("%s%s", dataList.get(i), separatorChar));
                }
            }
        }

        return value.toString();
    }

}
