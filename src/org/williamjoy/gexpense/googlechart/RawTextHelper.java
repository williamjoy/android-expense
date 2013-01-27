package org.williamjoy.gexpense.googlechart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.williamjoy.gexpense.R;

import android.content.Context;

public class RawTextHelper {

    private String rawText;

    public RawTextHelper(Context c) {
        InputStream inputStream = c.getResources().openRawResource(
                R.raw.charthtml);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
            rawText = byteArrayOutputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getRawText() {
        return rawText;
    }

}
