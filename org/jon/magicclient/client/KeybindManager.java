package org.jon.magicclient.client;

import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3675.class_307;
import org.jon.magicclient.client.gui.modern.ModernClickGuiScreen;

public class KeybindManager {
   private static boolean keybindEnabled = false;
   private static class_304 guiKey;

   public static void initialize() {
      guiKey = new class_304("key.open_gui", class_307.field_1668, 71, "MagicClient");
   }

   public static void toggleKeybindMode() {
      boolean var10001 = keybindEnabled;
      System.out.println("[KeybindManager] toggleKeybindMode called - current state: " + var10001);
      keybindEnabled = !keybindEnabled;
      System.out.println("[KeybindManager] New state: " + keybindEnabled);
      MessageHelper msgHelper = new MessageHelper();
      if (keybindEnabled) {
         msgHelper.sendMessage("§aKeybind mode enabled! Press G to open GUI.", true);
         System.out.println("[KeybindManager] Keybind mode ENABLED");
      } else {
         msgHelper.sendMessage("§cKeybind mode disabled!", true);
         System.out.println("[KeybindManager] Keybind mode DISABLED");
      }

   }

   public static boolean isKeybindEnabled() {
      return keybindEnabled;
   }

   public static void onKeyInput(int key, int action) {
      System.out.println("[KeybindManager] Key input: " + key + ", action: " + action + ", enabled: " + keybindEnabled);
      if (keybindEnabled && class_310.method_1551().field_1724 != null) {
         if (key == 71 && action == 1) {
            try {
               System.out.println("[KeybindManager] Opening GUI with G key");
               class_310.method_1551().method_1507(new ModernClickGuiScreen());
               System.out.println("[KeybindManager] GUI opened successfully");
            } catch (Exception var3) {
               System.err.println("[KeybindManager] Failed to open GUI: " + var3.getMessage());
               var3.printStackTrace();
            }
         }

      } else {
         System.out.println("[KeybindManager] Key input ignored - not enabled or no player");
      }
   }
}
