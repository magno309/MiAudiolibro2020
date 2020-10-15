package com.example.miaudiolibro2020;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectorFragment extends Fragment {

    private RecyclerView rvRecyclerView;
    private AdaptadorLibros adapter;
    private GridLayoutManager manager;

    MainActivity mainActivity;
    Context contexto;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.contexto = context;
        if ( context instanceof MainActivity){
            mainActivity = (MainActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listaselector, container, false);
        rvRecyclerView = v.findViewById(R.id.rvListaLibros2);
        manager = new GridLayoutManager(getActivity(), 2);
        rvRecyclerView.setLayoutManager(manager);
        adapter = new AdaptadorLibros(getActivity(), Libro.ejemploLibros());
        adapter.setOnClickListener(
                vista -> {
                    int posSeleccionado = rvRecyclerView.getChildAdapterPosition(vista);
                    Toast.makeText(getActivity(), "Elemento seleccionado " + posSeleccionado, Toast.LENGTH_SHORT).show();
                    mainActivity.mostrarDetalle(posSeleccionado);
                }
        );
        adapter.setOnLongClickListener( view -> {
            AlertDialog.Builder cuadroDialogo = new AlertDialog.Builder(contexto);
            cuadroDialogo.setTitle("Seleccionar la acción");
            cuadroDialogo.setItems(new String[]{"Compartir", "Eliminar", "Agregar"}, (dialogInterface, i) -> {
                Toast.makeText(getActivity(), "Opción seleccionado: " + i, Toast.LENGTH_SHORT).show();
            });
            cuadroDialogo.setMessage("Este es un cuadro de dialogo");
            cuadroDialogo.setPositiveButton("Ok",
                    (viewDialogo, i) -> {

                    }
            );
            return false;
        });
        rvRecyclerView.setAdapter(adapter);
        return v;
    }
}
