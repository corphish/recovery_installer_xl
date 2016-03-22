package com.tasohan.recoveryinstaller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.DialogPreference;

/**
 * Created by Avinaba on 11/12/2015.
 */
public class MainActivity extends Activity {


    public int act = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.RELEASE.contains("4.1") || Build.VERSION.RELEASE.contains("4.2")) {
            Intent intent = new Intent(this,StockROMActivity.class);
            act = 1;
            startActivity(intent);
        } else {
            Intent intent = new Intent(this,CustomROMActivity.class);
            act = 2;
            startActivity(intent);
        }


        finish();
    }
}
