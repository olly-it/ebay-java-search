package it.olly.ebay.api;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.naming.AuthenticationException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.olly.ebay.api.model.AuthResponse;
import it.olly.ebay.api.model.BrowseItemSummeryResponse;

public class ApiHelper {
    // sand
    public static final String AUTH_API_URL = "https://api.sandbox.ebay.com/identity/v1/oauth2/token";
    // prod
    // public static final String AUTH_API_URL = "https://api.ebay.com/identity/v1/oauth2/token";

    public static final String SEARCH_API_URL = "https://api.ebay.com/buy/browse/v1/item_summary/search";
    public static final String MARKETPLACE = "EBAY_IT";

    private final String clientId;
    private final String clientSecret;
    private final String affiliateCampaignId;
    private final String affiliateReferenceId;
    private final ObjectMapper om;

    public ApiHelper(String clientId, String clientSecret, String affiliateCampaignId, String affiliateReferenceId) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.affiliateCampaignId = affiliateCampaignId;
        this.affiliateReferenceId = affiliateReferenceId;

        om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * returns a Base64 string of [client_id]:[client_secret]
     */
    private String b64(String clientId, String clinetSecret) throws IOException {
        return Base64.getEncoder()
                .encodeToString((clientId + ":" + clinetSecret).getBytes("utf-8"));
    }

    private String urlEncodeParams(Map<String, String> parameters) {
        return parameters.entrySet()
                .stream()
                .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "="
                        + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
    }

    /**
     * 
     * @return
     * @throws AuthenticationException if token is not valid
     * @throws IOException
     */
    public AuthResponse getNewToken() throws AuthenticationException, IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", "client_credentials");
        parameters.put("scope", "https://api.ebay.com/oauth/api_scope");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(AUTH_API_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic " + b64(clientId, clientSecret))
                .POST(HttpRequest.BodyPublishers.ofString(urlEncodeParams(parameters)))
                .build();

        return callEbay(request, AuthResponse.class);
    }

    /**
     * 
     * @param token
     * @param query
     * @param limit
     * @return
     * @throws AuthenticationException if token is not valid
     * @throws IOException
     */
    public BrowseItemSummeryResponse doSearch(String token, String query, int limit)
            throws AuthenticationException, IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("q", query);
        parameters.put("limit", String.valueOf(limit));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SEARCH_API_URL + "?" + urlEncodeParams(parameters)))
                .header("Authorization", "Bearer " + token)
                .header("X-EBAY-C-MARKETPLACE-ID", MARKETPLACE)
                .header("X-EBAY-C-ENDUSERCTX",
                        "affiliateCampaignId=" + affiliateCampaignId + ",affiliateReferenceId=" + affiliateReferenceId)
                .GET()
                .build();
        return callEbay(request, BrowseItemSummeryResponse.class);
    }

    private <T> T callEbay(HttpRequest request, Class<T> clazz) throws AuthenticationException, IOException {
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<?> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new IOException(e);
        }
        if (response.statusCode() == 401) {
            throw new AuthenticationException(response.statusCode() + " - " + response.body()
                    .toString());
        }
        return om.readValue(response.body()
                .toString(), clazz);

    }
}
