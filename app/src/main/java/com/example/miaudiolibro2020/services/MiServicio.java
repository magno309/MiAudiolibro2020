package com.example.miaudiolibro2020.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Random;

public class MiServicio extends Service {

    // Binder given to clients
    private final IBinder binder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();

    public class LocalBinder extends Binder {
        MiServicio getService() {
            return MiServicio.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /** method for clients */
    public int getRandomNumber() {
        return mGenerator.nextInt(100);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MSAL", "Servicio creado");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MSAL", "Servicio finalizado");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Tarea exhaustiva
        Log.d("MSAL", "Iniciando tarea pesada");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("MSAL", "Iniciando tarea pesada");
        return super.onStartCommand(intent, flags, startId);
    }

}
