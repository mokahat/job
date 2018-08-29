package com.moka.job.utils;

import java.text.MessageFormat;


public class MapperUtils {

    public static String statement(String namespace, String id) {
        return MessageFormat.format("{0}.{1}", namespace, id);
    }
}