package lk.menara.smartcity360.logger;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import lk.menara.smartcity360.util.CustomFormatter; // නිවැරදි කළ පැකේජය

public class Log {
    public static Logger logger = Logger.getLogger("errorlog");

    public static void init(){
        FileHandler fh;
        try {
            File logDir = new File("logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            // l ogs ෆෝල්ඩර් එක ඇතුළේ error.log ලෙස ලොග් සටහන් වේ
            fh = new FileHandler("logs/error.log", true);
            logger.addHandler(fh);

            CustomFormatter formatter = new CustomFormatter();
            fh.setFormatter(formatter);

            logger.info("Logger initialized successfully for SmartCity360.");
        } catch (Exception e) {
            logger.log(Level.WARNING, "Exception occurred during logger initialization ::", e);
        }
    }
}