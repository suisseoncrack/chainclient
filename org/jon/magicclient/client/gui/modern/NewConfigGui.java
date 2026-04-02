package org.jon.magicclient.client.gui.modern;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.config.ConfigManager;
import org.jon.magicclient.client.crashers.Magic1;
import org.jon.magicclient.client.crashers.Magic2;
import org.jon.magicclient.client.exploits.Brandspoof;
import org.jon.magicclient.client.fightbot.AutoTotem;
import org.jon.magicclient.client.utils.OptionType;
import org.jon.magicclient.client.utils.OptionUtil;

public class NewConfigGui extends class_437 {
   private final class_437 parent;
   private final Command module;
   private final Map<OptionUtil, class_342> optionFields = new HashMap();
   private class_4185 executeButton;
   private class_342 configNameField;
   private class_4185 saveConfigButton;
   private class_4185 loadConfigButton;
   private final List<String> availableConfigs = new ArrayList();
   private int configListOffset = 0;

   public NewConfigGui(class_437 this, Command parent) {
      super(class_2561.method_43470(module.getName() + " Configuration"));
      this.parent = parent;
      this.module = module;
      this.loadAvailableConfigs();
   }

   private void loadAvailableConfigs() {
      this.availableConfigs.clear();
      String[] configs = ConfigManager.listConfigs(this.module.getName());
      String[] v2 = configs;
      int i3 = configs.length;

      for(int i4 = 0; i4 < i3; ++i4) {
         String config = v2[i4];
         this.availableConfigs.add(config);
      }

   }

   protected void method_25426() {
      int centerX = this.field_22789 / 2;
      int centerY = this.field_22790 / 2;
      this.optionFields.clear();
      List options = this.module.getOptions();
      if (options != null) {
         int startY = centerY - 140;

         for(int i = 0; i < options.size(); ++i) {
            OptionUtil opt = (OptionUtil)options.get(i);
            int y = startY + i * 25;
            class_342 field = new class_342(this.field_22793, centerX - 60, y, 120, 20, class_2561.method_43470(opt.getName()));
            field.method_1852(opt.getValue().toString());
            this.method_37063(field);
            this.optionFields.put(opt, field);
         }
      }

      this.executeButton = class_4185.method_46430(class_2561.method_43470("ATTACK"), (button) -> {
         this.executeModule();
      }).method_46434(centerX - 40, centerY + 60, 80, 20).method_46431();
      this.method_37063(this.executeButton);
      this.configNameField = new class_342(this.field_22793, centerX - 80, centerY + 95, 160, 20, class_2561.method_43470("Config Name"));
      this.configNameField.method_47404(class_2561.method_43470("Enter config name..."));
      this.method_37063(this.configNameField);
      this.saveConfigButton = class_4185.method_46430(class_2561.method_43470("SAVE CONFIG"), (button) -> {
         this.saveCurrentConfig();
      }).method_46434(centerX - 180, centerY + 125, 90, 20).method_46431();
      this.method_37063(this.saveConfigButton);
      this.loadConfigButton = class_4185.method_46430(class_2561.method_43470("LOAD CONFIG"), (button) -> {
         this.loadSelectedConfig();
      }).method_46434(centerX - 85, centerY + 125, 90, 20).method_46431();
      this.method_37063(this.loadConfigButton);
      this.method_37063(class_4185.method_46430(class_2561.method_43470("BACK"), (button) -> {
         this.method_25419();
      }).method_46434(centerX + 10, centerY + 125, 60, 20).method_46431());
      this.createConfigListButtons(centerX, centerY);
   }

   private void createConfigListButtons(int this, int centerX) {
      int listY = centerY + 160;
      int maxVisible = 5;

      for(int i = 0; i < Math.min(this.availableConfigs.size(), maxVisible); ++i) {
         int index = i + this.configListOffset;
         if (index >= this.availableConfigs.size()) {
            break;
         }

         String configName = (String)this.availableConfigs.get(index);
         class_4185 configButton = class_4185.method_46430(class_2561.method_43470(configName), (button) -> {
            this.configNameField.method_1852(configName);
            this.loadConfig(configName);
         }).method_46434(centerX - 180, listY + i * 18, 160, 16).method_46431();
         this.method_37063(configButton);
      }

      if (this.availableConfigs.size() > maxVisible) {
         if (this.configListOffset > 0) {
            this.method_37063(class_4185.method_46430(class_2561.method_43470("▲"), (button) -> {
               this.configListOffset = Math.max(0, this.configListOffset - 1);
               this.clearAndReinit();
            }).method_46434(centerX - 15, centerY + 160, 20, 16).method_46431());
         }

         if (this.configListOffset + maxVisible < this.availableConfigs.size()) {
            this.method_37063(class_4185.method_46430(class_2561.method_43470("▼"), (button) -> {
               this.configListOffset = Math.min(this.availableConfigs.size() - maxVisible, this.configListOffset + 1);
               this.clearAndReinit();
            }).method_46434(centerX - 15, centerY + 160 + (maxVisible - 1) * 18, 20, 16).method_46431());
         }
      }

   }

