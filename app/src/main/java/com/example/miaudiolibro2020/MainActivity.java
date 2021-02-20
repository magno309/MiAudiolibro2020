package com.example.miaudiolibro2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private boolean audioIniciado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int idLibro = -1;
        if(getIntent().getExtras() != null ){
            idLibro = getIntent().getExtras().getInt("idLibroDesdeServicio", -1);
            Log.d("DFSM", "id de ExtraIntent: " + idLibro );
        }
        if(idLibro==-1){
            audioIniciado=false;
            SelectorFragment selectorFragment = new SelectorFragment();
            if(findViewById(R.id.contenedor_pequenio) != null &&
                    getSupportFragmentManager().findFragmentById(R.id.contenedor_pequenio) == null){
                getSupportFragmentManager().
                        beginTransaction().
                        add(R.id.contenedor_pequenio, selectorFragment).
                        commit();
            }
        }
        else{
            audioIniciado=true;
            mostrarDetalle(idLibro);
        }
    }

    @Override
    public void onRestart() {
        Log.d("DFSM", "onRestart MainActivity");
        super.onRestart();
    }

    @Override
    public void onDestroy() {
        Log.d("DFSM", "onDestroy MainActivity");
        super.onDestroy();
    }

    public void mostrarDetalle(int index){
        FragmentManager fragmentManager = getSupportFragmentManager();
        DetalleFragment fragment = (DetalleFragment) fragmentManager.findFragmentById(R.id.detalle_fragment);
        if(fragment != null && fragment.isVisible()){
            Log.d("DFSM", "MainActivity Fragmento visible");
            fragment.ponInfoLibro(index);
        }else{
            //Log.d("DFSM", "MainActivity Nuevo fragmneto");
            fragment = new DetalleFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(DetalleFragment.ARG_ID_LIBRO, index);
            bundle.putBoolean(DetalleFragment.ARG_AUDIO_INICIADO, audioIniciado);
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.contenedor_pequenio, fragment).addToBackStack(null).commit();
        }
    }
}