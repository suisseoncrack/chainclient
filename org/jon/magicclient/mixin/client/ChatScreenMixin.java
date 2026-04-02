package org.jon.magicclient.mixin.client;

import java.util.Iterator;
import net.minecraft.class_310;
import net.minecraft.class_408;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.CommandManager;
import org.jon.magicclient.client.KeybindManager;
import org.jon.magicclient.client.commands.PluginsCommand;
import org.jon.magicclient.client.gui.modern.ModernClickGuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_408.class})
public class ChatScreenMixin {
   @Inject(
      method = {"method_44056"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onSendMessage(String message, boolean addToHistory, CallbackInfoReturnable<Boolean> cir) {
      if (message.startsWith("!")) {
         CommandManager.getManager().handleCommand(message);
         cir.setReturnValue(true);
         cir.cancel();
      }

      if (message.equalsIgnoreCase("!gui")) {
         try {
            class_310.method_1551().method_1507(new ModernClickGuiScreen());
            cir.setReturnValue(true);
            cir.cancel();
         } catch (Exception var11) {
            System.err.println("Failed to open GUI: " + var11.getMessage());
         }
      }

      if (message.equalsIgnoreCase("!key")) {
         try {
            KeybindManager.toggleKeybindMode();
            cir.setReturnValue(true);
            cir.cancel();
         } catch (Exception var10) {
            System.err.println("Failed to toggle keybind mode: " + var10.getMessage());
         }
      }

      if (message.startsWith("/") && message.contains(":")) {
         String command = message.substring(1);
         String[] parts = command.split(" ");
         if (parts.length > 0) {
            String namespacedCommand = parts[0];
            if (namespacedCommand.contains(":")) {
               String pluginName = namespacedCommand.split(":")[0];
               Iterator var8 = CommandManager.getManager().getCommands().iterator();

               while(var8.hasNext()) {
                  Command cmd = (Command)var8.next();
                  if (cmd.getName().equals("plugins")) {
                     ((PluginsCommand)cmd).addPlugin(pluginName);
                     break;
                  }
               }
            }
         }
      }

   }
}
