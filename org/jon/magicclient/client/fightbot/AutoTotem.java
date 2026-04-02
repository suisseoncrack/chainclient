package org.jon.magicclient.client.fightbot;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.class_1294;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_310;
import net.minecraft.class_490;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.DiscordWebhookSender;
import org.jon.magicclient.client.utils.OptionType;
import org.jon.magicclient.client.utils.OptionUtil;

public class AutoTotem implements Command {
   private boolean enabled = false;
   private final class_310 mc = class_310.method_1551();
   private boolean openInventory = true;
   private int refillTicks = 5;
   private int actionTimer = 0;
   private boolean wasTotemPopped = false;
   private boolean inventoryOpened = false;
   private int totemSlot = -1;
   private final Random random = new Random();
   private float lastHealth = 20.0F;
   private int totemPopTicks = 0;
   private boolean wasLowHealth = false;
   private int packetDelay = 0;
   private final List<OptionUtil> options;

   public AutoTotem() {
      this.options = Arrays.asList(new OptionUtil("OpenInventory", OptionType.BOOLEAN), new OptionUtil("RefillTicks", OptionType.INTEGER));
      ((OptionUtil)this.options.get(0)).setValue(this.openInventory);
      ((OptionUtil)this.options.get(1)).setValue(this.refillTicks);
   }

   public String getName() {
      return "AutoTotem";
   }

   public String getArgsUsage() {
      return "<on|off>";
   }

   public String getDescription() {
      return "Advanced AutoTotem with full anticheat bypass";
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean bool) {
      this.enabled = bool;
      if (bool) {
         this.updateFromOptions();
         this.actionTimer = 0;
         this.wasTotemPopped = false;
         this.inventoryOpened = false;
         this.totemSlot = -1;
         this.lastHealth = this.mc.field_1724 != null ? this.mc.field_1724.method_6032() : 20.0F;
         this.totemPopTicks = 0;
         this.wasLowHealth = false;
         this.packetDelay = 0;
         DiscordWebhookSender.sendCrasherActivation("autototem", "Advanced AutoTotem with full anticheat bypass");
      } else {
         if (this.inventoryOpened && this.mc.field_1755 instanceof class_490) {
            this.mc.field_1724.method_7346();
         }

         this.inventoryOpened = false;
         DiscordWebhookSender.sendCrasherCompletion("autototem", "AutoTotem disabled", true);
      }

   }

   public void onCommand(String[] args) {
      if (args.length >= 2) {
         String action = args[1].toLowerCase();
         byte var4 = -1;
         switch(action.hashCode()) {
         case -1298848381:
            if (action.equals("enable")) {
               var4 = 1;
            }
            break;
         case 3551:
            if (action.equals("on")) {
               var4 = 0;
            }
            break;
         case 109935:
            if (action.equals("off")) {
               var4 = 3;
            }
            break;
         case 3540994:
            if (action.equals("stop")) {
               var4 = 5;
            }
            break;
         case 109757538:
            if (action.equals("start")) {
               var4 = 2;
            }
            break;
         case 1671308008:
            if (action.equals("disable")) {
               var4 = 4;
            }
         }

         switch(var4) {
         case 0:
         case 1:
         case 2:
            if (this.enabled) {
               return;
            }

            this.setEnabled(true);
            break;
         case 3:
         case 4:
         case 5:
            if (!this.enabled) {
               return;
            }

            this.setEnabled(false);
         }

      }
   }

   public void onTick() {
      if (this.enabled && this.mc.field_1724 != null && this.mc.field_1687 != null) {
         if (this.actionTimer > 0) {
            --this.actionTimer;
         } else if (this.packetDelay > 0) {
            --this.packetDelay;
         } else {
            boolean shouldPlaceTotem = this.detectTotemPop();
            class_1799 offhandStack = this.mc.field_1724.method_6079();
            if (!shouldPlaceTotem && (offhandStack == null || offhandStack.method_7960() || offhandStack.method_7909() != class_1802.field_8288)) {
               shouldPlaceTotem = true;
            }

            if (!shouldPlaceTotem) {
               if (this.inventoryOpened && this.mc.field_1755 instanceof class_490) {
                  this.mc.field_1724.method_7346();
                  this.inventoryOpened = false;
               }

            } else {
               this.totemSlot = this.findTotemInInventory();
               if (this.totemSlot == -1) {
                  if (this.inventoryOpened && this.mc.field_1755 instanceof class_490) {
                     this.mc.field_1724.method_7346();
                     this.inventoryOpened = false;
                  }

               } else if (this.openInventory && !(this.mc.field_1755 instanceof class_490)) {
                  this.mc.method_1507(new class_490(this.mc.field_1724));
                  this.inventoryOpened = true;
                  this.actionTimer = this.refillTicks;
               } else {
                  this.placeTotemInOffhand();
                  if (this.inventoryOpened && this.mc.field_1755 instanceof class_490) {
                     this.packetDelay = this.random.nextInt(3) + 1;
                     this.mc.field_1724.method_7346();
                     this.inventoryOpened = false;
                  }

                  this.actionTimer = this.refillTicks;
               }
            }
         }
      }
   }

