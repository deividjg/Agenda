package com.example.david.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static Adaptador a;
    static ArrayList<Contacto> arrayList = new ArrayList();
    ListView lv;
    BDContactos bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bd = new BDContactos(getApplicationContext());

        lv = (ListView)findViewById(android.R.id.list);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapter, View view, int position, long arg) {
                Contacto contacto = (Contacto) lv.getAdapter().getItem(position);
                Intent intent = new Intent (getApplicationContext(), EditarActivity.class);
                intent.putExtra("idContacto", contacto.getId());
                startActivity(intent);
            }
        });

        arrayList = bd.consultarContactos();
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

        if (id == R.id.preferencias) {
            Intent intent = new Intent (this, Preferencias.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart () {
        super.onRestart();
        Toast.makeText(this, "Lista recargada", Toast.LENGTH_SHORT).show();
        arrayList = bd.consultarContactos();
        a = null;
        a = new Adaptador(this,arrayList);
        a.notifyDataSetChanged();
        lv.setAdapter(a);
    }
}
