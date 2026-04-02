package org.jon.magicclient.client.commands;

import java.util.Collections;
import java.util.List;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.MessageHelper;
import org.jon.magicclient.client.config.ConfigManager;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.OptionUtil;

public class BackgroundCommand implements Command {
   private final MessageHelper msgHelper = new MessageHelper();
   private boolean enabled = false;

   public String getName() {
      return "background";
   }

   public GuiCategory getCategory() {
      return GuiCategory.HIDDEN;
   }

   public void onCommand(String[] args) {
      if (args.length < 2) {
         this.msgHelper.sendMessage("&cUsage: !background <on|off>", true);
      } else {
         String action = args[1].toLowerCase();
         if (action.equals("on")) {
            ConfigManager.setBackgroundEnabled(true);
            this.msgHelper.sendMessage("&aGUI background enabled.", true);
         } else if (action.equals("off")) {
            ConfigManager.setBackgroundEnabled(false);
            this.msgHelper.sendMessage("&cGUI background disabled.", true);
         } else {
            this.msgHelper.sendMessage("&cUsage: !background <on|off>", true);
         }

      }
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean bool) {
      this.enabled = bool;
   }

   public String getArgsUsage() {
      return "<on|off>";
   }

   public String getDescription() {
      return "Toggles the custom GUI background image";
   }

   public List<OptionUtil> getOptions() {
      return Collections.emptyList();
   }
}
