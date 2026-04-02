package org.jon.magicclient.client.utils;

public class OptionUtil {
   private final String name;
   private final OptionType type;
   private Object value;
   private final Object[] listOptions;

   public OptionUtil(String name, OptionType type) {
      this.name = name;
      this.type = type;
      this.value = this.getDefaultValue(type);
      this.listOptions = null;
   }

   public OptionUtil(String name, OptionType type, Object[] listOptions) {
      this.name = name;
      this.type = type;
      this.value = listOptions != null && listOptions.length > 0 ? listOptions[0] : this.getDefaultValue(type);
      this.listOptions = listOptions;
   }

   private Object getDefaultValue(OptionType type) {
      Object var10000;
      switch(type) {
      case INTEGER:
         var10000 = 1;
         break;
      case FLOAT:
         var10000 = 1.0F;
         break;
      case DOUBLE:
         var10000 = 1.0D;
         break;
      case BOOLEAN:
         var10000 = false;
         break;
      case STRING:
         var10000 = "";
         break;
      case LIST:
         var10000 = "";
         break;
      default:
         throw new IncompatibleClassChangeError();
      }

      return var10000;
   }

   public String getName() {
      return this.name;
   }

   public OptionType getType() {
      return this.type;
   }

   public Object getValue() {
      return this.value;
   }

   public void setValue(Object value) {
      this.value = value;
   }

   public Object[] getListOptions() {
      return this.listOptions;
   }

   public int getIntValue() {
      return this.value instanceof Number ? ((Number)this.value).intValue() : 1;
   }

   public float getFloatValue() {
      return this.value instanceof Number ? ((Number)this.value).floatValue() : 1.0F;
   }

   public double getDoubleValue() {
      return this.value instanceof Number ? ((Number)this.value).doubleValue() : 1.0D;
   }

   public boolean getBooleanValue() {
      return this.value instanceof Boolean ? (Boolean)this.value : false;
   }

   public String getStringValue() {
      return this.value != null ? this.value.toString() : "";
   }
}
