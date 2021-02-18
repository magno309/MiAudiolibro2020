package com.example.miaudiolibro2020.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class ServicioMedia extends Service implements MediaPlayer.OnPreparedListener {

    MediaPlayer mediaPlayer = null;



    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    public void onDestroy() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
    }
}
