package com.tasohan.recoveryinstaller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;


public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkRootAccess();
        getRecoveryVers();
        CardView card_twrp = (CardView)findViewById(R.id.card_view_twrp);
        final TextView twrp_status = (TextView)findViewById(R.id.twrp_status);
        CardView card_cwm = (CardView)findViewById(R.id.card_view_cwm);
        final TextView cwm_status = (TextView)findViewById(R.id.cwm_status);
        CardView card_cot = (CardView)findViewById(R.id.card_view_cot);
        final TextView cot_status = (TextView)findViewById(R.id.cot_status);
        CardView card_cm = (CardView)findViewById(R.id.card_view_cm);
        final TextView cm_status = (TextView)findViewById(R.id.cm_status);
        CardView card_stock = (CardView)findViewById(R.id.card_view_stock);
        final TextView stock_status = (TextView)findViewById(R.id.stock_status);
        final SharedPreferences.Editor editor = getSharedPreferences("recovery", MODE_PRIVATE).edit();
        final TextView twrp_ver = (TextView)findViewById(R.id.twrp_version);
        final TextView cwm_ver = (TextView)findViewById(R.id.cwm_version);
        final TextView cot_ver = (TextView)findViewById(R.id.cot_version);
        final TextView cm_ver = (TextView)findViewById(R.id.cm_version);
        final TextView stock_ver = (TextView)findViewById(R.id.stock_version);
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
        card_cot.setClickable(true);
        card_cot.setLongClickable(true);
        card_cot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadTask(MainActivity.this,cot_status,"cot", editor, cot_ver.getText().toString()).execute("");
            }
        });
        card_cm.setClickable(true);
        card_cm.setLongClickable(true);
        card_cm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadTask(MainActivity.this,cm_status,"cm", editor, cm_ver.getText().toString()).execute("");
            }
        });
        card_stock.setClickable(true);
        card_stock.setLongClickable(true);
        card_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadTask(MainActivity.this,stock_status,"stock", editor, stock_ver.getText().toString()).execute("");
            }
        });
    }



    public void getRecoveryVers () {
        TextView twrp_ver = (TextView)findViewById(R.id.twrp_version);
        TextView cwm_ver = (TextView)findViewById(R.id.cwm_version);
        TextView cot_ver = (TextView)findViewById(R.id.cot_version);
        TextView cm_ver = (TextView)findViewById(R.id.cm_version);
        TextView stock_ver = (TextView)findViewById(R.id.stock_version);
        SharedPreferences pref = getSharedPreferences("recovery", MODE_PRIVATE);
        TextView twrp_status = (TextView)findViewById(R.id.twrp_status);
        TextView cwm_status = (TextView)findViewById(R.id.cwm_status);
        TextView cot_status = (TextView)findViewById(R.id.cot_status);
        TextView cm_status = (TextView)findViewById(R.id.cm_status);
        TextView stock_status = (TextView)findViewById(R.id.stock_status);
        new GetRecoveryVersion(MainActivity.this,twrp_ver, twrp_status ,"twrp", pref).execute("");
        new GetRecoveryVersion(MainActivity.this,cwm_ver, cwm_status ,"philz", pref).execute("");
        new GetRecoveryVersion(MainActivity.this,cot_ver, cot_status ,"cot", pref).execute("");
        new GetRecoveryVersion(MainActivity.this,cm_ver, cm_status ,"cm", pref).execute("");
        new GetRecoveryVersion(MainActivity.this,stock_ver, stock_status ,"stock", pref).execute("");
    }

    public void checkRootAccess () {
        Runtime runtime = Runtime.getRuntime();
        Process proc = null;
        OutputStreamWriter osw = null;

        try {
            proc = runtime.exec("su");
            osw = new OutputStreamWriter(proc.getOutputStream());

        } catch (IOException ex) {
            Log.e("execCommandLine()", "Command resulted in an IO Exception: ");

            finish();
            return;
        } finally {
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                }
            }
        }

        try {
            proc.waitFor();
        } catch (InterruptedException e) {
        }

        if (proc.exitValue() != 0) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
            builder1.setTitle(getResources().getString(R.string.no_root));
            builder1.setMessage(getResources().getString(R.string.no_root_desc));
            builder1.setCancelable(false);
            builder1.setPositiveButton(getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }  else {
            //Toast.makeText(this, "Root Access Granted", Toast.LENGTH_SHORT).show();

        }

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
