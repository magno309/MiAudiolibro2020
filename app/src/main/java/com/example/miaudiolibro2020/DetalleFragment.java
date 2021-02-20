package com.example.miaudiolibro2020;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.miaudiolibro2020.services.MiServicio;
import com.example.miaudiolibro2020.services.ServicioMedia;

import java.io.IOException;

public class DetalleFragment extends Fragment implements View.OnTouchListener, MediaController.MediaPlayerControl{
    public static final String ARG_ID_LIBRO = "id_libro";
    public static final String ARG_AUDIO_INICIADO = "audio_iniciado";
    private MediaController mc;
    private Intent intentServicioMedia;
    private boolean mBound = false;
    private ServicioMedia mService;
    private boolean audioIniciado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("DFSM", "entra onCreateView DetalleFragment");
        View v = inflater.inflate(R.layout.fragment_detalle, container, false);
        Bundle args = getArguments();
        if(args != null){
            int position = args.getInt(ARG_ID_LIBRO);
            audioIniciado = args.getBoolean(ARG_AUDIO_INICIADO);
            ponInfoLibro(position, v);
        }else{
            ponInfoLibro(0, v);
        }
        return v;
    }

    public void ponInfoLibro(int id){
        ponInfoLibro(id, getView());
    }

    private void ponInfoLibro(int id, View vista){
        Log.d("DFSM", "entra ponInfoLibro");
        Libro libro = Libro.ejemploLibros().elementAt(id);
        ((TextView) vista.findViewById(R.id.titulo)).setText(libro.titulo);
        ((TextView) vista.findViewById(R.id.autor)).setText(libro.autor);
        ((ImageView) vista.findViewById(R.id.portada)).setImageResource(libro.recursoImagen);
        vista.setOnTouchListener(this);
        intentServicioMedia = new Intent(getActivity().getBaseContext(), ServicioMedia.class);
        intentServicioMedia.putExtra("idLibro", id);
        intentServicioMedia.putExtra("audioIniciado", audioIniciado);
        if(!audioIniciado){
            Log.d("DFSM", "entra ponInfoLibro audio NO iniciado");
            //getActivity().startService(intentServicioMedia);
            getActivity().bindService(intentServicioMedia, mConnection, Context.BIND_AUTO_CREATE);
        }
        else{
            Log.d("DFSM", "entra ponInfoLibro audio SI iniciado");
            getActivity().bindService(intentServicioMedia, mConnection, Context.BIND_AUTO_CREATE);
        }

    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("DFSM", "Entra onServiceConnected Conectado");
            ServicioMedia.LocalBinder binder = (ServicioMedia.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            showMediaControllerHere();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("DFSM", "Entra onServiceDisconnected");
            mBound = false;
        }
    };

    public void showMediaControllerHere(){
        if (mBound){
            mc = new MediaController(getActivity());
            mc.setAnchorView(getView().findViewById(R.id.fragment_detalle));
            mc.setMediaPlayer(this);
            mc.setEnabled(true);
            Log.d("DFSM", "Se creó el MediaController");
        }
    }

    @Override
    public void start() {
        mService.mediaPlayer.start();
        Log.d("DFSM", "Start MediaPlayer desde Start MediaController");
    }

    @Override
    public void pause() {
        mService.mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        if(mService.mediaPlayer.isPlaying())
            return mService.mediaPlayer.getDuration();
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(mService.mediaPlayer.isPlaying())
            return mService.mediaPlayer.getCurrentPosition();
        return 0;
    }

    @Override
    public void seekTo(int pos) {
        if(mService.mediaPlayer.isPlaying())
            mService.mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        if(mService.mediaPlayer!=null){
            return mService.mediaPlayer.isPlaying();
        }
        return false;
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
        if(mService.mediaPlayer.isPlaying())
            return mService.mediaPlayer.getAudioSessionId();
        return 0;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("DFSM", "Tocaste la pantalla!");
        if(mService.mediaPlayer!=null){
            mc.show();
        }
        return false;
    }
}
