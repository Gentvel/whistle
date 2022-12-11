package com.whistle.starter.consts;

import java.time.format.DateTimeFormatter;

/**
 * @author Gentvel
 */
public final class DateConst {
    public static final String C_FORMATTER_STRING = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter C_FORMATTER = DateTimeFormatter.ofPattern(C_FORMATTER_STRING);
}
