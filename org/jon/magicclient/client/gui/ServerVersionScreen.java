package org.jon.magicclient.client.gui;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import org.jon.magicclient.client.MessageHelper;

public class ServerVersionScreen extends class_437 {
   private static final class_2561 TITLE = class_2561.method_43470("Server Version Selection");
   private final MessageHelper msgHelper = new MessageHelper();
   private List<ServerVersionScreen.ServerVersion> versions = new ArrayList();
   private int selectedVersion = 0;
   private List<class_339> buttons = new ArrayList();

   public ServerVersionScreen() {
      super(TITLE);
      this.loadVersions();
   }

   private void loadVersions() {
      this.versions.add(new ServerVersionScreen.ServerVersion("1.21.1", "Tricky Trials"));
      this.versions.add(new ServerVersionScreen.ServerVersion("1.21", "Tricky Trials"));
      this.versions.add(new ServerVersionScreen.ServerVersion("1.20.4", "Recent"));
      this.versions.add(new ServerVersionScreen.ServerVersion("1.20.1", "Wild Update"));
      this.versions.add(new ServerVersionScreen.ServerVersion("1.19.4", "Wild Update"));
      this.versions.add(new ServerVersionScreen.ServerVersion("1.18.2", "Caves & Cliffs II"));
      this.versions.add(new ServerVersionScreen.ServerVersion("1.17.1", "Caves & Cliffs I"));
      this.versions.add(new ServerVersionScreen.ServerVersion("1.16.5", "Nether Update"));
      this.versions.add(new ServerVersionScreen.ServerVersion("1.12.2", "World of Color"));
      this.versions.add(new ServerVersionScreen.ServerVersion("1.8.9", "Combat Update"));
   }

   protected void method_25426() {
      super.method_25426();
      int centerX = this.field_22789 / 2;
      int startY = 60;
      int buttonWidth = 200;
      int buttonHeight = 25;
      int spacing = 5;
      this.buttons.clear();

      for(int i = 0; i < this.versions.size(); ++i) {
         ServerVersionScreen.ServerVersion version = (ServerVersionScreen.ServerVersion)this.versions.get(i);
         int y = startY + i * (buttonHeight + spacing);
         class_4185 versionButton = class_4185.method_46430(class_2561.method_43470(version.name + " " + version.tag), (button) -> {
            this.selectedVersion = i;
            this.msgHelper.sendMessage("&aSelected version: &f" + version.name, true);

            try {
               this.msgHelper.sendMessage("&cPlease use the native ViaFabricPlus screen for version switching.", true);
               this.method_25419();
            } catch (Exception var5) {
               var5.printStackTrace();
               this.method_25419();
            }

         }).method_46434(centerX - buttonWidth / 2, y, buttonWidth, buttonHeight).method_46431();
         if (i == this.selectedVersion) {
            versionButton.field_22763 = false;
         }

         this.buttons.add(versionButton);
         this.method_37063(versionButton);
      }

      class_4185 backButton = class_4185.method_46430(class_2561.method_43470("Back"), (button) -> {
         this.method_25419();
      }).method_46434(centerX - 50, startY + this.versions.size() * (buttonHeight + spacing) + 20, 100, buttonHeight).method_46431();
      this.buttons.add(backButton);
      this.method_37063(backButton);
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      this.method_25420(context);
      super.method_25394(context, mouseX, mouseY, delta);
      context.method_27534(this.field_22793, TITLE, this.field_22789 / 2, 20, 16777215);
      if (this.selectedVersion < this.versions.size()) {
         ServerVersionScreen.ServerVersion version = (ServerVersionScreen.ServerVersion)this.versions.get(this.selectedVersion);
         class_2561 infoText = class_2561.method_43470("Selected: " + version.name + " (" + version.tag + ")");
         context.method_27534(this.field_22793, infoText, this.field_22789 / 2, 40, 5635925);
      }

   }

   private static class ServerVersion {
      final String name;
      final String tag;

      ServerVersion(String name, String tag) {
         this.name = name;
         this.tag = tag;
      }
   }
}
