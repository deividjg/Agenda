package com.example.david.agenda;

import java.io.Serializable;

/**
 * Created by David on 02/12/2016.
 */

public class Contacto implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
    private String nombre, direccion, webBlog, telefono,rFoto;

    public Contacto(long id, String nombre, String direccion, String webBlog, String telefono, String rFoto){
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.webBlog = webBlog;
        this.telefono = telefono;
        this.rFoto = rFoto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getWebBlog() {
        return webBlog;
    }

    public void setWebBlog(String webBlog) {
        this.webBlog = webBlog;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getrFoto() {
        return rFoto;
    }

    public void setrFoto(String rFoto) {
        this.rFoto = rFoto;
    }
}
