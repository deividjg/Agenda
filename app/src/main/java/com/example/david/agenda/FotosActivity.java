package com.example.david.agenda;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FotosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotos);
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
