package io.hohichh.logging;

import java.time.Instant;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

class HolyFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {

        Object[] params = record.getParameters();
        String day = (params != null && params.length > 0) ? params[0].toString() : "??";
        String factionName = (params != null && params.length > 1) ? params[1].toString() : "??";
        String source;
        if (record.getLoggerName().contains("Factory")) {
            source = "ЦЕНТРАЛЬНАЯ ФАБРИКА";
        } else if (record.getLoggerName().contains("Faction")) {
            source = "'" + factionName + "'";
        } else if (record.getLoggerName().contains("Main")) {
            source = "ВЕЛИКАЯ ХРОНИКА";
        }
        else {
            source = "НЕИЗВЕСТНЫЙ ИСТОЧНИК";
        }

        return String.format(
                "[ХРОНИКА. ДЕНЬ %s] :: Источник: %s :: Запись: %s%n",
                day,
                source,
                record.getMessage()
        );
    }
}