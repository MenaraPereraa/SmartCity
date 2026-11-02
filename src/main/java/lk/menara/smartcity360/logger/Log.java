package lk.menara.smartcity360.logger;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
    public static Logger logger = Logger.getLogger("errorlog");

    public static void init(){
        FileHandler fh;
        try {
            File logDir = new File("logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            fh = new FileHandler("logs/error.log", true);
            logger.addHandler(fh);

            logger.info("Logger initialized successfully for SmartCity360.");
        } catch (Exception e) {
            logger.log(Level.WARNING, "Exception occurred during logger initialization ::", e);
        }
    }
}