package org.jon.magicclient.client.commands;

import java.util.Collections;
import java.util.List;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.MessageHelper;
import org.jon.magicclient.client.config.ConfigManager;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.OptionUtil;

public class ConfigCommand implements Command {
   private final MessageHelper msgHelper = new MessageHelper();
   private boolean enabled = false;

   public String getName() {
      return "config";
   }

   public GuiCategory getCategory() {
      return GuiCategory.HIDDEN;
   }

   public String getDescription() {
      return "Save and load configuration";
   }

   public String getArgsUsage() {
      return "<save|load>";
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean bool) {
      this.enabled = bool;
   }

   public List<OptionUtil> getOptions() {
      return Collections.emptyList();
   }

   public void onCommand(String[] args) {
      if (args.length < 2) {
         this.msgHelper.sendMessage("§7Usage: !config <save|load|list|delete> [name]", true);
      } else {
         String action = args[1].toLowerCase();
         String name = args.length > 2 ? args[2] : "default";
         byte var5 = -1;
         switch(action.hashCode()) {
         case -1335458389:
            if (action.equals("delete")) {
               var5 = 3;
            }
            break;
         case 3322014:
            if (action.equals("list")) {
               var5 = 2;
            }
            break;
         case 3327206:
            if (action.equals("load")) {
               var5 = 1;
            }
            break;
         case 3522941:
            if (action.equals("save")) {
               var5 = 0;
            }
         }

         switch(var5) {
         case 0:
            ConfigManager.saveConfig(name);
            this.msgHelper.sendMessage("§a[Config] Configuration '" + name + "' saved!", true);
            break;
         case 1:
            if (ConfigManager.loadConfig(name)) {
               this.msgHelper.sendMessage("§a[Config] Configuration '" + name + "' loaded!", true);
            } else {
               this.msgHelper.sendMessage("§c[Config] Configuration '" + name + "' not found!", true);
            }
            break;
         case 2:
            String[] configs = ConfigManager.listConfigs();
            if (configs.length == 0) {
               this.msgHelper.sendMessage("§7[Config] No configurations found.", true);
               break;
            } else {
               this.msgHelper.sendMessage("§6[Config] Available configurations:", true);
               String[] var7 = configs;
               int var8 = configs.length;

               for(int var9 = 0; var9 < var8; ++var9) {
                  String cfg = var7[var9];
                  this.msgHelper.sendMessage("§7 - " + cfg, true);
               }

               return;
            }
         case 3:
            if (ConfigManager.deleteConfig(name)) {
               this.msgHelper.sendMessage("§a[Config] Configuration '" + name + "' deleted!", true);
            } else {
               this.msgHelper.sendMessage("§c[Config] Could not delete '" + name + "' (maybe it's default or doesn't exist).", true);
            }
            break;
         default:
            this.msgHelper.sendMessage("§c[Config] Unknown action: " + action, true);
            this.msgHelper.sendMessage("§7Usage: !config <save|load|list|delete> [name]", true);
         }

      }
   }
}
