package com.tasohan.recoveryinstaller;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

//import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.tasohan.recoveryinstaller.*;
import com.tasohan.recoveryinstaller.FlashUtils.FlashRecovery;
import com.tasohan.recoveryinstaller.NetUtils.DownloadFile;
import com.tasohan.recoveryinstaller.NetUtils.DownloadTask;
import com.tasohan.recoveryinstaller.NetUtils.GetRecoveryVersion;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.regex.Pattern;


public class CustomROMActivity extends AppCompatActivity {

    public boolean is_stock = false;
    public boolean isDonate = false;
    public String LOG_TAG = "RecoveryInstaller";


    // Element declarations
    TextView twrp_status = null;
    TextView cwm_status = null;
    TextView cot_status = null;
    TextView cm_status = null;
    TextView stock_status = null;
    TextView aroma_status = null;
    TextView beta_status = null;
    TextView twrp_ver = null;
    TextView cwm_ver = null;
    TextView cot_ver = null;
    TextView cm_ver = null;
    TextView stock_ver = null;
    TextView aroma_ver = null;
    TextView beta_ver = null;

    CardView card_beta = null;
    CardView card_aroma = null;
    CardView card_stock = null;
    CardView card_cm = null;
    CardView card_cot = null;
    CardView card_cwm = null;
    CardView card_twrp = null;


    SharedPreferences.Editor editor = null;
    SharedPreferences.Editor editor_aroma = null;
    SharedPreferences pref = null;
    SharedPreferences pref_aroma = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        if(isDonate) {
            SharedPreferences pref = getSharedPreferences("donate",MODE_PRIVATE);
            if(pref.getBoolean("shown",false) == false) {
                    new AlertDialog.Builder(CustomROMActivity.this)
                            .setTitle(getResources().getString(R.string.donate_title))
                            .setMessage(getResources().getString(R.string.donate_msg))
                            .setPositiveButton(getResources().getString(R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    }).show();
                    SharedPreferences.Editor editor = getSharedPreferences("donate", MODE_PRIVATE).edit();
                    editor.putBoolean("shown", true);
                    editor.commit();

            }
        }

        // Assign the declared views
        card_twrp = (CardView)findViewById(R.id.card_view_twrp);
        twrp_status = (TextView)findViewById(R.id.twrp_status);
        card_cwm = (CardView)findViewById(R.id.card_view_cwm);
        cwm_status = (TextView)findViewById(R.id.cwm_status);
        card_cot = (CardView)findViewById(R.id.card_view_cot);
        cot_status = (TextView)findViewById(R.id.cot_status);
        card_cm = (CardView)findViewById(R.id.card_view_cm);
        cm_status = (TextView)findViewById(R.id.cm_status);
        card_stock = (CardView)findViewById(R.id.card_view_stock);
        stock_status = (TextView)findViewById(R.id.stock_status);
        card_aroma = (CardView)findViewById(R.id.card_view_aroma);
        aroma_status = (TextView)findViewById(R.id.aroma_status);
        editor = getSharedPreferences("recovery", MODE_PRIVATE).edit();
        editor_aroma = getSharedPreferences("aroma", MODE_PRIVATE).edit();
        twrp_ver = (TextView)findViewById(R.id.twrp_version);
        cwm_ver = (TextView)findViewById(R.id.cwm_version);
        cot_ver = (TextView)findViewById(R.id.cot_version);
        cm_ver = (TextView)findViewById(R.id.cm_version);
        stock_ver = (TextView)findViewById(R.id.stock_version);
        aroma_ver = (TextView)findViewById(R.id.aroma_version);
        pref = getSharedPreferences("recovery", MODE_PRIVATE);
        pref_aroma = getSharedPreferences("aroma", MODE_PRIVATE);