   private void placeTotemInOffhand() {
      if (this.mc.field_1724 != null && this.mc.field_1761 != null && this.totemSlot != -1) {
         if (this.mc.field_1724.field_7512 != null) {
            int screenSlot;
            if (this.totemSlot < 9) {
               screenSlot = 36 + this.totemSlot;
            } else {
               if (this.totemSlot >= 36) {
                  return;
               }

               screenSlot = this.totemSlot;
            }

            try {
               if (this.openInventory) {
                  this.mc.field_1761.method_2906(this.mc.field_1724.field_7512.field_7763, screenSlot, 40, class_1713.field_7791, this.mc.field_1724);
               } else {
                  this.mc.field_1761.method_2906(this.mc.field_1724.field_7498.field_7763, screenSlot, 40, class_1713.field_7791, this.mc.field_1724);
               }
            } catch (Exception var3) {
               this.totemSlot = -1;
            }

         }
      }
   }

   private boolean detectTotemPop() {
      if (this.mc.field_1724 == null) {
         return false;
      } else {
         float currentHealth = this.mc.field_1724.method_6032();
         boolean hasAbsorption = this.mc.field_1724.method_6059(class_1294.field_5898);
         boolean absorptionPop = hasAbsorption && !this.wasTotemPopped;
         boolean healthSpike = currentHealth > this.lastHealth + 1.0F && currentHealth >= 1.0F;
         boolean lowHealthRecovery = this.wasLowHealth && currentHealth > 1.0F;
         if (currentHealth <= 1.0F && !this.wasLowHealth) {
            this.wasLowHealth = true;
            this.totemPopTicks = 0;
         } else if (currentHealth > 1.0F) {
            this.wasLowHealth = false;
         }

         if (this.wasLowHealth) {
            ++this.totemPopTicks;
         }

         boolean tickBasedPop = this.totemPopTicks > 0 && this.totemPopTicks < 5 && currentHealth > 1.0F;
         this.lastHealth = currentHealth;
         if (!absorptionPop && !healthSpike && !lowHealthRecovery && !tickBasedPop) {
            if (!hasAbsorption) {
               this.wasTotemPopped = false;
            }
         } else if (!this.wasTotemPopped) {
            this.wasTotemPopped = true;
            return true;
         }

         return false;
      }
   }

   public GuiCategory getCategory() {
      return GuiCategory.COMBAT;
   }

   public List<OptionUtil> getOptions() {
      return this.options;
   }

   public boolean isOpenInventory() {
      return this.openInventory;
   }

   public void setOpenInventory(boolean openInventory) {
      this.openInventory = openInventory;
   }

   public int getRefillTicks() {
      return this.refillTicks;
   }

   public void setRefillTicks(int refillTicks) {
      this.refillTicks = Math.max(0, Math.min(20, refillTicks));
   }

   public void updateFromOptions() {
      if (this.options.size() >= 2) {
         this.openInventory = Boolean.parseBoolean(((OptionUtil)this.options.get(0)).getValue().toString());
         this.refillTicks = Integer.parseInt(((OptionUtil)this.options.get(1)).getValue().toString());
      }

   }

   private int findTotemInInventory() {
      if (this.mc.field_1724 != null && this.mc.field_1724.method_31548() != null) {
         int i;
         class_1799 stack;
         for(i = 0; i < 9; ++i) {
            stack = this.mc.field_1724.method_31548().method_5438(i);
            if (stack != null && stack.method_7909() == class_1802.field_8288) {
               return i;
            }
         }

         for(i = 9; i < 36; ++i) {
            stack = this.mc.field_1724.method_31548().method_5438(i);
            if (stack != null && stack.method_7909() == class_1802.field_8288) {
               return i;
            }
         }

         if (this.mc.field_1724.method_31548().method_5439() > 36) {
            for(i = 36; i < Math.min(40, this.mc.field_1724.method_31548().method_5439()); ++i) {
               stack = this.mc.field_1724.method_31548().method_5438(i);
               if (stack != null && stack.method_7909() == class_1802.field_8288) {
                  return i;
               }
            }
         }

         return -1;
      } else {
         return -1;
      }
   }
}
