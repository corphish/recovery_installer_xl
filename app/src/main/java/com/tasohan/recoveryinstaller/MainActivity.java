package com.tasohan.recoveryinstaller;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

/**
 * Created by Avinaba on 11/12/2015.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.RELEASE.contains("4.1") || Build.VERSION.RELEASE.contains("4.2")) {
            Intent intent = new Intent(this,StockROMActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this,CustomROMActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
