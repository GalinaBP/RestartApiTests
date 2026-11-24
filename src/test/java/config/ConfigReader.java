package config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

public final class ConfigReader {
    private static java.util.Properties properties = null;
    private static String env;
    private static String fileName;

    private ConfigReader() {
    }

    private static synchronized java.util.Properties props() {
        if (properties == null) {
            env = System.getProperty("env", "local");
            fileName = "config-" + env + ".properties";
            Properties loaded = new Properties();

            try (InputStream stream = ConfigReader.class.getClassLoader()
                    .getResourceAsStream(fileName)) {
                if (stream == null) {
                    throw new IllegalStateException("No " + fileName + " found. Use -Denv=local or - Denv=railway");
                }
                loaded.load(stream);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            properties = loaded;
            System.out.println("Config: env=" + env + ", base.url=" + loaded.getProperty("base.url"));
        }
        return properties;
    }

    private static String required(String key) {
        String value = props().getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(key + " is missing in " + fileName);
        }
        return value.trim();
    }

    public static String baseUrl() {
        return required("base.url");
    }

    public static String smokeEmail() {
        return required("smoke.email");
    }

    public static String corsAllowedOrigin() {
        return required("cors.allowed.origin");
    }
    public static String smokePassword() {
        String fromFile = props().getProperty("smoke.password");
        if (fromFile != null && !fromFile.isBlank()) {
            return fromFile.trim();
        }

        String fromEnv = System.getenv("SMOKE_PASSWORD");
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv.trim();
        }

        throw new IllegalStateException(
                "smoke.password is not in " + fileName + " and SMOKE_PASSWORD is not set");
    }

    public static boolean localOnlyAllowed() {
        String value = props().getProperty("local.only.allowed");
        if (value == null) {
            return false;
        }
        return Boolean.parseBoolean(value.trim());
    }
}


