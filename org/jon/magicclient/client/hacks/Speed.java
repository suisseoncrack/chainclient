package org.jon.magicclient.client.hacks;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_310;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.OptionType;
import org.jon.magicclient.client.utils.OptionUtil;

public class Speed implements Command {
   private boolean enabled = false;
   private final class_310 mc = class_310.method_1551();
   private final List<OptionUtil> options = new ArrayList();

   public Speed() {
      this.options.add(new OptionUtil("Speed", OptionType.INTEGER));
      ((OptionUtil)this.options.get(0)).setValue(1);
   }

   public String getName() {
      return "Speed";
   }

   public String getArgsUsage() {
      return "";
   }

   public String getDescription() {
      return "Increases movement speed";
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
      return GuiCategory.MOVEMENT;
   }

   public void onUpdate() {
      if (this.enabled && this.mc.field_1724 != null) {
         int speedLevel = (Integer)((OptionUtil)this.options.get(0)).getValue();
         float speedMultiplier = 1.0F + (float)speedLevel * 0.3F;
         if (this.mc.field_1724.field_6250 != 0.0F || this.mc.field_1724.field_6212 != 0.0F) {
            this.mc.field_1724.method_6125(speedMultiplier * 0.1F);
            float velX = (float)((double)(this.mc.field_1724.field_6250 * speedMultiplier) * 0.3D);
            float velZ = (float)((double)(this.mc.field_1724.field_6212 * speedMultiplier) * 0.3D);
            double yaw = Math.toRadians((double)this.mc.field_1724.method_36454());
            double sin = Math.sin(yaw);
            double cos = Math.cos(yaw);
            this.mc.field_1724.method_5762(-sin * (double)velX + cos * (double)velZ, 0.0D, cos * (double)velX + sin * (double)velZ);
            if (this.mc.field_1724.method_24828() && this.mc.field_1724.field_3913.field_3904) {
               this.mc.field_1724.method_5762(0.0D, 0.42D * (double)speedMultiplier, 0.0D);
            }
         }

      }
   }

   public void onTick() {
      this.onUpdate();
   }
}
