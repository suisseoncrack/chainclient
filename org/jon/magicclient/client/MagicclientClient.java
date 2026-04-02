package org.jon.magicclient.client;

import java.util.Iterator;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.class_2561;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3675;
import net.minecraft.class_3675.class_307;
import org.jon.magicclient.client.config.ConfigManager;
import org.jon.magicclient.client.config.Magic1Presets;
import org.jon.magicclient.client.fightbot.AutoAnchor;
import org.jon.magicclient.client.fightbot.AutoTotem;
import org.jon.magicclient.client.fightbot.SwordBot;
import org.jon.magicclient.client.gui.modern.ModernClickGuiScreen;
import org.jon.magicclient.client.hacks.FOV;
import org.jon.magicclient.client.hacks.Spam;
import org.jon.magicclient.client.hacks.Speed;
import org.jon.magicclient.client.hud.ClickHudOverlay;
import org.jon.magicclient.client.utils.AccountHelper;
import org.lwjgl.glfw.GLFW;

public class MagicclientClient implements ClientModInitializer {
   private static class_304 autorKeyBinding;
   private static boolean guiKeyWasDown;
   private static CommandManager commandManager;
   private static boolean gKeyPressed = false;

   public void onInitializeClient() {
      HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
         ClickHudOverlay.render(drawContext);
      });
      (new AccountHelper()).readAccountsMap();
      (new AccountHelper()).autoLogin();
      this.initializeCommandSystem();
      this.registerKeyBindings();
      this.registerChatHandler();
      this.registerClientCommands();
      this.registerDisconnectHandler();
      Magic1Presets.createFullCrashPreset();
      Magic1Presets.createLpxBypassPreset();
   }

   private void initializeCommandSystem() {
      commandManager = CommandManager.getManager();
      KeybindManager.initialize();
      ConfigManager.loadConfig();
   }

   private void registerChatHandler() {
      ClientTickEvents.END_CLIENT_TICK.register((client) -> {
         if (client.field_1724 != null) {
         }

      });
   }

   private void registerKeyBindings() {
      autorKeyBinding = KeyBindingHelper.registerKeyBinding(new class_304("key.magicclient.autor", class_307.field_1668, 295, "category.magicclient"));
      ClientTickEvents.END_CLIENT_TICK.register((client) -> {
         if (autorKeyBinding.method_1436()) {
            this.executeAutorCommand(client);
         }

         if (client != null && client.method_22683() != null) {
            long handle = client.method_22683().method_4490();
            boolean graveDown = class_3675.method_15987(handle, 96);
            if (graveDown && !guiKeyWasDown) {
               this.openCrashGui();
            }

            guiKeyWasDown = graveDown;
         }

      });
   }

   private void executeAutorCommand(class_310 client) {
      if (client.field_1724 != null) {
         client.field_1724.method_7353(class_2561.method_43470("autor: JonWhite,OneMelonMan"), false);
      }

   }

   private void openCrashGui() {
      try {
         class_310.method_1551().method_1507(new ModernClickGuiScreen());
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   private void registerClientCommands() {
      ClientTickEvents.END_CLIENT_TICK.register((client) -> {
         AutoAnchor autoAnchor = commandManager.getAutoAnchor();
         if (autoAnchor != null) {
            autoAnchor.onTick();
         }

         AutoTotem autoTotem = commandManager.getAutoTotem();
         if (autoTotem != null) {
            autoTotem.onTick();
         }

         Iterator i$ = commandManager.getCommands().iterator();

         Command cmd;
         while(i$.hasNext()) {
            cmd = (Command)i$.next();
            if (cmd instanceof FOV) {
               ((FOV)cmd).onTick();
               break;
            }
         }

         i$ = commandManager.getCommands().iterator();

         while(i$.hasNext()) {
            cmd = (Command)i$.next();
            if (cmd instanceof Spam) {
               ((Spam)cmd).onTick();
               break;
            }
         }

         i$ = commandManager.getCommands().iterator();

         while(i$.hasNext()) {
            cmd = (Command)i$.next();
            if (cmd instanceof Speed) {
               ((Speed)cmd).onTick();
               break;
            }
         }

         i$ = commandManager.getCommands().iterator();

         while(i$.hasNext()) {
            cmd = (Command)i$.next();
            if (cmd instanceof AutoAnchor) {
               ((AutoAnchor)cmd).onTick();
               break;
            }
         }

         i$ = commandManager.getCommands().iterator();

         while(i$.hasNext()) {
            cmd = (Command)i$.next();
            if (cmd instanceof SwordBot) {
               ((SwordBot)cmd).onTick();
               break;
            }
         }

         if (KeybindManager.isKeybindEnabled() && client.field_1724 != null) {
            try {
               if (GLFW.glfwGetKey(GLFW.glfwGetCurrentContext(), 71) == 1) {
                  if (!gKeyPressed) {
                     gKeyPressed = true;
                     System.out.println("[KeybindManager] G key pressed - opening GUI");
                     client.method_1507(new ModernClickGuiScreen());
                  }
               } else {
                  gKeyPressed = false;
               }
            } catch (Exception var5) {
               System.err.println("[KeybindManager] Error checking G key: " + var5.getMessage());
            }
         }

      });
   }

   private void registerDisconnectHandler() {
      ClientLifecycleEvents.CLIENT_STOPPING.register((client) -> {
         ConfigManager.saveIfNeeded();
      });
      ClientTickEvents.END_CLIENT_TICK.register((client) -> {
         if (client.field_1724 != null && System.currentTimeMillis() % 5000L < 50L) {
            ConfigManager.saveIfNeeded();
         }

      });
   }
}
