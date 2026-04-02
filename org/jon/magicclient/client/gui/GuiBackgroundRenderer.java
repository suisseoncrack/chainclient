package org.jon.magicclient.client.gui;

import net.minecraft.class_310;
import net.minecraft.class_332;
import org.jon.magicclient.client.config.ConfigManager;

public final class GuiBackgroundRenderer {
   private GuiBackgroundRenderer() {
   }

   public static void render(class_332 context) {
      if (ConfigManager.isBackgroundEnabled()) {
         class_310 client = class_310.method_1551();
         if (client != null && client.method_22683() != null) {
            int width = context.method_51421();
            int height = context.method_51443();
            context.method_51448().method_22903();
            context.method_51448().method_46416(0.0F, 0.0F, -5000.0F);

            for(int y = 0; y < height; ++y) {
               float progress = (float)y / (float)height;
               int color = interpolateColor(-15066578, -15790321, progress);
               context.method_25294(0, y, width, y + 1, color);
            }

            drawDecorativeElements(context, width, height);
            context.method_51448().method_22909();
         }
      }
   }

   private static int interpolateColor(int color1, int color2, float factor) {
      int r1 = color1 >> 16 & 255;
      int g1 = color1 >> 8 & 255;
      int b1 = color1 & 255;
      int r2 = color2 >> 16 & 255;
      int g2 = color2 >> 8 & 255;
      int b2 = color2 & 255;
      int r = (int)((float)r1 + (float)(r2 - r1) * factor);
      int g = (int)((float)g1 + (float)(g2 - g1) * factor);
      int b = (int)((float)b1 + (float)(b2 - b1) * factor);
      return -16777216 | r << 16 | g << 8 | b;
   }

   private static void drawDecorativeElements(class_332 context, int width, int height) {
      for(int i = 0; i < 5; ++i) {
         int y = height / 6 * (i + 1);
         context.method_25294(0, y, width, y + 1, 1090519039);
      }

   }
}
