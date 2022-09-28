package com.woodyside.reactive.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@UtilityClass
public class DateResponseFormatter {

    public static String getTimestamp() {

        LocalDate localDate = LocalDate.now();

        LocalTime localTime = LocalTime.now();

        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDate,localTime,
                ZoneId.of("Europe/Moscow"));

        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)
                .format(zonedDateTime);
    }
}
