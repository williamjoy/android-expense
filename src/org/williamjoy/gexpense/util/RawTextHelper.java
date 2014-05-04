package org.williamjoy.gexpense.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

public class RawTextHelper {
 
    public static String getRawTextFromResource(Context c,int id){
    	InputStream inputStream = c.getResources().openRawResource(id);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
            return byteArrayOutputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
    		return e.toString();
        }
    }
}
