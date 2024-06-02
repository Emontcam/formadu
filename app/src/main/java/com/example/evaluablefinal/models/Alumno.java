package com.example.evaluablefinal.models;

import static android.app.PendingIntent.getActivity;
import static com.example.evaluablefinal.Activity.IntroActivity.nombreUsuario;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.evaluablefinal.controlErrores.Comprobaciones;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private Double l = 8.0d;
    private Double m = 8.0d;
    private Double x = 8.0d;
    private Double j = 8.0d;
    private Double v = 8.0d;
    private Date fecha;


    public Alumno(String nombre, String correo, Integer horasTotales, String empresa, String tutor, String imagen) {
        this.nombre = nombre;
        this.correo = correo;
        this.horasTotales = horasTotales;
        this.empresa = empresa;
        this.tutor = tutor;
        this.imagen = imagen.isEmpty()?
                "https://firebasestorage.googleapis.com/v0/b/evaluablefinal.appspot.com/o/logo.png?alt=media&token=a3fdd6b3-fdf7-4044-814a-f3570757ce4d"
                :imagen;
        generarContrasena();
    }

    public Alumno(String id, String nombre, String correo, String empresa, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getL() {
        return l;
    }

    public void setL(Double l) {
        this.l = l;
    }

    public Double getM() {
        return m;
    }

    public void setM(Double m) {
        this.m = m;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getJ() {
        return j;
    }

    public void setJ(Double j) {
        this.j = j;
    }

    public Double getV() {
        return v;
    }

    public void setV(Double v) {
        this.v = v;
    }
}
