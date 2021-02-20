package com.example.miaudiolibro2020.services;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.miaudiolibro2020.DetalleFragment;
import com.example.miaudiolibro2020.Libro;
import com.example.miaudiolibro2020.MainActivity;

import java.io.IOException;

public class ServicioMedia extends Service implements MediaPlayer.OnCompletionListener{

    public MediaPlayer mediaPlayer;
    public final IBinder binder = new LocalBinder();
    private static int idLibro;
    private boolean audioIniciado;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("DFSM","Servicio creado");
    }

    public class LocalBinder extends Binder {
        public ServicioMedia getService() {
            return ServicioMedia.this;
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("DFSM", "Entro a onStartCommand");
        /*idLibro = intent.getIntExtra("idLibro", -1);
        Libro libro = Libro.ejemploLibros().elementAt(idLibro);
        Uri audio = Uri.parse(libro.urlAudio);
        if (mediaPlayer != null){
            mediaPlayer.release();
            Log.d("DFSM", "MediaPlayer NO NULL");
        }
        mediaPlayer = MediaPlayer.create(this, audio);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.start();
        Log.d("DFSM", "MediaPlayer Iniciado");

        createNotificationChannel();
        foregroundService(libro);*/
        return Service.START_STICKY;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d("DFSM", "MediaPlayer Completado");
        stopForeground(true);
        Log.d("DFSM", "stopForeground detenido");
        onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("DFSM", "Entro a onBind");
        idLibro = intent.getIntExtra("idLibro", -1);
        audioIniciado = intent.getBooleanExtra("audioIniciado",false);
        //if(!audioIniciado){
            Libro libro = Libro.ejemploLibros().elementAt(idLibro);
            if (mediaPlayer != null){
                mediaPlayer.release();
                Log.d("DFSM", "MediaPlayer NO NULL");
            }
            Uri audio = Uri.parse(libro.urlAudio);

            mediaPlayer = MediaPlayer.create(this, audio);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.start();
            Log.d("DFSM", "MediaPlayer Iniciado");

            createNotificationChannel();
            foregroundService(libro);
        /*}
        else{

        }*/
        return binder;
    }

    private String CHANNEL_ID="CHANNEL_ID_1";
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "NotificacionForeground", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            Log.d("MiServicio","createNotificationChannel");
        }
    }

    private void foregroundService(Libro libro) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra("idLibroDesdeServicio", idLibro);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Audio Libros")
                .setContentText("Reproduciendo "+libro.titulo)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentIntent(pendingIntent)
                .setTicker("Se inicio el servicio")
                .build();
        // Notification ID cannot be 0.
        startForeground(1000, notification);
        Log.d("DFSM","Se inició Foreground");
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            Log.d("DFSM", "MediaPlayer Stop en onDestroy servicio");
        }
        mediaPlayer.release();
        mediaPlayer=null;
        stopSelf();
        Log.d("DFSM", "Servicio Destruido");
        super.onDestroy();
    }
}
