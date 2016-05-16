package com.tasohan.recoveryinstaller;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.app.ActionBar;
import android.util.Log;

/**
 * Created by Avinaba on 11/12/2015.
 */
public class MainActivity extends Activity {

    public String LOG_TAG = "RecoveryInstaller";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent;
        if(Build.VERSION.RELEASE.contains("4.1") || Build.VERSION.RELEASE.contains("4.2")) {
            intent = new Intent(this,StockROMActivity.class);
        } else {
            intent = new Intent(this,CustomROMActivity.class);
        }
        startActivity(intent);


        finish();
    }


}
