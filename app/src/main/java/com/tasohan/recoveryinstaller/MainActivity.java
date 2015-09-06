package com.tasohan.recoveryinstaller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkDevice();
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
        CardView card_aroma = (CardView)findViewById(R.id.card_view_aroma);
        final TextView aroma_status = (TextView)findViewById(R.id.aroma_status);
        final CardView card_beta = (CardView)findViewById(R.id.card_view_beta);
        final TextView beta_status = (TextView)findViewById(R.id.beta_status);
        final SharedPreferences.Editor editor = getSharedPreferences("recovery", MODE_PRIVATE).edit();
        final SharedPreferences.Editor editor_aroma = getSharedPreferences("aroma", MODE_PRIVATE).edit();
        final TextView twrp_ver = (TextView)findViewById(R.id.twrp_version);
        final TextView cwm_ver = (TextView)findViewById(R.id.cwm_version);
        final TextView cot_ver = (TextView)findViewById(R.id.cot_version);
        final TextView cm_ver = (TextView)findViewById(R.id.cm_version);
        final TextView stock_ver = (TextView)findViewById(R.id.stock_version);
        final TextView aroma_ver = (TextView)findViewById(R.id.aroma_version);
        final TextView beta_ver = (TextView)findViewById(R.id.beta_version);
        card_twrp.setClickable(true);
        card_twrp.setLongClickable(true);
        card_twrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadTask(MainActivity.this, twrp_status, "twrp", editor, twrp_ver.getText().toString()).execute("");

            }
        });
        card_cwm.setClickable(true);
        card_cwm.setLongClickable(true);
        card_cwm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadTask(MainActivity.this, cwm_status, "philz", editor, cwm_ver.getText().toString()).execute("");
            }
        });
        card_cot.setClickable(true);
        card_cot.setLongClickable(true);
        card_cot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadTask(MainActivity.this, cot_status, "cot", editor, cot_ver.getText().toString()).execute("");
            }
        });
        card_cm.setClickable(true);
        card_cm.setLongClickable(true);
        card_cm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadTask(MainActivity.this, cm_status, "cm", editor, cm_ver.getText().toString()).execute("");
            }
        });
        card_stock.setClickable(true);
        card_stock.setLongClickable(true);
        card_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadTask(MainActivity.this, stock_status, "stock", editor, stock_ver.getText().toString()).execute("");
            }
        });
        card_aroma.setClickable(true);
        card_aroma.setLongClickable(true);
        card_aroma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadTask(MainActivity.this, aroma_status, "aromafm", editor_aroma, aroma_ver.getText().toString()).execute("");
            }
        });
        card_beta.setClickable(true);
        card_beta.setLongClickable(true);
        final TextView beta_name = (TextView)findViewById(R.id.textView_beta_head);
        final TextView beta_head = (TextView)findViewById(R.id.textView_section_beta);
        final View view = (View)findViewById(R.id.view_section_beta);
        card_beta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadTask(MainActivity.this,beta_status,"beta", null, "").execute("");
            }
        });
    }



    public void getRecoveryVers () {
        TextView twrp_ver = (TextView)findViewById(R.id.twrp_version);
        TextView cwm_ver = (TextView)findViewById(R.id.cwm_version);
        TextView cot_ver = (TextView)findViewById(R.id.cot_version);
        TextView cm_ver = (TextView)findViewById(R.id.cm_version);
        TextView stock_ver = (TextView)findViewById(R.id.stock_version);
        TextView aroma_ver = (TextView)findViewById(R.id.aroma_version);
        SharedPreferences pref = getSharedPreferences("recovery", MODE_PRIVATE);
        SharedPreferences pref_aroma = getSharedPreferences("aroma", MODE_PRIVATE);
        TextView twrp_status = (TextView)findViewById(R.id.twrp_status);
        TextView cwm_status = (TextView)findViewById(R.id.cwm_status);
        TextView cot_status = (TextView)findViewById(R.id.cot_status);
        TextView cm_status = (TextView)findViewById(R.id.cm_status);
        TextView stock_status = (TextView)findViewById(R.id.stock_status);
        TextView aroma_status = (TextView)findViewById(R.id.aroma_status);
        final TextView beta_name = (TextView)findViewById(R.id.textView_beta_head);
        final TextView beta_head = (TextView)findViewById(R.id.textView_section_beta);
        final View view = (View)findViewById(R.id.view_section_beta);
        final CardView card_beta = (CardView)findViewById(R.id.card_view_beta);
        final TextView beta_status = (TextView)findViewById(R.id.beta_status);
        final TextView beta_ver = (TextView)findViewById(R.id.beta_version);
        new GetBetaName(MainActivity.this, card_beta, view, beta_head, beta_name, beta_ver, beta_status).execute("");
        new GetRecoveryVersion(MainActivity.this,twrp_ver, twrp_status ,"twrp", pref).execute("");
        new GetRecoveryVersion(MainActivity.this,cwm_ver, cwm_status ,"philz", pref).execute("");
        new GetRecoveryVersion(MainActivity.this,cot_ver, cot_status ,"cot", pref).execute("");
        new GetRecoveryVersion(MainActivity.this,cm_ver, cm_status ,"cm", pref).execute("");
        new GetRecoveryVersion(MainActivity.this,stock_ver, stock_status ,"stock", pref).execute("");
        new GetRecoveryVersion(MainActivity.this,aroma_ver, aroma_status ,"aromafm", pref_aroma).execute("");
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

    public void checkDevice () {
        String dev = Build.MODEL;
        if(dev.equals(getResources().getString(R.string.model_1)) || dev.equals(getResources().getString(R.string.model_2)) || dev.equals(getResources().getString(R.string.model_3)) || dev.toLowerCase().equals(getResources().getString(R.string.model_4))) {
            //compatible device found.
        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
            builder1.setTitle(getResources().getString(R.string.no_device));
            builder1.setMessage(getResources().getString(R.string.no_device_desc));
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
        if (id == R.id.action_about) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
            builder1.setTitle(getResources().getString(R.string.action_about));
            builder1.setMessage("App Version - 1.1\nRecovery Maintainer - Michael Di\nApp Maintainer - Avinaba Dalal");
            builder1.setCancelable(true);
            builder1.setPositiveButton(getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
