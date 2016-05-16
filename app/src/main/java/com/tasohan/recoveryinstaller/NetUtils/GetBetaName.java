package com.tasohan.recoveryinstaller;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Avinaba on 9/6/2015.
 */
public class GetBetaName extends AsyncTask<String,Integer,String> {

    CardView card;
    View view;
    TextView head;
    TextView tname;
    TextView tver;
    TextView tstatus;
    Context mContext;

    public GetBetaName(Context c, CardView cv, View v, TextView h, TextView t1, TextView t2, TextView t3) {
        mContext = c;
        card = cv;
        view = v;
        head = h;
        tname = t1;
        tver = t2;
        tstatus = t3;
    }

    protected void onPreExecute() {
        card.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        head.setVisibility(View.GONE);
    }

    @Override
    protected String doInBackground(String... sUrl) {
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

            HttpGet httpGet = new HttpGet("https://raw.githubusercontent.com/SdtBarbarossa/recovery_installer_xl_cache/master/beta/name");
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
    }

    @Override
    protected void onPostExecute(String result) {
        if(result != null) {
            if(result.toLowerCase().equals(mContext.getResources().getString(R.string.no_beta))) {
            Toast.makeText(mContext,mContext.getResources().getString(R.string.no_beta_desc),Toast.LENGTH_SHORT);
            } else {
            Toast.makeText(mContext,mContext.getResources().getString(R.string.beta_desc),Toast.LENGTH_SHORT);
            card.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
            head.setVisibility(View.VISIBLE);
            tname.setText(result);
            }
        }
    }
}
