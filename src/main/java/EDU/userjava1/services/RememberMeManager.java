package EDU.userjava1.services;

import java.io.*;
import java.util.Properties;

public class RememberMeManager {

    private static final String CONFIG_FILE = "config.properties";

    public static void saveCredentials(String email, String password, boolean rememberMe) {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            Properties prop = new Properties();
            prop.setProperty("email", email);
            prop.setProperty("password", password);
            prop.setProperty("rememberMe", String.valueOf(rememberMe));
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static String[] loadCredentials() {
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            Properties prop = new Properties();
            prop.load(input);
            String email = prop.getProperty("email");
            String password = prop.getProperty("password");
            return new String[]{email, password};
        } catch (IOException | NullPointerException ignored) {
            return null;
        }
    }

    public static boolean loadRememberMe() {
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            Properties prop = new Properties();
            prop.load(input);
            return Boolean.parseBoolean(prop.getProperty("rememberMe"));
        } catch (IOException | NullPointerException ignored) {
            return false;
        }
    }

    public static void clearCredentials() {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            Properties prop = new Properties();
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
