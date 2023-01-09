package com.company.todolistproject;

import static com.company.todolistproject.AppConstants.ITEMLIST;
import static com.company.todolistproject.AppConstants.SAVEDDATA;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Rafal Zaborowski on 06.01.2023.
 */
public class FileHelperService extends Service {
    private SharedPreferences sharedPrferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String itemListJson = intent.getStringExtra(ITEMLIST);
        saveDataToSharedPrefs(itemListJson);
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    private void saveDataToSharedPrefs(String itemListJSON){
        sharedPrferences = getSharedPreferences(SAVEDDATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sharedPrferences.edit();
        spEdit.clear();
        spEdit.putString(ITEMLIST, itemListJSON);
        spEdit.apply();
    }
}
