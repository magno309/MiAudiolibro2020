package com.example.miaudiolibro2020.services;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;

import androidx.annotation.Nullable;

import com.example.miaudiolibro2020.Libro;

import java.io.IOException;

public class ServicioMedia extends Service implements MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {

    private MediaPlayer mediaPlayer = null;
    private MediaController mediaController;


    public int onStartCommand(Intent intent, int flags, int startId) {
        int idLibro = intent.getIntExtra("idLibro", 1);
        Libro libro = Libro.ejemploLibros().elementAt(idLibro);
        if (mediaPlayer != null){
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaController = new MediaController(getApplicationContext());
        Uri audio = Uri.parse(libro.urlAudio);
        //Falta ponerle el anchor
        //mediaController.setAnchorView(getView());
        mediaController.setMediaPlayer(this);
        mediaController.setEnabled(true);
        mediaController.show();
        try {
            mediaPlayer.setDataSource(getApplicationContext(), audio);
            mediaPlayer.prepareAsync();
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        } catch (IOException e) {
            Log.e("Audiolibros", "ERROR: No se puede reproducir "+audio,e);
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

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int i) {
        mediaPlayer.seekTo(i);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return mediaPlayer.getAudioSessionId();
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
    }

}
