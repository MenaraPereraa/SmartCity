package lk.menara.smartcity360.util;

import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.util.Date;

public class CustomFormatter {

    private static final String PATTERN = "dd MMM yyyy HH:mm";

    private CustomFormatter() {}

    public static String formatDateTime(Date date) {
        if (date == null) return "\u2014";
        return new SimpleDateFormat(PATTERN).format(date);
    }

    public static String formatTimestamp(Timestamp ts) {
        if (ts == null) return "\u2014";
        return new SimpleDateFormat(PATTERN).format(ts);
    }
}