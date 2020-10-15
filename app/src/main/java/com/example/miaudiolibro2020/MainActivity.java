package com.example.miaudiolibro2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SelectorFragment selectorFragment = new SelectorFragment();
        if(findViewById(R.id.contenedor_pequenio) != null &&
                getSupportFragmentManager().findFragmentById(R.id.contenedor_pequenio) == null){
            getSupportFragmentManager().
                    beginTransaction().
                    add(R.id.contenedor_pequenio, selectorFragment).
                    commit();
        }

    }

    public void mostrarDetalle(int index){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.findFragmentById(R.id.detalle_fragment) != null){
            DetalleFragment fragment = (DetalleFragment) fragmentManager.findFragmentById(R.id.detalle_fragment);
            fragment.ponInfoLibro(index);
        }else{
            DetalleFragment detalleFragment = new DetalleFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(DetalleFragment.ARG_ID_LIBRO, index);
            detalleFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.contenedor_pequenio, detalleFragment).addToBackStack(null).commit();
        }
    }
}