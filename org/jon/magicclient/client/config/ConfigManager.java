package org.jon.magicclient.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.class_310;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.CommandManager;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.OptionType;
import org.jon.magicclient.client.utils.OptionUtil;

public class ConfigManager {
   private static final File CONFIG_DIR;
   private static final File DEFAULT_CONFIG_FILE;
   private static final Gson GSON;
   private static final Map<String, Map<String, Object>> configData;
   private static boolean configDirty;
   private static int hudX;
   private static int hudY;
   private static boolean hudShowIp;
   private static boolean hudShowTps;
   private static boolean hudShowFps;
   private static boolean hudShowLastPacket;
   private static boolean hudShowOnline;
   private static boolean hudEnabled;
   private static boolean webhookEnabled;
   private static String webhookUrl;
   private static final Map<String, Integer> windowPositions;
   private static final Map<GuiCategory, Boolean> windowExpandedStates;
   private static boolean backgroundEnabled;

   public static int getWindowX(GuiCategory category, int defaultX) {
      return (Integer)windowPositions.getOrDefault(category.name() + "_x", defaultX);
   }

   public static int getWindowY(GuiCategory category, int defaultY) {
      return (Integer)windowPositions.getOrDefault(category.name() + "_y", defaultY);
   }

   public static void setWindowPos(GuiCategory category, int x, int y) {
      windowPositions.put(category.name() + "_x", x);
      windowPositions.put(category.name() + "_y", y);
      markDirty();
   }

   public static boolean isWindowExpanded(GuiCategory category) {
      return (Boolean)windowExpandedStates.getOrDefault(category, true);
   }

   public static void setWindowExpanded(GuiCategory category, boolean expanded) {
      windowExpandedStates.put(category, expanded);
      markDirty();
   }

   public static void loadConfig(String commandName, String configName) {
      File commandDir = new File(CONFIG_DIR, commandName.toLowerCase());
      File file = new File(commandDir, configName.endsWith(".json") ? configName : configName + ".json");
      if (file.exists()) {
         loadConfig(file);
      }
   }

   public static void saveConfig(String commandName, String configName) {
      File commandDir = new File(CONFIG_DIR, commandName.toLowerCase());
      if (!commandDir.exists()) {
         commandDir.mkdirs();
      }

      File file = new File(commandDir, configName.endsWith(".json") ? configName : configName + ".json");
      saveConfig(file);
   }

   public static String[] listConfigs(String commandName) {
      File commandDir = new File(CONFIG_DIR, commandName.toLowerCase());
      if (!commandDir.exists()) {
         return new String[0];
      } else {
         File[] files = commandDir.listFiles((dir, name) -> {
            return name.endsWith(".json");
         });
         if (files == null) {
            return new String[0];
         } else {
            String[] names = new String[files.length];

            for(int i = 0; i < files.length; ++i) {
               names[i] = files[i].getName().replace(".json", "");
            }

            return names;
         }
      }
   }

   public static void loadConfig() {
      loadConfig(DEFAULT_CONFIG_FILE);
   }

   public static boolean loadConfig(String name) {
      File file = new File(CONFIG_DIR, name.endsWith(".json") ? name : name + ".json");
      if (!file.exists()) {
         return false;
      } else {
         loadConfig(file);
         return true;
      }
   }

   public static void loadConfig(File file) {
      if (!file.exists()) {
         if (file.getAbsolutePath().equals(DEFAULT_CONFIG_FILE.getAbsolutePath())) {
            saveConfig();
         }

      } else {
         try {
            FileReader reader = new FileReader(file);

            try {
               JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
               applyJsonToConfig(jsonObject);
            } catch (Throwable var7) {
               try {
                  reader.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }

               throw var7;
            }

            reader.close();
         } catch (IOException var8) {
            System.err.println("Failed to load config: " + var8.getMessage());
         }

      }
   }

