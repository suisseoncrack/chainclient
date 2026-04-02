package org.jon.magicclient.client.gui;

import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_4185;
import net.minecraft.class_4286;
import net.minecraft.class_437;
import net.minecraft.class_7842;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.MessageHelper;
import org.jon.magicclient.client.config.ConfigManager;
import org.jon.magicclient.client.gui.modern.ModernClickGuiScreen;
import org.jon.magicclient.client.utils.OptionType;
import org.jon.magicclient.client.utils.OptionUtil;

public class CommandConfigScreen extends class_437 {
   private static final int PANEL_WIDTH = 520;
   private static final int PANEL_HEIGHT = 320;
   private final class_437 parent;
   private final Command command;
   private final MessageHelper msgHelper;
   private final boolean saving;
   private class_342 nameField;
   private String selectedConfig;
   private int panelX;
   private int panelY;
   private class_4286 enabledCheckbox;
   private class_342[] fields;

   public CommandConfigScreen(class_437 parent, Command command) {
      this(parent, command, false);
   }

   public CommandConfigScreen(Command command, boolean saving) {
      this((class_437)null, command, saving);
   }

   public CommandConfigScreen(class_437 parent, Command command, boolean saving) {
      super(class_2561.method_43470(command.getName()));
      this.msgHelper = new MessageHelper();
      this.selectedConfig = null;
      this.parent = parent;
      this.command = command;
      this.saving = saving;
   }

