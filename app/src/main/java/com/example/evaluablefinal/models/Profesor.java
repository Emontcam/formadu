package com.example.evaluablefinal.models;

import com.example.evaluablefinal.controlErrores.Comprobaciones;

import java.security.SecureRandom;
import java.util.Date;

public class Profesor implements Comprobaciones {

    private String id;
    private String nombre;
    private String correo;//comprobaciones de formato

    private String imagen = "";



    public Profesor(String nombre, String correo) {
        this.nombre = nombre;
        this.correo = correo;
    }
    public Profesor(String id, String nombre, String imagen, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