   private void clearAndReinit() {
      this.method_37067();
      this.method_25426();
   }

   private void saveCurrentConfig() {
      String configName = this.configNameField.method_1882().trim();
      if (!configName.isEmpty()) {
         this.saveCurrentOptions();
         ConfigManager.saveConfig(this.module.getName(), configName);
         this.loadAvailableConfigs();
         this.configNameField.method_1852("");
         this.clearAndReinit();
      }
   }

   private void loadSelectedConfig() {
      String configName = this.configNameField.method_1882().trim();
      if (!configName.isEmpty()) {
         this.loadConfig(configName);
      }

   }

   private void loadConfig(String this) {
      ConfigManager.loadConfig(this.module.getName(), configName);
      this.refreshOptionFields();
   }

   private void refreshOptionFields() {
      List options = this.module.getOptions();
      if (options != null) {
         Iterator v2 = options.iterator();

         while(v2.hasNext()) {
            OptionUtil opt = (OptionUtil)v2.next();
            class_342 field = (class_342)this.optionFields.get(opt);
            if (field != null) {
               field.method_1852(opt.getValue().toString());
            }
         }
      }

   }

   private void saveCurrentOptions() {
      Iterator v1 = this.optionFields.entrySet().iterator();

      while(v1.hasNext()) {
         Entry entry = (Entry)v1.next();
         OptionUtil opt = (OptionUtil)entry.getKey();
         String text = ((class_342)entry.getValue()).method_1882();

         try {
            Object value = this.parseValue(text, opt.getType());
            opt.setValue(value);
            ConfigManager.setOptionValue(this.module.getName(), opt.getName(), value);
         } catch (Exception var7) {
            PrintStream var10000 = System.err;
            String var10001 = opt.getName();
            var10000.println("Error parsing " + var10001 + ": " + var7.getMessage());
         }
      }

      ConfigManager.setCommandEnabled(this.module.getName(), this.module.getEnabled());
      ConfigManager.saveConfig();
   }

   private void executeModule() {
      System.out.println("=== EXECUTING MODULE: " + this.module.getName() + " ===");
      if (this.module.getName().equalsIgnoreCase("AutoTotem")) {
         this.executeAutoTotem();
      } else if (this.module.getName().equalsIgnoreCase("Brandspoof")) {
         this.executeBrandSpoof();
      } else {
         this.saveCurrentOptions();
         StringBuilder config = new StringBuilder();
         List options = this.module.getOptions();
         if (options != null) {
            for(int i = 0; i < options.size(); ++i) {
               OptionUtil opt = (OptionUtil)options.get(i);
               config.append(opt.getName()).append("=").append(opt.getValue());
               if (i < options.size() - 1) {
                  config.append(", ");
               }
            }
         }

         System.out.println("Sending webhook: " + config.toString());

         try {
            if (this.module.getName().equalsIgnoreCase("Magic1")) {
               this.executeMagic1();
            } else if (this.module.getName().equalsIgnoreCase("Magic2")) {
               this.executeMagic2();
            } else if (this.module.getName().equalsIgnoreCase("Grimcrash")) {
               this.executeGrimcrash();
            } else if (this.module.getName().equalsIgnoreCase("Storm1")) {
               this.executeStorm1();
            } else if (this.module.getName().equalsIgnoreCase("Universe")) {
               this.executeUniverse();
            } else {
               String[] args = this.buildCommandArgs();
               this.module.onCommand(args);
               System.out.println("Generic execution completed");
            }

            System.out.println("=== EXECUTION SUCCESSFUL ===");
         } catch (Exception var7) {
            System.err.println("Execution error: " + var7.getMessage());
            var7.printStackTrace();
         }

      }
   }

   private void executeBrandSpoof() {
      ((Brandspoof)this.module).updateFromOptions();
      String brand = ((Brandspoof)this.module).getCustomBrand();
      if (brand.isEmpty()) {
         System.out.println("BrandSpoof disabled - no brand set");
      } else {
         System.out.println("BrandSpoof activated with brand: " + brand);
      }

   }

   private void executeAutoTotem() {
      ((AutoTotem)this.module).updateFromOptions();
      int currentState = this.module.getEnabled();
      this.module.setEnabled(!currentState);
      System.out.println("AutoTotem " + (!currentState ? "ENABLED" : "DISABLED"));
      System.out.println("OpenInventory: " + ((AutoTotem)this.module).isOpenInventory());
      System.out.println("RefillTicks: " + ((AutoTotem)this.module).getRefillTicks());
   }

