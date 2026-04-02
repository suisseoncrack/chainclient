package org.jon.magicclient.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.minecraft.class_310;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class APIUtils {
   public static String[] getProfileInfo(String token) throws IOException {
      try {
         CloseableHttpClient client = HttpClients.createDefault();
         HttpGet request = new HttpGet("https://api.minecraftservices.com/minecraft/profile");
         request.setHeader("Authorization", "Bearer " + token);
         CloseableHttpResponse response = client.execute(request);
         int statusCode = response.getStatusLine().getStatusCode();
         System.out.println("API Response Status: " + statusCode);
         if (statusCode == 401) {
            throw new RuntimeException("Token ungültig oder abgelaufen (401)");
         } else if (statusCode == 403) {
            throw new RuntimeException("Zugriff verweigert (403)");
         } else if (statusCode != 200) {
            throw new RuntimeException("API Fehler: " + statusCode);
         } else {
            String jsonString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            System.out.println("API Response: " + jsonString);
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            if (jsonObject.has("name") && jsonObject.has("id")) {
               String IGN = jsonObject.get("name").getAsString();
               String UUID = jsonObject.get("id").getAsString();
               return new String[]{IGN, UUID};
            } else {
               throw new RuntimeException("Ungültige API Antwort - fehlende Felder");
            }
         }
      } catch (Exception var9) {
         System.err.println("Fehler beim Abrufen der Profilinformationen: " + var9.getMessage());
         throw new RuntimeException("Token ungültig: " + var9.getMessage());
      }
   }

   public static Boolean validateSession(String token) {
      try {
         String[] profileInfo = getProfileInfo(token);
         String ign = profileInfo[0];
         String uuidString = profileInfo[1];
         if (uuidString.length() == 32) {
            String var10000 = uuidString.substring(0, 8);
            uuidString = var10000 + "-" + uuidString.substring(8, 12) + "-" + uuidString.substring(12, 16) + "-" + uuidString.substring(16, 20) + "-" + uuidString.substring(20);
         }

         UUID uuid = UUID.fromString(uuidString);
         return ign.equals(class_310.method_1551().method_1548().method_1676()) && uuid.equals(class_310.method_1551().method_1548().method_44717());
      } catch (Exception var5) {
         return false;
      }
   }

   public static int changeSkin(String url, String token) {
      try {
         CloseableHttpClient client = HttpClients.createDefault();
         HttpPost request = new HttpPost("https://api.minecraftservices.com/minecraft/profile/skins");
         request.setHeader("Authorization", "Bearer " + token);
         request.setHeader("Content-Type", "application/json");
         String jsonString = String.format("{ \"variant\": \"classic\", \"url\": \"%s\"}", url);
         request.setEntity(new StringEntity(jsonString));
         CloseableHttpResponse response = client.execute(request);
         return response.getStatusLine().getStatusCode();
      } catch (Exception var6) {
         return -1;
      }
   }

   public static int changeName(String newName, String token) {
      try {
         CloseableHttpClient client = HttpClients.createDefault();
         HttpPut request = new HttpPut("https://api.minecraftservices.com/minecraft/profile/name/" + newName);
         request.setHeader("Authorization", "Bearer " + token);
         CloseableHttpResponse response = client.execute(request);
         return response.getStatusLine().getStatusCode();
      } catch (Exception var5) {
         return -1;
      }
   }
}
