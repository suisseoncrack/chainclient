package org.jon.magicclient.mixin.client;

import net.minecraft.class_332;
import net.minecraft.class_500;
import org.jon.magicclient.client.gui.GuiBackgroundRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_500.class})
public class MultiplayerScreenMixin {
   @Inject(
      at = {@At("HEAD")},
      method = {"method_25394"}
   )
   public void onRender(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      GuiBackgroundRenderer.render(context);
   }

   @Inject(
      method = {"method_25426"},
      at = {@At("TAIL")}
   )
   private void onInit(CallbackInfo ci) {
   }
}
