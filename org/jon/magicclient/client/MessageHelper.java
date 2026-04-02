package org.jon.magicclient.client;

import net.minecraft.class_2561;
import net.minecraft.class_310;

public class MessageHelper {
   public void sendMessage(String message, boolean colored) {
      if (colored) {
         message = message.replace("&", "§");
      }

      if (class_310.method_1551().field_1724 != null) {
         class_310.method_1551().field_1724.method_7353(class_2561.method_43470(message), false);
      }

   }

   public void sendSeparateLine() {
      this.sendMessage("&7-----------------------------------------------------", true);
   }
}
