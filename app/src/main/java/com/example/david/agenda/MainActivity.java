package com.example.david.agenda;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static android.R.attr.path;

public class MainActivity extends AppCompatActivity {

    static Adaptador a;
    static ArrayList<Contacto> arrayList = new ArrayList();
    ListView lv;
    private static File path;
    private static Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lv = (ListView)findViewById(R.id.listView);
        Contacto cont1 = new Contacto(1, "Nombre1", "direc1", "webblog1", "telefono1", "foto1");
        Contacto cont2 = new Contacto(2, "Nombre2", "direc2", "webblog2", "telefono2", "foto2");
        Contacto cont3 = new Contacto(2, "Nombre2", "direc2", "webblog2", "telefono2", "foto3");
        Contacto cont4 = new Contacto(2, "Nombre2", "direc2", "webblog2", "telefono2", "foto4");
        Contacto cont5 = new Contacto(2, "Nombre2", "direc2", "webblog2", "telefono2", "foto5");
        Contacto cont6 = new Contacto(2, "Nombre2", "direc2", "webblog2", "telefono2", "foto6");
        arrayList.add(cont1);
        arrayList.add(cont2);
        arrayList.add(cont3);
        arrayList.add(cont4);
        arrayList.add(cont5);
        arrayList.add(cont6);
        arrayList.add(cont1);
        arrayList.add(cont2);
        arrayList.add(cont3);
        arrayList.add(cont4);
        arrayList.add(cont5);
        arrayList.add(cont6);
        a = new Adaptador(this,arrayList);
        a.notifyDataSetChanged();
        lv.setAdapter(a);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nuevoContacto) {
            Intent intent = new Intent (this, NuevoActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart () {
        super.onRestart();
        Toast.makeText(this, "Lista recargada", Toast.LENGTH_SHORT).show();
        a = null;
        a = new Adaptador(this,arrayList);
        a.notifyDataSetChanged();
        lv.setAdapter(a);
    }
}
