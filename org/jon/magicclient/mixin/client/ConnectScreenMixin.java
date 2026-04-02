package org.jon.magicclient.mixin.client;

import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_412;
import org.jon.magicclient.client.gui.GuiBackgroundRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_412.class})
public class ConnectScreenMixin {
   @Inject(
      at = {@At("HEAD")},
      method = {"method_25394"}
   )
   public void onRenderHead(class_332 this, int context, int mouseX, float mouseY, CallbackInfo delta) {
      GuiBackgroundRenderer.render(context);
   }

   @Inject(
      at = {@At("TAIL")},
      method = {"method_25394"}
   )
   public void onRenderTail(class_332 this, int context, int mouseX, float mouseY, CallbackInfo delta) {
      class_412 screen = (class_412)this;
      int x = screen.field_22789 / 2;
      int y = 50;
      context.method_51448().method_22903();
      context.method_51448().method_46416((float)x, (float)y, 0.0F);
      context.method_51448().method_22905(2.5F, 2.5F, 2.5F);
      context.method_25300(class_310.method_1551().field_1772, "ChainClient", 0, 0, -16711681);
      context.method_51448().method_22909();
      context.method_25300(class_310.method_1551().field_1772, "on top <3", x, y + 30, -256);
   }

   @Inject(
      method = {"method_25426"},
      at = {@At("TAIL")}
   )
   private void onInit(CallbackInfo this) {
   }
}
