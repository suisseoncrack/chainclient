package org.jon.magicclient.client.hacks;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_310;
import net.minecraft.class_7172;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.CommandManager;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.OptionType;
import org.jon.magicclient.client.utils.OptionUtil;

public class FOV implements Command {
   private static float handDistance = 0.0F;
   private boolean enabled = false;
   private final class_310 mc = class_310.method_1551();
   private float originalFOV = 70.0F;
   private final List<OptionUtil> options = new ArrayList();

   public FOV() {
      this.options.add(new OptionUtil("FOV", OptionType.INTEGER));
      this.options.add(new OptionUtil("HandFOV", OptionType.BOOLEAN));
      this.options.add(new OptionUtil("HandFOVValue", OptionType.INTEGER));
      ((OptionUtil)this.options.get(0)).setValue(120);
      ((OptionUtil)this.options.get(1)).setValue(false);
      ((OptionUtil)this.options.get(2)).setValue(120);
      if (this.mc.field_1690 != null) {
         try {
            class_7172<Integer> fovOption = this.mc.field_1690.method_41808();
            if (fovOption != null) {
               this.originalFOV = ((Integer)fovOption.method_41753()).floatValue();
            }
         } catch (Exception var2) {
            System.err.println("Failed to get original FOV: " + var2.getMessage());
            this.originalFOV = 70.0F;
         }
      }

   }

   public String getName() {
      return "FOV";
   }

   public String getArgsUsage() {
      return "";
   }

   public String getDescription() {
      return "Custom field of view";
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean bool) {
      this.enabled = bool;
      if (!bool && this.mc.field_1690 != null) {
         try {
            class_7172<Integer> fovOption = this.mc.field_1690.method_41808();
            if (fovOption != null) {
               fovOption.method_41748((int)this.originalFOV);
            }
         } catch (Exception var3) {
            System.err.println("Failed to restore FOV: " + var3.getMessage());
         }
      }

   }

   public void onCommand(String[] args) {
      this.enabled = !this.enabled;
      if (this.enabled && this.mc.field_1690 != null) {
         try {
            class_7172<Integer> fovOption = this.mc.field_1690.method_41808();
            if (fovOption != null) {
               this.originalFOV = ((Integer)fovOption.method_41753()).floatValue();
            }
         } catch (Exception var3) {
            System.err.println("Failed to store original FOV: " + var3.getMessage());
         }
      }

   }

   public List<OptionUtil> getOptions() {
      return this.options;
   }

   public GuiCategory getCategory() {
      return GuiCategory.RENDER;
   }

   public void updateFOV() {
      if (this.enabled && this.mc.field_1690 != null) {
         int fovValue = (Integer)((OptionUtil)this.options.get(0)).getValue();
         boolean handFOV = (Boolean)((OptionUtil)this.options.get(1)).getValue();
         float handDistanceValue = ((Integer)((OptionUtil)this.options.get(2)).getValue()).floatValue();
         handDistance = handDistanceValue;
         float finalFOV;
         if (handFOV && this.mc.field_1724 != null && !this.mc.field_1724.method_6047().method_7960()) {
            finalFOV = (float)fovValue;
         } else {
            finalFOV = (float)fovValue;
         }

         try {
            finalFOV = Math.max(30.0F, Math.min(180.0F, finalFOV));
            class_7172<Integer> fovOption = this.mc.field_1690.method_41808();
            if (fovOption != null) {
               fovOption.method_41748((int)finalFOV);
            }

            if (this.mc.field_1690 != null) {
               class_7172<Integer> fovOption2 = this.mc.field_1690.method_41808();
               if (fovOption2 != null) {
                  fovOption2.method_41748((int)finalFOV);
               }
            }

            System.out.println("[FOV] Set FOV to: " + finalFOV + " (enabled: " + this.enabled + ")");
         } catch (Exception var7) {
            System.err.println("[FOV] Failed to set FOV: " + var7.getMessage());
            var7.printStackTrace();
         }

      }
   }

   public void onTick() {
      this.updateFOV();
   }

   public static float getHandDistance() {
      CommandManager.getManager();
      FOV mod = (FOV)CommandManager.getCommand("FOV");
      return mod != null && mod.enabled ? handDistance : 0.0F;
   }

   public float getHandDistanceValue() {
      return handDistance;
   }

   public void setHandDistanceValue(float value) {
      handDistance = Math.max(0.0F, Math.min(1.5F, value));
      ((OptionUtil)this.options.get(2)).setValue(handDistance);
   }
}
