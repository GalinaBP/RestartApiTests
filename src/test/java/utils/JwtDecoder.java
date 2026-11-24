package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;
import java.util.Map;

public final class JwtDecoder {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JwtDecoder() {
    }

    public static DecodedJwt decode(String token) {
        String[] parts = token.split("\\.", -1);
        if (parts.length != 3) {
            throw new IllegalArgumentException("A JWT has 3 dot-separated parts, this one has " + parts.length);
        }
        return new DecodedJwt(readJson(parts[0]), readJson(parts[1]));
    }

    private static Map<String, Object> readJson(String base64Url) {
        byte[] json = Base64.getUrlDecoder().decode(base64Url);
        try {
            return MAPPER.readValue(json, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
