package com.example.david.agenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by David on 02/12/2016.
 */

public class BDContactos extends SQLiteOpenHelper {

    private static final int VERSION_BASEDATOS = 1;
    private static final String NOMBRE_BASEDATOS = "Contactos.db";
    private static final String ins = "CREATE TABLE CONTACTOS (idContacto INTEGER PRIMARY KEY, Nombre VARCHAR(50), Direccion VARCHAR(50), Email VARCHAR(50), WebBlog VARCHAR(100))";
    private static final String ins2 = "CREATE TABLE TELEFONOS (idTelefono INTEGER PRIMARY KEY AUTOINCREMENT, Telefono VARCHAR(45), Contactos_idContacto INTEGER)";
    private static final String ins3 = "CREATE TABLE FOTOS (idFoto INTEGER PRIMARY KEY AUTOINCREMENT, NomFichero VARCHAR(50), ObservFoto VARCHAR(255), Contactos_IdContacto INTEGER)";

    public BDContactos(Context context) {
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

    public long insertarContacto(long idContacto, String nombre, String telefono, String direccion, String eMail, String webBlog, String rFoto){
        long nreg_afectados = -1;
        System.out.println(idContacto);

        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("idContacto", idContacto);
            valores.put("Nombre", nombre);
            valores.put("Direccion", direccion);
            valores.put("eMail", eMail);
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

    public String nuevoNombreFoto(){
        SQLiteDatabase db = getReadableDatabase();
        long nuevoNumeroFoto;
        if (db != null) {
            String[] campos = {"idFoto"};
            Cursor c = db.query("FOTOS", campos, null, null, null, null, null);
            nuevoNumeroFoto = c.getCount() + 1;
            c.close();
            db.close();
            return "foto_" + nuevoNumeroFoto + ".jpg";
        }
        return "Error al asignar nombre";
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

    public ArrayList<Contacto> consultarContactos(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Contacto> contactos = new ArrayList();
        Contacto contacto;
        long idContacto;
        String nombre, direccion, eMail, webBlog, telefono, rFoto;

        if (db != null) {
            String[] campos = {"idContacto", "Nombre", "Direccion", "Email", "WebBlog"};
            Cursor c = db.query("CONTACTOS", campos, null, null, null, null, null, null);
            c.moveToFirst();
            for(int i=0; i<c.getCount(); i++){
                idContacto = c.getLong(0);
                nombre = c.getString(1);
                direccion = c.getString(2);
                eMail = c.getString(3);
                webBlog = c.getString(4);
                telefono = consultarTelefonos(idContacto)[0];
                rFoto = consultarFotos(idContacto)[0];
                contacto = new Contacto(idContacto, nombre, direccion, eMail, webBlog, telefono, rFoto);
                contactos.add(contacto);
                c.moveToNext();
            }
            c.close();
        }
        db.close();
        return contactos;
    }

    public Contacto consultaContacto(long idContacto){
        SQLiteDatabase db = getReadableDatabase();
        Contacto contacto = null;
        String nombre, direccion, eMail, webBlog, telefono, rFoto;

        if (db != null) {
            String[] campos = {"Nombre", "Direccion", "Email", "WebBlog"};
            Cursor c = db.query("CONTACTOS", campos, "idContacto=" + idContacto, null, null, null, null);
            if(c.moveToFirst()){
                nombre = c.getString(0);
                direccion = c.getString(1);
                eMail = c.getString(2);
                webBlog = c.getString(3);
                telefono = consultarTelefonos(idContacto)[0];
                rFoto = consultarFotos(idContacto)[0];
                contacto = new Contacto(idContacto, nombre, direccion, eMail, webBlog, telefono, rFoto);
            }
            c.close();
        }
        db.close();
        return contacto;
    }

    public String[] consultarTelefonos(long idContacto){
        SQLiteDatabase db = getReadableDatabase();
        String[] telefonos = new String[0];
        if (db != null) {
            String[] campos = {"Telefono"};
            Cursor c = db.query("TELEFONOS", campos, "Contactos_idContacto=" + idContacto , null, null, null, null);
            telefonos = new String[c.getCount()];
            c.moveToFirst();
            for(int i=0; i<c.getCount(); i++){
                telefonos[i] = c.getString(0);
                c.moveToNext();
            }
            c.close();
        }
        db.close();
        return telefonos;
    }

    public String[] consultarFotos(long idContacto){
        SQLiteDatabase db = getReadableDatabase();
        String[] fotos = new String[0];
        if (db != null) {
            String[] campos = {"NomFichero"};
            Cursor c = db.query("FOTOS", campos, "Contactos_idContacto=" + idContacto , null, null, null, null);
            fotos = new String[c.getCount()];
            c.moveToFirst();
            for(int i=0; i<c.getCount(); i++){
                fotos[i] = c.getString(0);
                c.moveToNext();
            }
            c.close();
        }
        db.close();
        return fotos;
    }
}
