package com.example.evaluablefinal;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.evaluablefinal.Activity.MainActivity.comprobarEncabezado;
import static com.example.evaluablefinal.Activity.MainActivity.fotoEnc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.HashMap;

public class PerfilAlumnoFragment extends Fragment {

    View view;
    private DatabaseReference mDatabase;
    private LinearLayout layaoutAlumnos;
    private TextView nombrePerfil;
    private TextView nombreTutor;
    private ProgressBar barraHoras;
    private TextView horasRealizadas;
    private String nombreAlumno;
    //horas cada dia de la semana
    private TextView horasDia;

    public PerfilAlumnoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nombreAlumno = getArguments().getString("nombreAlumno");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_perfil_alumno, container, false);

        nombrePerfil = view.findViewById(R.id.nombrePerfilAlumno);
        nombreTutor = view.findViewById(R.id.tutorAlumno);
        barraHoras = view.findViewById(R.id.progressBarHoras);
        horasRealizadas = view.findViewById(R.id.horas);
        nombrePerfil.setText(nombreAlumno);
        //comprobamos el encabezado
        comprobarEncabezado();
        //Ref a la base de datos
        // Inicializar Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Referencia a la tabla "alumnos"
        DatabaseReference alumnosRef = mDatabase.child("Alumnos");
        buscarDatos(alumnosRef);
        return view;
    }

    private void buscarDatos(DatabaseReference alumnosRef) {
        // Escuchar cambios en la tabla "alumnos"
        alumnosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Iteramos sobre cada registro en la tabla "alumnos"
                for (DataSnapshot alumnoSnapshot : dataSnapshot.getChildren()) {
                    // Obtener el ID del alumno
                    String emId = alumnoSnapshot.getKey();

                    if (alumnoSnapshot.child("nombre").getValue(String.class).equals(nombreAlumno)) {
                        // Obtenemos los valores de cada campo del alumno
                       String tutor = alumnoSnapshot.child("tutor").getValue(String.class);
                       String imagen = alumnoSnapshot.child("imagen").getValue(String.class);

                        int horasTotales = alumnoSnapshot.hasChild("horasTotales") ?
                                alumnoSnapshot.child("horasTotales").getValue(Integer.class) : 0;
                        int horasHechas = alumnoSnapshot.hasChild("horasCubiertas") ?
                                alumnoSnapshot.child("horasCubiertas").getValue(Integer.class) : 0;
                        Double horasLunes = alumnoSnapshot.hasChild("l") ?
                                alumnoSnapshot.child("l").getValue(Double.class) : 0.0;
                        Double horasMartes = alumnoSnapshot.hasChild("m") ?
                                alumnoSnapshot.child("m").getValue(Double.class) : 0.0;
                        Double horasMiercoles = alumnoSnapshot.hasChild("x") ?
                                alumnoSnapshot.child("x").getValue(Double.class) : 0.0;
                        Double horasJueves = alumnoSnapshot.hasChild("j") ?
                                alumnoSnapshot.child("j").getValue(Double.class) : 0.0;
                        Double horasViernes = alumnoSnapshot.hasChild("v") ?
                                alumnoSnapshot.child("v").getValue(Double.class) : 0.0;

                        //asignamos los datos
                        nombreTutor.setText(String.format("%s %s", getResources().getText(R.string.tutor), tutor));
                        barraHoras.setMax(horasTotales);
                        barraHoras.setProgress(horasHechas, true);
                        horasRealizadas.setText(horasTotales - horasHechas + getResources().getString(R.string.horasN));
                        //horas por semana
                        horasSemana(horasLunes, horasMartes, horasMiercoles, horasJueves, horasViernes);

                        agregarFotos(imagen);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores de lectura de la base de datos
                Log.e("Firebase", "Error al leer datos", databaseError.toException());
            }
        });
    }

    private void horasSemana(Double horasLunes, Double horasMartes, Double horasMiercoles, Double horasJueves, Double horasViernes) {
        String h = " " + getResources().getString(R.string.horasN);
        horasDia = view.findViewById(R.id.horasL);
        horasDia.setText(String.format("%s%s", horasLunes, h));
        horasDia = view.findViewById(R.id.horasM);
        horasDia.setText(String.format("%s%s", horasMartes, h));
        horasDia = view.findViewById(R.id.horasX);
        horasDia.setText(String.format("%s%s", horasMiercoles, h));
        horasDia = view.findViewById(R.id.horasJ);
        horasDia.setText(String.format("%s%s", horasJueves, h));
        horasDia = view.findViewById(R.id.horasV);
        horasDia.setText(String.format("%s%s", horasViernes, h));
    }

    public void agregarFotos(String img) {
        // Configuramos las opciones de Glide
        RequestOptions requestOptions = new RequestOptions();
        int altura = 300;
        int anchura = 250;
        requestOptions = requestOptions.override(anchura, altura)
                .transform(new MultiTransformation<>(new CircleCrop()));

        if (img != null && img.isEmpty()) {
            Log.d(TAG, "la foto esta vacia");
            Glide.with(this).
                    load(R.drawable.logo).apply(requestOptions).into(fotoEnc);
        } else {
            if (img == null) {
                Glide.with(this).
                        load(R.drawable.logo).apply(requestOptions).into(fotoEnc);
                Log.d(TAG, "la foto es nula");
            } else {
                // aplicar transformaciones
                Glide.with(this)
                        .load(img)
                        .apply(requestOptions)
                        .into(fotoEnc);
            }

        }


    }
}