        checkDevice();
        getStoragePerms();
        checkRootAccess();
        initCards();

    }

    public void card_setOnClickListener(CardView card, final TextView status, final String filename, final String recovery_key, final String recovery_version, final SharedPreferences.Editor editor) {
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File("/sdcard/"+filename);
                if(file.exists() && !status.getText().toString().equals(getResources().getString(R.string.update))) {
                    new AlertDialog.Builder(CustomROMActivity.this)
                            .setTitle(getResources().getString(R.string.flash_head))
                            .setMessage(getResources().getString(R.string.flash_msg))
                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String temp_rec = recovery_version;
                                    Log.i(LOG_TAG,"Got recovery version " + temp_rec);
                                    if(recovery_version.equals(getResources().getString(R.string.fetch_version)) || recovery_version.equals(getResources().getString(R.string.unknown))) {
                                        Log.wtf(LOG_TAG,"Improper recovery version. Trying to fix it.");
                                        /* Fatal situation, put proper recovery version instead */
                                        SharedPreferences preferences = getSharedPreferences("local_recovery",MODE_PRIVATE);
                                        temp_rec = preferences.getString(recovery_key,null);
                                        Log.i(LOG_TAG,"Tried to fix it. New version " + temp_rec);
                                    }
                                    new FlashRecovery(CustomROMActivity.this, status, recovery_key, editor, temp_rec).execute("");
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                } else {
                        new DownloadTask(CustomROMActivity.this, status, recovery_key, editor, recovery_version).execute("");
                }
            }
        });
    }

    public void card_setOnLongClickListener(CardView card, final String filename, final TextView status) {
        card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final File file = new File("/sdcard/"+filename);
                if(file.exists()) {
                    new AlertDialog.Builder(CustomROMActivity.this)
                            .setTitle(getResources().getString(R.string.delete_head))
                            .setMessage("Are you sure to delete " + file.getAbsolutePath() + "?")
                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    file.delete();
                                    status.setText(getResources().getString(R.string.not_installed));
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();

                } else {
                    Toast.makeText(CustomROMActivity.this,"Nothing to do!",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    public void getStoragePerms() {
        Log.i(LOG_TAG,"Requesting Permissions");
        if(Build.VERSION.SDK_INT >= 23) {
            String[] perm = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // Proceed without doing anything
            } else {
                ActivityCompat.requestPermissions(this,perm,1);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        Log.i(LOG_TAG,"Request Code - " + requestCode);
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.i(LOG_TAG,"Got permissions. Proceeding!");
        } else {
            Log.i(LOG_TAG,"Could not get required permissions. Proceeding anyway!");
        }

    }



    public void initCards () {

        /* Get Recovery versions for each recovery at first */
        new GetRecoveryVersion(CustomROMActivity.this,twrp_ver, twrp_status ,"twrp", pref).execute("");
        new GetRecoveryVersion(CustomROMActivity.this,cwm_ver, cwm_status ,"philz", pref).execute("");
        new GetRecoveryVersion(CustomROMActivity.this,cot_ver, cot_status ,"cot", pref).execute("");
        new GetRecoveryVersion(CustomROMActivity.this,cm_ver, cm_status ,"cm", pref).execute("");
        new GetRecoveryVersion(CustomROMActivity.this,stock_ver, stock_status ,"stock", pref).execute("");
        new GetRecoveryVersion(CustomROMActivity.this,aroma_ver, aroma_status ,"aromafm", pref_aroma).execute("");

        /* Make the cardviews functional */
        /* Part 1: Handle single taps*/
        card_setOnClickListener(card_twrp,twrp_status,"fotatwrp.img","twrp",twrp_ver.getText().toString(),editor);
        card_setOnClickListener(card_cwm,cwm_status,"fotaphilz.img","philz",cwm_ver.getText().toString(),editor);
        card_setOnClickListener(card_cot,cot_status,"fotacot.img","cot",cot_ver.getText().toString(),editor);
        card_setOnClickListener(card_cm,cm_status,"fotacm.img","cm",cm_ver.getText().toString(),editor);
        card_setOnClickListener(card_stock,stock_status,"fotastock.img","stock",stock_ver.getText().toString(),editor);
        card_setOnClickListener(card_aroma,aroma_status,"aromafm.zip","aromafm",aroma_ver.getText().toString(),editor_aroma);

        /* Part 2: Handle long taps */
        card_setOnLongClickListener(card_twrp,"fotatwrp.img",twrp_status);
        card_setOnLongClickListener(card_cwm,"fotaphilz.img",cwm_status);
        card_setOnLongClickListener(card_cot,"fotacot.img",cot_status);
        card_setOnLongClickListener(card_cm,"fotacm.img",cm_status);
        card_setOnLongClickListener(card_stock,"fotastock.img",stock_status);
        card_setOnLongClickListener(card_aroma,"aromafm.zip",aroma_status);
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
            AlertDialog.Builder builder1 = new AlertDialog.Builder(CustomROMActivity.this);
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
            AlertDialog.Builder builder1 = new AlertDialog.Builder(CustomROMActivity.this);
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
        if (id == R.id.action_custom_flash) {

            new MaterialFilePicker()
                    .withActivity(this)
                    .withRequestCode(1)
                    .withFilter(Pattern.compile(".*\\.img$")) // Filtering files and directories by file name using regexp
                    .withRootPath("/storage")
                    .withHiddenFiles(false) // Show hidden files and folders
                    .start();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            final String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            Log.i(LOG_TAG,"Got filepath - " + filePath);
            new AlertDialog.Builder(this)
                           .setTitle("Flash Custom Recovery")
                           .setMessage("Are you sure to flash " + filePath + " to recovery partition?")
                           .setNegativeButton("No", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialogInterface, int i) {

                               }
                           })
                           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new FlashRecovery(CustomROMActivity.this,filePath,editor).execute();
                                }
                            }).show();
        }
    }


}
