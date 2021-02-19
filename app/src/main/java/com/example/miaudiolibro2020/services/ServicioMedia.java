package com.example.miaudiolibro2020.services;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;

import androidx.annotation.Nullable;

import com.example.miaudiolibro2020.Libro;

import java.io.IOException;

public class ServicioMedia extends Service implements MediaPlayer.OnCompletionListener{

    public MediaPlayer mediaPlayer = null;
    public final IBinder binder = new LocalBinder();
    public boolean Bond = false;

    public class LocalBinder extends Binder {
        public ServicioMedia getService() {
            return ServicioMedia.this;
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        int idLibro = intent.getIntExtra("idLibro", 1);
        Libro libro = Libro.ejemploLibros().elementAt(idLibro);
        if (mediaPlayer != null){
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        //mediaController = new MediaController(getApplicationContext());
        Uri audio = Uri.parse(libro.urlAudio);
        Log.d("DFSM", "Entra al servicio");
        try {
            mediaPlayer.setDataSource(getApplicationContext(), audio);
            mediaPlayer.prepare();
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                Log.d("DFSM", "Pasa el start");
            }
        } catch (IOException e) {
            Log.e("Audiolibros", "ERROR: No se puede reproducir "+audio,e);
        }
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("DFSM", "Queda bindeado");
        return binder;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        this.stopSelf();
    }
}
