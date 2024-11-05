package it.olly.ebay.api;

import org.junit.jupiter.api.Test;

import it.olly.ebay.api.model.AuthResponse;

public class EBayTestAuth {
    static final String clientId = "<App ID (Client ID) freom developer's portal>";
    static final String clientSecret = "<Cert ID (Client Secret) from developer's portal>";
    static final String affiliateCampaignId = "<from developer's portal>";
    static final String affiliateReferenceId = "<from developer's portal>";

    @Test
    public void retrieveToken() throws Exception {
        ApiHelper helper = new ApiHelper(clientId, clientSecret, affiliateCampaignId, affiliateReferenceId);
        AuthResponse newToken = helper.getNewToken();
        System.out.println("token> " + newToken);
    }

}
