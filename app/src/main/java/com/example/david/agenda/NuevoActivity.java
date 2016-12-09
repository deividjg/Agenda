package com.example.david.agenda;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.os.Environment.DIRECTORY_PICTURES;

public class NuevoActivity extends AppCompatActivity {

    BDContactos bd;
    long idNuevoContacto;
    private static int ACT_GALERIA = 0;
    private static int ACT_CAMARA = 1;
    private static Uri fotoGaleria;
    private static InputStream is;
    private static BufferedInputStream bis;
    private static Bitmap bm;
    private static ImageView iv;
    private static String fuente;
    private static EditText etNombre;
    private static OutputStream os;
    private static File path;
    private static File fich_salida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo);
        bd = new BDContactos(this);
        idNuevoContacto = bd.nuevaID();

        iv = (ImageView)findViewById(R.id.imageView);
        iv.setImageDrawable(null);
        etNombre = (EditText)findViewById(R.id.editText);

        SharedPreferences prefs = getSharedPreferences("com.example.david.agenda_preferences",MODE_PRIVATE);
        fuente = prefs.getString("foto","galeria");
    }

    protected void guardar(View v){
        if(iv.getDrawable() == null) {
            Toast.makeText(getApplicationContext(),"Ninguna foto que guardar", Toast.LENGTH_LONG).show();
        } else {
            path = getExternalFilesDir(DIRECTORY_PICTURES);
            fich_salida= new File(path, bd.nuevoNombreFoto());
            try {
                os = new FileOutputStream(fich_salida);
            } catch (FileNotFoundException e) {}
            iv.setDrawingCacheEnabled(true);
            bm = iv.getDrawingCache();
            try {
                bm.compress(Bitmap.CompressFormat.JPEG, 50, os);
                os.flush();
                os.close();
            } catch (IOException e) {}
        }

        bd.insertarContacto(idNuevoContacto, "David", "610049154", "Granada", "www.web.com", path.getAbsolutePath() + "/" + bd.nuevoNombreFoto());
        Toast.makeText(getApplicationContext(),"Contacto guardado", Toast.LENGTH_LONG).show();
    }

    protected void añadirFoto(View v){
        if(fuente.equals("galeria")){
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, ACT_GALERIA);
        }
        if(fuente.equals("camara")){
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent, ACT_CAMARA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACT_GALERIA && resultCode == RESULT_OK) {
            fotoGaleria = data.getData();
            try {
                is = getContentResolver().openInputStream(fotoGaleria);
                bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                iv.setImageBitmap(bm);
            } catch (FileNotFoundException e) {}
        }
        if (requestCode == ACT_CAMARA && resultCode == RESULT_OK) {
            bm = (Bitmap) data.getExtras().get("data");
            iv.setImageBitmap(bm);
        }
    }

}
