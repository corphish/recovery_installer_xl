package com.tasohan.recoveryinstaller.NetUtils;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.tasohan.recoveryinstaller.CustomROMActivity;
import com.tasohan.recoveryinstaller.FlashUtils.FlashRecovery;
import com.tasohan.recoveryinstaller.R;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Avinaba on 8/26/2015.
 */
public class DownloadFile extends AsyncTask<String, Integer, String> {

    Context mContext;
    TextView status;
    String s_url;
    String recovery;
    private SharedPreferences.Editor pref;
    String ver;
    private static CustomROMActivity parent;

    private String LOG_TAG = "DownloadFile";

    public DownloadFile(Context context, TextView t, String s, String rec, SharedPreferences.Editor sharedPref, String v) {
        mContext = context;
        status = t;
        s_url = s;
        recovery = rec;
        pref = sharedPref;
        ver = v;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        status.setText(mContext.getResources().getString(R.string.downloading));
        status.setTextColor(mContext.getResources().getColor(R.color.black));
    }

    @Override
    protected String doInBackground(String... sUrl) {
        Log.i(LOG_TAG, "Downloading "+s_url);
        try {
            URL url = new URL(s_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();
            Log.i(LOG_TAG, "Length - " + fileLength + " Response Code - " + connection.getResponseCode());

                // download the file
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output;
                if (!recovery.equals("aromafm"))
                    output = new FileOutputStream("/sdcard/fota" + recovery + ".img");
                else
                    output = new FileOutputStream("/sdcard/aromafm.zip");
                Log.i(LOG_TAG, "Started");
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    //Log.i("Download:","Data "+ total);
                    // publishing the progress....
                    if (fileLength > 0) {// only if total length is known
                        publishProgress((int) total, fileLength);
                    }
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

                return "done";

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error");
            e.printStackTrace();
        }



        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Integer percent = (values[0] * 100) / values[1];
        status.setText(mContext.getResources().getString(R.string.downloading) + "(" + percent + "%)");
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(result != null) {
            status.setText(mContext.getResources().getString(R.string.downloaded));
            pref.putString("recovery", recovery);
            pref.putString("version", ver);
            pref.commit();
            new AlertDialog.Builder(mContext)
                    .setTitle(mContext.getResources().getString(R.string.flash_head))
                    .setMessage(mContext.getResources().getString(R.string.flash_msg))
                    .setPositiveButton(mContext.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            new FlashRecovery(mContext, status, recovery, pref, ver).execute("");
                        }
                    })
                    .setNegativeButton(mContext.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        } else {
            status.setTextColor(mContext.getResources().getColor(R.color.red));
            status.setText(mContext.getResources().getString(R.string.error));
        }
    }
}
