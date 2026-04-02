package org.jon.magicclient.utils;

import net.minecraft.class_2561;
import net.minecraft.class_2583;

public class FormattingUtils {
   public static class_2561 surroundWithObfuscated(class_2561 baseText, int count) {
      class_2583 baseStyle = baseText.method_10866().method_36141(false);
      class_2583 obfStyle = baseStyle.method_36141(true);
      String padding = "@".repeat(count);
      class_2561 obfuscatedLeft = class_2561.method_43470(padding + " ").method_10862(obfStyle);
      class_2561 obfuscatedRight = class_2561.method_43470(" " + padding).method_10862(obfStyle);
      class_2561 middle = baseText.method_27661().method_10862(baseStyle);
      return class_2561.method_43473().method_10852(obfuscatedLeft).method_10852(middle).method_10852(obfuscatedRight);
   }
}
