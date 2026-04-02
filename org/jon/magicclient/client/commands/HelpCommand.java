package org.jon.magicclient.client.commands;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.CommandManager;
import org.jon.magicclient.client.MessageHelper;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.OptionUtil;

public class HelpCommand implements Command {
   private static final String COMMAND_NAME = "help";
   private final MessageHelper msgHelper = new MessageHelper();
   private boolean enabled;

   public String getName() {
      return "help";
   }

   public GuiCategory getCategory() {
      return GuiCategory.HIDDEN;
   }

   public void onCommand(String[] this) {
      this.msgHelper.sendSeparateLine();
      this.msgHelper.sendMessage("&6&lChainClient Commands", true);
      this.msgHelper.sendSeparateLine();
      CommandManager manager = CommandManager.getManager();
      Iterator v3 = manager.getCommands().iterator();

      while(v3.hasNext()) {
         Command cmd = (Command)v3.next();
         String usage = cmd.getArgsUsage().isEmpty() ? "" : " " + cmd.getArgsUsage();
         String description = cmd.getDescription();
         String status = cmd.getEnabled() ? "&a" : "&c";
         this.msgHelper.sendMessage(status + "!" + cmd.getName() + usage + "&7 - " + description, true);
      }

      this.msgHelper.sendSeparateLine();
      this.msgHelper.sendMessage("&7Use !crash <method> to execute crash methods", true);
      this.msgHelper.sendMessage("&7Available crash methods: storm1, magic1, grim, universe", true);
      this.msgHelper.sendMessage("&7Use !fakegm 1,2,3 to go in fakegm", true);
      this.msgHelper.sendMessage("&7Use !hud <toggle|show|hide> [all|ip|tps|fps|lastpacket|online] to control HUD", true);
      this.msgHelper.sendSeparateLine();
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean this) {
      this.enabled = bool;
   }

   public String getArgsUsage() {
      return "";
   }

   public String getDescription() {
      return "Shows all available commands";
   }

   public List<OptionUtil> getOptions() {
      return Collections.emptyList();
   }
}
