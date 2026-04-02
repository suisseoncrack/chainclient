package org.jon.magicclient.client.gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_4185;
import net.minecraft.class_4286;
import net.minecraft.class_437;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.CommandManager;
import org.jon.magicclient.client.MessageHelper;
import org.jon.magicclient.client.config.ConfigManager;
import org.jon.magicclient.client.utils.OptionType;
import org.jon.magicclient.client.utils.OptionUtil;

public class EnhancedCrashGui extends class_437 {
   private final MessageHelper msgHelper = new MessageHelper();
   private final List<Command> crashers = new ArrayList();
   private int currentCrasherIndex = 0;
   private Command currentCrasher;
   private class_342[] optionFields;
   private class_4286 enabledCheckbox;
   private class_4185 prevButton;
   private class_4185 nextButton;
   private class_4185 executeButton;
   private class_4185 saveButton;
   private class_4185 loadButton;
   private static final int GUI_WIDTH = 400;
   private static final int GUI_HEIGHT = 300;
   private static final int FIELD_WIDTH = 200;
   private static final int FIELD_HEIGHT = 20;
   private static final int BUTTON_WIDTH = 80;
   private static final int BUTTON_HEIGHT = 20;

   public EnhancedCrashGui() {
      super(class_2561.method_43470("chainclient Enhanced GUI"));
      this.loadCrashers();
   }

   private void loadCrashers() {
      this.crashers.clear();
      Iterator v1 = CommandManager.getManager().getCommands().iterator();

      while(v1.hasNext()) {
         Command command = (Command)v1.next();
         if (command.getOptions() != null && !command.getOptions().isEmpty()) {
            this.crashers.add(command);
         }
      }

      if (!this.crashers.isEmpty()) {
         this.currentCrasher = (Command)this.crashers.get(0);
      }

   }

   protected void method_25426() {
      if (this.currentCrasher != null) {
         int centerX = this.field_22789 / 2;
         int centerY = this.field_22790 / 2;
         int startX = centerX - 200;
         int startY = centerY - 150;
         List options = this.currentCrasher.getOptions();
         this.optionFields = new class_342[options.size()];

         for(int i = 0; i < options.size(); ++i) {
            OptionUtil option = (OptionUtil)options.get(i);
            int fieldY = startY + 60 + i * 30;
            this.optionFields[i] = new class_342(this.field_22793, startX + 120, fieldY, 200, 20, class_2561.method_43470(option.getName()));
            this.optionFields[i].method_1852(option.getValue().toString());
            this.method_37063(this.optionFields[i]);
         }

         this.enabledCheckbox = new class_4286(startX + 20, startY + 30, 100, 20, class_2561.method_43470("Enabled"), this.currentCrasher.getEnabled());
         this.method_37063(this.enabledCheckbox);
         this.prevButton = class_4185.method_46430(class_2561.method_43470("←"), (button) -> {
            this.previousCrasher();
         }).method_46434(startX + 20, startY + 300 - 60, 80, 20).method_46431();
         this.nextButton = class_4185.method_46430(class_2561.method_43470("→"), (button) -> {
            this.nextCrasher();
         }).method_46434(startX + 110, startY + 300 - 60, 80, 20).method_46431();
         this.executeButton = class_4185.method_46430(class_2561.method_43470("Execute"), (button) -> {
            this.executeCurrentCrasher();
         }).method_46434(startX + 200, startY + 300 - 60, 80, 20).method_46431();
         this.saveButton = class_4185.method_46430(class_2561.method_43470("Save"), (button) -> {
            this.saveConfig();
         }).method_46434(startX + 290, startY + 300 - 60, 80, 20).method_46431();
         this.method_37063(class_4185.method_46430(class_2561.method_43470("Close"), (button) -> {
            this.method_25419();
         }).method_46434(centerX - 40, startY + 300 - 30, 80, 20).method_46431());
         this.method_37063(this.prevButton);
         this.method_37063(this.nextButton);
         this.method_37063(this.executeButton);
         this.method_37063(this.saveButton);
         this.updateNavigationButtons();
      }
   }

   private void previousCrasher() {
      if (this.currentCrasherIndex > 0) {
         this.saveCurrentOptions();
         --this.currentCrasherIndex;
         this.currentCrasher = (Command)this.crashers.get(this.currentCrasherIndex);
         this.clearAndReinit();
      }

   }

   private void nextCrasher() {
      if (this.currentCrasherIndex < this.crashers.size() - 1) {
         this.saveCurrentOptions();
         ++this.currentCrasherIndex;
         this.currentCrasher = (Command)this.crashers.get(this.currentCrasherIndex);
         this.clearAndReinit();
      }

   }

   private void saveCurrentOptions() {
      if (this.currentCrasher != null && this.optionFields != null) {
         List options = this.currentCrasher.getOptions();

         for(int i = 0; i < options.size() && i < this.optionFields.length; ++i) {
            OptionUtil option = (OptionUtil)options.get(i);
            String textValue = this.optionFields[i].method_1882();
            Object value = this.parseOptionValue(textValue, option.getType());
            option.setValue(value);
            ConfigManager.setOptionValue(this.currentCrasher.getName(), option.getName(), value);
         }

         this.currentCrasher.setEnabled(this.enabledCheckbox.method_20372());
         ConfigManager.setCommandEnabled(this.currentCrasher.getName(), this.enabledCheckbox.method_20372());
      }
   }

