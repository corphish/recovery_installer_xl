package com.tasohan.recoveryinstaller;

import android.os.AsyncTask;
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
public class FlashStockRecovery extends AsyncTask<String, Integer, String> {
    private TextView mStatus;
    private Context mContext;
    String mRecovery;

    public FlashStockRecovery(Context context, TextView textView, String Recovery) {
        mContext = context;
        mStatus = textView;
        mRecovery = Recovery;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        mStatus.setText(mContext.getResources().getString(R.string.installing));
        mStatus.setTextColor(mContext.getResources().getColor(R.color.grey));
    }

    @Override
    protected String doInBackground(String... sUrl) {
        final Runtime runtime = Runtime.getRuntime();
        Process proc = null;
        String cmds[] = {"mkdir /data/local/tmp/cwm",
                          "cat sdcard/recovery.sh >  /data/local/tmp/cwm/recovery.sh",
                          "cat sdcard/e2fsck.sh > /data/local/tmp/cwm/e2fsck.sh",
                          "cat sdcard/"+mRecovery+".tar > /data/local/tmp/cwm/recovery.tar",
                          "cat sdcard/busybox > /data/local/tmp/cwm/busybox",
                          "cat sdcard/step3.sh > /data/local/tmp/cwm/step3.sh",
                          "chmod 755 /data/local/tmp/cwm/busybox",
                          "chmod 755 /data/local/tmp/cwm/step3.sh",
                          "su -c /data/local/tmp/cwm/step3.sh",
                          "rm -r /data/local/tmp/cwm"};

        try {
            proc = runtime.exec("su");
            DataOutputStream os = new DataOutputStream(proc.getOutputStream());
            for (String tmpCmd : cmds) {
                os.writeBytes(tmpCmd + "\n");
                publishProgress();
            }
            os.writeBytes("reboot\n");
            os.flush();
        } catch (IOException e) {


        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        mStatus.setText(mContext.getResources().getString(R.string.installing));
        mStatus.setTextColor(mContext.getResources().getColor(R.color.black));
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        SharedPreferences.Editor pref = mContext.getSharedPreferences("recovery", mContext.MODE_PRIVATE).edit();
        pref.putString("recovery",mRecovery);
        pref.commit();
        mStatus.setText(mContext.getResources().getString(R.string.reboot));
        mStatus.setTextColor(mContext.getResources().getColor(R.color.red));
        Log.i("Flash Recovery","Installing" + mRecovery);

    }
}
