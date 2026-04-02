package org.jon.magicclient.client.gui;

import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import org.jon.magicclient.Magicclient;
import org.jon.magicclient.client.utils.AccountHelper;

public class AltManagerScreen extends class_437 {
   private final class_437 parent;
   private final AccountHelper accountHelper = new AccountHelper();
   private class_342 usernameField;
   private String status = "§7Idle";

   public AltManagerScreen(class_437 parent) {
      super(class_2561.method_43470("Alt Manager"));
      this.parent = parent;
      this.accountHelper.readAccountsMap();
   }

   protected void method_25426() {
      int centerX = this.field_22789 / 2;
      int startY = 40;
      this.usernameField = new class_342(this.field_22793, centerX - 100, startY, 200, 20, class_2561.method_43470("Username / Token"));
      this.usernameField.method_47404(class_2561.method_43470("Username or Token"));
      this.usernameField.method_1880(2048);
      this.method_37063(this.usernameField);
      this.method_37063(class_4185.method_46430(class_2561.method_43470("Offline Login"), (button) -> {
         String name = this.usernameField.method_1882();
         if (!name.isEmpty()) {
            this.accountHelper.offlineLogin(name);
            this.status = "§aLogged in as " + name + " (Offline)";
         } else {
            this.status = "§cEnter a username!";
         }

      }).method_46434(centerX - 100, startY + 30, 200, 20).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_43470("Microsoft Login (Webview)"), (button) -> {
         this.status = "§eLogging in via Microsoft...";
         (new Thread(() -> {
            try {
               this.accountHelper.loginByMicrosoft();
               this.status = "§aSuccessfully logged in via Microsoft!";
            } catch (Exception var2) {
               this.status = "§cMicrosoft Login failed!";
               var2.printStackTrace();
            }

         })).start();
      }).method_46434(centerX - 100, startY + 60, 200, 20).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_43470("Token Login"), (button) -> {
         String token = this.usernameField.method_1882();
         if (!token.isEmpty()) {
            this.status = "§eLogging in via Token...";
            (new Thread(() -> {
               try {
                  this.accountHelper.directTokenLogin(token);
                  this.status = "§aSuccessfully logged in via Token!";
               } catch (Exception var3) {
                  this.status = "§cToken Login failed: " + var3.getMessage();
                  var3.printStackTrace();
               }

            })).start();
         } else {
            this.status = "§cEnter a token in the field!";
         }

      }).method_46434(centerX - 100, startY + 90, 200, 20).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_43470("Quick Token Login"), (button) -> {
         this.status = "§eQuick login with predefined token...";
         (new Thread(() -> {
            try {
               this.accountHelper.directTokenLogin(Magicclient.sessionid);
               this.status = "§aQuick login successful!";
            } catch (Exception var2) {
               this.status = "§cQuick login failed: " + var2.getMessage();
               var2.printStackTrace();
            }

         })).start();
      }).method_46434(centerX - 100, startY + 120, 200, 20).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_43470("Cookie Login"), (button) -> {
         this.status = "§eWaiting for file selection...";
         (new Thread(() -> {
            try {
               this.accountHelper.cookieLogin();
               this.status = "§aSuccessfully logged in via Cookie!";
            } catch (Exception var2) {
               this.status = "§cCookie Login failed!";
            }

         })).start();
      }).method_46434(centerX - 100, startY + 120, 200, 20).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_43470("Back"), (button) -> {
         this.field_22787.method_1507(this.parent);
      }).method_46434(centerX - 100, this.field_22790 - 30, 200, 20).method_46431());
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      GuiBackgroundRenderer.render(context);
      super.method_25394(context, mouseX, mouseY, delta);
      context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 20, 16777215);
      context.method_25300(this.field_22793, this.status, this.field_22789 / 2, 45, 16777215);
      String currentUser = this.field_22787.method_1548().method_1676();
      context.method_25303(this.field_22793, "Current User: §b" + currentUser, 10, 10, 16777215);
   }

   public void method_25419() {
      this.field_22787.method_1507(this.parent);
   }
}
