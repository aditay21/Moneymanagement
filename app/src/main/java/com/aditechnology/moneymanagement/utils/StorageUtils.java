package com.aditechnology.moneymanagement.utils;

import android.os.Environment;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class StorageUtils {

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
   public static void writeTextData(File file, JSONObject data) {
      FileOutputStream fileOutputStream = null;
      try {
         fileOutputStream = new FileOutputStream(file);
         fileOutputStream.write(data.toString().getBytes());
        // Toast.makeText(, "Done" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         if (fileOutputStream != null) {
            try {
               fileOutputStream.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
   }
}
