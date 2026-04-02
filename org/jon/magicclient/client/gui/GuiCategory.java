package org.jon.magicclient.client.gui;

public enum GuiCategory {
   COMBAT("COMBAT"),
   PLAYER("PLAYER"),
   MOVEMENT("MOVEMENT"),
   RENDER("RENDER"),
   WORLD("WORLD"),
   CRASHES("CRASHES"),
   EXPLOITS("EXPLOITS"),
   MISC("MISC"),
   HIDDEN("HIDDEN");

   private final String displayName;

   private GuiCategory(String displayName) {
      this.displayName = displayName;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   // $FF: synthetic method
   private static GuiCategory[] $values() {
      return new GuiCategory[]{COMBAT, PLAYER, MOVEMENT, RENDER, WORLD, CRASHES, EXPLOITS, MISC, HIDDEN};
   }
}
