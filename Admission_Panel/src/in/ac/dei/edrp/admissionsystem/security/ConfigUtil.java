package in.ac.dei.edrp.admissionsystem.security;

//File: ConfigUtil.java
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
 private static Properties props = new Properties();

 // Call this once during app initialization
 public static void loadProperties(InputStream inputStream) {
     try {
         props.load(inputStream);
     } catch (Exception e) {
         e.printStackTrace();
     }
 }

 public static String get(String key) {
     return props.getProperty(key);
 }
}
