package org.jon.magicclient.mixin.client;

import io.netty.buffer.Unpooled;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import net.minecraft.class_2540;
import net.minecraft.class_2596;
import net.minecraft.class_2817;
import net.minecraft.class_2960;
import net.minecraft.class_634;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.CommandManager;
import org.jon.magicclient.client.exploits.Brandspoof;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({class_634.class})
public class ClientPlayNetworkHandlerBrandMixin {
   private static final class_2960 BRAND_CHANNEL = class_2960.method_43902("minecraft", "brand");

   @ModifyVariable(
      method = {"method_2883"},
      at = @At("HEAD"),
      argsOnly = true
   )
   private class_2596<?> modifyBrandPacket(class_2596<?> packet) {
      if (packet instanceof class_2817) {
         class_2817 customPacket = (class_2817)packet;
         if (!customPacket.method_36169().equals(BRAND_CHANNEL)) {
            return packet;
         } else {
            Brandspoof brandspoof = this.getBrandspoof();
            if (brandspoof != null && brandspoof.getEnabled() && !brandspoof.getCustomBrand().isEmpty()) {
               try {
                  byte[] brandData = brandspoof.getCustomBrand().getBytes(StandardCharsets.UTF_8);
                  class_2540 buf = new class_2540(Unpooled.buffer());
                  buf.writeBytes(brandData);
                  return new class_2817(BRAND_CHANNEL, buf);
               } catch (Exception var6) {
                  return packet;
               }
            } else {
               return packet;
            }
         }
      } else {
         return packet;
      }
   }

   private Brandspoof getBrandspoof() {
      CommandManager manager = CommandManager.getManager();
      if (manager == null) {
         return null;
      } else {
         Iterator var2 = manager.getCommands().iterator();

         Command cmd;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            cmd = (Command)var2.next();
         } while(!(cmd instanceof Brandspoof));

         return (Brandspoof)cmd;
      }
   }
}
