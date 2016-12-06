package com.example.david.agenda;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by david on 03/12/2016.
 */

public class Adaptador extends BaseAdapter {
    private ArrayList<Contacto> lista;
    private final Activity actividad;

    public Adaptador(Activity a, ArrayList<Contacto> v) {
        super();
        this.lista = v;
        this.actividad = a;
    }
// En el constructor de la clase se indica la actividad donde se ejecutará la lista de datos a visualizar.
    @Override

    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int arg0) {
        return lista.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return lista.get(arg0).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater ly = actividad.getLayoutInflater();
        View view = ly.inflate(R.layout.item, null, true);
        TextView tvNombre = (TextView) view.findViewById(R.id.tvNombre);
        tvNombre.setText((CharSequence) lista.get(position).getNombre());
        TextView tvTelefono = (TextView) view.findViewById(R.id.tvTelefono);
        tvTelefono.setText("" + lista.get(position).getTelefono());
        File imgFile = new File(lista.get(position).getrFoto());
        if (imgFile.exists()) {
            ImageView im = (ImageView) view.findViewById(R.id.ivFoto);
            im.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
            im.setAdjustViewBounds(true);
        }
        return view;
    }
}
