package com.tasohan.recoveryinstaller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
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
    String ver;

    public FlashRecovery(Context context, TextView textView, String Recovery, SharedPreferences.Editor sharedPref, String v) {
        c = context;
        status = textView;
        pref = sharedPref;
        rc = Recovery;
        ver = v;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        status.setText(c.getResources().getString(R.string.installing));
        status.setTextColor(c.getResources().getColor(R.color.grey));
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
                    "dd if=sdcard/fota" + rc + ".img of=/dev/block/platform/msm_sdcc.1/by-name/FOTAKernel",
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
        status.setText(c.getResources().getString(R.string.installing));
        status.setTextColor(c.getResources().getColor(R.color.black));
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        status.setText(c.getResources().getString(R.string.reboot));
        status.setTextColor(c.getResources().getColor(R.color.red));
        Log.i("Flash Recovery","Installing" + rc);
        pref.putString("installed", rc);
        pref.putString("installed_version", ver);
        pref.commit();
    }
}