   private Object parseOptionValue(String this, OptionType text) {
      Object var10000;
      switch(type) {
      case INTEGER:
         Integer v3;
         try {
            v3 = Integer.parseInt(text);
         } catch (NumberFormatException var7) {
            v3 = 1;
            var10000 = v3;
            break;
         }

         var10000 = v3;
         break;
      case FLOAT:
         Float v3;
         try {
            v3 = Float.parseFloat(text);
         } catch (NumberFormatException var6) {
            v3 = 1.0F;
            var10000 = v3;
            break;
         }

         var10000 = v3;
         break;
      case DOUBLE:
         Double v3;
         try {
            v3 = Double.parseDouble(text);
         } catch (NumberFormatException var5) {
            v3 = 1.0D;
            var10000 = v3;
            break;
         }

         var10000 = v3;
         break;
      case BOOLEAN:
         Serializable v3 = Boolean.parseBoolean(text);
         var10000 = v3;
         break;
      case STRING:
      case LIST:
         var10000 = text;
         break;
      default:
         throw new IncompatibleClassChangeError();
      }

      return var10000;
   }

   private void clearAndReinit() {
      this.method_37067();
      this.method_25426();
   }

   private void updateNavigationButtons() {
      if (this.prevButton != null) {
         this.prevButton.field_22763 = this.currentCrasherIndex > 0;
      }

      if (this.nextButton != null) {
         this.nextButton.field_22763 = this.currentCrasherIndex < this.crashers.size() - 1;
      }

   }

   private void executeCurrentCrasher() {
      if (this.currentCrasher != null && this.currentCrasher.getEnabled()) {
         this.saveCurrentOptions();
         List options = this.currentCrasher.getOptions();
         String[] args = new String[options.size() + 1];
         args[0] = this.currentCrasher.getName();

         int i;
         for(i = 0; i < options.size(); ++i) {
            args[i + 1] = ((OptionUtil)options.get(i)).getValue().toString();
         }

         try {
            this.currentCrasher.onCommand(args);
            this.msgHelper.sendMessage("&a" + this.currentCrasher.getName() + " executed successfully!", true);
            StringBuilder config = new StringBuilder();

            for(i = 0; i < options.size(); ++i) {
               config.append(((OptionUtil)options.get(i)).getName()).append("=").append(((OptionUtil)options.get(i)).getValue());
               if (i < options.size() - 1) {
                  config.append(", ");
               }
            }
         } catch (Exception var6) {
            MessageHelper var10000 = this.msgHelper;
            String var10001 = this.currentCrasher.getName();
            var10000.sendMessage("&cError executing " + var10001 + ": " + var6.getMessage(), true);
         }

      } else {
         this.msgHelper.sendMessage("&cCrasher is not enabled!", true);
      }
   }

   private void saveConfig() {
      this.saveCurrentOptions();
      ConfigManager.saveConfig();
      this.msgHelper.sendMessage("&aConfiguration saved!", true);
   }

   public void method_25394(class_332 this, int context, int mouseX, float mouseY) {
      this.method_25420(context);
      if (this.currentCrasher == null) {
         context.method_27534(this.field_22793, class_2561.method_43470("No crashers available"), this.field_22789 / 2, this.field_22790 / 2, 16777215);
      } else {
         int centerX = this.field_22789 / 2;
         int centerY = this.field_22790 / 2;
         int startX = centerX - 200;
         int startY = centerY - 150;
         context.method_27534(this.field_22793, class_2561.method_43470("chainclient - " + this.currentCrasher.getName()), centerX, startY + 10, 16777215);
         context.method_27534(this.field_22793, class_2561.method_43470(this.currentCrasher.getDescription()), centerX, startY + 25, 11184810);
         List options = this.currentCrasher.getOptions();

         for(int i = 0; i < options.size(); ++i) {
            OptionUtil option = (OptionUtil)options.get(i);
            int labelY = startY + 60 + i * 30;
            context.method_27535(this.field_22793, class_2561.method_43470(option.getName() + ":"), startX + 20, labelY + 3, 16777215);
            if (option.getType() == OptionType.LIST && option.getListOptions() != null) {
               String listText = Arrays.toString(option.getListOptions());
               context.method_27535(this.field_22793, class_2561.method_43470(listText.substring(1, listText.length() - 1)), startX + 330, labelY + 3, 11184810);
            }
         }

         int var10002 = this.currentCrasherIndex + 1;
         context.method_27534(this.field_22793, class_2561.method_43470(var10002 + " / " + this.crashers.size()), centerX, startY + 300 - 70, 11184810);
         context.method_27535(this.field_22793, class_2561.method_43470("Press ^ to open/close this GUI"), startX + 20, startY + 300 - 10, 11184810);
         super.method_25394(context, mouseX, mouseY, delta);
      }
   }

   public boolean method_25421() {
      return false;
   }

   public void method_25419() {
      this.saveCurrentOptions();
      super.method_25419();
   }
}
