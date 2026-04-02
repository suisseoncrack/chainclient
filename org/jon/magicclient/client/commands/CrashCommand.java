package org.jon.magicclient.client.commands;

import java.util.Collections;
import java.util.List;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.MessageHelper;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.OptionUtil;

public class CrashCommand implements Command {
   private static final String COMMAND_NAME = "crash";
   private final MessageHelper msgHelper = new MessageHelper();
   private boolean enabled;

   public String getName() {
      return "crash";
   }

   public GuiCategory getCategory() {
      return GuiCategory.HIDDEN;
   }

   public void onCommand(String[] args) {
      if (args.length < 2) {
         this.msgHelper.sendMessage("&cUsage: !crash <crashname>", true);
         this.msgHelper.sendMessage("&7Available crashes: magic1, magic2, storm1, grim, universe", true);
      } else {
         String crashName = args[1].toLowerCase();
         this.msgHelper.sendMessage("&7Please use the direct commands: &f!magic1, !magic2, !storm1, !grim, !universe", true);
      }
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean bool) {
      this.enabled = bool;
   }

   public String getArgsUsage() {
      return "<crashname> [args]";
   }

   public String getDescription() {
      return "Execute crash methods by name";
   }

   public List<OptionUtil> getOptions() {
      return Collections.emptyList();
   }
}
