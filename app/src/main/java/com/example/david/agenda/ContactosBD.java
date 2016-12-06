package com.example.david.agenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by David on 02/12/2016.
 */

public class ContactosBD extends SQLiteOpenHelper {

    private static String url;
    private static final int VERSION_BASEDATOS = 1;
    private static final String NOMBRE_BASEDATOS = "Contactos.db";
    private static final String ins = "CREATE TABLE CONTACTOS (idContacto INTEGER PRIMARY KEY, Nombre VARCHAR(50), Direccion VARCHAR(50), WebBlog VARCHAR(100))";
    private static final String ins2 = "CREATE TABLE TELEFONOS (idTelefono INTEGER PRIMARY KEY AUTOINCREMENT, Telefono VARCHAR(45), Contactos_idContacto INTEGER)";
    private static final String ins3 = "CREATE TABLE FOTOS (idFoto INTEGER PRIMARY KEY AUTOINCREMENT, NomFichero VARCHAR(50), ObservFoto VARCHAR(255), Contactos_IdContacto INTEGER)";

    public ContactosBD(Context context) {
        super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ins);
        db.execSQL(ins2);
        db.execSQL(ins3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CONTACTOS");
        db.execSQL("DROP TABLE IF EXISTS TELEFONOS");
        db.execSQL("DROP TABLE IF EXISTS FOTOS");
        onCreate(db);
    }

    public long insertarContacto(String nombre, String telefono, String direccion, String webBlog, String rFoto){
        long nreg_afectados = -1;
        long idContacto = nuevaID();
        System.out.println(idContacto);

        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("idContacto", idContacto);
            valores.put("Nombre", nombre);
            valores.put("Direccion", direccion);
            valores.put("WebBlog", webBlog);
            nreg_afectados = db.insert("CONTACTOS", null, valores);
            valores = null;
            valores = new ContentValues();
            valores.put("Telefono", telefono);
            valores.put("Contactos_idContacto", idContacto);
            db.insert("TELEFONOS", null, valores);
            valores = null;
            valores = new ContentValues();
            valores.put("NomFichero", rFoto);
            valores.put("Contactos_idContacto", idContacto);
            db.insert("FOTOS", null, valores);
        }
        db.close();
        return nreg_afectados;
    }

    public void añadirFoto(String idContacto){

    }

    public void añadirTelefono(String idContacto){

    }

    public long nuevaID(){
        SQLiteDatabase db = getReadableDatabase();
        long nContactos;
        if (db != null) {
            String[] campos = {"idContacto"};
            Cursor c = db.query("CONTACTOS", campos, null, null, null, null, "idContacto DESC");
            if(c.moveToFirst()){
                nContactos = Long.parseLong(c.getString(0));
            }else{
                nContactos = 0;
            }
            c.close();
            db.close();
            return nContactos + 1;
        }
        return -1;
    }

    public String[] consultarContactos(){
        SQLiteDatabase db = getReadableDatabase();
        String[] historial = new String[0];

        if (db != null) {
            String[] campos = {"url"};
            Cursor c = db.query("URLs", campos, null, null, null, null, null, null);
            historial = new String[c.getCount()];
            c.moveToFirst();
            for(int i=0; i<c.getCount(); i++){
                historial[i] = c.getString(0);
                c.moveToNext();
            }
            c.close();
        }
        db.close();
        return historial;
    }

    public boolean compruebaContacto(String url){
        SQLiteDatabase db = getReadableDatabase();

        if (db != null) {
            String[] campos = {"url"};
            Cursor c = db.query("URLs", campos, "url='" + url + "'", null, null, null, null, null);
            if(c.moveToFirst()){
                c.close();
                db.close();
                return true;
            }else {
                c.close();
                db.close();
                return false;
            }
        }
        db.close();
        return false;
    }
}
