package com.tasohan.recoveryinstaller;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getRecoveryVers();
        CardView card_twrp = (CardView)findViewById(R.id.card_view_twrp);
        final TextView twrp_status = (TextView)findViewById(R.id.twrp_status);
        CardView card_cwm = (CardView)findViewById(R.id.card_view_cwm);
        final TextView cwm_status = (TextView)findViewById(R.id.cwm_status);
        final SharedPreferences.Editor editor = getSharedPreferences("recovery", MODE_PRIVATE).edit();
        final TextView twrp_ver = (TextView)findViewById(R.id.twrp_version);
        final TextView cwm_ver = (TextView)findViewById(R.id.cwm_version);
        card_twrp.setClickable(true);
        card_twrp.setLongClickable(true);
        card_twrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadTask(MainActivity.this,twrp_status,"twrp", editor, twrp_ver.getText().toString()).execute("");
            }
        });
        card_cwm.setClickable(true);
        card_cwm.setLongClickable(true);
        card_cwm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadTask(MainActivity.this,cwm_status,"philz", editor, cwm_ver.getText().toString()).execute("");
            }
        });
    }



    public void getRecoveryVers () {
        TextView twrp_ver = (TextView)findViewById(R.id.twrp_version);
        TextView cwm_ver = (TextView)findViewById(R.id.cwm_version);
        SharedPreferences pref = getSharedPreferences("recovery", MODE_PRIVATE);
        TextView twrp_status = (TextView)findViewById(R.id.twrp_status);
        TextView cwm_status = (TextView)findViewById(R.id.cwm_status);
        new GetRecoveryVersion(MainActivity.this,twrp_ver, twrp_status ,"twrp", pref).execute("");
        new GetRecoveryVersion(MainActivity.this,cwm_ver, cwm_status ,"philz", pref).execute("");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
