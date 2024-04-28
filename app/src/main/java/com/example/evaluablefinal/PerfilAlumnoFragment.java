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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PerfilAlumnoFragment extends Fragment {

    View view;
    private DatabaseReference mDatabase;
    private LinearLayout layaoutAlumnos;
    private TextView nombrePerfil;
    private TextView nombreTutor;
    private String nombreAlumno;
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
                for (DataSnapshot emSnapshot : dataSnapshot.getChildren()) {
                    // Obtener el ID del alumno
                    String emId = emSnapshot.getKey();

                    // Obtener los valores de cada campo del alumno
                    //solo si su empresa coincide con la del perfil
                    if (emSnapshot.child("nombre").getValue(String.class).equals(nombreAlumno)) {
                        String tutor = emSnapshot.child("tutor").getValue(String.class);
                        String imagen = emSnapshot.child("imagen").getValue(String.class);
                        String url = emSnapshot.child("web").getValue(String.class);
                        nombreTutor.setText(getResources().getText(R.string.tutor) + " " +tutor);
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
    public void agregarFotos(String img) {
        // Configuramos las opciones de Glide
        RequestOptions requestOptions = new RequestOptions();
        int altura = 300;
        int anchura = 250;
        requestOptions.override(anchura, altura).transform(new MultiTransformation<>(new CircleCrop()));

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