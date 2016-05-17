package com.tasohan.recoveryinstaller.FlashUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.tasohan.recoveryinstaller.R;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Avinaba on 8/26/2015.
 */
public class FlashRecovery extends AsyncTask<String, Integer, String> {
    private TextView status = null;
    private SharedPreferences.Editor pref = null;
    private Context c = null;
    String rc = null;
    String ver = null;
    String path = null;

    public FlashRecovery(Context context, TextView textView, String Recovery, SharedPreferences.Editor sharedPref, String v) {
        c = context;
        status = textView;
        pref = sharedPref;
        rc = Recovery;
        ver = v;
        path = "/sdcard/fota"+rc+".img";
    }
    public FlashRecovery(Context context,String rec,SharedPreferences.Editor ed) {
        c = context;
        rc = rec;
        pref = ed;
        path = rc;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        if(status != null) {
            status.setText(c.getResources().getString(R.string.installing));
            status.setTextColor(c.getResources().getColor(R.color.grey));
        }
    }

    @Override
    protected String doInBackground(String... sUrl) {
        final Runtime runtime = Runtime.getRuntime();
        Process proc = null;
        String cmds[];
        if(rc.equals("aromafm")) {
            String cmd[] = {"su",
                        "mkdir -p /cache/recovery/",
                        "rm -f /cache/recovery/command",
                        "rm -f /cache/recovery/extendedcommand",
                        "echo 'boot-recovery' >> /cache/recovery/command",
                        "echo '--update_package=/sdcard/aromafm.zip' >> /cache/recovery/command",
                         };
            cmds = cmd;
        }
        else {
            String cmd[] = {"su",
                    "dd if="+path + " of=/dev/block/platform/msm_sdcc.1/by-name/FOTAKernel",
            };
            cmds = cmd;
        }
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
        if(status != null) {
            status.setText(c.getResources().getString(R.string.installing));
            status.setTextColor(c.getResources().getColor(R.color.black));
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(status != null) {
            status.setText(c.getResources().getString(R.string.reboot));
            status.setTextColor(c.getResources().getColor(R.color.red));
        }
        Log.i("Flash Recovery","Installing " + rc);
        if(pref != null) {
            pref.putString("installed", rc);
            pref.putString("installed_version", ver);
            pref.commit();
        }
    }
}
