package org.jon.magicclient.mixin.client;

import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;
import org.jon.magicclient.client.gui.GuiBackgroundRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_437.class})
public class GenericLoadingScreenMixin {
   @Inject(
      at = {@At("HEAD")},
      method = {"method_25394"}
   )
   public void onRender(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      String screenName = ((class_437)this).method_25440().getString();
      if (screenName.contains("Connecting") || screenName.contains("Loading") || screenName.contains("Joining") || screenName.contains("Terrain") || screenName.contains("World") || screenName.contains("Downloading")) {
         GuiBackgroundRenderer.render(context);
         class_437 screen = (class_437)this;
         int x = screen.field_22789 / 2;
         int y = 50;
         context.method_51448().method_22903();
         context.method_51448().method_46416((float)x, (float)y, 0.0F);
         context.method_51448().method_22905(2.5F, 2.5F, 2.5F);
         context.method_25300(class_310.method_1551().field_1772, "MagicClient", 0, 0, -16711681);
         context.method_51448().method_22909();
         context.method_25300(class_310.method_1551().field_1772, "on top <3", x, y + 30, -256);
      }

   }
}
