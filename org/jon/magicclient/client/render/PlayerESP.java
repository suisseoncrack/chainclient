package org.jon.magicclient.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_1657;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_310;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.OptionType;
import org.jon.magicclient.client.utils.OptionUtil;

public class PlayerESP implements Command {
   private boolean enabled = false;
   private final class_310 mc = class_310.method_1551();
   private final List<OptionUtil> options = new ArrayList();

   public PlayerESP() {
      this.options.add(new OptionUtil("PlayersColor", OptionType.STRING));
      this.options.add(new OptionUtil("FriendsColor", OptionType.STRING));
      this.options.add(new OptionUtil("Outline", OptionType.BOOLEAN));
      ((OptionUtil)this.options.get(0)).setValue("#FF9200");
      ((OptionUtil)this.options.get(1)).setValue("#30FF00");
      ((OptionUtil)this.options.get(2)).setValue(true);
   }

   public String getName() {
      return "PlayerESP";
   }

   public String getArgsUsage() {
      return "";
   }

   public String getDescription() {
      return "Highlights players through walls";
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

   public void render(class_4587 matrices, class_4184 camera) {
      if (this.enabled && this.mc.field_1687 != null) {
         class_243 cameraPos = camera.method_19326();
         class_289 tessellator = class_289.method_1348();
         class_287 bufferBuilder = tessellator.method_1349();
         RenderSystem.setShader(class_757::method_34540);
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.disableDepthTest();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         Color playersColor = this.parseColor(((OptionUtil)this.options.get(0)).getValue().toString());
         Color friendsColor = this.parseColor(((OptionUtil)this.options.get(1)).getValue().toString());
         boolean outline = (Boolean)((OptionUtil)this.options.get(2)).getValue();
         Iterator var9 = this.mc.field_1687.method_18456().iterator();

         while(var9.hasNext()) {
            class_1657 player = (class_1657)var9.next();
            if (player != this.mc.field_1724 && player.method_5805()) {
               double x = player.field_6014 + (player.method_23317() - player.field_6014) * (double)this.mc.method_1488() - cameraPos.field_1352;
               double y = player.field_6036 + (player.method_23318() - player.field_6036) * (double)this.mc.method_1488() - cameraPos.field_1351;
               double z = player.field_5969 + (player.method_23321() - player.field_5969) * (double)this.mc.method_1488() - cameraPos.field_1350;
               class_238 box = player.method_5829().method_989(-player.method_23317(), -player.method_23318(), -player.method_23321()).method_989(x, y, z);
               box = box.method_1009(0.2D, 0.1D, 0.2D);
               float r = (float)playersColor.getRed() / 255.0F;
               float g = (float)playersColor.getGreen() / 255.0F;
               float b = (float)playersColor.getBlue() / 255.0F;
               this.renderBox(matrices, tessellator, bufferBuilder, box, r, g, b, 0.8F);
            }
         }

         RenderSystem.enableDepthTest();
         RenderSystem.disableBlend();
      }
   }

   private Color parseColor(String colorString) {
      try {
         return colorString.startsWith("#") ? Color.decode(colorString) : Color.WHITE;
      } catch (Exception var3) {
         return Color.WHITE;
      }
   }

   private void renderBox(class_4587 matrices, class_289 tessellator, class_287 buffer, class_238 box, float r, float g, float b, float a) {
      Matrix4f matrix = matrices.method_23760().method_23761();
      buffer.method_1328(class_5596.field_29344, class_290.field_1576);
      buffer.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a).method_1344();
      buffer.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a).method_1344();
      tessellator.method_1350();
   }
}
