package org.jon.magicclient.client.commands;

import java.util.List;
import java.util.Locale;
import net.minecraft.class_1934;
import net.minecraft.class_310;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.MessageHelper;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.OptionUtil;

public class Fakegm implements Command {
   private static final String COMMAND_NAME = "fakegm";
   private final MessageHelper msgHelper = new MessageHelper();
   private final class_310 mc = class_310.method_1551();
   private boolean enabled = false;

   public String getName() {
      return "fakegm";
   }

   public GuiCategory getCategory() {
      return GuiCategory.HIDDEN;
   }

   public void onCommand(String[] args) {
      if (args.length == 2 && this.mc.field_1761 != null) {
         String var2 = args[1].toLowerCase(Locale.ROOT);
         byte var3 = -1;
         switch(var2.hashCode()) {
         case -1684593425:
            if (var2.equals("spectator")) {
               var3 = 6;
            }
            break;
         case -1600582850:
            if (var2.equals("survival")) {
               var3 = 9;
            }
            break;
         case -694094064:
            if (var2.equals("adventure")) {
               var3 = 3;
            }
            break;
         case 48:
            if (var2.equals("0")) {
               var3 = 10;
            }
            break;
         case 49:
            if (var2.equals("1")) {
               var3 = 1;
            }
            break;
         case 50:
            if (var2.equals("2")) {
               var3 = 4;
            }
            break;
         case 51:
            if (var2.equals("3")) {
               var3 = 7;
            }
            break;
         case 97:
            if (var2.equals("a")) {
               var3 = 5;
            }
            break;
         case 99:
            if (var2.equals("c")) {
               var3 = 2;
            }
            break;
         case 115:
            if (var2.equals("s")) {
               var3 = 11;
            }
            break;
         case 3677:
            if (var2.equals("sp")) {
               var3 = 8;
            }
            break;
         case 1820422063:
            if (var2.equals("creative")) {
               var3 = 0;
            }
         }

         switch(var3) {
         case 0:
         case 1:
         case 2:
            this.mc.field_1761.method_2907(class_1934.field_9220);
            break;
         case 3:
         case 4:
         case 5:
            this.mc.field_1761.method_2907(class_1934.field_9216);
            break;
         case 6:
         case 7:
         case 8:
            this.mc.field_1761.method_2907(class_1934.field_9219);
            break;
         case 9:
         case 10:
         case 11:
         default:
            this.mc.field_1761.method_2907(class_1934.field_9215);
         }

         this.msgHelper.sendMessage("Your GameMode was changed to: &f" + this.mc.field_1761.method_2920().method_8381(), true);
      } else {
         this.msgHelper.sendMessage("Usage&8: &f!fakegm <mode>", true);
      }

   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean bool) {
      this.enabled = bool;
   }

   public String getArgsUsage() {
      return "<mode>";
   }

   public String getDescription() {
      return "Fakes your gamemode client-side";
   }

   public List<OptionUtil> getOptions() {
      return List.of();
   }
}
