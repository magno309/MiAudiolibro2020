package com.example.miaudiolibro2020;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

public class AdaptadorLibros extends RecyclerView.Adapter<AdaptadorLibros.ViewHolder> {

    private Vector<Libro> vectorLibros;
    private Context contexto;
    private LayoutInflater inflador;

    private View.OnLongClickListener onLongClickListener;
    private View.OnClickListener onClickListener;

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    /**
     * Se utiliza para crear un objeto de tipo Adaptador que luego se utilizará para
     * crear el layout
     * @param contexto Contexto de la aplicación
     * @param vectorLibros Lista de libros que contendrá el Adaptador
     */
    public AdaptadorLibros(Context contexto, Vector<Libro> vectorLibros) {
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.vectorLibros = vectorLibros;
        this.contexto = contexto;
    }

    /**
     * Crear instancia de ViewHolder que representa un layaout de ElementoSelector.
     * Representa uno de los elementos XML
     * @param parent
     * @param viewType
     * @return La vista de los elementos
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflador.inflate(R.layout.elemento_selector, null);
        v.setOnClickListener(this.onClickListener);
        v.setOnLongClickListener(this.onLongClickListener);
        return new ViewHolder(v);
    }

    /**
     * Se enlaza cada elemento del objeto Vector con su diseño.
     * Se mapean los elementos del vector al ViewHolder
     * @param holder
     * @param position Posición del elemento
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Libro libro = vectorLibros.get(position);
        holder.portada.setImageResource(libro.recursoImagen);
        holder.titulo.setText(libro.titulo);
    }



    /**
     * La cantidad de elementos del vector
     * @return Cantidad de elementos en el vector
     */
    @Override
    public int getItemCount() {
        return vectorLibros.size();
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView portada;
        public TextView titulo;

        public ViewHolder(View itemView)
        {
            super(itemView);
            portada = (ImageView) itemView.findViewById(R.id.portada);
            portada.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            titulo = (TextView) itemView.findViewById(R.id.titulo);
        }
    }

}
