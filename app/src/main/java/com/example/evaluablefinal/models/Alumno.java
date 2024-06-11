package com.example.evaluablefinal.models;

import static android.app.PendingIntent.getActivity;

import com.example.evaluablefinal.controlErrores.Comprobaciones;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

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
    private Date fechaInicio;


    public Alumno(String nombre, String correo, Integer horasTotales, String empresa, String tutor, String imagen, Date fecha) {
        this.nombre = nombre;
        this.correo = correo;
        this.horasTotales = horasTotales;
        this.empresa = empresa;
        this.tutor = tutor;
        this.imagen = imagen.isEmpty()?
                "https://firebasestorage.googleapis.com/v0/b/evaluablefinal.appspot.com/o/logo.png?alt=media&token=a3fdd6b3-fdf7-4044-814a-f3570757ce4d"
                :imagen;
        this.fechaInicio = fecha;

    }

    public Alumno(String id, String nombre, String correo, String empresa, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.empresa = empresa;
        this.imagen = imagen;

    }

    public Double horasHechas(){

        Double[] horasSemana  = {0.0, l, m, x, j, v, 0.0};
        //obtenemos la fecha actual
        Date fechaAct = new Date();

        // Calcular la diferencia entre las fechas en milisegundos
        Double diferenciaMs = (double) (fechaAct.getTime() - fechaInicio.getTime());

        // Obtener la diferencia en horas
        Double horasTotales = diferenciaMs / (1000 * 60 * 60);

        // Utilizamos Calendar para iterar entre las fechas
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaInicio);
        // Iteramos desde la fecha de inicio hasta la fecha de fin
        while (cal.getTime().before(fechaAct) || cal.getTime().equals(fechaAct)) {
            int diaSemana = cal.get(Calendar.DAY_OF_WEEK) - 1; // -1 para ajustar a 0=Domingo, 1=Lunes, ...
            horasTotales += horasSemana[diaSemana];
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        return (double) Math.round(horasTotales);

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

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
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
