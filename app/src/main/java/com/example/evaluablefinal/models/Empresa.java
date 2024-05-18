package com.example.evaluablefinal.models;

public class Empresa {
    private String nombre;
    private String tipo;
    private String descrip;
    private String img;

    public Empresa(String nombre, String tipo, String descrip, String img) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.descrip = descrip;
        this.img = img;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
