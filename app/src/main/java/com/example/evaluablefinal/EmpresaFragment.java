package com.example.evaluablefinal;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.evaluablefinal.Activity.MainActivity.comprobarEncabezado;
import static com.example.evaluablefinal.Activity.MainActivity.fotoEnc;
import static com.example.evaluablefinal.InicioFragment.fuenteTitulo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

public class EmpresaFragment extends Fragment {

    View view;
    private DatabaseReference mDatabase;
    private LinearLayout layaoutAlumnos;
    private TextView nombrePerfil;
    private TextView tipoEmpresa;
    private String nombreEmpresa;
    private Button paginaWeb;

    public EmpresaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nombreEmpresa = getArguments().getString("nombreEmpresa");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_empresa, container, false);
        //comprobamos el encabezado
        comprobarEncabezado();
        // Relacionamos el layout de los alumnos
        layaoutAlumnos = view.findViewById(R.id.alumnosDeEmpresa);
        //borramos los datos
        layaoutAlumnos.removeAllViews();

        //relacionamos los datos
        nombrePerfil = view.findViewById(R.id.nombrePerfilAlumno);
        tipoEmpresa = view.findViewById(R.id.tipoEmpresa);
        paginaWeb = view.findViewById(R.id.paginaWeb);
        //ponemos los datos
        nombrePerfil.setText(nombreEmpresa);


        //Ref a la base de datos
        // Inicializar Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Referencia a la tabla "alumnos"
        DatabaseReference alumnosRef = mDatabase.child("Alumnos");
        DatabaseReference emRef = mDatabase.child("Empresas");
        buscarAlumnos(alumnosRef);
        buscarDatos(emRef);

        return view;
    }

    private void buscarAlumnos(DatabaseReference alumnosRef) {
        // Escuchar cambios en la tabla "alumnos"
        alumnosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //borramos los datos otra vez
                layaoutAlumnos.removeAllViews();
                // Iteramos sobre cada registro en la tabla "alumnos"
                for (DataSnapshot alumnoSnapshot : dataSnapshot.getChildren()) {
                    // Obtener el ID del alumno
                    String alumnoId = alumnoSnapshot.getKey();

                    // Obtener los valores de cada campo del alumno
                    //solo si su empresa coincide con la del perfil
                    if (alumnoSnapshot.child("empresa").getValue(String.class).equals(nombreEmpresa)) {
                        String nombre = alumnoSnapshot.child("nombre").getValue(String.class);
                        String imagen = alumnoSnapshot.child("imagen").getValue(String.class);

                        // datos recuperados
                        Log.d("Alumno", "ID: " + alumnoId + ", Nombre: " + nombre + ", Imagen: " + imagen);
                        mostrarAlumnos(nombre, imagen);
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

    private void mostrarAlumnos(String nombre, String img) {

        if (nombre != null) {
            // Creamos una tarjeta
            LinearLayout tarjeta = new LinearLayout(requireContext());

            // Configuramos los márgenes para la tarjeta
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            params.setMargins(20, 10, 10, 10);

            // Añadimos los parámetros a la tarjeta
            tarjeta.setLayoutParams(params);
            tarjeta.setGravity(Gravity.CENTER);
            tarjeta.setOrientation(LinearLayout.VERTICAL);
            int distancia = 15;
            int margenTitulo = 15;
            // Creamos un texto para el título
            TextView titulo = new TextView(requireContext());
            titulo.setText(nombre);
            titulo.setTextSize(13);
            titulo.setTextColor(getResources().getColor(R.color.azulOscuro));
            // Aplicamos la fuente desde la carpeta "fonts"
            titulo.setTypeface(fuenteTitulo);
            titulo.setPadding(0, margenTitulo, 0, margenTitulo);
            // titulo.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            // titulo.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            //acortamos
            String texto = titulo.getText().toString();
            if (texto.length() > 13) {
                texto = texto.substring(0, 13) + "...";
            }
            titulo.setText(texto);

            // Creamos una imagen
            ImageView imagen = new ImageView(requireContext());
            int altura = 300;
            int anchura = 250;
            // Establecemos el ancho y el alto
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    anchura,
                    altura
            );
            imagen.setLayoutParams(layoutParams);

            // Margenes
            layoutParams.setMargins(0, 10, 0, 10);

            // Establecemos el fondo
            imagen.setBackgroundResource(R.drawable.foto_perfil);

            // Ponemos un padding

            imagen.setPadding(distancia, distancia, distancia, distancia);

            // Ajustar restricciones de diseño
            ConstraintLayout.LayoutParams constraintLayoutParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            constraintLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID; // Fijar parte inferior al padre
            constraintLayoutParams.endToEnd = R.id.toolbar; // Fijar extremo al extremo de la barra de herramientas
            constraintLayoutParams.startToStart = R.id.toolbar; // Fijar inicio al inicio de la barra de herramientas
            constraintLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID; // Fijar parte superior al padre
            constraintLayoutParams.horizontalBias = 0.5f; // Sesgo horizontal de 0.5 (centro)
            imagen.setLayoutParams(constraintLayoutParams);


            // Configuramos las opciones de Glide
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.override(anchura, altura).transform(new MultiTransformation<>(new CircleCrop()));
            if (img != null && img.isEmpty()) {

                Glide.with(this).
                        load(R.drawable.logo).apply(requestOptions).into(imagen);
            } else {
                if (img == null) {
                    Glide.with(this).
                            load(R.drawable.logo).apply(requestOptions).into(imagen);
                } else {
                    // aplicar transformaciones
                    Glide.with(this)
                            .load(img)
                            .apply(requestOptions)
                            .into(imagen);
                }

            }
            // Agregamos la imagen al layout de la tarjeta
            tarjeta.addView(imagen);
            // Añadimos el contenido al layout
            tarjeta.addView(titulo);

            // Agregamos la tarjeta al layout
            layaoutAlumnos.addView(tarjeta);
        } else {
            Log.e(TAG, nombre);
        }
    }

    private void buscarDatos(DatabaseReference empresasRef) {
        // Escuchar cambios en la tabla "empresas"
        empresasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Iteramos sobre cada registro en la tabla "alumnos"
                for (DataSnapshot emSnapshot : dataSnapshot.getChildren()) {
                    // Obtener el ID del alumno
                    String emId = emSnapshot.getKey();

                    // Obtener los valores de cada campo del alumno
                    //solo si su empresa coincide con la del perfil
                    if (emSnapshot.child("nombre").getValue(String.class).equals(nombreEmpresa)) {
                        String tipo = emSnapshot.child("tipo").getValue(String.class);
                        String imagen = emSnapshot.child("imagen").getValue(String.class);
                        String url = emSnapshot.child("web").getValue(String.class);
                        tipoEmpresa.setText(tipo);
                        agregarFotos(imagen);
                        if(url == null || url.isEmpty()){
                           paginaWeb.setVisibility(View.GONE);
                        }else{
                            paginaWeb.setOnClickListener(b -> {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(url));
                                startActivity(intent);
                            });
                        }

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