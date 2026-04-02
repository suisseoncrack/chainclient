package org.jon.magicclient.client.hud;

import java.util.Locale;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import org.jon.magicclient.client.config.ConfigManager;

public final class ClickHudOverlay {
   private static volatile long lastPacketTimeMs = -1L;
   private static volatile long lastReceivePacketTimeMs = -1L;
   private static volatile int packetsPerSecond = 0;
   private static volatile int packetCount = 0;
   private static volatile long lastSecond = System.currentTimeMillis() / 1000L;
   private static long lastTimeUpdate = -1L;
   private static float tps = 20.0F;
   private static long lastTpsDropTime = -1L;

   private ClickHudOverlay() {
   }

   public static void markPacket() {
      lastPacketTimeMs = System.currentTimeMillis();
      long currentSecond = System.currentTimeMillis() / 1000L;
      if (currentSecond != lastSecond) {
         packetsPerSecond = packetCount;
         packetCount = 0;
         lastSecond = currentSecond;
      }

      ++packetCount;
   }

   public static void markReceivePacket() {
      lastReceivePacketTimeMs = System.currentTimeMillis();
   }

   public static void resetLagStatus() {
      lastReceivePacketTimeMs = System.currentTimeMillis();
      lastTimeUpdate = -1L;
      tps = 20.0F;
      lastTpsDropTime = -1L;
   }

   public static void onTimeUpdate() {
      long currentTime = System.currentTimeMillis();
      if (lastTimeUpdate != -1L) {
         long timeDiff = currentTime - lastTimeUpdate;
         if (timeDiff > 0L) {
            float currentTps = 20000.0F / (float)timeDiff;
            tps = Math.min(20.0F, currentTps);
         }
      }

      lastTimeUpdate = currentTime;
      lastTpsDropTime = currentTime;
   }

