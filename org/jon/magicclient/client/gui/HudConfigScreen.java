package org.jon.magicclient.client.gui;

import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_4185;
import net.minecraft.class_4286;
import net.minecraft.class_437;
import org.jon.magicclient.client.config.ConfigManager;

public class HudConfigScreen extends class_437 {
   private static final int PANEL_WIDTH = 360;
   private static final int PANEL_HEIGHT = 240;
   private final class_437 parent;
   private int x;
   private int y;
   private class_4286 hudEnabled;
   private class_4286 showIp;
   private class_4286 showTps;
   private class_4286 showFps;
   private class_4286 showLastPacket;
   private class_4286 showOnline;
   private boolean dragging = false;
   private int dragX;
   private int dragY;

   public HudConfigScreen(class_437 parent) {
      super(class_2561.method_43470("ClickHUD"));
      this.parent = parent;
   }

   protected void method_25426() {
      this.x = (this.field_22789 - 360) / 2;
      this.y = (this.field_22790 - 240) / 2;
      int cy = this.y + 30;
      this.hudEnabled = (class_4286)this.method_37063(new class_4286(this.x + 14, cy, 200, 20, class_2561.method_43470("HUD Enabled"), ConfigManager.isHudEnabled()));
      cy += 22;
      this.showIp = (class_4286)this.method_37063(new class_4286(this.x + 14, cy, 200, 20, class_2561.method_43470("Show IP"), ConfigManager.isHudShowIp()));
      cy += 22;
      this.showTps = (class_4286)this.method_37063(new class_4286(this.x + 14, cy, 200, 20, class_2561.method_43470("Show TPS"), ConfigManager.isHudShowTps()));
      cy += 22;
      this.showFps = (class_4286)this.method_37063(new class_4286(this.x + 14, cy, 200, 20, class_2561.method_43470("Show FPS"), ConfigManager.isHudShowFps()));
      cy += 22;
      this.showLastPacket = (class_4286)this.method_37063(new class_4286(this.x + 14, cy, 200, 20, class_2561.method_43470("Show Last Packet"), ConfigManager.isHudShowLastPacket()));
      cy += 22;
      this.showOnline = (class_4286)this.method_37063(new class_4286(this.x + 14, cy, 200, 20, class_2561.method_43470("Show Online"), ConfigManager.isHudShowOnline()));
      this.method_37063(class_4185.method_46430(class_2561.method_43470("Save"), (b) -> {
         this.save();
      }).method_46434(this.x + 14, this.y + 240 - 28, 60, 20).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_43470("Back"), (b) -> {
         this.method_25419();
      }).method_46434(this.x + 360 - 74, this.y + 240 - 28, 60, 20).method_46431());
   }

   private void save() {
      ConfigManager.setHudEnabled(this.hudEnabled.method_20372());
      ConfigManager.setHudShowIp(this.showIp.method_20372());
      ConfigManager.setHudShowTps(this.showTps.method_20372());
      ConfigManager.setHudShowFps(this.showFps.method_20372());
      ConfigManager.setHudShowLastPacket(this.showLastPacket.method_20372());
      ConfigManager.setHudShowOnline(this.showOnline.method_20372());
      ConfigManager.saveConfig();
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      GuiBackgroundRenderer.render(context);
      context.method_25294(0, 0, this.field_22789, this.field_22790, -1442840576);
      context.method_25294(this.x, this.y, this.x + 360, this.y + 240, -15658216);
      context.method_27535(this.field_22793, class_2561.method_43470("ClickHUD"), this.x + 14, this.y + 12, 16777215);
      int hX = ConfigManager.getHudX();
      int hY = ConfigManager.getHudY();
      String previewText = "HUD Anchor (Drag me)";
      int tw = this.field_22793.method_1727(previewText);
      context.method_25294(hX - 2, hY - 2, hX + tw + 2, hY + 12, -1996553985);
      context.method_27535(this.field_22793, class_2561.method_43470(previewText), hX, hY, 16777215);
      if (this.dragging) {
         ConfigManager.setHudX(mouseX - this.dragX);
         ConfigManager.setHudY(mouseY - this.dragY);
      }

      super.method_25394(context, mouseX, mouseY, delta);
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      int hX = ConfigManager.getHudX();
      int hY = ConfigManager.getHudY();
      int tw = this.field_22793.method_1727("HUD Anchor (Drag me)");
      if (mouseX >= (double)(hX - 2) && mouseX <= (double)(hX + tw + 2) && mouseY >= (double)(hY - 2) && mouseY <= (double)(hY + 12)) {
         this.dragging = true;
         this.dragX = (int)mouseX - hX;
         this.dragY = (int)mouseY - hY;
         return true;
      } else {
         return super.method_25402(mouseX, mouseY, button);
      }
   }

   public boolean method_25406(double mouseX, double mouseY, int button) {
      if (this.dragging) {
         this.dragging = false;
         ConfigManager.saveConfig();
         return true;
      } else {
         return super.method_25406(mouseX, mouseY, button);
      }
   }

   public void method_25419() {
      this.field_22787.method_1507(this.parent);
   }

   public boolean method_25421() {
      return false;
   }
}
