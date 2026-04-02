package org.jon.magicclient.client.render;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_310;
import net.minecraft.class_315;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.OptionType;
import org.jon.magicclient.client.utils.OptionUtil;

public class FOV implements Command {
   private boolean enabled = false;
   private final class_310 mc = class_310.method_1551();
   private final List<OptionUtil> options = new ArrayList();

   public FOV() {
      this.options.add(new OptionUtil("FOVModifier", OptionType.INTEGER));
      this.options.add(new OptionUtil("ItemFOV", OptionType.BOOLEAN));
      this.options.add(new OptionUtil("ItemFOVModifier", OptionType.INTEGER));
      ((OptionUtil)this.options.get(0)).setValue(120);
      ((OptionUtil)this.options.get(1)).setValue(false);
      ((OptionUtil)this.options.get(2)).setValue(120);
   }

   public String getName() {
      return "FOV";
   }

   public String getArgsUsage() {
      return "";
   }

   public String getDescription() {
      return "Adjust field of view settings";
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean bool) {
      this.enabled = bool;
   }

   public void onCommand(String[] args) {
      this.enabled = !this.enabled;
   }

   public List<OptionUtil> getOptions() {
      return this.options;
   }

   public GuiCategory getCategory() {
      return GuiCategory.RENDER;
   }

   public float getModifiedFOV(float originalFOV) {
      if (!this.enabled) {
         return originalFOV;
      } else {
         int fovModifier = (Integer)((OptionUtil)this.options.get(0)).getValue();
         boolean itemFOV = (Boolean)((OptionUtil)this.options.get(1)).getValue();
         int itemFOVModifier = (Integer)((OptionUtil)this.options.get(2)).getValue();
         float modifiedFOV = originalFOV + (float)(fovModifier - 70);
         if (itemFOV && this.mc.field_1724 != null && !this.mc.field_1724.method_6047().method_7960()) {
            modifiedFOV = (float)itemFOVModifier;
         }

         return modifiedFOV;
      }
   }

   public void updateFOV() {
      if (this.mc.field_1690 != null && this.enabled) {
         class_315 options = this.mc.field_1690;
         float currentFOV = (float)(Integer)options.method_41808().method_41753();
         float newFOV = this.getModifiedFOV(currentFOV);
         options.method_41808().method_41748((int)newFOV);
      }

   }
}
