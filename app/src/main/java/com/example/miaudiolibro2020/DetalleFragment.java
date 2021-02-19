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

public class DetalleFragment extends Fragment implements MediaController.MediaPlayerControl, View.OnTouchListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String ARG_ID_LIBRO = "id_libro";

    //Reproductores
    private MediaController mc;

    //Servicios
    Intent servicioAudio;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetalleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetalleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetalleFragment newInstance(String param1, String param2) {
        DetalleFragment fragment = new DetalleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detalle, container, false);
        Bundle args = getArguments();

        if(args != null){
            int position = args.getInt(ARG_ID_LIBRO);
            ponInfoLibro(position, v);
        }else{
            ponInfoLibro(0, v);
        }
        return v;
    }

    public void ponInfoLibro(int id){
        ponInfoLibro(id, getView());
    }

    private int mId;

    private void ponInfoLibro(int id, View vista){
        Libro libro = Libro.ejemploLibros().elementAt(id);
        ((TextView) vista.findViewById(R.id.titulo)).setText(libro.titulo);
        ((TextView) vista.findViewById(R.id.autor)).setText(libro.autor);
        ((ImageView) vista.findViewById(R.id.portada)).setImageResource(libro.recursoImagen);
        mId = id;
        mc = new MediaController(getActivity());
        servicioAudio = new Intent(getContext(), ServicioMedia.class);
        servicioAudio.putExtra("idLibro", id);
        getActivity().startService(servicioAudio);
        getActivity().bindService(servicioAudio, mConnection, Context.BIND_AUTO_CREATE);
    }

    private boolean mBound = true;
    ServicioMedia mService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServicioMedia.LocalBinder binder = (ServicioMedia.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            showMediaControllerHere();
            Log.d("DFSM", "Dibujó el MediaController");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("DFSM", "Hizo unbind");
            mBound = false;
        }
    };

    public void showMediaControllerHere(){
        if (mBound){
            mc.setAnchorView(getView());
            mc.setMediaPlayer(this);
            mc.setEnabled(true);
            mc.show(0);
        }
    }

    @Override
    public void start() {
        /*Intent intent = new Intent(getContext(), ServicioMedia.class);
        intent.putExtra("idLibro", mId);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);*/
        //Log.d("DFSM", "Entra al Start");
        mService.mediaPlayer.start();
    }

    @Override
    public void pause() {
        if(mBound){
            //getActivity().stopService(servicioAudio);
            getActivity().unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public int getDuration() {
        return mService.mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mService.mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mService.mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mService.mediaPlayer.isPlaying();
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
        return mService.mediaPlayer.getAudioSessionId();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("DFSM", "Tocaste la pantalla!");
        mc.show();
        return false;
    }
}
