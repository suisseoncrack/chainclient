package org.jon.magicclient.client.gui.modern;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.jon.magicclient.client.utils.OptionType;
import org.jon.magicclient.client.utils.OptionUtil;

public class ConfigGui extends class_437 {
   private final class_437 parent;
   private final Command module;
   private class_342 configNameField;
   private final List<String> configs = new ArrayList();
   private final Map<OptionUtil, class_342> optionFields = new HashMap();
   private int startOfConfigsY;

   public ConfigGui(class_437 this, Command parent) {
      super(class_2561.method_43470("Configs for " + module.getName()));
      this.parent = parent;
      this.module = module;
      this.loadConfigs();
   }

   private void loadConfigs() {
      this.configs.clear();
      String[] list = ConfigManager.listConfigs(this.module.getName());
      String[] v2 = list;
      int i3 = list.length;

      for(int i4 = 0; i4 < i3; ++i4) {
         String s = v2[i4];
         this.configs.add(s);
      }

   }

   protected void method_25426() {
      int centerX = this.field_22789 / 2;
      int centerY = this.field_22790 / 2;
      this.configNameField = new class_342(this.field_22793, centerX - 100, centerY - 60, 200, 20, class_2561.method_43470("Config Name"));
      this.method_37063(this.configNameField);
      this.optionFields.clear();
      List options = this.module.getOptions();
      int optY = centerY - 120;
      if (options != null) {
         Iterator v5 = options.iterator();

         label27:
         while(true) {
            OptionUtil opt;
            do {
               if (!v5.hasNext()) {
                  break label27;
               }

               opt = (OptionUtil)v5.next();
            } while(opt.getType() != OptionType.INTEGER && opt.getType() != OptionType.FLOAT && opt.getType() != OptionType.DOUBLE && opt.getType() != OptionType.STRING);

            class_342 field = new class_342(this.field_22793, centerX + 10, optY, 90, 16, class_2561.method_43470(opt.getName()));
            field.method_1852(opt.getValue().toString());
            this.method_37063(field);
            this.optionFields.put(opt, field);
            optY += 20;
         }
      }

      this.startOfConfigsY = Math.max(optY + 10, centerY - 80);
      this.method_37063(class_4185.method_46430(class_2561.method_43470("Attack"), (button) -> {
         System.out.println("=== SEND BUTTON CLICKED ===");
         System.out.println("Module: " + this.module.getName());
         this.saveOptions();
         System.out.println("Options saved");
         StringBuilder config = new StringBuilder();
         List moduleOptions = this.module.getOptions();
         OptionUtil opt;
         if (moduleOptions != null) {
            for(int i = 0; i < moduleOptions.size(); ++i) {
               opt = (OptionUtil)moduleOptions.get(i);
               config.append(opt.getName()).append("=").append(opt.getValue());
               if (i < moduleOptions.size() - 1) {
                  config.append(", ");
               }
            }
         }

         System.out.println("Webhook config: " + config.toString());
         System.out.println("Webhook sent");
         StringBuilder command = (new StringBuilder("!")).append(this.module.getName().toLowerCase());
         if (moduleOptions != null) {
            Iterator i$ = moduleOptions.iterator();

            while(i$.hasNext()) {
               opt = (OptionUtil)i$.next();
               command.append(" ").append(opt.getValue().toString());
            }
         }

         System.out.println("GUI sending command: " + command.toString());
         String[] args = command.toString().substring(1).split(" ");
         System.out.println("Calling onMethod with args: " + Arrays.toString(args));

         try {
            String[] methodArgs;
            if (this.module.getName().equalsIgnoreCase("Chain1")) {
               methodArgs = new String[args.length + 2];
               methodArgs[0] = "";
               methodArgs[1] = "";
               System.arraycopy(args, 0, methodArgs, 2, args.length);
               System.out.println("Final methodArgs for Chain1: " + Arrays.toString(methodArgs));
               ((Magic1)this.module).onMethod(methodArgs);
               System.out.println("Chain1.onMethod called successfully");
            } else if (this.module.getName().equalsIgnoreCase("Magic2")) {
               methodArgs = new String[args.length + 2];
               methodArgs[0] = "";
               methodArgs[1] = "";
               System.arraycopy(args, 0, methodArgs, 2, args.length);
               System.out.println("Final methodArgs for Magic2: " + Arrays.toString(methodArgs));
               ((Magic2)this.module).onMethod(methodArgs);
               System.out.println("Magic2.onMethod called successfully");
            } else if (this.module.getName().equalsIgnoreCase("Cat1")) {
               System.out.println("Calling onCommand for Cat1");
               this.module.onCommand(args);
               System.out.println("Cat1.onCommand called successfully");
            } else if (this.module.getName().equalsIgnoreCase("Cat2")) {
               System.out.println("Calling onCommand for Cat2");
               this.module.onCommand(args);
               System.out.println("Cat2.onCommand called successfully");
            } else {
               System.out.println("Calling onCommand for: " + this.module.getName());
               this.module.onCommand(args);
               System.out.println("onCommand called successfully");
            }
         } catch (Exception var11) {
            System.err.println("Error executing module: " + var11.getMessage());
            var11.printStackTrace();
         }

         System.out.println("=== SEND BUTTON FINISHED ===");
         if (!this.module.getName().equalsIgnoreCase("Cat1") && !this.module.getName().equalsIgnoreCase("Cat2")) {
            this.method_25419();
         } else {
            System.out.println("Keeping GUI open for: " + this.module.getName());
         }

      }).method_46434(centerX - 50, centerY + 100, 100, 20).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_43470("Save Config"), (button) -> {
         String name = this.configNameField.method_1882();
         if (!name.isEmpty()) {
            this.saveOptions();
            ConfigManager.saveConfig(this.module.getName(), name);
            this.loadConfigs();
            this.configNameField.method_1852("");
         }

      }).method_46434(centerX - 100, centerY + 130, 95, 20).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_43470("Load Config"), (button) -> {
         String name = this.configNameField.method_1882();
         if (!name.isEmpty()) {
            ConfigManager.loadConfig(this.module.getName(), name);
            this.refreshOptionFields();
         }

      }).method_46434(centerX + 5, centerY + 130, 95, 20).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_43470("Back"), (button) -> {
         this.method_25419();
      }).method_46434(centerX - 50, centerY + 155, 100, 20).method_46431());
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

   private void saveOptions() {
      Iterator v1 = this.optionFields.entrySet().iterator();

      while(v1.hasNext()) {
         Entry entry = (Entry)v1.next();
         OptionUtil opt = (OptionUtil)entry.getKey();
         String text = ((class_342)entry.getValue()).method_1882();

         try {
            switch(opt.getType()) {
            case INTEGER:
               opt.setValue(Integer.parseInt(text));
               break;
            case FLOAT:
               opt.setValue(Float.parseFloat(text));
               break;
            case DOUBLE:
               opt.setValue(Double.parseDouble(text));
               break;
            case STRING:
               opt.setValue(text);
            }

            ConfigManager.setOptionValue(this.module.getName(), opt.getName(), opt.getValue());
         } catch (NumberFormatException var6) {
         }
      }

   }

   public void method_25394(class_332 this, int context, int mouseX, float mouseY) {
      this.method_25420(context);
      context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 10, -1);
      int centerX = this.field_22789 / 2;
      int centerY = this.field_22790 / 2;
      int optY = centerY - 70;
      List options = this.module.getOptions();
      if (options != null) {
         Iterator v9 = options.iterator();

         while(v9.hasNext()) {
            OptionUtil opt = (OptionUtil)v9.next();
            if (this.optionFields.containsKey(opt)) {
               context.method_25303(this.field_22793, opt.getName() + ":", centerX - 100, optY + 4, -1);
               optY += 20;
            }
         }
      }

      context.method_25303(this.field_22793, "Existing Configs:", centerX - 100, this.startOfConfigsY, -5592406);
      int listY = this.startOfConfigsY + 15;

      for(int i = 0; i < this.configs.size(); ++i) {
         String name = (String)this.configs.get(i);
         int hovered = mouseX >= centerX - 100 && mouseX <= centerX + 100 && mouseY >= listY && mouseY <= listY + 12;
         context.method_25303(this.field_22793, name, centerX - 100, listY, hovered ? -1 : -4473925);
         listY += 12;
      }

      super.method_25394(context, mouseX, mouseY, delta);
   }

   public boolean method_25402(double this, double mouseX, int mouseY) {
      int centerX = this.field_22789 / 2;
      int listY = this.startOfConfigsY + 15;

      for(Iterator v8 = this.configs.iterator(); v8.hasNext(); listY += 12) {
         String name = (String)v8.next();
         if (mouseX >= (double)(centerX - 100) && mouseX <= (double)(centerX + 100) && mouseY >= (double)listY && mouseY <= (double)(listY + 12)) {
            this.configNameField.method_1852(name);
            return true;
         }
      }

      return super.method_25402(mouseX, mouseY, button);
   }

   public void method_25419() {
      class_310.method_1551().method_1507(this.parent);
   }
}
