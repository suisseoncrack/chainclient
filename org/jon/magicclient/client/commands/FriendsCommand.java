package org.jon.magicclient.client.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.class_310;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.MessageHelper;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.OptionUtil;

public class FriendsCommand implements Command {
   private static final Set<String> friends = new HashSet();
   private final MessageHelper msgHelper = new MessageHelper();
   private static final File FRIENDS_FILE;

   public FriendsCommand() {
      this.loadFriends();
   }

   public static boolean isFriend(String name) {
      return friends.contains(name.toLowerCase());
   }

   public String getName() {
      return "friends";
   }

   public void onCommand(String[] this) {
      if (args.length < 2) {
         this.msgHelper.sendMessage("§7Usage: .friends <add|remove|list> [name]", true);
      } else {
         String action = args[1].toLowerCase();
         String name;
         if (action.equals("add") && args.length >= 3) {
            name = args[2].toLowerCase();
            friends.add(name);
            this.saveFriends();
            this.msgHelper.sendMessage("§aAdded §f" + args[2] + " §ato friends.", true);
         } else if (action.equals("remove") && args.length >= 3) {
            name = args[2].toLowerCase();
            if (friends.remove(name)) {
               this.saveFriends();
               this.msgHelper.sendMessage("§aRemoved §f" + args[2] + " §afrom friends.", true);
            } else {
               this.msgHelper.sendMessage("§c" + args[2] + " is not in your friend list.", true);
            }
         } else if (action.equals("list")) {
            this.msgHelper.sendMessage("§6Friends: §f" + String.join(", ", friends), true);
         } else {
            this.msgHelper.sendMessage("§7Usage: .friends <add|remove|list> [name]", true);
         }

      }
   }

   private void loadFriends() {
      if (FRIENDS_FILE.exists()) {
         try {
            BufferedReader reader = new BufferedReader(new FileReader(FRIENDS_FILE));

            String line;
            try {
               while((line = reader.readLine()) != null) {
                  if (!line.trim().isEmpty()) {
                     friends.add(line.trim().toLowerCase());
                  }
               }
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
            var8.printStackTrace();
         }

      }
   }

   private void saveFriends() {
      if (!FRIENDS_FILE.getParentFile().exists()) {
         FRIENDS_FILE.getParentFile().mkdirs();
      }

      try {
         PrintWriter writer = new PrintWriter(new FileWriter(FRIENDS_FILE));

         try {
            Iterator v2 = friends.iterator();

            while(v2.hasNext()) {
               String friend = (String)v2.next();
               writer.println(friend);
            }
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
         var8.printStackTrace();
      }

   }

   public boolean getEnabled() {
      return true;
   }

   public void setEnabled(boolean this) {
   }

   public String getArgsUsage() {
      return "<add|remove|list> [name]";
   }

   public String getDescription() {
      return "Manages friend list for modules like FightBot";
   }

   public List<OptionUtil> getOptions() {
      return new ArrayList();
   }

   public GuiCategory getCategory() {
      return GuiCategory.HIDDEN;
   }

   static {
      FRIENDS_FILE = new File(class_310.method_1551().field_1697, "chainclient/friends.txt");
   }
}
