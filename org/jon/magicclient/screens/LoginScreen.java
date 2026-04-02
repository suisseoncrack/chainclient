package org.jon.magicclient.screens;

import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_442;
import org.jon.magicclient.Magicclient;
import org.jon.magicclient.client.utils.AccountHelper;
import org.jon.magicclient.utils.FormattingUtils;
import org.jon.magicclient.utils.SessionUtils;

public class LoginScreen extends class_437 {
   private class_342 sessionField;
   private class_4185 loginButton;
   private class_4185 restoreButton;
   private class_2561 currentTitle;

   public LoginScreen() {
      super(class_2561.method_43470(""));
      this.currentTitle = FormattingUtils.surroundWithObfuscated(class_2561.method_43470("Token Login").method_27692(class_124.field_1065), 5);
   }

   protected void method_25426() {
      int centerX = this.field_22789 / 2;
      int centerY = this.field_22790 / 2;
      this.sessionField = new class_342(this.field_22793, centerX - 100, centerY - 20, 200, 20, class_2561.method_43470("Token eingeben"));
      this.sessionField.method_1880(32767);
      this.sessionField.method_1852(Magicclient.sessionid);
      this.sessionField.method_25365(true);
      this.method_25429(this.sessionField);
      this.loginButton = class_4185.method_46430(class_2561.method_43470("Login"), (button) -> {
         String sessionInput = this.sessionField.method_1882().trim();
         if (!sessionInput.isEmpty()) {
            try {
               System.out.println("Login mit Token...");
               AccountHelper accountHelper = new AccountHelper();
               accountHelper.directTokenLogin(sessionInput);
               this.currentTitle = FormattingUtils.surroundWithObfuscated(class_2561.method_43470("Login erfolgreich!").method_27692(class_124.field_1060), 5);
               System.out.println("Login erfolgreich!");
               this.restoreButton.field_22763 = true;
               this.loginButton.field_22763 = false;
            } catch (Exception var5) {
               String errorMsg = var5.getMessage();
               System.err.println("Login fehlgeschlagen: " + errorMsg);
               this.currentTitle = FormattingUtils.surroundWithObfuscated(class_2561.method_43470("Login fehlgeschlagen").method_27692(class_124.field_1061), 7);
            }
         } else {
            this.currentTitle = FormattingUtils.surroundWithObfuscated(class_2561.method_43470("Token darf nicht leer sein").method_27692(class_124.field_1061), 5);
         }

      }).method_46434(centerX - 100, centerY + 10, 200, 20).method_46431();
      this.method_37063(this.loginButton);
      this.restoreButton = class_4185.method_46430(class_2561.method_43470("Restore"), (button) -> {
         SessionUtils.restoreSession();
         this.currentTitle = FormattingUtils.surroundWithObfuscated(class_2561.method_43470("Original-Session wiederhergestellt").method_27692(class_124.field_1060), 7);
         this.loginButton.field_22763 = true;
         this.restoreButton.field_22763 = false;
      }).method_46434(centerX - 100, centerY + 35, 200, 20).method_46431();
      this.method_37063(this.restoreButton);
      class_4185 backButton = class_4185.method_46430(class_2561.method_43470("Zurück"), (button) -> {
         assert this.field_22787 != null;

         this.field_22787.method_1507(new class_442());
      }).method_46434(centerX - 100, centerY + 60, 200, 20).method_46431();
      this.method_37063(backButton);
      if (Magicclient.currentSession.equals(Magicclient.originalSession)) {
         this.restoreButton.field_22763 = false;
      }

   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      this.method_25420(context);
      int centerX = this.field_22789 / 2;
      int centerY = this.field_22790 / 2;
      context.method_27534(this.field_22793, this.currentTitle, this.field_22789 / 2, 50, 16777215);
      context.method_27534(this.field_22793, class_2561.method_43470("Token:").method_27692(class_124.field_1080), this.field_22789 / 2, centerY - 35, 16777215);
      super.method_25394(context, mouseX, mouseY, delta);
   }

   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
      return !this.sessionField.method_25404(keyCode, scanCode, modifiers) && !this.sessionField.method_20315() ? super.method_25404(keyCode, scanCode, modifiers) : true;
   }

   public boolean method_25400(char chr, int modifiers) {
      return this.sessionField.method_25400(chr, modifiers) ? true : super.method_25400(chr, modifiers);
   }
}
