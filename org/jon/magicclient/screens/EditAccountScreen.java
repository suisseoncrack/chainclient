package org.jon.magicclient.screens;

import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_442;
import net.minecraft.class_500;
import org.jon.magicclient.Magicclient;
import org.jon.magicclient.utils.APIUtils;
import org.jon.magicclient.utils.FormattingUtils;
import org.jon.magicclient.utils.SessionUtils;

public class EditAccountScreen extends class_437 {
   private class_342 nameField;
   private class_342 skinUrlField;
   private class_4185 nameButton;
   private class_4185 skinButton;
   private class_2561 currentTitle;

   public EditAccountScreen() {
      super(class_2561.method_43470(""));
      this.currentTitle = FormattingUtils.surroundWithObfuscated(class_2561.method_43470("Edit Account").method_27692(class_124.field_1075), 5);
   }

   protected void method_25426() {
      int centerX = this.field_22789 / 2;
      int centerY = this.field_22790 / 2;
      this.nameField = new class_342(this.field_22793, centerX - 100, centerY - 40, 200, 20, class_2561.method_43470("New Username"));
      this.nameField.method_1880(16);
      this.nameField.method_25365(true);
      this.method_25429(this.nameField);
      this.skinUrlField = new class_342(this.field_22793, centerX - 100, centerY, 200, 20, class_2561.method_43470("Skin URL"));
      this.skinUrlField.method_1880(2048);
      this.method_25429(this.skinUrlField);
      this.nameButton = class_4185.method_46430(class_2561.method_43470("Change Name"), (button) -> {
         String newName = this.nameField.method_1882().trim();
         if (!newName.isEmpty()) {
            if (newName.matches("^[a-zA-Z0-9_]{3,16}$")) {
               int statusCode = APIUtils.changeName(newName, Magicclient.currentSession.method_1674());
               class_2561 var10001;
               switch(statusCode) {
               case 200:
                  Magicclient.currentSession = SessionUtils.createSession(newName, Magicclient.currentSession.method_44717(), Magicclient.currentSession.method_1674());
                  var10001 = FormattingUtils.surroundWithObfuscated(class_2561.method_43470("Successfully changed name").method_27692(class_124.field_1060), 4);
                  break;
               case 400:
                  var10001 = FormattingUtils.surroundWithObfuscated(class_2561.method_43470("Invalid name").method_27692(class_124.field_1061), 7);
                  break;
               case 401:
                  var10001 = FormattingUtils.surroundWithObfuscated(class_2561.method_43470("Invalid token").method_27692(class_124.field_1061), 7);
                  break;
               case 403:
                  var10001 = FormattingUtils.surroundWithObfuscated(class_2561.method_43470("Name is unavailable or Player already changed name in the last 35 days").method_27692(class_124.field_1061), 2);
                  break;
               case 429:
                  var10001 = FormattingUtils.surroundWithObfuscated(class_2561.method_43470("Too many requests").method_27692(class_124.field_1061), 5);
                  break;
               default:
                  var10001 = FormattingUtils.surroundWithObfuscated(class_2561.method_43470("Unknown error").method_27692(class_124.field_1061), 2);
               }

               this.currentTitle = var10001;
            } else {
               this.currentTitle = FormattingUtils.surroundWithObfuscated(class_2561.method_43470("Invalid name").method_27692(class_124.field_1061), 7);
            }
         } else {
            this.currentTitle = FormattingUtils.surroundWithObfuscated(class_2561.method_43470("Please input a name").method_27692(class_124.field_1061), 5);
         }

      }).method_46434(centerX - 100, centerY + 25, 97, 20).method_46431();
      this.method_37063(this.nameButton);
      this.skinButton = class_4185.method_46430(class_2561.method_43470("Change Skin"), (button) -> {
         String skinUrl = this.skinUrlField.method_1882().trim();
         if (!skinUrl.isEmpty()) {
            int statusCode = APIUtils.changeSkin(skinUrl, Magicclient.currentSession.method_1674());
            class_2561 var10001;
            switch(statusCode) {
            case -1:
               var10001 = FormattingUtils.surroundWithObfuscated(class_2561.method_43470("Unknown error").method_27692(class_124.field_1061), 7);
               break;
            case 200:
               var10001 = FormattingUtils.surroundWithObfuscated(class_2561.method_43470("Successfully changed skin").method_27692(class_124.field_1060), 4);
               break;
            case 401:
               var10001 = FormattingUtils.surroundWithObfuscated(class_2561.method_43470("Invalid token").method_27692(class_124.field_1061), 7);
               break;
            case 429:
               var10001 = FormattingUtils.surroundWithObfuscated(class_2561.method_43470("Too many requests").method_27692(class_124.field_1061), 5);
               break;
            default:
               var10001 = FormattingUtils.surroundWithObfuscated(class_2561.method_43470("Invalid Skin").method_27692(class_124.field_1061), 7);
            }

            this.currentTitle = var10001;
         } else {
            this.currentTitle = FormattingUtils.surroundWithObfuscated(class_2561.method_43470("Please input an URL").method_27692(class_124.field_1061), 5);
         }

      }).method_46434(centerX + 3, centerY + 25, 97, 20).method_46431();
      this.method_37063(this.skinButton);
      class_4185 backButton = class_4185.method_46430(class_2561.method_43470("Back"), (button) -> {
         assert this.field_22787 != null;

         this.field_22787.method_1507(new class_500(new class_442()));
      }).method_46434(centerX - 100, centerY + 50, 200, 20).method_46431();
      this.method_37063(backButton);
      if (Magicclient.originalSession.equals(Magicclient.currentSession)) {
         this.nameButton.field_22763 = false;
         this.skinButton.field_22763 = false;
         this.currentTitle = FormattingUtils.surroundWithObfuscated(class_2561.method_43470("Cannot modify original session").method_27692(class_124.field_1054), 4);
      }

   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      this.method_25420(context);
      super.method_25394(context, mouseX, mouseY, delta);
      context.method_27535(this.field_22793, class_2561.method_43470("Username:"), this.field_22789 / 2 - 100, this.field_22790 / 2 - 52, 10526880);
      this.nameField.method_25394(context, mouseX, mouseY, delta);
      context.method_27535(this.field_22793, class_2561.method_43470("Skin URL:"), this.field_22789 / 2 - 100, this.field_22790 / 2 - 10, 10526880);
      this.skinUrlField.method_25394(context, mouseX, mouseY, delta);
      context.method_27534(this.field_22793, this.currentTitle, this.field_22789 / 2, this.field_22790 / 2 - 75, 16777215);
   }

   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
      return this.nameField.method_25404(keyCode, scanCode, modifiers) || this.skinUrlField.method_25404(keyCode, scanCode, modifiers) || super.method_25404(keyCode, scanCode, modifiers);
   }

   public boolean method_25400(char chr, int modifiers) {
      return this.nameField.method_25400(chr, modifiers) || this.skinUrlField.method_25400(chr, modifiers) || super.method_25400(chr, modifiers);
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      boolean nameFocused = this.nameField.method_25402(mouseX, mouseY, button);
      boolean skinFocused = this.skinUrlField.method_25402(mouseX, mouseY, button);
      this.nameField.method_25365(nameFocused);
      this.skinUrlField.method_25365(skinFocused);
      return nameFocused || skinFocused || super.method_25402(mouseX, mouseY, button);
   }
}
