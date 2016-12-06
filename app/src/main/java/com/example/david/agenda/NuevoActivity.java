package com.example.david.agenda;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NuevoActivity extends AppCompatActivity {

    ContactosBD bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo);
        bd = new ContactosBD(this);
    }

    public void guardar(View v){
        bd.insertarContacto("David", "610049154", "Granada", "www.web.com", "rutafoto");
    }
}
