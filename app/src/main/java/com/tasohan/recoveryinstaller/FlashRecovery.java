package com.tasohan.recoveryinstaller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Avinaba on 8/26/2015.
 */
public class FlashRecovery extends AsyncTask<String, Integer, String> {
    private TextView status;
    private SharedPreferences.Editor pref;
    private Context c;
    String rc;

    public FlashRecovery(Context context, TextView textView, String Recovery, SharedPreferences.Editor sharedPref) {
        c = context;
        status = textView;
        pref = sharedPref;
        rc = Recovery;
    }

    @Override
    protected String doInBackground(String... sUrl) {
        final Runtime runtime = Runtime.getRuntime();
        Process proc = null;
        String cmds[] = {"su",
                "dd if=sdcard/fota" + rc + ".img of=/dev/block/platform/msm_sdcc.1/by-name/FOTAKernel",
        };
        try {
            proc = runtime.exec("su");
            DataOutputStream os = new DataOutputStream(proc.getOutputStream());
            for (String tmpCmd : cmds) {
                os.writeBytes(tmpCmd + "\n");
                publishProgress();
            }
            os.writeBytes("reboot recovery\n");
            os.flush();
        } catch (IOException e) {


        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        status.setText(c.getResources().getString(R.string.installing));
        status.setTextColor(c.getResources().getColor(R.color.black));
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        status.setText(c.getResources().getString(R.string.installed));
        status.setTextColor(c.getResources().getColor(R.color.green));
        pref.putString("recovery", rc);
        pref.commit();
    }
}
