package org.jon.magicclient.mixin.client;

import net.minecraft.class_3675;
import org.jon.magicclient.client.KeybindManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_3675.class})
public class KeyInputMixin {
   @Inject(
      method = {"method_15987"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void onKeyPressed(int keyCode, CallbackInfoReturnable<Boolean> cir) {
      if (keyCode == 71 && KeybindManager.isKeybindEnabled()) {
         try {
            KeybindManager.onKeyInput(keyCode, 1);
            cir.setReturnValue(true);
            cir.cancel();
         } catch (Exception var3) {
            System.err.println("[KeyInputMixin] Error handling key input: " + var3.getMessage());
         }
      }

   }
}
