package org.jon.magicclient.client.fightbot;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_2846;
import net.minecraft.class_2885;
import net.minecraft.class_310;
import net.minecraft.class_3965;
import net.minecraft.class_4969;
import net.minecraft.class_239.class_240;
import net.minecraft.class_2846.class_2847;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.DiscordWebhookSender;
import org.jon.magicclient.client.utils.OptionType;
import org.jon.magicclient.client.utils.OptionUtil;

public class AutoAnchor implements Command {
   private boolean enabled = false;
   private final class_310 mc = class_310.method_1551();
   private class_2338 lastAnchorPos = null;
   private boolean exploitCheck = true;
   private final List<OptionUtil> options = new ArrayList();

   public AutoAnchor() {
      this.options.add(new OptionUtil("ExploitCheck", OptionType.BOOLEAN));
      ((OptionUtil)this.options.get(0)).setValue(this.exploitCheck);
   }

   public String getName() {
      return "AutoAnchor";
   }

   public String getArgsUsage() {
      return "";
   }

   public String getDescription() {
      return "Automatically places anchors with glowstone and exploits";
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean bool) {
      this.enabled = bool;
   }

   public void onCommand(String[] args) {
      this.enabled = !this.enabled;
      if (this.enabled) {
         DiscordWebhookSender.sendCrasherActivation("autoanchor", "Automatic anchor placement with glowstone exploit");
      } else {
         DiscordWebhookSender.sendCrasherCompletion("autoanchor", "AutoAnchor disabled", true);
      }

   }

   public List<OptionUtil> getOptions() {
      return this.options;
   }

   public GuiCategory getCategory() {
      return GuiCategory.COMBAT;
   }

   public void onTick() {
      if (this.enabled && this.mc.field_1724 != null && this.mc.field_1687 != null) {
         this.exploitCheck = (Boolean)((OptionUtil)this.options.get(0)).getValue();
         class_1799 mainHand = this.mc.field_1724.method_6047();
         if (mainHand.method_7909() == class_1802.field_23141) {
            if (this.mc.field_1690.field_1904.method_1436()) {
               this.tryPlaceAnchor();
            }

            if (this.mc.field_1765 != null && this.mc.field_1765.method_17783() == class_240.field_1332) {
               class_3965 blockHit = (class_3965)this.mc.field_1765;
               class_2338 pos = blockHit.method_17777();
               if (this.mc.field_1687.method_8320(pos).method_26204() == class_2246.field_23152 && !pos.equals(this.lastAnchorPos)) {
                  this.lastAnchorPos = pos;
                  this.mc.execute(() -> {
                     this.handleAnchorPlacement(pos);
                  });
               }
            }

         }
      }
   }

   private void tryPlaceAnchor() {
      if (this.mc.field_1724 != null) {
         class_1799 mainHand = this.mc.field_1724.method_6047();
         if (mainHand.method_7909() == class_1802.field_23141) {
            if (this.mc.field_1765 != null && this.mc.field_1765.method_17783() == class_240.field_1332) {
               class_3965 hit = (class_3965)this.mc.field_1765;
               class_2338 pos = hit.method_17777().method_10093(hit.method_17780());
               if (this.mc.field_1687.method_8320(pos).method_26204() == class_2246.field_10124) {
                  this.mc.field_1724.field_3944.method_2883(new class_2885(class_1268.field_5808, hit, 0));
                  this.mc.field_1724.method_6104(class_1268.field_5808);
                  this.lastAnchorPos = pos;
                  this.mc.execute(() -> {
                     if (this.lastAnchorPos != null) {
                        this.handleAnchorPlacement(this.lastAnchorPos);
                     }

                  });
               }
            }

         }
      }
   }

   private void handleAnchorPlacement(class_2338 anchorPos) {
      int glowstoneSlot = this.findGlowstoneSlot();
      if (glowstoneSlot != -1) {
         int previousSlot = this.mc.field_1724.method_31548().field_7545;
         this.mc.field_1724.method_31548().field_7545 = glowstoneSlot;
         this.placeGlowstoneInAnchor(anchorPos);
         this.mc.field_1724.method_31548().field_7545 = previousSlot;
         if (this.exploitCheck) {
            this.performExploitCheck(anchorPos);
         }

      }
   }

   private int findGlowstoneSlot() {
      if (this.mc.field_1724 == null) {
         return -1;
      } else {
         for(int i = 0; i < 9; ++i) {
            class_1799 stack = this.mc.field_1724.method_31548().method_5438(i);
            if (stack.method_7909() == class_1802.field_8801 || stack.method_7909() == class_1802.field_8601) {
               return i;
            }
         }

         return -1;
      }
   }

   private void placeGlowstoneInAnchor(class_2338 anchorPos) {
      if (this.mc.field_1724 != null && this.mc.field_1761 != null) {
         try {
            int glowstoneSlot = this.findGlowstoneSlot();
            if (glowstoneSlot == -1) {
               return;
            }

            int previousSlot = this.mc.field_1724.method_31548().field_7545;
            this.mc.field_1724.method_31548().field_7545 = glowstoneSlot;
            class_243 anchorCenter = class_243.method_24953(anchorPos);
            class_3965 hitResult = new class_3965(anchorCenter, class_2350.field_11036, anchorPos, false);
            this.mc.field_1724.field_3944.method_2883(new class_2885(class_1268.field_5808, hitResult, 0));
            this.mc.field_1724.method_6104(class_1268.field_5808);
            this.mc.field_1724.method_31548().field_7545 = previousSlot;
         } catch (Exception var6) {
         }

      }
   }

   private void performExploitCheck(class_2338 anchorPos) {
      if (this.mc.field_1724 != null) {
         try {
            if (this.mc.field_1687 != null) {
               class_2680 state = this.mc.field_1687.method_8320(anchorPos);
               if (state.method_26204() == class_2246.field_23152) {
                  int charges = (Integer)state.method_11654(class_4969.field_23153);
                  if (charges > 0) {
                     this.sendExploitPackets(anchorPos);
                  }
               }
            }
         } catch (Exception var4) {
         }

      }
   }

   private void sendExploitPackets(class_2338 anchorPos) {
      try {
         this.mc.field_1724.field_3944.method_2883(new class_2846(class_2847.field_12968, anchorPos, class_2350.field_11036));
         this.mc.execute(() -> {
            this.mc.field_1724.field_3944.method_2883(new class_2846(class_2847.field_12973, anchorPos, class_2350.field_11036));
         });
      } catch (Exception var3) {
      }

   }

   public class_2338 getLastAnchorPos() {
      return this.lastAnchorPos;
   }

   public void setLastAnchorPos(class_2338 pos) {
      this.lastAnchorPos = pos;
   }
}
