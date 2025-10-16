package io.hohichh.logging;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.FileHandler;

public class HolyLoggerConfig {
    private HolyLoggerConfig() {}

    public static void setup() throws IOException {
         Logger rootLogger = Logger.getLogger("");

        for (Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }

        FileHandler fileHandler = new FileHandler("chronicles.log", false);
        fileHandler.setFormatter(new HolyFormatter());

        rootLogger.addHandler(fileHandler);

        rootLogger.setLevel(Level.INFO);
    }
}
