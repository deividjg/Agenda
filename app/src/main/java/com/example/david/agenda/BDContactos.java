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

    public long insertarContacto(long idContacto, String nombre, String telefono, String direccion, String eMail, String webBlog, String nomFoto){
        long nreg_afectados = -1;
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("idContacto", idContacto);
            valores.put("Nombre", nombre);
            valores.put("Direccion", direccion);
            valores.put("eMail", eMail);
            valores.put("WebBlog", webBlog);
            nreg_afectados = db.insert("CONTACTOS", null, valores);
            añadirTelefono(idContacto, telefono);
            añadirFoto(idContacto, nomFoto);
        }
        db.close();
        return nreg_afectados;
    }

    public long modificarContacto(long idContacto, String nombre, String direccion, String eMail, String webBlog){
        long nreg_afectados = -1;
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("Nombre", nombre);
            valores.put("Direccion", direccion);
            valores.put("eMail", eMail);
            valores.put("WebBlog", webBlog);
            nreg_afectados = db.update("CONTACTOS", valores, "idContacto=" + idContacto, null);
        }
        db.close();
        return nreg_afectados;
    }

    public long eliminarContacto(long idContacto){
        long nreg_afectados = -1;
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            nreg_afectados = db.delete("CONTACTOS", "idContacto=" + idContacto, null);
            db.delete("TELEFONOS", "Contactos_idContacto=" + idContacto, null);
            db.delete("FOTOS", "Contactos_idContacto=" + idContacto, null);
        }
        db.close();
        return nreg_afectados;
    }

    public ArrayList<Contacto> consultarContactos(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Contacto> contactos = new ArrayList();
        Contacto contacto;
        long idContacto;
        String nombre, direccion, eMail, webBlog, telefono, rFoto;

        if (db != null) {
            String[] campos = {"idContacto", "Nombre", "Direccion", "Email", "WebBlog"};
            Cursor c = db.query("CONTACTOS", campos, null, null, null, null, "Nombre ASC");
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

    public long añadirTelefono(long idContacto, String telefono){
        long nreg_afectados = -1;
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("Contactos_idContacto", idContacto);
            valores.put("Telefono", telefono);
            nreg_afectados = db.insert("TELEFONOS", null, valores);
        }
        db.close();
        return nreg_afectados;
    }

    public long modificarTelefono(long idContacto, String telAnt, String telNue){
        long nreg_afectados = -1;
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("Telefono", telNue);
            nreg_afectados = db.update("TELEFONOS", valores, "Contactos_idContacto=" + idContacto + " AND Telefono='" + telAnt + "'", null);
        }
        db.close();
        return nreg_afectados;
    }

    public long eliminarTelefono(long idContacto, String tel){
        long nreg_afectados = -1;
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            nreg_afectados = db.delete("TELEFONOS", "Contactos_idContacto=" + idContacto + " AND Telefono='" + tel + "'", null);
        }
        db.close();
        return nreg_afectados;
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

    public long añadirFoto(long idContacto, String nFoto){
        long nreg_afectados = -1;
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put("Contactos_idContacto", idContacto);
            valores.put("NomFichero", nFoto);
            nreg_afectados = db.insert("FOTOS", null, valores);
        }
        db.close();
        return nreg_afectados;
    }

    public long eliminarFoto(long idContacto, String nFoto){
        long nreg_afectados = -1;
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            nreg_afectados = db.delete("FOTOS", "Contactos_idContacto=" + idContacto + " AND NomFichero='" + nFoto + "'", null);
        }
        db.close();
        return nreg_afectados;
    }

    public String nuevoNombreFoto(){
        SQLiteDatabase db = getReadableDatabase();
        long nuevoNumeroFoto = 1;
        if (db != null) {
            String[] campos = {"idFoto"};
            Cursor c = db.query("FOTOS", campos, null, null, null, null, null);
            if(c.moveToLast()){
                nuevoNumeroFoto = c.getLong(0) + 1;
            }
            c.close();
            db.close();
            return "foto_" + nuevoNumeroFoto + ".jpg";
        }
        return "Error al asignar nombre";
    }

    public String[] consultarFotos(long idContacto){
        SQLiteDatabase db = getReadableDatabase();
        String[] fotos = new String[0];
        if (db != null) {
            String[] campos = {"NomFichero"};
            Cursor c = db.query("FOTOS", campos, "Contactos_idContacto=" + idContacto , null, null, null, "NomFichero DESC");
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
