package org.jon.magicclient.client.gui.modern;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;
import org.jon.magicclient.client.gui.GuiBackgroundRenderer;
import org.jon.magicclient.client.gui.GuiCategory;

public class ModernClickGuiScreen extends class_437 {
   private final List<ModernWindow> windows = new ArrayList();

   public ModernClickGuiScreen() {
      super(class_2561.method_43470("ChainClient Modern GUI"));
      class_310 client = class_310.method_1551();
      int screenWidth = client.method_22683().method_4486();
      int screenHeight = client.method_22683().method_4502();
      int windowWidth = 100;
      int windowSpacing = 10;
      int totalWidth = GuiCategory.values().length * (windowWidth + windowSpacing);
      int startX;
      if (totalWidth > screenWidth - 40) {
         windowWidth = (screenWidth - 40 - GuiCategory.values().length * windowSpacing) / GuiCategory.values().length;
         startX = 20;
      } else {
         startX = (screenWidth - totalWidth) / 2;
      }

      int yOffset = 40;
      GuiCategory[] v9 = GuiCategory.values();
      int i10 = v9.length;

      for(int i11 = 0; i11 < i10; ++i11) {
         GuiCategory category = v9[i11];
         if (category != GuiCategory.HIDDEN) {
            this.windows.add(new ModernWindow(category, startX, yOffset));
            startX += windowWidth + windowSpacing;
         }
      }

   }

   public void method_25394(class_332 this, int context, int mouseX, float mouseY) {
      GuiBackgroundRenderer.render(context);
      Iterator v5 = this.windows.iterator();

      while(v5.hasNext()) {
         ModernWindow window = (ModernWindow)v5.next();
         window.render(context, mouseX, mouseY);
      }

      super.method_25394(context, mouseX, mouseY, delta);
   }

   public boolean method_25402(double this, double mouseX, int mouseY) {
      for(int i = this.windows.size() - 1; i >= 0; --i) {
         if (((ModernWindow)this.windows.get(i)).mouseClicked(mouseX, mouseY, button)) {
            ModernWindow clicked = (ModernWindow)this.windows.remove(i);
            this.windows.add(clicked);
            return true;
         }
      }

      return super.method_25402(mouseX, mouseY, button);
   }

   public boolean method_25406(double this, double mouseX, int mouseY) {
      Iterator v6 = this.windows.iterator();

      while(v6.hasNext()) {
         ModernWindow window = (ModernWindow)v6.next();
         window.mouseReleased(mouseX, mouseY, button);
      }

      return super.method_25406(mouseX, mouseY, button);
   }

   public boolean method_25403(double this, double mouseX, int mouseY, double button, double deltaX) {
      Iterator v10 = this.windows.iterator();

      while(v10.hasNext()) {
         ModernWindow window = (ModernWindow)v10.next();
         window.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
      }

      return super.method_25403(mouseX, mouseY, button, deltaX, deltaY);
   }

   public boolean method_25401(double this, double mouseX, double mouseY) {
      Iterator v7 = this.windows.iterator();

      ModernWindow window;
      do {
         if (!v7.hasNext()) {
            return super.method_25401(mouseX, mouseY, amount);
         }

         window = (ModernWindow)v7.next();
      } while(!window.mouseScrolled(mouseX, mouseY, amount));

      return true;
   }

   public boolean method_25421() {
      return false;
   }
}
