package lk.menara.smartcity360.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomFormatter extends Formatter {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        sb.append(dateFormat.format(new Date(record.getMillis()))).append(" - ");
        sb.append(record.getLevel().getLocalizedName()).append(": ");
        if (record.getSourceClassName() != null) {
            sb.append(record.getSourceClassName());
            if (record.getSourceMethodName() != null) {
                sb.append(".").append(record.getSourceMethodName());
            }
        } else {
            sb.append(record.getLoggerName());
        }
        sb.append(" - ").append(formatMessage(record)).append(System.lineSeparator());
        if (record.getThrown() != null) {
            Throwable thrown = record.getThrown();
            sb.append(thrown.toString()).append(System.lineSeparator());
            StackTraceElement[] stackTrace = thrown.getStackTrace();
            if (stackTrace.length > 0) {
                sb.append("\tat ").append(stackTrace[0].toString()).append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
}