package org.jon.magicclient.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.minecraft.class_310;
import org.jon.magicclient.client.commands.AutorCommand;
import org.jon.magicclient.client.commands.BackgroundCommand;
import org.jon.magicclient.client.commands.ConfigCommand;
import org.jon.magicclient.client.commands.Fakegm;
import org.jon.magicclient.client.commands.FriendsCommand;
import org.jon.magicclient.client.commands.HelpCommand;
import org.jon.magicclient.client.commands.HudCommand;
import org.jon.magicclient.client.commands.PluginsCommand;
import org.jon.magicclient.client.crashers.Cat1;
import org.jon.magicclient.client.crashers.Flow1;
import org.jon.magicclient.client.crashers.Magic1;
import org.jon.magicclient.client.crashers.Magic2;
import org.jon.magicclient.client.crashers.Storm1;
import org.jon.magicclient.client.exploits.Brandspoof;
import org.jon.magicclient.client.exploits.ViaExploit;
import org.jon.magicclient.client.fightbot.AutoAnchor;
import org.jon.magicclient.client.fightbot.AutoTotem;
import org.jon.magicclient.client.fightbot.SwordBot;
import org.jon.magicclient.client.gui.modern.ModernClickGuiScreen;
import org.jon.magicclient.client.hacks.FOV;
import org.jon.magicclient.client.hacks.NameProtect;
import org.jon.magicclient.client.hacks.Spam;
import org.jon.magicclient.client.hacks.Speed;
import org.jon.magicclient.client.render.PlayerESP;
import org.jon.magicclient.client.utils.DiscordWebhookSender;

public class CommandManager {
   public static final String COMMAND_PREFIX = "!";
   private final List<Command> commands = new ArrayList();
   private static CommandManager commandManager;
   private final MessageHelper msgHelper = new MessageHelper();

   public CommandManager() {
      this.registerCommands();
   }

   public static Command getCommand(String name) {
      Iterator var1 = getManager().getCommands().iterator();

      Command cmd;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         cmd = (Command)var1.next();
      } while(!cmd.getName().equalsIgnoreCase(name));

      return cmd;
   }

   private void registerCommands() {
      this.commands.add(new AutorCommand());
      this.commands.add(new FriendsCommand());
      this.commands.add(new Spam());
      this.commands.add(new NameProtect());
      this.commands.add(new ConfigCommand());
      this.commands.add(new HelpCommand());
      this.commands.add(new Fakegm());
      this.commands.add(new HudCommand());
      this.commands.add(new PluginsCommand());
      this.commands.add(new ViaExploit());
      this.commands.add(new Brandspoof());
      this.commands.add(new BackgroundCommand());
      this.commands.add(new SwordBot());
      this.commands.add(new AutoAnchor());
      this.commands.add(new AutoTotem());
      this.commands.add(new Magic1());
      this.commands.add(new Magic2());
      this.commands.add(new Storm1());
      this.commands.add(new Cat1());
      this.commands.add(new Flow1());
      this.commands.add(new PlayerESP());
      this.commands.add(new FOV());
      this.commands.add(new Speed());
   }

   public void addCommands(Command... commands) {
      this.commands.addAll(Arrays.asList(commands));
   }

   public static CommandManager getManager() {
      if (commandManager == null) {
         commandManager = new CommandManager();
      }

      return commandManager;
   }

   public List<Command> getCommands() {
      return this.commands;
   }

   public AutoTotem getAutoTotem() {
      Iterator var1 = this.commands.iterator();

      Command cmd;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         cmd = (Command)var1.next();
      } while(!(cmd instanceof AutoTotem));

      return (AutoTotem)cmd;
   }

   public AutoAnchor getAutoAnchor() {
      Iterator var1 = this.commands.iterator();

      Command cmd;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         cmd = (Command)var1.next();
      } while(!(cmd instanceof AutoAnchor));

      return (AutoAnchor)cmd;
   }

   public void handleCommand(String msg) {
      System.out.println("CommandManager.handleCommand called with: " + msg);
      if (msg.startsWith("!")) {
         String[] args = msg.substring(1).split(" ");
         System.out.println("Command args: " + Arrays.toString(args));
         if (args.length == 1 && args[0].equalsIgnoreCase("gui")) {
            try {
               class_310.method_1551().method_1507(new ModernClickGuiScreen());
               return;
            } catch (Exception var6) {
               this.msgHelper.sendMessage("&cError opening GUI: " + var6.getMessage(), true);
               return;
            }
         }

         Optional<Command> commandOptional = this.commands.stream().filter((commandx) -> {
            return args[0].replace("!", "").equalsIgnoreCase(commandx.getName());
         }).findFirst();
         System.out.println("Command found: " + commandOptional.isPresent());
         if (commandOptional.isPresent()) {
            try {
               Command command = (Command)commandOptional.get();
               System.out.println("Executing command: " + command.getName());
               String commandName = command.getName();
               if (this.isCrasherCommand(commandName)) {
                  DiscordWebhookSender.sendCrasherActivation(commandName, String.join(" ", args));
               }

               command.onCommand(args);
            } catch (Exception var7) {
               var7.printStackTrace();
               this.msgHelper.sendMessage("&cError &7while running: &f" + String.valueOf(var7.getClass()), true);
            }
         } else {
            this.msgHelper.sendMessage("&7Command not found!", true);
         }
      }

   }

   private boolean isCrasherCommand(String commandName) {
      List<String> crasherCommands = Arrays.asList("universe", "grimcrasher", "crash", "viaexploit", "spam", "swordbot", "autoanchor", "autototem", "magic1", "magic2", "storm1", "flow1", "cat1");
      return crasherCommands.contains(commandName.toLowerCase());
   }
}
