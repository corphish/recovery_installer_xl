package com.tasohan.recoveryinstaller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Avinaba on 11/12/2015.
 */
public class StockROMActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        CardView card_twrp = (CardView)findViewById(R.id.card_view_twrp);
        CardView card_philz = (CardView)findViewById(R.id.card_view_philz);
        CardView card_cwm = (CardView)findViewById(R.id.card_view_cwm);
        CardView card_aroma = (CardView)findViewById(R.id.card_view_aroma);
        final TextView twrp_status = (TextView)findViewById(R.id.twrp_status);
        final TextView philz_status = (TextView)findViewById(R.id.philz_status);
        final TextView cwm_status = (TextView)findViewById(R.id.cwm_status);
        final TextView aroma_status = (TextView)findViewById(R.id.aroma_status);
        final TextView aroma_ver = (TextView)findViewById(R.id.aroma_version);
        checkRootAccess();
        checkDevice();
        init_cards();
        card_twrp.setClickable(true);
        card_twrp.setLongClickable(true);
        card_twrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File("sdcard/twrp.tar");
                if(file.exists() && file_checklist()) {
                    new AlertDialog.Builder(StockROMActivity.this)
                            .setTitle("Flash Recovery")
                            .setMessage("Are you sure you want to flash the newly downloaded recovery and reboot to recovery?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    new FlashStockRecovery(StockROMActivity.this,twrp_status,"twrp").execute();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                } else {
                    if(!file_checklist()) {
                        new DownloadStockFile(StockROMActivity.this,twrp_status,"https://dl.dropbox.com/s/6prqhapnlxiiyss/busybox?dl=0","busybox").execute();
                        new DownloadStockFile(StockROMActivity.this,twrp_status,"https://dl.dropbox.com/s/edlpfcvuemgpu0j/e2fsck.sh?dl=0","e2fsck.sh").execute();
                        new DownloadStockFile(StockROMActivity.this,twrp_status,"https://dl.dropbox.com/s/e2tkyu4i13463o5/recovery.sh?dl=0","recovery.sh").execute();
                        new DownloadStockFile(StockROMActivity.this,twrp_status,"https://dl.dropbox.com/s/jvjrk6pch9gktsm/step3.sh?dl=0","step3.sh").execute();
                    }
                    if(!file.exists())
                        new DownloadStockFile(StockROMActivity.this,twrp_status,"https://dl.dropbox.com/s/8byq5foo0q1x11s/recovery.tar?dl=0","twrp.tar").execute();

                }

            }
        });
        card_cwm.setClickable(true);
        card_cwm.setLongClickable(true);
        card_cwm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File("sdcard/cwm.tar");
                if(file.exists() && file_checklist()) {
                    new AlertDialog.Builder(StockROMActivity.this)
                            .setTitle("Flash Recovery")
                            .setMessage("Are you sure you want to flash the newly downloaded recovery and reboot to recovery?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    new FlashStockRecovery(StockROMActivity.this,cwm_status,"cwm").execute();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                } else {
                    if(!file_checklist()) {
                        new DownloadStockFile(StockROMActivity.this,cwm_status,"https://dl.dropbox.com/s/6prqhapnlxiiyss/busybox?dl=0","busybox").execute();
                        new DownloadStockFile(StockROMActivity.this,cwm_status,"https://dl.dropbox.com/s/edlpfcvuemgpu0j/e2fsck.sh?dl=0","e2fsck.sh").execute();
                        new DownloadStockFile(StockROMActivity.this,cwm_status,"https://dl.dropbox.com/s/e2tkyu4i13463o5/recovery.sh?dl=0","recovery.sh").execute();
                        new DownloadStockFile(StockROMActivity.this,cwm_status,"https://dl.dropbox.com/s/jvjrk6pch9gktsm/step3.sh?dl=0","step3.sh").execute();
                    }
                    if(!file.exists())
                        new DownloadStockFile(StockROMActivity.this,cwm_status,"https://dl.dropbox.com/s/0yk4e33n4yrdw34/recovery.tar?dl=0","cwm.tar").execute();

                }

            }
        });
        card_philz.setClickable(true);
        card_philz.setLongClickable(true);
        card_philz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File("sdcard/philz.tar");
                if(file.exists() && file_checklist()) {
                    new AlertDialog.Builder(StockROMActivity.this)
                            .setTitle("Flash Recovery")
                            .setMessage("Are you sure you want to flash the newly downloaded recovery and reboot to recovery?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    new FlashStockRecovery(StockROMActivity.this,philz_status,"philz").execute();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                } else {
                    if(!file_checklist()) {
                        new DownloadStockFile(StockROMActivity.this,philz_status,"https://dl.dropbox.com/s/6prqhapnlxiiyss/busybox?dl=0","busybox").execute();
                        new DownloadStockFile(StockROMActivity.this,philz_status,"https://dl.dropbox.com/s/edlpfcvuemgpu0j/e2fsck.sh?dl=0","e2fsck.sh").execute();
                        new DownloadStockFile(StockROMActivity.this,philz_status,"https://dl.dropbox.com/s/e2tkyu4i13463o5/recovery.sh?dl=0","recovery.sh").execute();
                        new DownloadStockFile(StockROMActivity.this,philz_status,"https://dl.dropbox.com/s/jvjrk6pch9gktsm/step3.sh?dl=0","step3.sh").execute();
                    }
                    if(!file.exists())
                        new DownloadStockFile(StockROMActivity.this,philz_status,"https://dl.dropbox.com/s/ljv379haaai6ada/recovery.tar?dl=0","philz.tar").execute();

                }

            }
        });
        final SharedPreferences.Editor editor_aroma = getSharedPreferences("aroma",MODE_PRIVATE).edit();
        card_aroma.setClickable(true);
        card_aroma.setLongClickable(true);
        card_aroma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File("sdcard/aromafm.zip");
                if(file.exists() && !aroma_status.getText().toString().equals(getResources().getString(R.string.update))) {
                    new AlertDialog.Builder(StockROMActivity.this)
                            .setTitle("Flash Aroma File Manager")
                            .setMessage("Are you sure you want to reboot to recovery and flash AromaFM?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    new FlashRecovery(StockROMActivity.this, aroma_status, "aromafm", editor_aroma, aroma_ver.getText().toString()).execute("");
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                } else
                    new DownloadTask(StockROMActivity.this, aroma_status, "aromafm", editor_aroma, aroma_ver.getText().toString()).execute("");
            }
        });


    }


    public boolean file_checklist_core (String filename) {
        File file = new File(filename);
        if(file.exists())
            return true;
        else
            return false;
    }

    public boolean file_checklist () {
        String[] files = {"sdcard/busybox","sdcard/e2fsck.sh","sdcard/recovery.sh","sdcard/step3.sh"};
        for(String file:files) {
            if(!file_checklist_core(file))
               return false;
        }
        return true;
    }

    public void init_cards() {
        String[] files = {"sdcard/twrp.tar","sdcard/cwm.tar","sdcard/philz.tar","sdcard/aromafm.zip"};
        File file;
        final TextView twrp_status = (TextView)findViewById(R.id.twrp_status);
        final TextView philz_status = (TextView)findViewById(R.id.philz_status);
        final TextView cwm_status = (TextView)findViewById(R.id.cwm_status);
        final TextView aroma_status = (TextView)findViewById(R.id.aroma_status);
        file = new File(files[0]);
        if(file.exists() && file_checklist())
            twrp_status.setText(R.string.downloaded);
        file = new File(files[2]);
        if(file.exists() && file_checklist())
            philz_status.setText(R.string.downloaded);
        file = new File(files[1]);
        if(file.exists() && file_checklist())
            cwm_status.setText(R.string.downloaded);
        file = new File(files[3]);
        if(file.exists() && file_checklist())
            aroma_status.setText(R.string.downloaded);

        SharedPreferences pref = getSharedPreferences("recovery",MODE_PRIVATE);
        if(pref.getString("recovery","").equals("twrp")) {
            twrp_status.setTextColor(getResources().getColor(R.color.green));
            twrp_status.setText(R.string.installed);
        }
        if(pref.getString("recovery","").equals("cwm")) {
            cwm_status.setTextColor(getResources().getColor(R.color.green));
            cwm_status.setText(R.string.installed);
        }
        if(pref.getString("recovery","").equals("philz")) {
            philz_status.setTextColor(getResources().getColor(R.color.green));
            philz_status.setText(R.string.installed);
        }
        Log.d("Init","Pref - "+ pref.getString("recovery",""));

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
            AlertDialog.Builder builder1 = new AlertDialog.Builder(StockROMActivity.this);
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
        if(dev.equals(getResources().getString(R.string.model_1)) || dev.equals(getResources().getString(R.string.model_2)) || dev.equals(getResources().getString(R.string.model_3)) || dev.toLowerCase().equals(getResources().getString(R.string.model_4)) || dev.equals(getResources().getString(R.string.model_5))) {
            //compatible device found.
        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(StockROMActivity.this);
            builder1.setTitle(getResources().getString(R.string.no_device));
            builder1.setMessage(getResources().getString(R.string.no_device_desc));
            builder1.setCancelable(false);
            builder1.setPositiveButton(getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
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
            Intent intent = new Intent(this, AboutActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
