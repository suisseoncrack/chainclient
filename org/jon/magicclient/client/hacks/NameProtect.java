package org.jon.magicclient.client.hacks;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_310;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.CommandManager;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.OptionType;
import org.jon.magicclient.client.utils.OptionUtil;

public class NameProtect implements Command {
   private boolean enabled = false;
   private final List<OptionUtil> options = new ArrayList();

   public NameProtect() {
      this.options.add(new OptionUtil("Name", OptionType.STRING));
      ((OptionUtil)this.options.get(0)).setValue("ChainUser");
   }

   public String getName() {
      return "NameProtect";
   }

   public void onCommand(String[] this) {
      this.enabled = !this.enabled;
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
      return "Protects your name by masking it in chat and GUI";
   }

   public List<OptionUtil> getOptions() {
      return this.options;
   }

   public GuiCategory getCategory() {
      return GuiCategory.MISC;
   }

   public static String getCustomName() {
      CommandManager.getManager();
      NameProtect mod = (NameProtect)CommandManager.getCommand("NameProtect");
      return mod != null && mod.enabled ? ((String)((OptionUtil)mod.options.get(0)).getValue()).replace("&", "§") : class_310.method_1551().method_1548().method_1676();
   }

   public String getCustomNameInstance() {
      return !this.enabled ? class_310.method_1551().method_1548().method_1676() : ((String)((OptionUtil)this.options.get(0)).getValue()).replace("&", "§");
   }
}
