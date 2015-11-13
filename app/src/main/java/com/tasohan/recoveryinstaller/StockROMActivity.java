package com.tasohan.recoveryinstaller;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

/**
 * Created by Avinaba on 11/12/2015.
 */
public class StockROMActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
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

}
