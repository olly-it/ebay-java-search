package it.olly.ebay.api;

import java.util.Base64;

public class GenerateBase64String {

    public static void main(String[] args) throws Exception {
        String clientId = "<App ID (Client ID)>";
        String clinetSecret = "<Cert ID (Client Secret>";

        // <client_id>:<client_secret>
        String b64 = Base64.getEncoder()
                .encodeToString((clientId + ":" + clinetSecret).getBytes("utf-8"));

        System.out.println(b64);
    }

}