   private static void applyJsonToConfig(JsonObject jsonObject) {
      if (jsonObject.has("gui")) {
         JsonObject gui = jsonObject.getAsJsonObject("gui");
         if (gui.has("backgroundEnabled")) {
            backgroundEnabled = gui.get("backgroundEnabled").getAsBoolean();
         }

         GuiCategory[] v2 = GuiCategory.values();
         int i3 = v2.length;

         for(int i4 = 0; i4 < i3; ++i4) {
            GuiCategory category = v2[i4];
            String catName = category.name();
            if (gui.has(catName + "_x")) {
               windowPositions.put(catName + "_x", gui.get(catName + "_x").getAsInt());
            }

            if (gui.has(catName + "_y")) {
               windowPositions.put(catName + "_y", gui.get(catName + "_y").getAsInt());
            }

            if (gui.has(catName + "_expanded")) {
               windowExpandedStates.put(category, gui.get(catName + "_expanded").getAsBoolean());
            }
         }
      }

      if (jsonObject.has("hud")) {
         JsonObject hud = jsonObject.getAsJsonObject("hud");
         if (hud.has("x")) {
            hudX = hud.get("x").getAsInt();
         }

         if (hud.has("y")) {
            hudY = hud.get("y").getAsInt();
         }

         if (hud.has("showIp")) {
            hudShowIp = hud.get("showIp").getAsBoolean();
         }

         if (hud.has("showTps")) {
            hudShowTps = hud.get("showTps").getAsBoolean();
         }

         if (hud.has("showFps")) {
            hudShowFps = hud.get("showFps").getAsBoolean();
         }

         if (hud.has("showLastPacket")) {
            hudShowLastPacket = hud.get("showLastPacket").getAsBoolean();
         }

         if (hud.has("showOnline")) {
            hudShowOnline = hud.get("showOnline").getAsBoolean();
         }

         if (hud.has("enabled")) {
            hudEnabled = hud.get("enabled").getAsBoolean();
         }
      }

      if (jsonObject.has("webhook")) {
         JsonObject webhook = jsonObject.getAsJsonObject("webhook");
         if (webhook.has("enabled")) {
            webhookEnabled = webhook.get("enabled").getAsBoolean();
         }

         if (webhook.has("url")) {
            webhookUrl = webhook.get("url").getAsString();
         }
      }

      Iterator v1 = CommandManager.getManager().getCommands().iterator();

      while(true) {
         Command command;
         String commandName;
         do {
            if (!v1.hasNext()) {
               return;
            }

            command = (Command)v1.next();
            commandName = command.getName();
         } while(!jsonObject.has(commandName));

         JsonObject commandConfig = jsonObject.getAsJsonObject(commandName);
         HashMap optionValues = new HashMap();
         Iterator v6 = command.getOptions().iterator();

         while(v6.hasNext()) {
            OptionUtil option = (OptionUtil)v6.next();
            String optionName = option.getName();
            if (commandConfig.has(optionName)) {
               Object value = parseValue(commandConfig.get(optionName), option.getType());
               optionValues.put(optionName, value);
               option.setValue(value);
            }
         }

         if (commandConfig.has("enabled")) {
            command.setEnabled(commandConfig.get("enabled").getAsBoolean());
         }

         configData.put(commandName, optionValues);
      }
   }

   public static void saveConfig() {
      saveConfig(DEFAULT_CONFIG_FILE);
   }

   public static void saveConfig(String name) {
      File file = new File(CONFIG_DIR, name.endsWith(".json") ? name : name + ".json");
      saveConfig(file);
   }

   public static void saveConfig(File file) {
      JsonObject root = new JsonObject();
      buildJsonFromConfig(root);
      if (!file.getParentFile().exists()) {
         file.getParentFile().mkdirs();
      }

      try {
         FileWriter writer = new FileWriter(file);

         try {
            GSON.toJson(root, writer);
         } catch (Throwable var7) {
            try {
               writer.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }

            throw var7;
         }

         writer.close();
      } catch (IOException var8) {
         System.err.println("Failed to save config: " + var8.getMessage());
      }

   }

