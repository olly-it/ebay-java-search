package it.olly.ebay.api;

public class SDKEBayCall {
    /*
    // Sostituisci con i tuoi dettagli di applicazione
    private static final String CLIENT_ID = "YOUR_CLIENT_ID";
    private static final String CLIENT_SECRET = "YOUR_CLIENT_SECRET";
    
    public static void main(String[] args) {
        try {
            // Step 1: Ottenere il token di accesso OAuth2
            String accessToken = getOAuthAccessToken();
    
            // Step 2: Usare il token per effettuare una ricerca
            searchItems("laptop", accessToken);
    
        } catch (OAuthException e) {
            e.printStackTrace();
        }
    }
    
    private static String getOAuthAccessToken() throws OAuthException {
        // Configura l'istanza OAuth2Api con le credenziali
        OAuth2Api oauth2Api = new OAuth2Api();
        CredentialUtil.load(CLIENT_ID, CLIENT_SECRET);
    
        // Ottiene il token di accesso
        OAuthResponse response = oauth2Api.getApplicationToken(Environment.PRODUCTION,
                                                               Arrays.asList(OAuthScope.BUY_BROWSE));
        return response.getAccessToken()
                .getToken();
    }
    
    private static void searchItems(String query, String accessToken) {
        // Crea un'istanza della Browse API
        Browse browse = new Browse(Environment.PRODUCTION);
    
        // Effettua una ricerca
        try {
            SearchPagedCollection searchResults = browse.search(query, null, null, null, accessToken);
    
            // Elenca i risultati
            System.out.println("Risultati della ricerca:");
            List<ItemSummary> items = searchResults.getItemSummaries();
            if (items != null) {
                for (ItemSummary item : items) {
                    System.out.println("Titolo: " + item.getTitle());
                    System.out.println("Prezzo: " + item.getPrice()
                            .getValue() + " "
                            + item.getPrice()
                                    .getCurrency());
                    System.out.println("Condizione: " + item.getCondition());
                    System.out.println("URL: " + item.getItemWebUrl());
                    System.out.println("-----");
                }
            } else {
                System.out.println("Nessun risultato trovato.");
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
}
