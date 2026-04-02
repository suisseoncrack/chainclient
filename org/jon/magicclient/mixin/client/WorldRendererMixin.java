package org.jon.magicclient.mixin.client;

import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_761;
import net.minecraft.class_765;
import org.joml.Matrix4f;
import org.jon.magicclient.client.CommandManager;
import org.jon.magicclient.client.render.PlayerESP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_761.class})
public class WorldRendererMixin {
   @Inject(
      method = {"method_22710"},
      at = {@At("TAIL")}
   )
   private void onRender(class_4587 matrices, float tickDelta, long limitTime, boolean renderBlockOutline, class_4184 camera, class_757 gameRenderer, class_765 lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
      CommandManager.getManager().getCommands().stream().filter((cmd) -> {
         return cmd instanceof PlayerESP;
      }).map((cmd) -> {
         return (PlayerESP)cmd;
      }).forEach((esp) -> {
         if (esp.getEnabled()) {
            esp.render(matrices, camera);
         }

      });
   }
}
