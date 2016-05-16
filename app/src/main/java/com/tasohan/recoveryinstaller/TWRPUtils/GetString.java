package com.tasohan.recoveryinstaller.TWRPUtils;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Avinaba on 5/16/2016.
 */
public class GetString {

    public static String TWRP_Variables_URL = "https://raw.githubusercontent.com/omnirom/android_bootable_recovery/android-6.0/variables.h";
    public static String TWRP_Version_Identifer = "TW_VERSION_STR";

    public static String getLatestVersion () {
        // @BadSkillz codes with same changes
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(TWRP_Variables_URL);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            BufferedHttpEntity buf = new BufferedHttpEntity(entity);

            InputStream is = buf.getContent();

            BufferedReader r = new BufferedReader(new InputStreamReader(is));


            String line,result = null;
            while ((line = r.readLine()) != null) {
                if (line.contains(TWRP_Version_Identifer)) {
                    String[] parts = line.split(" ");
                    result = parts[parts.length -1];
                    break;
                }
            }
            Log.i("Get URL", "Downloaded string: " + formatVersion(result));
            return formatVersion(result);
        } catch (Exception e) {
            Log.e("Get Url", "Error in downloading: " + e.toString());
        }
        return null;

    }
    public static String formatVersion(String string) {
        return string.substring(1,string.length() - 1);
    }
}
