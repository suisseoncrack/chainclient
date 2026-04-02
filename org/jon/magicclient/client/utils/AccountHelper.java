package org.jon.magicclient.client.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.net.ssl.HttpsURLConnection;
import net.minecraft.class_310;
import net.minecraft.class_320;
import net.minecraft.class_320.class_321;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class AccountHelper {
   public static final Map<String, String> accountMap = new HashMap();
   private final Gson gson = new Gson();
   private final String filePath;

   public AccountHelper() {
      String var10001 = System.getenv("APPDATA") != null ? System.getenv("APPDATA") : System.getProperty("user.dir");
      this.filePath = var10001 + "/.minecraft/AccountsMagicMap.ser";
   }

   public List<String> getAccountsName() {
      return new ArrayList(accountMap.keySet());
   }

   public String getToken(String userName) {
      return (String)accountMap.get(userName);
   }

   public void saveAccountsMap() {
      try {
         ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.filePath));

         try {
            oos.writeObject(accountMap);
         } catch (Throwable var5) {
            try {
               oos.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }

            throw var5;
         }

         oos.close();
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   public void readAccountsMap() {
      File file = new File(this.filePath);
      if (file.exists()) {
         try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(this.filePath));

            try {
               Map<String, String> loadedMap = (Map)ois.readObject();
               accountMap.clear();
               accountMap.putAll(loadedMap);
            } catch (Throwable var6) {
               try {
                  ois.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }

               throw var6;
            }

            ois.close();
         } catch (IOException | ClassNotFoundException var7) {
            var7.printStackTrace();
         }
      }

   }

   public void addNewAccount(String username, String password) {
      accountMap.put(username, password);
      this.saveAccountsMap();
   }

   public void removeAccount(String username) {
      accountMap.remove(username);
      this.saveAccountsMap();
   }

   public File initFileSelector() {
      FileDialog fileSelector = new FileDialog((Frame)null, "Select Cookie File");
      fileSelector.setMode(0);
      fileSelector.setVisible(true);
      return fileSelector.getFiles().length > 0 ? fileSelector.getFiles()[0] : null;
   }

   public List<String> getCookiesList(File file) {
      if (file == null) {
         return null;
      } else {
         try {
            List<String> content = new ArrayList();
            Scanner scanner = new Scanner(new FileReader(file));

            while(scanner.hasNextLine()) {
               content.add(scanner.nextLine());
            }

            scanner.close();
            return content;
         } catch (IOException var4) {
            var4.printStackTrace();
            return null;
         }
      }
   }

   private String extractAuthToken(List<String> cookieEntries) throws IOException {
      Set<String> uniqueCookies = new HashSet();
      String cookieString = (String)cookieEntries.stream().map((entry) -> {
         return entry.split("\t");
      }).filter((elements) -> {
         return elements.length > 6 && elements[0].endsWith("login.live.com") && uniqueCookies.add(elements[5]);
      }).map((elements) -> {
         return elements[5] + "=" + elements[6];
      }).collect(Collectors.joining("; "));
      return this.fetchAccessToken(cookieString);
   }

   private String parseAuthToken(String authToken) {
      String decodedToken = new String(Base64.getDecoder().decode(authToken), StandardCharsets.UTF_8);
      return decodedToken.split("\"rp://api.minecraftservices.com/\",", 2)[1];
   }

   private String buildXblAuth(String parsedToken) {
      String tokenValue = parsedToken.split("\"Token\":\"")[1].split("\"")[0];
      String userHash = parsedToken.split(Pattern.quote("{\"DisplayClaims\":{\"xui\":[{\"uhs\":\""))[1].split("\"")[0];
      return "XBL3.0 x=" + userHash + ";" + tokenValue;
   }

   private JsonObject authenticateWithXbl(String xblAuth) {
      String requestBody = "{\"identityToken\":\"" + xblAuth + "\",\"ensureLegacyEnabled\":true}";
      String response = this.sendPostRequest(requestBody);
      return (JsonObject)this.gson.fromJson(response, JsonObject.class);
   }

   private JsonObject fetchProfileData(String mcToken) {
      String profileData = this.fetchBearerData(mcToken);
      return (JsonObject)this.gson.fromJson(profileData, JsonObject.class);
   }

   private String fetchAccessToken(String cookies) throws IOException {
      String initialUrl = "https://sisu.xboxlive.com/connect/XboxLive/?state=login&cobrandId=8058f65d-ce06-4c30-9559-473c9275a65d&tid=896928775&ru=https%3A%2F%2Fwww.minecraft.net%2Fen-us%2Flogin&aid=1142970254";
      String followRedirect = this.followRedirect(initialUrl, (String)null, cookies);
      String followRedirect1 = this.followRedirect(followRedirect, cookies, cookies);
      String location = this.followRedirect(followRedirect1, cookies, cookies);
      return this.extractAccessToken(location);
   }

   private String followRedirect(String url, String cookies, String initialCookies) throws IOException {
      HttpsURLConnection connection = (HttpsURLConnection)(new URL(url)).openConnection();
      connection.setRequestMethod("GET");
      this.setRequestHeaders(connection, cookies != null ? cookies : initialCookies);
      connection.setInstanceFollowRedirects(false);
      connection.connect();
      String location = connection.getHeaderField("Location");
      return location == null ? "" : location.replaceAll(" ", "%20");
   }

   private void setRequestHeaders(HttpsURLConnection connection, String cookies) {
      connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
      connection.setRequestProperty("Accept-Language", "en-GB,en;q=0.9");
      connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
      if (cookies != null) {
         connection.setRequestProperty("Cookie", cookies);
      }

   }

   private String extractAccessToken(String url) {
      return url != null && url.contains("accessToken=") ? url.split("accessToken=")[1] : "";
   }

   private String sendPostRequest(String payload) {
      try {
         HttpsURLConnection conn = this.createHttpsConnection(payload);
         int statusCode = conn.getResponseCode();
         InputStream responseStream = statusCode >= 200 && statusCode < 400 ? conn.getInputStream() : conn.getErrorStream();
         if (responseStream == null) {
            return null;
         } else {
            StringBuilder result = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(responseStream));

            String line;
            try {
               while((line = bufferedReader.readLine()) != null) {
                  result.append(line);
               }
            } catch (Throwable var11) {
               try {
                  bufferedReader.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }

               throw var11;
            }

            bufferedReader.close();
            return result.toString();
         }
      } catch (Exception var12) {
         var12.printStackTrace();
         return null;
      }
   }

   public List<String> fetchUserDetails(List<String> cookieEntries) {
      List<String> userInfo = new ArrayList();
      if (cookieEntries != null && !cookieEntries.isEmpty()) {
         try {
            String authToken = this.extractAuthToken(cookieEntries);
            if (authToken.isEmpty()) {
               return userInfo;
            }

            String parsedToken = this.parseAuthToken(authToken);
            String xblAuth = this.buildXblAuth(parsedToken);
            JsonObject dataResp = this.authenticateWithXbl(xblAuth);
            if (dataResp != null && dataResp.has("access_token")) {
               String mcToken = dataResp.get("access_token").getAsString();
               JsonObject profileResp = this.fetchProfileData(mcToken);
               if (profileResp != null && profileResp.has("id") && profileResp.has("name")) {
                  userInfo.add(profileResp.get("name").getAsString());
                  userInfo.add(profileResp.get("id").getAsString());
                  userInfo.add(mcToken);
               }
            }
         } catch (Exception var9) {
            var9.printStackTrace();
         }

         return userInfo;
      } else {
         return userInfo;
      }
   }

   private HttpsURLConnection createHttpsConnection(String requestBody) throws IOException {
      HttpsURLConnection conn = (HttpsURLConnection)(new URL("https://api.minecraftservices.com/authentication/login_with_xbox")).openConnection();
      conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
      conn.setRequestMethod("POST");
      conn.setDoOutput(true);
      byte[] requestData = requestBody.getBytes(StandardCharsets.UTF_8);
      conn.setFixedLengthStreamingMode(requestData.length);
      conn.addRequestProperty("Content-Type", "application/json");
      conn.addRequestProperty("Accept", "application/json");
      conn.connect();
      OutputStream os = conn.getOutputStream();

      try {
         os.write(requestData);
      } catch (Throwable var8) {
         if (os != null) {
            try {
               os.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (os != null) {
         os.close();
      }

      return conn;
   }

   private String fetchBearerData(String bearerToken) {
      try {
         InputStream responseStream = getInputStream(bearerToken);
         if (responseStream == null) {
            return null;
         } else {
            StringBuilder result = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));

            String line;
            try {
               while((line = reader.readLine()) != null) {
                  result.append(line);
               }
            } catch (Throwable var9) {
               try {
                  reader.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }

               throw var9;
            }

            reader.close();
            return result.toString();
         }
      } catch (Exception var10) {
         var10.printStackTrace();
         return null;
      }
   }

   private static InputStream getInputStream(String bearerToken) throws IOException {
      HttpsURLConnection conn = (HttpsURLConnection)(new URL("https://api.minecraftservices.com/minecraft/profile")).openConnection();
      conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
      conn.addRequestProperty("Accept", "application/json");
      conn.addRequestProperty("Authorization", "Bearer " + bearerToken);
      return conn.getResponseCode() == 200 ? conn.getInputStream() : conn.getErrorStream();
   }

   public void loginByMicrosoft() {
      MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();

      try {
         MicrosoftAuthResult result = authenticator.loginWithWebview();
         class_320 session = new class_320(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), Optional.empty(), Optional.empty(), class_321.field_34962);
         this.setSession(session);
         accountMap.put(result.getProfile().getName(), result.getRefreshToken());
         this.saveAccountsMap();
         this.saveDefaultAccount(result.getProfile().getName());
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   private void saveDefaultAccount(String username) {
      try {
         File file = new File(this.filePath.replace("AccountsMagicMap.ser", "DefaultAccount.txt"));
         FileWriter writer = new FileWriter(file);

         try {
            writer.write(username);
         } catch (Throwable var7) {
            try {
               writer.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }

            throw var7;
         }

         writer.close();
      } catch (IOException var8) {
         var8.printStackTrace();
      }

   }

   public void autoLogin() {
      try {
         File file = new File(this.filePath.replace("AccountsMagicMap.ser", "DefaultAccount.txt"));
         if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            try {
               String username = reader.readLine();
               if (username != null && accountMap.containsKey(username)) {
                  String token = (String)accountMap.get(username);
                  if (token != null && !token.isEmpty()) {
                     this.tokenLogin(token);
                  }
               }
            } catch (Throwable var6) {
               try {
                  reader.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }

               throw var6;
            }

            reader.close();
         }
      } catch (IOException var7) {
         var7.printStackTrace();
      }

   }

   public void offlineLogin(String name) {
      UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
      class_320 session = new class_320(name, uuid.toString(), "0", Optional.empty(), Optional.empty(), class_321.field_1988);
      this.setSession(session);
   }

   public void cookieLogin() {
      File file = this.initFileSelector();
      List<String> cookieList = this.getCookiesList(file);
      List<String> accountData = this.fetchUserDetails(cookieList);
      if (accountData != null && accountData.size() >= 3) {
         class_320 session = new class_320((String)accountData.get(0), (String)accountData.get(1), (String)accountData.get(2), Optional.empty(), Optional.empty(), class_321.field_1990);
         this.setSession(session);
      }

   }

   public void directTokenLogin(String sessionToken) {
      try {
         System.out.println("Direct Token Login mit: " + sessionToken.substring(0, Math.min(10, sessionToken.length())) + "...");
         String[] parts = sessionToken.split(":");
         if (parts.length < 3 || !"token".equals(parts[0])) {
            System.err.println("Ungültiges Token-Format. Erwartet: token:accessToken:uuid");
            System.err.println("Aktuelles Format: " + sessionToken);
            throw new Exception("Ungültiges Token-Format. Erwartet: token:accessToken:uuid");
         }

         String accessToken = parts[1];
         String uuid = parts[2];
         System.out.println("AccessToken: " + accessToken.substring(0, Math.min(20, accessToken.length())) + "...");
         System.out.println("UUID: " + uuid);
         String[] accountInfo = this.getAccountInfoFromToken(accessToken);
         String realUsername = accountInfo[0];
         String realUuid = accountInfo[1];
         System.out.println("Account-Info erhalten: " + realUsername + " / " + realUuid);
         class_320 session = new class_320(realUsername, realUuid, sessionToken, Optional.empty(), Optional.empty(), class_321.field_34962);
         this.setSession(session);
         System.out.println("Direct Token Login erfolgreich für: " + realUsername);
      } catch (Exception var9) {
         System.err.println("Direct Token Login fehlgeschlagen: " + var9.getMessage());
         var9.printStackTrace();
      }

   }

   private String[] getAccountInfoFromToken(String accessToken) throws Exception {
      CloseableHttpClient client = HttpClients.createDefault();
      HttpGet request = new HttpGet("https://api.minecraftservices.com/minecraft/profile");
      request.setHeader("Authorization", "Bearer " + accessToken);
      CloseableHttpResponse response = client.execute(request);

      String[] var10;
      try {
         int statusCode = response.getStatusLine().getStatusCode();
         System.out.println("API Status Code: " + statusCode);
         String errorResponse;
         if (statusCode != 200) {
            if (statusCode == 401) {
               throw new Exception("Token ungültig oder abgelaufen (401)");
            }

            if (statusCode == 403) {
               throw new Exception("Zugriff verweigert (403)");
            }

            errorResponse = "";

            try {
               errorResponse = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            } catch (Exception var12) {
            }

            throw new Exception("API Fehler: " + statusCode + " - " + errorResponse);
         }

         errorResponse = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
         System.out.println("API Response: " + errorResponse.substring(0, Math.min(100, errorResponse.length())) + "...");
         JsonObject jsonObject = JsonParser.parseString(errorResponse).getAsJsonObject();
         String username = jsonObject.get("name").getAsString();
         String uuid = jsonObject.get("id").getAsString();
         var10 = new String[]{username, uuid};
      } catch (Throwable var13) {
         if (response != null) {
            try {
               response.close();
            } catch (Throwable var11) {
               var13.addSuppressed(var11);
            }
         }

         throw var13;
      }

      if (response != null) {
         response.close();
      }

      return var10;
   }

   public void tokenLogin(String token) {
      CookieManager cookieManager = (CookieManager)CookieHandler.getDefault();
      if (cookieManager != null) {
         cookieManager.getCookieStore().removeAll();
      }

      (new Thread(() -> {
         try {
            MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
            MicrosoftAuthResult result = authenticator.loginWithRefreshToken(token);
            if (result.getAccessToken() != null) {
               class_320 session = new class_320(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), Optional.empty(), Optional.empty(), class_321.field_34962);
               this.setSession(session);
            }
         } catch (Exception var5) {
            var5.printStackTrace();
         }

      })).start();
   }

   private void setSession(class_320 session) {
      try {
         class_310 client = class_310.method_1551();
         Field sessionField = null;
         String[] fieldNames = new String[]{"session", "field_1724", "authSession"};
         Class clazz = client.getClass();

         int var7;
         int var8;
         while(clazz != null && sessionField == null) {
            String[] var6 = fieldNames;
            var7 = fieldNames.length;
            var8 = 0;

            while(true) {
               if (var8 < var7) {
                  String name = var6[var8];

                  try {
                     sessionField = clazz.getDeclaredField(name);
                  } catch (NoSuchFieldException var12) {
                     ++var8;
                     continue;
                  }
               }

               clazz = clazz.getSuperclass();
               break;
            }
         }

         if (sessionField != null) {
            sessionField.setAccessible(true);

            try {
               Field modifiersField = Field.class.getDeclaredField("modifiers");
               modifiersField.setAccessible(true);
               modifiersField.setInt(sessionField, sessionField.getModifiers() & -17);
            } catch (Throwable var11) {
            }

            sessionField.set(client, session);
            System.out.println("Session successfully changed to: " + session.method_1676());
         } else {
            Field[] var15 = class_310.class.getDeclaredFields();
            var7 = var15.length;

            for(var8 = 0; var8 < var7; ++var8) {
               Field f = var15[var8];
               if (f.getType() == class_320.class) {
                  f.setAccessible(true);
                  f.set(client, session);
                  System.out.println("Session changed via type fallback to: " + session.method_1676());
                  break;
               }
            }
         }
      } catch (Exception var13) {
         System.err.println("Failed to set session: " + var13.getMessage());
         var13.printStackTrace();
      }

   }
}
