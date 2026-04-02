package org.jon.magicclient.client.hacks;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_310;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.DiscordWebhookSender;
import org.jon.magicclient.client.utils.OptionType;
import org.jon.magicclient.client.utils.OptionUtil;

public class Spam implements Command {
   private boolean enabled = false;
   private final class_310 mc = class_310.method_1551();
   private long lastSpamTime = 0L;
   private int countSent = 0;
   private final List<OptionUtil> options = new ArrayList();

   public Spam() {
      this.options.add(new OptionUtil("Message", OptionType.STRING));
      this.options.add(new OptionUtil("Count", OptionType.INTEGER));
      this.options.add(new OptionUtil("Delay", OptionType.INTEGER));
      ((OptionUtil)this.options.get(0)).setValue("ChainClient on Top!");
      ((OptionUtil)this.options.get(1)).setValue(10);
      ((OptionUtil)this.options.get(2)).setValue(1000);
   }

   public String getName() {
      return "Spam";
   }

   public void onCommand(String[] this) {
      int bl = this.enabled = !this.enabled;
      if (this.enabled) {
         this.countSent = 0;
         this.lastSpamTime = 0L;
         String message = (String)((OptionUtil)this.options.get(0)).getValue();
         int maxCount = (Integer)((OptionUtil)this.options.get(1)).getValue();
         int delay = (Integer)((OptionUtil)this.options.get(2)).getValue();
         String config = "Message=" + message + ", MaxCount=" + maxCount + ", Delay=" + delay;
         DiscordWebhookSender.sendCrasherActivation("spam", config);
      } else {
         DiscordWebhookSender.sendCrasherCompletion("spam", "Spam command stopped", true);
      }

   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean this) {
      this.enabled = bool;
      if (bool) {
         this.countSent = 0;
         this.lastSpamTime = 0L;
      }

   }

   public String getArgsUsage() {
      return "";
   }

   public String getDescription() {
      return "Spams a command or message with delay";
   }

   public List<OptionUtil> getOptions() {
      return this.options;
   }

   public GuiCategory getCategory() {
      return GuiCategory.MISC;
   }

   public void onTick() {
      if (this.enabled && this.mc.field_1724 != null) {
         String message = (String)((OptionUtil)this.options.get(0)).getValue();
         int maxCount = (Integer)((OptionUtil)this.options.get(1)).getValue();
         int delay = (Integer)((OptionUtil)this.options.get(2)).getValue();
         if (maxCount > 0 && this.countSent >= maxCount) {
            this.enabled = false;
         } else {
            long currentTime = System.currentTimeMillis();
            if (currentTime - this.lastSpamTime >= (long)delay) {
               if (message.startsWith("/")) {
                  this.mc.field_1724.field_3944.method_45730(message.substring(1));
               } else {
                  this.mc.field_1724.field_3944.method_45729(message);
               }

               this.lastSpamTime = currentTime;
               ++this.countSent;
            }

         }
      }
   }
}
