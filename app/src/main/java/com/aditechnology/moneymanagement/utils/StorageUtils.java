package com.aditechnology.moneymanagement.utils;

import android.os.Environment;

class StorageUtils {

   private static boolean isExternalStorageReadOnly() {
      String extStorageState = Environment.getExternalStorageState();
      if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
         return true;
      }
      return false;
   }

   private static boolean isExternalStorageAvailable() {
      String extStorageState = Environment.getExternalStorageState();
      if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
         return true;
      }
      return false;
   }
}
