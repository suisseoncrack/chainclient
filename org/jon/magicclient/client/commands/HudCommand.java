package org.jon.magicclient.client.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.class_310;
import net.minecraft.class_437;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.MessageHelper;
import org.jon.magicclient.client.config.ConfigManager;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.gui.HudConfigScreen;
import org.jon.magicclient.client.utils.OptionUtil;

public class HudCommand implements Command {
   private final MessageHelper msgHelper = new MessageHelper();

   public String getName() {
      return "hud";
   }

   public GuiCategory getCategory() {
      return GuiCategory.HIDDEN;
   }

   public String getArgsUsage() {
      return "<toggle|show|hide|config> [all|ip|tps|fps|lastpacket|online]";
   }

   public String getDescription() {
      return "Control HUD visibility and settings";
   }

   public boolean getEnabled() {
      return true;
   }

   public void setEnabled(boolean bool) {
   }

   public void onCommand(String[] args) {
      if (args.length < 2) {
         this.msgHelper.sendMessage("§6[Hud] Usage: !hud <toggle|show|hide> [all|ip|tps|fps|lastpacket|online]", true);
      } else {
         String action = args[1].toLowerCase();
         byte var4 = -1;
         switch(action.hashCode()) {
         case -1354792126:
            if (action.equals("config")) {
               var4 = 3;
            }
            break;
         case -868304044:
            if (action.equals("toggle")) {
               var4 = 0;
            }
            break;
         case 3202370:
            if (action.equals("hide")) {
               var4 = 2;
            }
            break;
         case 3529469:
            if (action.equals("show")) {
               var4 = 1;
            }
         }

         int i;
         switch(var4) {
         case 0:
            if (args.length < 3) {
               this.toggleHud("all");
               break;
            } else {
               for(i = 2; i < args.length; ++i) {
                  this.toggleHud(args[i].toLowerCase());
               }

               return;
            }
         case 1:
            if (args.length < 3) {
               this.setHudVisibility("all", true);
               break;
            } else {
               for(i = 2; i < args.length; ++i) {
                  this.setHudVisibility(args[i].toLowerCase(), true);
               }

               return;
            }
         case 2:
            if (args.length < 3) {
               this.setHudVisibility("all", false);
               break;
            } else {
               for(i = 2; i < args.length; ++i) {
                  this.setHudVisibility(args[i].toLowerCase(), false);
               }

               return;
            }
         case 3:
            this.openHudConfig();
            break;
         default:
            this.msgHelper.sendMessage("§c[Hud] Invalid action! Use: toggle, show, hide, or config", true);
         }

      }
   }

   private void toggleHud(String target) {
      byte var3 = -1;
      switch(target.hashCode()) {
      case -1542973794:
         if (target.equals("lastpacket")) {
            var3 = 4;
         }
         break;
      case -1012222381:
         if (target.equals("online")) {
            var3 = 5;
         }
         break;
      case 3367:
         if (target.equals("ip")) {
            var3 = 1;
         }
         break;
      case 96673:
         if (target.equals("all")) {
            var3 = 0;
         }
         break;
      case 101609:
         if (target.equals("fps")) {
            var3 = 3;
         }
         break;
      case 115063:
         if (target.equals("tps")) {
            var3 = 2;
         }
      }

      switch(var3) {
      case 0:
         boolean newState = !ConfigManager.isHudEnabled();
         ConfigManager.setHudEnabled(newState);
         this.msgHelper.sendMessage("§6[Hud] HUD " + (newState ? "§aenabled" : "§cdisabled") + "!", true);
         break;
      case 1:
         this.toggleHudOption("IP", ConfigManager::isHudShowIp, ConfigManager::setHudShowIp);
         break;
      case 2:
         this.toggleHudOption("TPS", ConfigManager::isHudShowTps, ConfigManager::setHudShowTps);
         break;
      case 3:
         this.toggleHudOption("FPS", ConfigManager::isHudShowFps, ConfigManager::setHudShowFps);
         break;
      case 4:
         this.toggleHudOption("Last Packet", ConfigManager::isHudShowLastPacket, ConfigManager::setHudShowLastPacket);
         break;
      case 5:
         this.toggleHudOption("Online", ConfigManager::isHudShowOnline, ConfigManager::setHudShowOnline);
         break;
      default:
         this.msgHelper.sendMessage("§c[Hud] Invalid target! Use: all, ip, tps, fps, lastpacket, online", true);
      }

   }

   private void setHudVisibility(String target, boolean visible) {
      byte var4 = -1;
      switch(target.hashCode()) {
      case -1542973794:
         if (target.equals("lastpacket")) {
            var4 = 4;
         }
         break;
      case -1012222381:
         if (target.equals("online")) {
            var4 = 5;
         }
         break;
      case 3367:
         if (target.equals("ip")) {
            var4 = 1;
         }
         break;
      case 96673:
         if (target.equals("all")) {
            var4 = 0;
         }
         break;
      case 101609:
         if (target.equals("fps")) {
            var4 = 3;
         }
         break;
      case 115063:
         if (target.equals("tps")) {
            var4 = 2;
         }
      }

      switch(var4) {
      case 0:
         ConfigManager.setHudEnabled(visible);
         this.msgHelper.sendMessage("§6[Hud] HUD " + (visible ? "§ashown" : "§chidden") + "!", true);
         break;
      case 1:
         this.setHudOption("IP", visible, ConfigManager::setHudShowIp);
         break;
      case 2:
         this.setHudOption("TPS", visible, ConfigManager::setHudShowTps);
         break;
      case 3:
         this.setHudOption("FPS", visible, ConfigManager::setHudShowFps);
         break;
      case 4:
         this.setHudOption("Last Packet", visible, ConfigManager::setHudShowLastPacket);
         break;
      case 5:
         this.setHudOption("Online", visible, ConfigManager::setHudShowOnline);
         break;
      default:
         this.msgHelper.sendMessage("§c[Hud] Invalid target! Use: all, ip, tps, fps, lastpacket, online", true);
      }

   }

   private void toggleHudOption(String name, BooleanSupplier getter, Consumer<Boolean> setter) {
      boolean currentState = getter.getAsBoolean();
      setter.accept(!currentState);
      this.msgHelper.sendMessage("§6[Hud] " + name + " " + (!currentState ? "§aenabled" : "§cdisabled") + "!", true);
   }

   private void setHudOption(String name, boolean visible, Consumer<Boolean> setter) {
      setter.accept(visible);
      this.msgHelper.sendMessage("§6[Hud] " + name + " " + (visible ? "§ashown" : "§chidden") + "!", true);
   }

   private void openHudConfig() {
      try {
         class_310 client = class_310.method_1551();
         client.method_1507(new HudConfigScreen((class_437)null));
         this.msgHelper.sendMessage("§6[Hud] §aOpening HUD configuration...", true);
      } catch (Exception var2) {
         this.msgHelper.sendMessage("§c[Hud] Error opening HUD config: " + var2.getMessage(), true);
      }

   }

   public List<OptionUtil> getOptions() {
      return new ArrayList();
   }
}