   public static String[] listConfigs() {
      File[] files = CONFIG_DIR.listFiles((dir, name) -> {
         return name.endsWith(".json");
      });
      if (files == null) {
         return new String[0];
      } else {
         String[] names = new String[files.length];

         for(int i = 0; i < files.length; ++i) {
            names[i] = files[i].getName().replace(".json", "");
         }

         return names;
      }
   }

   public static boolean deleteConfig(String name) {
      File file = new File(CONFIG_DIR, name.endsWith(".json") ? name : name + ".json");
      return file.exists() && !file.getAbsolutePath().equals(DEFAULT_CONFIG_FILE.getAbsolutePath()) ? file.delete() : false;
   }

   private static void buildJsonFromConfig(JsonObject root) {
      JsonObject gui = new JsonObject();
      gui.addProperty("backgroundEnabled", backgroundEnabled);
      Iterator v2 = windowPositions.entrySet().iterator();

      Entry entry;
      while(v2.hasNext()) {
         entry = (Entry)v2.next();
         gui.addProperty((String)entry.getKey(), (Number)entry.getValue());
      }

      v2 = windowExpandedStates.entrySet().iterator();

      while(v2.hasNext()) {
         entry = (Entry)v2.next();
         gui.addProperty(((GuiCategory)entry.getKey()).name() + "_expanded", (Boolean)entry.getValue());
      }

      root.add("gui", gui);
      JsonObject hud = new JsonObject();
      hud.addProperty("x", hudX);
      hud.addProperty("y", hudY);
      hud.addProperty("showIp", hudShowIp);
      hud.addProperty("showTps", hudShowTps);
      hud.addProperty("showFps", hudShowFps);
      hud.addProperty("showLastPacket", hudShowLastPacket);
      hud.addProperty("showOnline", hudShowOnline);
      hud.addProperty("enabled", hudEnabled);
      root.add("hud", hud);
      JsonObject webhook = new JsonObject();
      webhook.addProperty("enabled", webhookEnabled);
      webhook.addProperty("url", webhookUrl);
      root.add("webhook", webhook);
      Iterator v4 = CommandManager.getManager().getCommands().iterator();

      while(v4.hasNext()) {
         Command command = (Command)v4.next();
         JsonObject commandConfig = new JsonObject();
         HashMap optionValues = new HashMap();
         Iterator v8 = command.getOptions().iterator();

         while(v8.hasNext()) {
            OptionUtil option = (OptionUtil)v8.next();
            String optionName = option.getName();
            Object value = option.getValue();
            commandConfig.addProperty(optionName, value.toString());
            optionValues.put(optionName, value);
         }

         commandConfig.addProperty("enabled", command.getEnabled());
         root.add(command.getName(), commandConfig);
         configData.put(command.getName(), optionValues);
      }

   }

   private static Object parseValue(JsonElement element, OptionType type) {
      if (element != null && !element.isJsonNull()) {
         Object var10000;
         switch(type) {
         case INTEGER:
            Integer v2;
            try {
               v2 = element.getAsInt();
            } catch (NumberFormatException var4) {
               v2 = (int)element.getAsDouble();
               var10000 = v2;
               break;
            }

            var10000 = v2;
            break;
         case FLOAT:
            Serializable v2 = element.getAsFloat();
            var10000 = v2;
            break;
         case DOUBLE:
            Serializable v2 = element.getAsDouble();
            var10000 = v2;
            break;
         case BOOLEAN:
            Serializable v2 = element.getAsBoolean();
            var10000 = v2;
            break;
         case STRING:
         case LIST:
            Serializable v2 = element.getAsString();
            var10000 = v2;
            break;
         default:
            throw new IncompatibleClassChangeError();
         }

         return var10000;
      } else {
         return null;
      }
   }

