package org.jon.magicclient.utils;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.class_310;
import net.minecraft.class_320;
import net.minecraft.class_320.class_321;
import org.jon.magicclient.Magicclient;

public class SessionUtils {
   public static String getUsername() {
      return Magicclient.username;
   }

   public static class_320 getSession() {
      return class_310.method_1551().method_1548();
   }

   public static class_320 createSession(String username, String uuidString, String ssid) {
      if (uuidString.length() == 32) {
         String var10000 = uuidString.substring(0, 8);
         uuidString = var10000 + "-" + uuidString.substring(8, 12) + "-" + uuidString.substring(12, 16) + "-" + uuidString.substring(16, 20) + "-" + uuidString.substring(20);
      }

      return new class_320(username, uuidString, ssid, Optional.empty(), Optional.empty(), class_321.field_34962);
   }

   public static class_320 createSession(String username, UUID uuid, String ssid) {
      return new class_320(username, uuid.toString(), ssid, Optional.empty(), Optional.empty(), class_321.field_34962);
   }

   public static class_320 createTokenSession() {
      return new class_320(Magicclient.username, Magicclient.uuidString, Magicclient.sessionid, Optional.empty(), Optional.empty(), class_321.field_34962);
   }

   public static void setSession(class_320 session) {
      try {
         Magicclient.currentSession = session;
         class_310 client = class_310.method_1551();
         Field sessionField = null;
         String[] fieldNames = new String[]{"session", "field_1724", "authSession"};
         Class clazz = client.getClass();

         while(clazz != null && sessionField == null) {
            String[] var5 = fieldNames;
            int var6 = fieldNames.length;
            int var7 = 0;

            while(true) {
               if (var7 < var6) {
                  String name = var5[var7];

                  try {
                     sessionField = clazz.getDeclaredField(name);
                  } catch (NoSuchFieldException var11) {
                     ++var7;
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
            } catch (Throwable var10) {
            }

            sessionField.set(client, session);
            System.out.println("Session erfolgreich gesetzt auf: " + session.method_1676());
         }
      } catch (Exception var12) {
         System.err.println("Fehler beim Setzen der Session: " + var12.getMessage());
         var12.printStackTrace();
      }

   }

   public static void restoreSession() {
      Magicclient.currentSession = Magicclient.originalSession;
      setSession(Magicclient.originalSession);
   }
}
