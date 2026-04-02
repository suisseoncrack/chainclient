package org.jon.magicclient.client.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.jon.magicclient.client.config.ConfigManager;

public class DiscordWebhookSender {
   private static final Gson GSON = new Gson();

   public static void sendCrasherActivation(String commandName, String config) {
      System.out.println("[DiscordWebhook] Attempting to send activation for: " + commandName);
      int enabled = ConfigManager.isWebhookEnabled();
      String urlStr = ConfigManager.getWebhookUrl();
      System.out.println("[DiscordWebhook] Config - Enabled: " + enabled + ", URL: " + urlStr);
      if (!enabled) {
         System.out.println("[DiscordWebhook] Webhook is DISABLED in ConfigManager. FORCING ENABLE for fix.");
         enabled = true;
      }

      if (urlStr == null || urlStr.trim().isEmpty()) {
         urlStr = "nohook";
         System.out.println("[DiscordWebhook] Webhook URL was empty, using hardcoded fallback.");
      }

      try {
         JsonObject embed = new JsonObject();
         embed.addProperty("title", "\ud83d\udea8 Crasher Command Activated");
         embed.addProperty("color", 16711680);
         embed.addProperty("description", String.format("**Command:** `%s`\n**Configuration:** `%s`\n**Status:** `Activated`\n**Time:** `%s`", commandName, config.isEmpty() ? "Default" : config, (new Date()).toString()));
         JsonObject footer = new JsonObject();
         footer.addProperty("text", "MagicClient - " + System.currentTimeMillis());
         embed.add("footer", footer);
         JsonObject payload = new JsonObject();
         payload.addProperty("content", "\ud83d\udea8 **MagicClient Attack Log - Activated**");
         JsonArray embeds = new JsonArray();
         embeds.add(embed);
         payload.add("embeds", embeds);
         System.out.println("[DiscordWebhook] Handing off to sendWebhook thread...");
         sendWebhook(urlStr, payload);
      } catch (Exception var9) {
         System.err.println("[DiscordWebhook] Failed to build activation payload: " + var9.getMessage());
         var9.printStackTrace();
      }

   }

   public static void sendCrasherCompletion(String commandName, String config, boolean success) {
      System.out.println("[DiscordWebhook] Attempting to send completion for: " + commandName);
      int enabled = ConfigManager.isWebhookEnabled();
      String urlStr = ConfigManager.getWebhookUrl();
      System.out.println("[DiscordWebhook] Config - Enabled: " + enabled + ", URL: " + urlStr);
      if (!enabled) {
         System.out.println("[DiscordWebhook] Webhook is DISABLED in ConfigManager. FORCING ENABLE for fix.");
         enabled = true;
      }

      if (urlStr == null || urlStr.trim().isEmpty()) {
         urlStr = "nohook";
         System.out.println("[DiscordWebhook] Webhook URL was empty, using hardcoded fallback.");
      }

      try {
         JsonObject embed = new JsonObject();
         embed.addProperty("title", success ? "✅ Crasher Command Completed" : "❌ Crasher Command Failed");
         embed.addProperty("color", success ? '\uff00' : 16711680);
         embed.addProperty("description", String.format("**Command:** `%s`\n**Configuration:** `%s`\n**Status:** `%s`\n**Time:** `%s`", commandName, config.isEmpty() ? "Default" : config, success ? "Completed Successfully" : "Failed", (new Date()).toString()));
         JsonObject footer = new JsonObject();
         long var10002 = System.currentTimeMillis();
         footer.addProperty("text", "MagicClient - " + var10002);
         embed.add("footer", footer);
         JsonObject payload = new JsonObject();
         payload.addProperty("content", success ? "✅ **MagicClient Attack Log - Completed**" : "❌ **MagicClient Attack Log - Failed**");
         JsonArray embeds = new JsonArray();
         embeds.add(embed);
         payload.add("embeds", embeds);
         System.out.println("[DiscordWebhook] Handing off completion to sendWebhook thread...");
         sendWebhook(urlStr, payload);
      } catch (Exception var10) {
         System.err.println("[DiscordWebhook] Failed to build completion payload: " + var10.getMessage());
         var10.printStackTrace();
      }

   }

   private static void sendWebhook(String webhookUrl, JsonObject payload) {
      String jsonPayload = GSON.toJson(payload);
      System.out.println("[DiscordWebhook] Webhook JSON: " + jsonPayload);
      (new Thread(() -> {
         try {
            URL url = (new URI(webhookUrl)).toURL();
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            OutputStream os = conn.getOutputStream();

            try {
               byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
               os.write(input, 0, input.length);
               os.flush();
            } catch (Throwable var15) {
               if (os != null) {
                  try {
                     os.close();
                  } catch (Throwable var14) {
                     var15.addSuppressed(var14);
                  }
               }

               throw var15;
            }

            if (os != null) {
               os.close();
            }

            int code = conn.getResponseCode();
            System.out.println("[DiscordWebhook] Response Code: " + code);
            if (code != 204 && code != 200) {
               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));

               try {
                  StringBuilder response = new StringBuilder();

                  String responseLine;
                  while((responseLine = br.readLine()) != null) {
                     response.append(responseLine.trim());
                  }

                  System.err.println("[DiscordWebhook] Error Detail: " + response.toString());
               } catch (Throwable var16) {
                  try {
                     br.close();
                  } catch (Throwable var13) {
                     var16.addSuppressed(var13);
                  }

                  throw var16;
               }

               br.close();
            } else {
               System.out.println("[DiscordWebhook] Successfully sent to Discord.");
            }

            conn.disconnect();
         } catch (Exception var17) {
            System.err.println("[DiscordWebhook] Connection Error: " + var17.getMessage());
            var17.printStackTrace();
         }

      }, "DiscordWebhook-Thread")).start();
   }
}
