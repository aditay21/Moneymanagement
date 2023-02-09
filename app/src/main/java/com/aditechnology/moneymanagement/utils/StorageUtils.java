package com.aditechnology.moneymanagement.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

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

   public static String getdata(Uri myfile,Context context) {
      InputStream fileInputStream=null;
      try {
          fileInputStream = context.getContentResolver().openInputStream(myfile);
         int i = -1;
         StringBuffer buffer = new StringBuffer();
         while ((i = fileInputStream.read()) != -1) {
            buffer.append((char) i);
         }
         return buffer.toString();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         if (fileInputStream != null) {
            try {
               fileInputStream.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
      return null;
   }
}

