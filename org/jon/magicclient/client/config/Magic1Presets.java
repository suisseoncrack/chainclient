package org.jon.magicclient.client.config;

import java.io.File;
import net.minecraft.class_310;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.CommandManager;

public class Magic1Presets {
   public static void createFullCrashPreset() {
      Command magic1 = findMagic1();
      if (magic1 != null) {
         setOption(magic1, "Packets", 9999);
         setOption(magic1, "Size", 2096233);
         setOption(magic1, "Depth", 12222222);
         setOption(magic1, "Type", "Main2");
         savePreset("FullCrash");
      }
   }

   public static void createLpxBypassPreset() {
      Command magic1 = findMagic1();
      if (magic1 != null) {
         setOption(magic1, "Packets", 500);
         setOption(magic1, "Size", 50000);
         setOption(magic1, "Depth", 50);
         setOption(magic1, "Type", "Netty2");
         savePreset("LPXBypass");
      }
   }

   private static Command findMagic1() {
      return (Command)CommandManager.getManager().getCommands().stream().filter((c) -> {
         return c.getName().equalsIgnoreCase("Chain1");
      }).findFirst().orElse((Object)null);
   }

   private static void setOption(Command cmd, String name, Object value) {
      cmd.getOptions().stream().filter((o) -> {
         return o.getName().equalsIgnoreCase(name);
      }).findFirst().ifPresent((o) -> {
         o.setValue(value);
      });
   }

   private static void savePreset(String name) {
      File dir = new File(class_310.method_1551().field_1697, "config/chainclient/configs");
      if (!dir.exists()) {
         dir.mkdirs();
      }

      ConfigManager.saveConfig(new File(dir, name + ".json"));
   }
}
