package org.jon.magicclient.client.commands;

import java.util.Collections;
import java.util.List;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.MessageHelper;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.OptionUtil;

public class AutorCommand implements Command {
   private static final String COMMAND_NAME = "autor";
   private final MessageHelper msgHelper = new MessageHelper();
   private boolean enabled;

   public String getName() {
      return "autor";
   }

   public GuiCategory getCategory() {
      return GuiCategory.HIDDEN;
   }

   public void onCommand(String[] this) {
      System.out.println("AutorCommand executed!");
      this.msgHelper.sendSeparateLine();
      this.msgHelper.sendMessage("Author of the client: &fBenSuisse", true);
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
      return "Shows authors";
   }

   public List<OptionUtil> getOptions() {
      return Collections.emptyList();
   }
}
