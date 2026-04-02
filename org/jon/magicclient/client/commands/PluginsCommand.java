package org.jon.magicclient.client.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.MessageHelper;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.OptionUtil;

public class PluginsCommand implements Command {
   private final MessageHelper msgHelper = new MessageHelper();
   private final Set<String> detectedPlugins = new HashSet();

   public String getName() {
      return "plugins";
   }

   public GuiCategory getCategory() {
      return GuiCategory.HIDDEN;
   }

   public String getArgsUsage() {
      return "<list|clear|scan>";
   }

   public String getDescription() {
      return "Detect and list server plugins by checking for namespaced commands";
   }

   public boolean getEnabled() {
      return true;
   }

   public void setEnabled(boolean bool) {
   }

   public void onCommand(String[] args) {
      if (args.length < 2) {
         this.msgHelper.sendMessage("§6[Plugins] Usage: !plugins <list|clear|scan>", true);
      } else {
         String action = args[1].toLowerCase();
         byte var4 = -1;
         switch(action.hashCode()) {
         case 3322014:
            if (action.equals("list")) {
               var4 = 0;
            }
            break;
         case 3524221:
            if (action.equals("scan")) {
               var4 = 2;
            }
            break;
         case 94746189:
            if (action.equals("clear")) {
               var4 = 1;
            }
         }

         switch(var4) {
         case 0:
            this.listPlugins();
            break;
         case 1:
            this.clearPlugins();
            break;
         case 2:
            this.scanPlugins();
            break;
         default:
            this.msgHelper.sendMessage("§c[Plugins] Invalid action! Use: list, clear, or scan", true);
         }

      }
   }

   private void listPlugins() {
      if (this.detectedPlugins.isEmpty()) {
         this.msgHelper.sendMessage("§c[Plugins] No plugins detected yet! Use !plugins scan to scan for plugins.", true);
      } else {
         this.msgHelper.sendMessage("§6[Plugins] Detected Plugins (" + this.detectedPlugins.size() + "):", true);
         Iterator var1 = this.detectedPlugins.iterator();

         while(var1.hasNext()) {
            String plugin = (String)var1.next();
            this.msgHelper.sendMessage("§a- " + plugin, true);
         }

      }
   }

   private void clearPlugins() {
      int count = this.detectedPlugins.size();
      this.detectedPlugins.clear();
      this.msgHelper.sendMessage("§6[Plugins] Cleared " + count + " detected plugin(s).", true);
   }

   private void scanPlugins() {
      this.msgHelper.sendMessage("§6[Plugins] Scanning for plugins... (Send commands like /help to detect namespaced commands)", true);
      this.msgHelper.sendMessage("§6[Plugins] Use !plugins list after scanning to see results.", true);
   }

   public void addPlugin(String pluginName) {
      if (pluginName != null && !pluginName.isEmpty() && pluginName.contains(":")) {
         String plugin = pluginName.split(":")[0];
         if (!plugin.equalsIgnoreCase("minecraft") && !plugin.equalsIgnoreCase("bukkit") && this.detectedPlugins.add(plugin)) {
            this.msgHelper.sendMessage("§a[Plugins] Detected plugin: " + plugin, true);
         }
      }

   }

   public List<OptionUtil> getOptions() {
      return new ArrayList();
   }
}
