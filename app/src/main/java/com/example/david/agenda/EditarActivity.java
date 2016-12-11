package com.example.david.agenda;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class EditarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    long idContacto;
    Contacto contacto;
    BDContactos bd;
    EditText etNombre, etTelefono, etDireccion, etEmail, etWebBlog, etFoto;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        iv = (ImageView)findViewById(R.id.imageView);
        etNombre = (EditText)findViewById(R.id.etNombre);
        etTelefono = (EditText)findViewById(R.id.etTelefono);
        etDireccion = (EditText)findViewById(R.id.etDireccion);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etWebBlog = (EditText)findViewById(R.id.etWebBlog);
        etFoto = (EditText)findViewById(R.id.etFoto);

        bd = new BDContactos(this);
        recibirDatos();
        contacto = bd.consultaContacto(idContacto);
        rellenaCampos();
        rellenaImageView();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.editar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i;

        if (id == R.id.llamar) {
            i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:" + contacto.getTelefono()));
            startActivity(i);
        } else if (id == R.id.enviarSms) {
            i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("tel:" + contacto.getTelefono()));
            i.setType("vnd.android-dir/mms-sms");
            i.putExtra("address", contacto.getTelefono());
            i.putExtra("sms_body", "sms text");
            startActivity(i);
        } else if (id == R.id.enviarEmail) {
            i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Asunto del Correo");
            i.putExtra(Intent.EXTRA_TEXT, "Texto por Defecto del Correo");
            i.putExtra(Intent.EXTRA_EMAIL, new String[] {contacto.geteMail()});
            startActivity(i);
        } else if (id == R.id.visitarWebBlog) {
            i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("http://" + contacto.getWebBlog()));
            startActivity(i);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    protected void recibirDatos(){
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(getApplicationContext(),"Error en la toma de datos", Toast.LENGTH_LONG).show();
        } else {
            idContacto = extras.getLong("idContacto");
        }
    }

    protected void rellenaCampos(){
        etNombre.setText(contacto.getNombre());
        etTelefono.setText(contacto.getTelefono());
        etDireccion.setText(contacto.getDireccion());
        etEmail.setText(contacto.geteMail());
        etWebBlog.setText(contacto.getWebBlog());
        etFoto.setText(contacto.getrFoto());
    }

    protected void rellenaImageView(){
        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Fotos_Contactos", contacto.getrFoto());
        Bitmap bm = BitmapFactory.decodeFile(path.getAbsolutePath());
        iv.setImageBitmap(bm);
    }

    protected void borrarContacto(View view){
        AlertDialog.Builder alertDialogBu = new AlertDialog.Builder(this);
        alertDialogBu.setTitle("Confirmar borrado");
        alertDialogBu.setMessage("¿Está seguro?");
        //alertDialogBu.setIcon();

        alertDialogBu.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialogBu.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                bd.eliminarContacto(contacto.getId());
                Toast.makeText(getApplicationContext(),"Contacto Eliminado", Toast.LENGTH_LONG).show();
                volver();
            }
        });

        AlertDialog alertDialog = alertDialogBu.create();
        alertDialog.show();
    }

    protected void modificarContacto(View view){
        String nombre = etNombre.getText().toString();
        String direccion = etDireccion.getText().toString();
        String eMail = etEmail.getText().toString();
        String webBlog = etWebBlog.getText().toString();

        if(nombre.equals("")){
            Toast.makeText(getApplicationContext(),"No puede dejar el nombre en blanco", Toast.LENGTH_LONG).show();
        }else{
            bd.modificarContacto(contacto.getId(), nombre, direccion, eMail, webBlog);
            Toast.makeText(getApplicationContext(),"Contacto modificado", Toast.LENGTH_LONG).show();
            volver();
        }
    }

    protected void volver(){
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
