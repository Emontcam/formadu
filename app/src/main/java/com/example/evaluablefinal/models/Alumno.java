package com.example.evaluablefinal.models;

import com.example.evaluablefinal.controlErrores.Comprobaciones;

import java.security.SecureRandom;

public class Alumno implements Comprobaciones {

    private String id;
    private String nombre;
    private String correo;//comprobaciones de formato
    private Integer horasTotales;
    private Integer horasCubiertas = 0;
    private String imagen = "";

    private String empresa;
    private String tutor;
    private String contrasena;//autogenerar
    //semana
    private Integer l = 0;
    private Integer m = 0;
    private Integer x = 0;
    private Integer j = 0;
    private Integer v = 0;


    public Alumno(String nombre, String correo, Integer horasTotales, String empresa, String tutor, String imagen) {
        this.nombre = nombre;
        this.correo = correo;
        this.horasTotales = horasTotales;
        this.empresa = empresa;
        this.tutor = tutor;
        this.imagen = imagen;
        generarContrasena();
    }
    public Alumno(String id, String nombre, String empresa, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.empresa = empresa;
        this.imagen = imagen;
        generarContrasena();
    }

    private void generarContrasena() {
        int longitudMin = 6;
        StringBuilder password = new StringBuilder(longitudMin);
        SecureRandom random = new SecureRandom();
        String letrasLower = "abcdefghijklmnopqrstuvwxyz";
        String letrasUpper = letrasLower.toUpperCase();
        String nums = "0123456789";
        String especial = "!@#$%&";
        String caracteres = letrasLower + letrasUpper + nums + especial;

        for (int i = 0; i < longitudMin; i++) {
            int randomIndex = random.nextInt(caracteres.length());
            password.append(caracteres.charAt(randomIndex));
        }
        setContrasena(password.toString());
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

    public Integer getHorasTotales() {
        return horasTotales;
    }

    public void setHorasTotales(Integer horasTotales) {
        this.horasTotales = horasTotales;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getTutor() {
        return tutor;
    }

    public void setTutor(String tutor) {
        this.tutor = tutor;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Integer getHorasCubiertas() {
        return horasCubiertas;
    }

    public void setHorasCubiertas(Integer horasCubiertas) {
        this.horasCubiertas = horasCubiertas;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getL() {
        return l;
    }

    public void setL(Integer l) {
        this.l = l;
    }

    public Integer getM() {
        return m;
    }

    public void setM(Integer m) {
        this.m = m;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getJ() {
        return j;
    }

    public void setJ(Integer j) {
        this.j = j;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
}
