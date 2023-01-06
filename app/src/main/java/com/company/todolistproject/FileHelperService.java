package com.company.todolistproject;

import static com.company.todolistproject.AppConstants.ITEMLIST;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Rafal Zaborowski on 06.01.2023.
 */
public class FileHelperService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ArrayList<String> itemList = intent.getStringArrayListExtra(ITEMLIST);
        FileHelper.writeData(itemList, getApplicationContext());
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }
}
