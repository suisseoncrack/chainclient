package org.jon.magicclient.mixin.client;

import java.lang.reflect.Method;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_442;
import org.jon.magicclient.client.gui.AltManagerScreen;
import org.jon.magicclient.client.gui.GuiBackgroundRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_442.class})
public class TitleScreenMixin {
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
      class_442 screen = (class_442)this;
      class_4185 altManagerButton = class_4185.method_46430(class_2561.method_43470("Alt Manager"), (button) -> {
         class_310.method_1551().method_1507(new AltManagerScreen(screen));
      }).method_46434(5, 5, 100, 20).method_46431();

      try {
         Method addDrawableChild = null;
         Class<?> screenClass = class_437.class;
         String[] methodNames = new String[]{"addDrawableChild", "method_37063", "func_230480_a_"};
         String[] var7 = methodNames;
         int var8 = methodNames.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            String name = var7[var9];

            try {
               Method[] var11 = screenClass.getDeclaredMethods();
               int var12 = var11.length;

               for(int var13 = 0; var13 < var12; ++var13) {
                  Method m = var11[var13];
                  if (m.getName().equals(name)) {
                     addDrawableChild = m;
                     break;
                  }
               }

               if (addDrawableChild != null) {
                  break;
               }
            } catch (Exception var15) {
            }
         }

         if (addDrawableChild != null) {
            addDrawableChild.setAccessible(true);
            addDrawableChild.invoke(screen, altManagerButton);
         }
      } catch (Exception var16) {
         var16.printStackTrace();
      }

   }
}
