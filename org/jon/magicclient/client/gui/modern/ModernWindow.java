package org.jon.magicclient.client.gui.modern;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_310;
import net.minecraft.class_332;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.CommandManager;
import org.jon.magicclient.client.config.ConfigManager;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.OptionType;
import org.jon.magicclient.client.utils.OptionUtil;

public class ModernWindow {
   private String title;
   private int x;
   private int y;
   private int w;
   private int h;
   private GuiCategory category;
   private boolean dragging = false;
   private int dragX;
   private int dragY;
   private boolean expanded = true;
   private int scrollOffset = 0;
   private Command selectedModule = null;

   public ModernWindow(GuiCategory category, int x, int y) {
      this.title = category.getDisplayName();
      this.x = x;
      this.y = y;
      this.w = 100;
      this.h = 16;
      this.category = category;
      this.expanded = ConfigManager.isWindowExpanded(category);
   }

   private List<Command> getModules() {
      List<Command> modules = new ArrayList();
      Iterator var2 = CommandManager.getManager().getCommands().iterator();

      while(var2.hasNext()) {
         Command cmd = (Command)var2.next();
         if (cmd.getCategory() == this.category) {
            modules.add(cmd);
         }
      }

      return modules;
   }

   public void render(class_332 context, int mouseX, int mouseY) {
      int headerColor = -8704009;
      context.method_25294(this.x, this.y, this.x + this.w, this.y + 16, headerColor);
      int var10003 = this.x + this.w / 2;
      int var10004 = this.y + 4;
      context.method_25300(class_310.method_1551().field_1772, this.title, var10003, var10004, -1);
      if (this.expanded) {
         List<Command> modules = this.getModules();
         int currentY = this.y + 16;
         context.method_25294(this.x, currentY, this.x + this.w, currentY + modules.size() * 14, -16777216);

         for(Iterator var7 = modules.iterator(); var7.hasNext(); currentY += 14) {
            Command mod = (Command)var7.next();
            boolean hovered = mouseX >= this.x && mouseX <= this.x + this.w && mouseY >= currentY && mouseY <= currentY + 14;
            int textColor = mod.getEnabled() ? -1 : (hovered ? -3355444 : -4473925);
            context.method_25300(class_310.method_1551().field_1772, mod.getName(), this.x + this.w / 2, currentY + 3, textColor);
         }

         this.h = currentY - this.y;
         if (this.selectedModule != null && this.selectedModule.getCategory() == this.category) {
            this.drawSettings(context, mouseX, mouseY);
         }
      } else {
         this.h = 16;
      }

   }

   private void drawSettings(class_332 context, int mouseX, int mouseY) {
      int settingsX = this.x;
      int settingsY = this.y + this.h + 2;
      int settingsW = this.w;
      List<OptionUtil> options = this.selectedModule.getOptions();
      int optionsSize = options == null ? 0 : options.size();
      int settingsH = optionsSize * 14;
      context.method_25294(settingsX, settingsY, settingsX + settingsW, settingsY + settingsH, -300279270);
      int optY = settingsY;
      if (options != null) {
         for(Iterator var11 = options.iterator(); var11.hasNext(); optY += 14) {
            OptionUtil opt = (OptionUtil)var11.next();
            boolean hovered = mouseX >= settingsX && mouseX <= settingsX + settingsW && mouseY >= optY && mouseY <= optY + 14;
            String var10000 = opt.getName();
            String text = var10000 + ": " + String.valueOf(opt.getValue());
            context.method_25300(class_310.method_1551().field_1772, text, settingsX + settingsW / 2, optY + 3, hovered ? -1 : -4473925);
         }
      }

   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (mouseX >= (double)this.x && mouseX <= (double)(this.x + this.w) && mouseY >= (double)this.y && mouseY <= (double)(this.y + 16)) {
         if (button == 0) {
            this.dragging = true;
            this.dragX = (int)(mouseX - (double)this.x);
            this.dragY = (int)(mouseY - (double)this.y);
            return true;
         }

         if (button == 1) {
            this.expanded = !this.expanded;
            ConfigManager.setWindowExpanded(this.category, this.expanded);
            return true;
         }
      }

      List<Command> modules = this.getModules();
      int currentY;
      if (this.expanded && mouseX >= (double)this.x && mouseX <= (double)(this.x + this.w) && mouseY >= (double)(this.y + 16) && mouseY <= (double)(this.y + 16 + modules.size() * 14)) {
         currentY = this.y + 16;

         for(Iterator var8 = modules.iterator(); var8.hasNext(); currentY += 14) {
            Command mod = (Command)var8.next();
            if (mouseY >= (double)currentY && mouseY <= (double)(currentY + 14)) {
               if (button == 0) {
                  mod.setEnabled(!mod.getEnabled());
                  return true;
               }

               if (button == 1) {
                  class_310.method_1551().method_1507(new NewConfigGui(class_310.method_1551().field_1755, mod));
                  return true;
               }
            }
         }
      }

      if (this.selectedModule != null && this.expanded) {
         currentY = this.x;
         int sY = this.y + this.h + 2;
         int sW = this.w;
         List<OptionUtil> options = this.selectedModule.getOptions();
         if (options != null && !options.isEmpty() && mouseX >= (double)currentY && mouseX <= (double)(currentY + sW)) {
            int startY = sY;

            for(Iterator var12 = options.iterator(); var12.hasNext(); startY += 14) {
               OptionUtil opt = (OptionUtil)var12.next();
               if (mouseY >= (double)startY && mouseY <= (double)(startY + 14)) {
                  if (opt.getType() == OptionType.BOOLEAN) {
                     boolean newValue = !(Boolean)opt.getValue();
                     opt.setValue(newValue);
                     ConfigManager.setOptionValue(this.selectedModule.getName(), opt.getName(), newValue);
                  } else if (opt.getType() == OptionType.LIST) {
                     Object[] optionsArr = opt.getListOptions();
                     if (optionsArr != null && optionsArr.length > 0) {
                        String current = opt.getValue().toString();
                        int idx = 0;

                        for(int i = 0; i < optionsArr.length; ++i) {
                           if (optionsArr[i].toString().equalsIgnoreCase(current)) {
                              idx = (i + 1) % optionsArr.length;
                              break;
                           }
                        }

                        Object nextVal = optionsArr[idx];
                        opt.setValue(nextVal);
                        ConfigManager.setOptionValue(this.selectedModule.getName(), opt.getName(), nextVal);
                     }
                  } else if (opt.getType() == OptionType.INTEGER) {
                     int current = (Integer)opt.getValue();
                     int next = current + (button == 0 ? 1 : -1);
                     opt.setValue(next);
                     ConfigManager.setOptionValue(this.selectedModule.getName(), opt.getName(), next);
                  }

                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean isCrasherOrExploit(Command mod) {
      GuiCategory cat = mod.getCategory();
      return cat == GuiCategory.CRASHES || cat == GuiCategory.EXPLOITS;
   }

   public void mouseReleased(double mouseX, double mouseY, int button) {
      if (button == 0 && this.dragging) {
         this.dragging = false;
         ConfigManager.setWindowPos(this.category, this.x, this.y);
      }

   }

   public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (this.dragging) {
         this.x = (int)(mouseX - (double)this.dragX);
         this.y = (int)(mouseY - (double)this.dragY);
      }

   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
      return false;
   }
}
