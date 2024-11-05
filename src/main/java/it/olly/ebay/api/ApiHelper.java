package it.olly.ebay.api;

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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.olly.ebay.api.model.AuthResponse;
import it.olly.ebay.api.model.BrowseItemSummeryResponse;

public class ApiHelper {
    public static final String AUTH_API_URL = "https://api.ebay.com/identity/v1/oauth2/token";
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
    private String b64(String clientId, String clinetSecret) throws Exception {
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

    public AuthResponse getNewToken() throws Exception {
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

        HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return om.readValue(response.body()
                .toString(), AuthResponse.class);
    }

    public BrowseItemSummeryResponse doSearch(String token, String query, int limit) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

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

        HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return om.readValue(response.body()
                .toString(), BrowseItemSummeryResponse.class);

    }
}
