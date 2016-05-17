package com.tasohan.recoveryinstaller.NetUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.tasohan.recoveryinstaller.FlashUtils.FlashStockRecovery;
import com.tasohan.recoveryinstaller.R;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Avinaba on 11/13/2015.
 */
public class DownloadStockFile extends AsyncTask<String, Integer, String> {

    Context mContext;
    TextView mStatus;
    String mUrl;
    String mFilename;

    public DownloadStockFile(Context context, TextView status, String url, String filename) {
        mContext = context;
        mStatus = status;
        mUrl = url;
        mFilename = filename;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        mStatus.setText(mContext.getResources().getString(R.string.downloading));
        mStatus.setTextColor(mContext.getResources().getColor(R.color.black));
    }

    @Override
    protected String doInBackground(String... sUrl) {
        Log.i("Download:", "Initiated");
        //connection = null;
        try {
            int c = sUrl.length;
            URL url = new URL(mUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
        /*if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            Log.i("Download:","Server error");
        } else {
            Log.i("Download:","Server connect");
        }*/

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();
            Log.i("Download:", "Length" + fileLength);
            // download the file
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output;
            output = new FileOutputStream("/sdcard/"+mFilename);
            Log.i("Download:", "Started");
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
            Log.e("Download", "Error");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Integer percent = (values[0] * 100) / values[1];
        mStatus.setText(mContext.getResources().getString(R.string.downloading) + "(" + percent + "%)");
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(result != null) {
            mStatus.setText(mContext.getResources().getString(R.string.downloaded));
            if (mFilename.contains("tar")) /*lolz */ {
                new AlertDialog.Builder(mContext)
                        .setTitle(mContext.getResources().getString(R.string.flash_head))
                        .setMessage(mContext.getResources().getString(R.string.flash_msg))
                        .setPositiveButton(mContext.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String recovery="";
                                if(mFilename.contains("twrp"))
                                    recovery="twrp";
                                if(mFilename.contains("cwm"))
                                    recovery="cwm";
                                if(mFilename.contains("twrp"))
                                    recovery="philz";
                                new FlashStockRecovery(mContext,mStatus,recovery).execute();
                            }
                        })
                        .setNegativeButton(mContext.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        }   else {
                mStatus.setTextColor(mContext.getResources().getColor(R.color.red));
                mStatus.setText(mContext.getResources().getString(R.string.error));
            }
    }

}
