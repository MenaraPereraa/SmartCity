package lk.menara.smartcity360.util;

import lk.menara.smartcity360.logger.Log;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

public class Env {
    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(Env.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            Log.logger.log(Level.WARNING, "Exception ::", e);
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String key){
        return properties.getProperty(key);
    }
}
