package it.olly.ebay.api.model;

public record AuthResponse(String access_token, Integer expires_in, String token_type) {
}