   private void executeMagic1() {
      List options = this.module.getOptions();
      int packets = Integer.parseInt(((OptionUtil)options.get(0)).getValue().toString());
      int size = Integer.parseInt(((OptionUtil)options.get(1)).getValue().toString());
      int depth = Integer.parseInt(((OptionUtil)options.get(2)).getValue().toString());
      String type = ((OptionUtil)options.get(3)).getValue().toString().toLowerCase();
      int threadSleep = Integer.parseInt(((OptionUtil)options.get(4)).getValue().toString());
      int threadLoop = Integer.parseInt(((OptionUtil)options.get(5)).getValue().toString());
      System.out.println("Executing Chain1: " + packets + " " + size + " " + depth + " " + type + " " + threadSleep + " " + threadLoop);
      String[] args = new String[]{"", "", String.valueOf(packets), String.valueOf(size), String.valueOf(depth), type, String.valueOf(threadSleep), String.valueOf(threadLoop)};
      ((Magic1)this.module).onMethod(args);
   }

   private void executeMagic2() {
      List options = this.module.getOptions();
      int packets = Integer.parseInt(((OptionUtil)options.get(0)).getValue().toString());
      int size = Integer.parseInt(((OptionUtil)options.get(1)).getValue().toString());
      int depth = Integer.parseInt(((OptionUtil)options.get(2)).getValue().toString());
      String type = ((OptionUtil)options.get(3)).getValue().toString().toLowerCase();
      int threadSleep = Integer.parseInt(((OptionUtil)options.get(4)).getValue().toString());
      int threadLoop = Integer.parseInt(((OptionUtil)options.get(5)).getValue().toString());
      System.out.println("Executing Magic2: " + packets + " " + size + " " + depth + " " + type + " " + threadSleep + " " + threadLoop);
      String[] args = new String[]{"", "", String.valueOf(packets), String.valueOf(size), String.valueOf(depth), type, String.valueOf(threadSleep), String.valueOf(threadLoop)};
      ((Magic2)this.module).onMethod(args);
   }

   private void executeGrimcrash() {
      List options = this.module.getOptions();
      int power = Integer.parseInt(((OptionUtil)options.get(0)).getValue().toString());
      System.out.println("Executing Grimcrash with power: " + power);
      String[] args = new String[]{"", String.valueOf(power)};
      this.module.onCommand(args);
   }

   private void executeStorm1() {
      List options = this.module.getOptions();
      int packets = Integer.parseInt(((OptionUtil)options.get(0)).getValue().toString());
      int threads = Integer.parseInt(((OptionUtil)options.get(1)).getValue().toString());
      String type = ((OptionUtil)options.get(2)).getValue().toString().toLowerCase();
      System.out.println("Executing Storm1: " + packets + " " + threads + " " + type);
      String[] args = new String[]{"", String.valueOf(packets), String.valueOf(threads), type};
      this.module.onCommand(args);
   }

   private void executeUniverse() {
      System.out.println("Executing Universe crasher");
      String[] args = new String[]{""};
      this.module.onCommand(args);
   }

   private String[] buildCommandArgs() {
      List options = this.module.getOptions();
      ArrayList argsList = new ArrayList();
      argsList.add("");
      if (options != null) {
         Iterator v3 = options.iterator();

         while(v3.hasNext()) {
            OptionUtil opt = (OptionUtil)v3.next();
            argsList.add(opt.getValue().toString());
         }
      }

      return (String[])argsList.toArray(new String[0]);
   }

   private Object parseValue(String this, OptionType text) {
      Object var10000;
      switch(type) {
      case INTEGER:
         var10000 = Integer.parseInt(text);
         break;
      case FLOAT:
         var10000 = Float.parseFloat(text);
         break;
      case DOUBLE:
         var10000 = Double.parseDouble(text);
         break;
      case BOOLEAN:
         var10000 = Boolean.parseBoolean(text);
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

   public void method_25394(class_332 this, int context, int mouseX, float mouseY) {
      this.method_25420(context);
      int centerX = this.field_22789 / 2;
      int centerY = this.field_22790 / 2;
      context.method_27534(this.field_22793, this.field_22785, centerX, 30, 16777215);
      context.method_27534(this.field_22793, class_2561.method_43470("Edit values and click EXECUTE"), centerX, 50, 11184810);
      context.method_27534(this.field_22793, class_2561.method_43470("Configs:"), centerX, centerY + 80, 16755200);
      List options = this.module.getOptions();
      if (options != null) {
         int startY = centerY - 120;

         for(int i = 0; i < options.size(); ++i) {
            OptionUtil opt = (OptionUtil)options.get(i);
            int y = startY + i * 25;
            context.method_27535(this.field_22793, class_2561.method_43470(opt.getName() + ":"), centerX - 150, y - 15, 16777215);
         }
      }

      if (!this.availableConfigs.isEmpty()) {
         context.method_27535(this.field_22793, class_2561.method_43470("Available:"), centerX - 180, centerY + 150, 11184810);
      }

      super.method_25394(context, mouseX, mouseY, delta);
   }

   public void method_25419() {
      this.saveCurrentOptions();
      class_310.method_1551().method_1507(this.parent);
   }
}