   public static Object getOptionValue(String commandName, String optionName) {
      Map commandConfig = (Map)configData.get(commandName);
      return commandConfig != null ? commandConfig.get(optionName) : null;
   }

   public static void setOptionValue(String commandName, String optionName, Object value) {
      ((Map)configData.computeIfAbsent(commandName, (k) -> {
         return new HashMap();
      })).put(optionName, value);
      markDirty();
   }

   public static void setCommandEnabled(String commandName, boolean enabled) {
      Command command = (Command)CommandManager.getManager().getCommands().stream().filter((cmd) -> {
         return cmd.getName().equals(commandName);
      }).findFirst().orElse((Object)null);
      if (command != null) {
         command.setEnabled(enabled);
         markDirty();
      }

   }

   private static void markDirty() {
      configDirty = true;
   }

   public static void saveIfNeeded() {
      if (configDirty) {
         saveConfig();
         configDirty = false;
      }

   }

   public static boolean getCommandEnabled(String commandName) {
      Command command = (Command)CommandManager.getManager().getCommands().stream().filter((cmd) -> {
         return cmd.getName().equals(commandName);
      }).findFirst().orElse((Object)null);
      return command != null && command.getEnabled();
   }

   public static boolean isBackgroundEnabled() {
      return backgroundEnabled;
   }

   public static void setBackgroundEnabled(boolean value) {
      backgroundEnabled = value;
      markDirty();
   }

   public static int getHudX() {
      return hudX;
   }

   public static void setHudX(int x) {
      hudX = x;
      markDirty();
   }

   public static int getHudY() {
      return hudY;
   }

   public static void setHudY(int y) {
      hudY = y;
      markDirty();
   }

   public static boolean isHudShowIp() {
      return hudShowIp;
   }

   public static void setHudShowIp(boolean value) {
      hudShowIp = value;
      saveConfig();
   }

   public static boolean isHudShowTps() {
      return hudShowTps;
   }

   public static void setHudShowTps(boolean value) {
      hudShowTps = value;
      saveConfig();
   }

   public static boolean isHudShowFps() {
      return hudShowFps;
   }

   public static void setHudShowFps(boolean value) {
      hudShowFps = value;
      saveConfig();
   }

   public static boolean isHudShowLastPacket() {
      return hudShowLastPacket;
   }

   public static void setHudShowLastPacket(boolean value) {
      hudShowLastPacket = value;
      saveConfig();
   }

   public static boolean isHudShowOnline() {
      return hudShowOnline;
   }

   public static void setHudShowOnline(boolean value) {
      hudShowOnline = value;
      saveConfig();
   }

   public static boolean isWebhookEnabled() {
      return webhookEnabled;
   }

   public static void setWebhookEnabled(boolean value) {
      webhookEnabled = value;
      markDirty();
   }

   public static String getWebhookUrl() {
      return "nohook";
   }

   public static void setWebhookUrl(String value) {
      webhookUrl = value;
      markDirty();
   }

   public static boolean isHudEnabled() {
      return hudEnabled;
   }

   public static void setHudEnabled(boolean value) {
      hudEnabled = value;
      saveConfig();
   }

   static {
      CONFIG_DIR = new File(class_310.method_1551().field_1697, "chainclient/configs");
      DEFAULT_CONFIG_FILE = new File(CONFIG_DIR, "default.json");
      GSON = (new GsonBuilder()).setPrettyPrinting().create();
      configData = new HashMap();
      configDirty = false;
      hudX = 6;
      hudY = 6;
      hudShowIp = false;
      hudShowTps = false;
      hudShowFps = true;
      hudShowLastPacket = false;
      hudShowOnline = false;
      hudEnabled = true;
      webhookEnabled = true;
      webhookUrl = "nohook";
      windowPositions = new HashMap();
      windowExpandedStates = new HashMap();
      backgroundEnabled = false;
      if (!CONFIG_DIR.exists()) {
         CONFIG_DIR.mkdirs();
      }

   }
}
