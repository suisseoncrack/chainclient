package org.jon.magicclient.mixin.client;

import net.minecraft.class_2596;
import net.minecraft.class_2761;
import net.minecraft.class_310;
import net.minecraft.class_634;
import org.jon.magicclient.client.CommandManager;
import org.jon.magicclient.client.gui.modern.ModernClickGuiScreen;
import org.jon.magicclient.client.hud.ClickHudOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_634.class})
public class ClientPlayNetworkHandlerMixin {
   @Inject(
      method = {"method_2883"},
      at = {@At("HEAD")}
   )
   private void onSendPacket(class_2596<?> packet, CallbackInfo ci) {
      ClickHudOverlay.markPacket();
   }

   @Inject(
      method = {"method_11079"},
      at = {@At("HEAD")}
   )
   private void onWorldTimeUpdate(class_2761 packet, CallbackInfo ci) {
      ClickHudOverlay.onTimeUpdate();
      ClickHudOverlay.markReceivePacket();
   }

   @Inject(
      method = {"method_11120"},
      at = {@At("RETURN")}
   )
   private void onGameJoin(CallbackInfo ci) {
      ClickHudOverlay.resetLagStatus();
   }

   @Inject(
      method = {"method_45729"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void magicclient$interceptChatMessage(String content, CallbackInfo ci) {
      if (content != null) {
         class_310 client = class_310.method_1551();
         if (content.startsWith("!gui")) {
            client.execute(() -> {
               client.method_1507(new ModernClickGuiScreen());
            });
            ci.cancel();
         } else if (content.startsWith("!oldgui")) {
            client.execute(() -> {
               client.method_1507(new ModernClickGuiScreen());
            });
            ci.cancel();
         } else {
            if (content.startsWith("!")) {
               CommandManager.getManager().handleCommand(content);
               ci.cancel();
            }

         }
      }
   }

   @Inject(
      method = {"method_45730"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void magicclient$interceptChatCommand(String command, CallbackInfo ci) {
      if (command != null) {
         if (command.startsWith("!")) {
            CommandManager.getManager().handleCommand(command);
            ci.cancel();
         }

      }
   }
}
