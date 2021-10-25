package com.capstone.catstone_eatmorning;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ImageStorage {
    public static String encodeImage(Drawable image){
        String simage = "";
        Bitmap bitmap = ((BitmapDrawable)image).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] bimage = stream.toByteArray();
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ; i < bimage.length; ++i){
            sb.append(byteToBinaryString(bimage[i]));
        }
        return sb.toString();
    }
    public static String byteToBinaryString(byte n){
        StringBuilder sb = new StringBuilder("00000000");
        for(int bit = 0 ; bit < 8 ; bit++){
            if(((n>>bit) & 1) > 0){
                sb.setCharAt(7-bit,'1');
            }
        }
        return sb.toString();
    }
    public static byte[] binaryStringToByteArray(String s) {
        int count = s.length() / 8;
        byte[] b = new byte[count];
        for (int i = 1; i < count; ++i) {
            String t = s.substring((i - 1) * 8, i * 8);
            b[i - 1] = binaryStringToByte(t);
        }
        return b;
    }
    public static byte binaryStringToByte(String s){
        byte ret = 0, total = 0;
        for(int i = 0 ; i < 8 ; i++){
            ret = (s.charAt(7-i) == '1')? (byte)(1<< i):0;
            total = (byte) (ret | total);
        }
        return total;
    }

    public static Drawable decodeImage(String name,String string){
        byte[] b = binaryStringToByteArray(string);
        ByteArrayInputStream is = new ByteArrayInputStream(b);
        Drawable image = Drawable.createFromStream(is,name);
        return image;
    }
}
