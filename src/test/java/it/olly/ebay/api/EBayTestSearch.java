package it.olly.ebay.api;

import org.junit.jupiter.api.Test;

import it.olly.ebay.api.model.BrowseItemSummeryResponse;

public class EBayTestSearch {
    static final String clientId = "<App ID (Client ID) freom developer's portal>";
    static final String clientSecret = "<Cert ID (Client Secret) from developer's portal>";
    static final String affiliateCampaignId = "<from developer's portal>";
    static final String affiliateReferenceId = "<from developer's portal>";

    static final String token = "v^1.1#i^1 ... BxIAAA==";

    @Test
    public void doSearch() throws Exception {
        ApiHelper helper = new ApiHelper(clientId, clientSecret, affiliateCampaignId, affiliateReferenceId);
        BrowseItemSummeryResponse doSearch = helper.doSearch(token, "televisore", 3);
        System.out.println("search> " + doSearch);
    }

}
