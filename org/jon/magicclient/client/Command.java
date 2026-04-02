package org.jon.magicclient.client;

import java.util.List;
import org.jon.magicclient.client.gui.GuiCategory;
import org.jon.magicclient.client.utils.OptionUtil;

public interface Command {
   String getName();

   void onCommand(String[] var1);

   boolean getEnabled();

   void setEnabled(boolean var1);

   String getArgsUsage();

   String getDescription();

   List<OptionUtil> getOptions();

   default GuiCategory getCategory() {
      return GuiCategory.MISC;
   }
}
