package org.jon.magicclient.mixin.client;

import net.minecraft.class_2535;
import net.minecraft.class_2547;
import net.minecraft.class_2596;
import org.jon.magicclient.client.hud.ClickHudOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_2535.class})
public class ClientConnectionMixin {
   @Inject(
      method = {"method_10759"},
      at = {@At("HEAD")}
   )
   private static void magicclient$markLastPacket(class_2596<?> packet, class_2547 listener, CallbackInfo ci) {
      ClickHudOverlay.markPacket();
   }
}
