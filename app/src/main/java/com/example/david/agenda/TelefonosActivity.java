package com.example.david.agenda;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class TelefonosActivity extends AppCompatActivity {

    long idContacto;
    BDContactos bd;
    EditText etTelefono1, etTelefono2, etTelefono3;
    ImageButton ibBorrar2, ibBorrar3, ibGuardar2, ibGuardar3;
    String tel1, tel2, tel3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telefonos);

        ibBorrar2 = (ImageButton)findViewById(R.id.ibBorrar2);
        ibBorrar3 = (ImageButton)findViewById(R.id.ibBorrar3);
        ibGuardar2 = (ImageButton)findViewById(R.id.ibGuardar2);
        ibGuardar3 = (ImageButton)findViewById(R.id.ibGuardar3);
        etTelefono1 = (EditText)findViewById(R.id.etTelefono1);
        etTelefono2 = (EditText)findViewById(R.id.etTelefono2);
        etTelefono3 = (EditText)findViewById(R.id.etTelefono3);

        bd = new BDContactos(this);
        recibirDatos();
        rellenaCampos();
        guardaValores();
    }

    protected void recibirDatos(){
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(getApplicationContext(),R.string.errorTomaDatos, Toast.LENGTH_LONG).show();
        } else {
            idContacto = extras.getLong("idContacto");
        }
    }

    protected void modificarTel1(View view){
        if(etTelefono1.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),R.string.telefonoPrincipalVacio, Toast.LENGTH_LONG).show();
        }else{
            bd.modificarTelefono(idContacto, tel1, etTelefono1.getText().toString());
            Toast.makeText(getApplicationContext(),R.string.telefonoModificado, Toast.LENGTH_LONG).show();
        }
    }

    protected void guardarTel2(View view){
        if(etTelefono2.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),R.string.campoVacio, Toast.LENGTH_LONG).show();
        }else{
            bd.añadirTelefono(idContacto, etTelefono2.getText().toString());
            Toast.makeText(getApplicationContext(),R.string.telefonoAñadido, Toast.LENGTH_LONG).show();
            ibGuardar2.setEnabled(false);
            ibBorrar2.setEnabled(true);
        }
    }

    protected void eliminarTel2(View view){
        bd.eliminarTelefono(idContacto, etTelefono2.getText().toString());
        Toast.makeText(getApplicationContext(),R.string.telefonoEliminado, Toast.LENGTH_LONG).show();
        ibBorrar2.setEnabled(false);
        ibGuardar2.setEnabled(true);
        rellenaCampos();
    }

    protected void guardarTel3(View view){
        if(etTelefono3.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),R.string.campoVacio, Toast.LENGTH_LONG).show();
        }else{
            bd.añadirTelefono(idContacto, etTelefono3.getText().toString());
            Toast.makeText(getApplicationContext(),R.string.telefonoAñadido, Toast.LENGTH_LONG).show();
            ibGuardar3.setEnabled(false);
            ibBorrar3.setEnabled(true);
        }
    }

    protected void eliminarTel3(View view){
        bd.eliminarTelefono(idContacto, etTelefono3.getText().toString());
        Toast.makeText(getApplicationContext(),R.string.telefonoEliminado, Toast.LENGTH_LONG).show();
        ibBorrar3.setEnabled(false);
        ibGuardar3.setEnabled(true);
        rellenaCampos();
    }

    protected  void rellenaCampos(){
        ibBorrar2.setEnabled(false);
        ibBorrar3.setEnabled(false);
        etTelefono2.setText("");
        etTelefono3.setText("");
        String[] telefonos = bd.consultarTelefonos(idContacto);
        int nTelefonos = telefonos.length;
        if(nTelefonos == 1) {
            etTelefono1.setText(telefonos[0]);
        }
        if(nTelefonos == 2) {
            etTelefono1.setText(telefonos[0]);
            etTelefono2.setText(telefonos[1]);
            ibBorrar2.setEnabled(true);
        }
        if(nTelefonos == 3){
            etTelefono1.setText(telefonos[0]);
            etTelefono2.setText(telefonos[1]);
            etTelefono3.setText(telefonos[2]);
            ibBorrar3.setEnabled(true);
        }
    }

    protected void guardaValores(){
        tel1 = etTelefono1.getText().toString();
        tel2 = etTelefono2.getText().toString();
        tel3 = etTelefono3.getText().toString();
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
