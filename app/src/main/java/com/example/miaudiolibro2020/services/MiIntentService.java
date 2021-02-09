package com.example.miaudiolibro2020.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;

public class MiIntentService extends IntentService {

    public MiIntentService() {
        super("MiIntentService");
    }

    class MiTareaAsincrona extends AsyncTask<Integer, Integer, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("MIIS", "Se inició el subproceso!");
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {
            Log.d("MIIS", "Se recibieron " + integers.length + "parámetros!");

            return null;
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
