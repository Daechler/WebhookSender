package net.daechler.webhooksender;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebhookUtils {

    public static void sendWebhook(String webhookURL, String message) throws IOException {
        URL url = new URL(webhookURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        String jsonInputString = "{\"content\": \"" + message + "\"}";

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != 204) {  // If it's not 204 (No Content), then an error occurred.
            throw new IOException("Received non-204 response code from webhook: " + responseCode);
        }
    }
}
