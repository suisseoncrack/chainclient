package org.jon.magicclient.client.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;

public class WebhookTester {
   public static void main(String[] args) {
      String webhookUrl = "nohook";
      System.out.println("Testing Discord webhook...");
      System.out.println("URL: " + webhookUrl);
      String simpleMessage = "{\"content\":\"Test message from WebhookTester - " + System.currentTimeMillis() + "\"}";
      testRequest(webhookUrl, simpleMessage, "Simple Message");
      String embedMessage = "{\"embeds\":[{\"title\":\"Test Embed\",\"description\":\"This is a test embed from MagicClient\",\"color\":65280,\"footer\":{\"text\":\"WebhookTester - " + System.currentTimeMillis() + "\"}}]}";
      testRequest(webhookUrl, embedMessage, "Embed Message");
      System.out.println("Webhook testing complete!");
   }

   private static void testRequest(String webhookUrl, String payload, String testName) {
      try {
         System.out.println("\n=== Testing " + testName + " ===");
         System.out.println("Payload: " + payload);
         HttpClient client = HttpClient.newHttpClient();
         HttpRequest request = HttpRequest.newBuilder().uri(URI.create(webhookUrl)).header("Content-Type", "application/json").header("User-Agent", "WebhookTester").POST(BodyPublishers.ofString(payload, StandardCharsets.UTF_8)).build();
         HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
         System.out.println("Response Code: " + response.statusCode());
         System.out.println("Response Body: " + (String)response.body());
         if (response.statusCode() == 204) {
            System.out.println("✅ " + testName + " - SUCCESS");
         } else {
            System.out.println("❌ " + testName + " - FAILED");
         }
      } catch (Exception var6) {
         System.err.println("❌ " + testName + " - ERROR: " + var6.getMessage());
         var6.printStackTrace();
      }

   }
}
