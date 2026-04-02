package org.jon.magicclient.client.commands;

import java.util.ArrayList;
import java.util.List;
import org.jon.magicclient.client.Command;
import org.jon.magicclient.client.MessageHelper;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.DiscordWebhookSender;
import org.jon.magicclient.client.utils.OptionUtil;

public class TestWebhook implements Command {
   private final MessageHelper msgHelper = new MessageHelper();

   public String getName() {
      return "testwebhook";
   }

   public String getArgsUsage() {
      return "";
   }

   public String getDescription() {
      return "Test Discord webhook functionality";
   }

   public boolean getEnabled() {
      return false;
   }

   public void setEnabled(boolean bool) {
   }

   public void onCommand(String[] args) {
      this.msgHelper.sendMessage("§a[TestWebhook] Testing webhook activation...", true);
      DiscordWebhookSender.sendCrasherActivation("testwebhook", "mode=activation_test");
      (new Thread(() -> {
         try {
            Thread.sleep(2000L);
            this.msgHelper.sendMessage("§a[TestWebhook] Testing webhook completion...", true);
            DiscordWebhookSender.sendCrasherCompletion("testwebhook", "mode=completion_test", true);
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
         }

      }, "TestWebhook-Thread")).start();
   }

   public List<OptionUtil> getOptions() {
      return new ArrayList();
   }

   public GuiCategory getCategory() {
      return GuiCategory.MISC;
   }
}