   protected void method_25426() {
      this.panelX = (this.field_22789 - 520) / 2;
      this.panelY = (this.field_22790 - 320) / 2;
      List<OptionUtil> options = this.command.getOptions();
      this.fields = new class_342[options.size()];
      int startY = this.panelY + 46;
      this.enabledCheckbox = new class_4286(this.panelX + 14, this.panelY + 14, 90, 20, class_2561.method_43470("Enabled"), this.command.getEnabled());
      this.method_37063(this.enabledCheckbox);

      int configY;
      int scrollX;
      for(configY = 0; configY < options.size(); ++configY) {
         OptionUtil opt = (OptionUtil)options.get(configY);
         scrollX = startY + configY * 26;
         class_342 tf = new class_342(this.field_22793, this.panelX + 160, scrollX, 220, 18, class_2561.method_43470(opt.getName()));
         tf.method_1852(opt.getValue() != null ? opt.getValue().toString() : "");
         this.fields[configY] = tf;
         this.method_37063(tf);
      }

      configY = this.panelY + 320 - 80;
      this.method_37063(new class_7842(this.panelX + 14, configY, 100, 20, class_2561.method_43470("Configs:"), this.field_22793));
      this.nameField = new class_342(this.field_22793, this.panelX + 14, configY + 20, 150, 18, class_2561.method_43470("Config Name"));
      this.nameField.method_47404(class_2561.method_43470("New Config Name..."));
      this.method_37063(this.nameField);
      this.method_37063(class_4185.method_46430(class_2561.method_43470("Save New"), (b) -> {
         this.saveNewConfig();
      }).method_46434(this.panelX + 170, configY + 20, 70, 20).method_46431());
      String[] configs = ConfigManager.listConfigs(this.command.getName());
      scrollX = this.panelX + 250;
      int scrollY = configY;

      for(int i = 0; i < Math.min(configs.length, 5); ++i) {
         String cfg = configs[i];
         this.method_37063(class_4185.method_46430(class_2561.method_43470(cfg), (b) -> {
            this.selectedConfig = cfg;
            ConfigManager.loadConfig(this.command.getName(), cfg);
            this.rebuild();
         }).method_46434(scrollX, scrollY + i * 22, 80, 20).method_46431());
      }

      this.method_37063(class_4185.method_46430(class_2561.method_43470("Execute"), (b) -> {
         this.execute();
      }).method_46434(this.panelX + 14, this.panelY + 320 - 28, 70, 20).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_43470("Save Default"), (b) -> {
         this.save();
      }).method_46434(this.panelX + 90, this.panelY + 320 - 28, 90, 20).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_43470("Back"), (b) -> {
         this.method_25419();
      }).method_46434(this.panelX + 520 - 70, this.panelY + 320 - 28, 60, 20).method_46431());
   }

   private void rebuild() {
      this.method_37067();
      this.method_25426();
   }

   private void saveNewConfig() {
      String name = this.nameField.method_1882();
      if (name.isEmpty()) {
         this.msgHelper.sendMessage("&cPlease enter a name!", true);
      } else {
         this.saveCurrentOptions();
         ConfigManager.saveConfig(this.command.getName(), name);
         this.msgHelper.sendMessage("&aSaved as " + name, true);
         this.rebuild();
      }
   }

   private void saveCurrentOptions() {
      List<OptionUtil> options = this.command.getOptions();

      for(int i = 0; i < options.size(); ++i) {
         OptionUtil opt = (OptionUtil)options.get(i);
         String textValue = this.fields[i].method_1882();
         Object value = this.parseOptionValue(textValue, opt.getType());
         opt.setValue(value);
         ConfigManager.setOptionValue(this.command.getName(), opt.getName(), value);
      }

      this.command.setEnabled(this.enabledCheckbox.method_20372());
      ConfigManager.setCommandEnabled(this.command.getName(), this.enabledCheckbox.method_20372());
   }

   private Object parseOptionValue(String text, OptionType type) {
      Object var10000;
      switch(type) {
      case INTEGER:
         Integer var10;
         try {
            var10 = Integer.parseInt(text);
         } catch (NumberFormatException var7) {
            var10 = 1;
            var10000 = var10;
            break;
         }

         var10000 = var10;
         break;
      case FLOAT:
         Float var9;
         try {
            var9 = Float.parseFloat(text);
         } catch (NumberFormatException var6) {
            var9 = 1.0F;
            var10000 = var9;
            break;
         }

         var10000 = var9;
         break;
      case DOUBLE:
         Double var8;
         try {
            var8 = Double.parseDouble(text);
         } catch (NumberFormatException var5) {
            var8 = 1.0D;
            var10000 = var8;
            break;
         }

         var10000 = var8;
         break;
      case BOOLEAN:
         Boolean var3 = Boolean.parseBoolean(text);
         var10000 = var3;
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

   private void execute() {
      if (!this.enabledCheckbox.method_20372()) {
         this.msgHelper.sendMessage("&cModule is not enabled!", true);
      } else {
         this.saveCurrentOptions();
         List<OptionUtil> options = this.command.getOptions();
         String[] args = new String[options.size() + 1];
         args[0] = this.command.getName();

         for(int i = 0; i < options.size(); ++i) {
            args[i + 1] = ((OptionUtil)options.get(i)).getValue() != null ? ((OptionUtil)options.get(i)).getValue().toString() : "";
         }

         try {
            this.command.onCommand(args);
            this.msgHelper.sendMessage("&aExecuted " + this.command.getName(), true);
            StringBuilder config = new StringBuilder();

            for(int i = 0; i < options.size(); ++i) {
               config.append(((OptionUtil)options.get(i)).getName()).append("=").append(((OptionUtil)options.get(i)).getValue());
               if (i < options.size() - 1) {
                  config.append(", ");
               }
            }
         } catch (Exception var5) {
            this.msgHelper.sendMessage("&cError: " + var5.getMessage(), true);
         }

      }
   }

   private void save() {
      this.saveCurrentOptions();
      ConfigManager.saveConfig();
      this.msgHelper.sendMessage("&aSaved", true);
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      GuiBackgroundRenderer.render(context);
      context.method_25294(0, 0, this.field_22789, this.field_22790, -1442840576);
      context.method_25294(this.panelX, this.panelY, this.panelX + 520, this.panelY + 320, -15658216);
      context.method_25294(this.panelX, this.panelY, this.panelX + 520, this.panelY + 1, -13881546);
      context.method_27535(this.field_22793, class_2561.method_43470(this.command.getName()), this.panelX + 14, this.panelY + 32, 16777215);
      context.method_27535(this.field_22793, class_2561.method_43470(this.command.getDescription()), this.panelX + 14, this.panelY + 46 - 14, 11184810);
      List<OptionUtil> options = this.command.getOptions();
      int startY = this.panelY + 46;

      for(int i = 0; i < options.size(); ++i) {
         int y = startY + i * 26;
         class_327 var10001 = this.field_22793;
         Object var10002 = options.get(i);
         context.method_27535(var10001, class_2561.method_43470(((OptionUtil)var10002).getName() + ":"), this.panelX + 14, y + 5, 16777215);
      }

      super.method_25394(context, mouseX, mouseY, delta);
   }

   public void method_25419() {
      if (this.parent != null) {
         this.field_22787.method_1507(this.parent);
      } else {
         this.field_22787.method_1507(new ModernClickGuiScreen());
      }

   }

   public boolean method_25421() {
      return false;
   }
}
