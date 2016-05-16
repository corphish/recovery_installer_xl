package com.tasohan.recoveryinstaller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Avinaba on 8/26/2015.
 */
public class GetRecoveryVersion extends AsyncTask<String, Integer, String> {

    Context mContext;
    TextView version;
    TextView status;
    String recovery;
    SharedPreferences pref;

    public GetRecoveryVersion(Context context,TextView textView_version, TextView textView_status, String rec, SharedPreferences p) {
        mContext = context;
        version = textView_version;
        status = textView_status;
        recovery = rec;
        pref = p;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        version.setText(mContext.getResources().getString(R.string.fetch_version));
        status.setText(mContext.getResources().getString(R.string.fetch_version));
        version.setTextColor(mContext.getResources().getColor(R.color.grey));
        status.setTextColor(mContext.getResources().getColor(R.color.grey));
    }

    @Override
    protected String doInBackground(String... sUrl) {
        if(recovery.equals("twrp"))
            return com.tasohan.recoveryinstaller.TWRPUtils.GetString.getLatestVersion();
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            // The default value is zero, that means the timeout is not used.
            int timeoutConnection = 30000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            // Set the default socket timeout (SO_TIMEOUT)
            // in milliseconds which is the timeout for waiting for data.
            int timeoutSocket = 50000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            HttpGet httpGet = new HttpGet("https://raw.githubusercontent.com/SdtBarbarossa/recovery_installer_xl_cache/master/"+recovery+"/version");
            //Log.i("ota","https://raw.githubusercontent.com/corphish/Gesture_Control/master/"+cur_dev+"/dl"+flg);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            BufferedHttpEntity buf = new BufferedHttpEntity(entity);

            InputStream is = buf.getContent();

            BufferedReader r = new BufferedReader(new InputStreamReader(is));

            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
                this.publishProgress();
            }
            String result = total.toString();
            Log.i("Get URL", "Downloaded string: " + result);
            return result;
        } catch (Exception e) {
            Log.e("Get Url", "Error in downloading: " + e.toString());
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        version.setText(mContext.getResources().getString(R.string.fetch_version));
        version.setTextColor(mContext.getResources().getColor(R.color.grey));
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(result != null) {
            SharedPreferences.Editor ed = mContext.getSharedPreferences("local_recovery",Context.MODE_PRIVATE).edit();
            ed.putString(recovery,result);
            ed.commit();
            version.setText(result);
            version.setTextColor(mContext.getResources().getColor(R.color.black));
        } else {
            SharedPreferences preferences = mContext.getSharedPreferences("local_recovery", Context.MODE_PRIVATE);
            if (!preferences.getString(recovery, "").equals("")) {
                version.setText(preferences.getString(recovery, ""));

            } else {
                version.setText(mContext.getResources().getString(R.string.error));
                version.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        }



        if(pref.getString("installed","").equals(recovery)) {
            status.setText(mContext.getResources().getString(R.string.installed));
            status.setTextColor(mContext.getResources().getColor(R.color.green));
            if(!pref.getString("installed_version","").equals(result) && result != null) {
                status.setText(mContext.getResources().getString(R.string.update));
                status.setTextColor(mContext.getResources().getColor(R.color.blue));
            }
        } else {
            status.setTextColor(mContext.getResources().getColor(R.color.grey));
            File file = new File("sdcard/fota" + recovery + ".img");
            File aroma = new File("sdcard/aromafm.zip");
            if (recovery.equals("aromafm")) {
                if(aroma.exists())
                    status.setText(mContext.getResources().getString(R.string.downloaded));
                else
                    status.setText(mContext.getResources().getString(R.string.not_installed));

            } else {
                if (file.exists())
                    status.setText(mContext.getResources().getString(R.string.downloaded));
                else
                    status.setText(mContext.getResources().getString(R.string.not_installed));
            }
        }
    }
}