   public static void render(class_332 context) {
      class_310 client = class_310.method_1551();
      if (client != null && client.field_1772 != null && ConfigManager.isHudEnabled()) {
         long now = System.currentTimeMillis();
         if (lastTpsDropTime != -1L && client.method_1562() != null) {
            long timeSinceLastUpdate = now - lastTpsDropTime;
            if (timeSinceLastUpdate > 2000L) {
               int intervals = (int)((timeSinceLastUpdate - 2000L) / 2000L);
               if (intervals > 0) {
                  float droppedTps = 20.0F - (float)intervals * 1.0F;
                  tps = Math.max(0.0F, Math.min(tps, droppedTps));
               }
            }
         }

         int x = ConfigManager.getHudX();
         int y = ConfigManager.getHudY();
         context.method_51448().method_22903();
         context.method_51448().method_46416(0.0F, 0.0F, 500.0F);
         String displayName = "ChainClient";
         context.method_51448().method_22903();
         context.method_51448().method_46416((float)x, (float)y, 0.0F);
         context.method_51448().method_22905(1.5F, 1.5F, 1.0F);
         context.method_27535(client.field_1772, class_2561.method_43470(displayName).method_27695(new class_124[]{class_124.field_1076, class_124.field_1067}), 0, 0, 16777215);
         int nameWidth = client.field_1772.method_1727(displayName);
         context.method_51448().method_22909();
         context.method_27535(client.field_1772, class_2561.method_43470("v1.2").method_27692(class_124.field_1080), x + (int)((float)nameWidth * 1.5F) + 10, y + 10, 16777215);
         if (lastReceivePacketTimeMs != -1L && client.method_1562() != null && !client.method_1542()) {
            long lagMs = now - lastReceivePacketTimeMs;
            if (lagMs > 1000L) {
               String lagText = "SERVER LAGGING";
               String msText = lagMs + "ms";
               int screenWidth = client.method_22683().method_4486();
               int screenHeight = client.method_22683().method_4502();
               context.method_51448().method_22903();
               context.method_51448().method_46416((float)screenWidth / 2.0F, (float)screenHeight / 4.0F, 0.0F);
               context.method_51448().method_22905(2.0F, 2.0F, 1.0F);
               context.method_27534(client.field_1772, class_2561.method_43470(lagText).method_27695(new class_124[]{class_124.field_1061, class_124.field_1067}), 0, 0, 16777215);
               context.method_51448().method_22909();
               context.method_51448().method_22903();
               context.method_51448().method_46416((float)screenWidth / 2.0F, (float)screenHeight / 4.0F + 25.0F, 0.0F);
               context.method_51448().method_22905(1.5F, 1.5F, 1.0F);
               context.method_27534(client.field_1772, class_2561.method_43470(msText).method_27692(class_124.field_1054), 0, 0, 16777215);
               context.method_51448().method_22909();
            }
         }

         int lineY = y + 20;
         if (ConfigManager.isHudShowIp()) {
            context.method_27535(client.field_1772, class_2561.method_43470("IP: ").method_27692(class_124.field_1080).method_10852(class_2561.method_43470(getServerIp(client)).method_27692(class_124.field_1068)), x, lineY, 16777215);
            lineY += 10;
         }

         if (ConfigManager.isHudShowFps()) {
            context.method_27535(client.field_1772, class_2561.method_43470("FPS: ").method_27692(class_124.field_1080).method_10852(class_2561.method_43470(String.valueOf(client.method_47599())).method_27692(class_124.field_1068)), x, lineY, 16777215);
            lineY += 10;
         }

         if (ConfigManager.isHudShowTps()) {
            context.method_27535(client.field_1772, class_2561.method_43470("TPS: ").method_27692(class_124.field_1080).method_10852(class_2561.method_43470(getTps(client)).method_27692(class_124.field_1068)), x, lineY, 16777215);
            lineY += 10;
         }

         String pingText = getLastPacketAgo();
         context.method_27535(client.field_1772, class_2561.method_43470("Last Packet: ").method_27692(class_124.field_1080).method_10852(class_2561.method_43470(pingText).method_27692(class_124.field_1068)), x, lineY, 16777215);
         lineY += 10;
         if (packetsPerSecond > 0) {
            context.method_27535(client.field_1772, class_2561.method_43470("Packets/s: ").method_27692(class_124.field_1080).method_10852(class_2561.method_43470(String.valueOf(packetsPerSecond)).method_27692(class_124.field_1060)), x, lineY, 16777215);
            lineY += 10;
         }

         if (ConfigManager.isHudShowOnline()) {
            context.method_27535(client.field_1772, class_2561.method_43470("Online: ").method_27692(class_124.field_1080).method_10852(class_2561.method_43470(getOnline(client)).method_27692(class_124.field_1068)), x, lineY, 16777215);
         }

         context.method_51448().method_22909();
      }
   }

   private static String getServerIp(class_310 client) {
      if (client.method_1542()) {
         return "singleplayer";
      } else {
         return client.method_1558() != null && client.method_1558().field_3761 != null ? client.method_1558().field_3761 : "unknown";
      }
   }

   private static String getTps(class_310 client) {
      return client.method_1562() == null ? "-" : String.format(Locale.ROOT, "%.1f", tps);
   }

   private static String getLastPacketAgo() {
      class_310 client = class_310.method_1551();
      if (client.method_1562() == null) {
         return "-";
      } else {
         int ping = 0;
         if (client.field_1724 != null && client.method_1562().method_2871(client.field_1724.method_5667()) != null) {
            ping = client.method_1562().method_2871(client.field_1724.method_5667()).method_2959();
         }

         if (ping <= 0) {
            long now = System.currentTimeMillis();
            long ts = lastReceivePacketTimeMs > 0L ? lastReceivePacketTimeMs : lastPacketTimeMs;
            if (ts <= 0L) {
               return "-";
            }

            ping = (int)Math.max(0L, now - ts);
         }

         if (ping < 50) {
            return "§a" + ping + "ms";
         } else if (ping < 100) {
            return "§e" + ping + "ms";
         } else {
            return ping < 200 ? "§c" + ping + "ms" : "§4" + ping + "ms";
         }
      }
   }

   private static String getOnline(class_310 client) {
      return client.method_1562() == null ? "-" : String.valueOf(client.method_1562().method_2880().size());
   }
}
