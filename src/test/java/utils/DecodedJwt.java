package utils;

import java.util.Map;

public record DecodedJwt(Map<String, Object> header, Map<String, Object> payload) {

    public String alg() {
        return (String) header.get("alg");
    }

    public String sub() {
        return (String) payload.get("sub");
    }

    public String tokenUse() {
        return (String) payload.get("token_use");
    }

    public String email() {
        return (String) payload.get("email");
    }

    public String role() {
        return (String) payload.get("role");
    }

    public long iat() {
        return ((Number) payload.get("iat")).longValue();
    }

    public long exp() {
        return ((Number) payload.get("exp")).longValue();
    }

    public Object claim(String name) {
        return payload.get(name);
    }
}
