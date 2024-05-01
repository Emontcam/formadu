package com.example.evaluablefinal.Activity;

import static com.example.evaluablefinal.Activity.IntroActivity.fotoPerfilUsuario;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.evaluablefinal.databinding.ActivityMainBinding;


import com.example.evaluablefinal.R;

import java.util.Objects;


public class MainActivity extends BaseActivity {
    View view;

    private ActivityMainBinding binding;
    private ImageView fotoPerfilEnc;
    public static ImageView fotoEnc;
    private ImageButton casa;
    private static ConstraintLayout encNormal;
    private static ConstraintLayout encFoto;
    private static ConstraintLayout encFotoDif;
    public static NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //asignamos variables
        casa = findViewById(R.id.inicio);
        encNormal = findViewById(R.id.constraintLayou2);
        encFoto = findViewById(R.id.constraintLayout);
        encFotoDif = findViewById(R.id.constraintLayout3);
        fotoPerfilEnc = findViewById(R.id.fotoPerfilUser);
        fotoEnc = findViewById(R.id.foto);
        //navigation
        //navController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();

        //proximamente
        //ImageButton atras = findViewById();

        //eventos
        //comprobamos el fragment en el que está
        comprobarEncabezado();
        //inicio
        binding.perfil.setOnClickListener(this::cambioPantalla);

        //perfil
        binding.inicio.setOnClickListener(this::cambioPantalla);

        //anadir
        binding.masBoton.setOnClickListener(this::cambioPantalla);
        agregarFotos();
        //hacia atrás
        //atras.setOnClickListener(view ->{
        //navegar hacia atrás
        // navController.navigateUp();
        // });

    }


    public void cambioPantalla(View boton) {
        int id = Objects.requireNonNull(navController.getCurrentDestination()).getId();
        int idBoton = boton.getId();

        if (idBoton == R.id.inicio) {
            if (id == R.id.perfilFragment) {
                navController.navigate(R.id.action_perfil_a_inicio);
            } else if (id == R.id.empresaFragment) {
                navController.navigate(R.id.action_empresaFragment_to_inicioFragment);
            }else if (id == R.id.perfilAlumnoFragment) {
                navController.navigate(R.id.action_perfilAlumnoFragment2_to_inicioFragment3);
            } else if (id == R.id.anadirFragment) {
                navController.navigate(R.id.action_anadirFragment_to_inicioFragment);
            }

        }else if (idBoton == R.id.perfil) {
            if (id == R.id.inicioFragment) {
                navController.navigate(R.id.action_inicio_a_perfil);
            } else if (id == R.id.empresaFragment) {
                navController.navigate(R.id.action_empresaFragment_to_perfilFragment);
            }else if (id == R.id.perfilAlumnoFragment) {
                navController.navigate(R.id.action_perfilAlumnoFragment_to_perfilFragment);
            } else if (id == R.id.anadirFragment) {
                navController.navigate(R.id.action_anadirFragment_to_perfilFragment);
            }
        }else if (idBoton == R.id.masBoton) {
            if (id == R.id.inicioFragment) {
                navController.navigate(R.id.action_inicioFragment_to_anadirFragment);
            } else if (id == R.id.empresaFragment) {
                navController.navigate(R.id.action_empresaFragment_to_anadirFragment);
            }else if (id == R.id.perfilAlumnoFragment) {
                navController.navigate(R.id.action_perfilAlumnoFragment_to_anadirFragment);
            } else if (id == R.id.perfilFragment) {
                navController.navigate(R.id.action_perfilFragment_to_anadirFragment);
            }
        }
        comprobarEncabezado();
    }

    @SuppressLint("ResourceAsColor")
    public static void comprobarEncabezado() {
        int id = navController.getCurrentDestination().getId();
        if (id == R.id.inicioFragment || id == R.id.anadirFragment) {
            encFoto.setVisibility(View.GONE);
            encFotoDif.setVisibility(View.GONE);
            encNormal.setVisibility(View.VISIBLE);

        } else if (id == R.id.perfilFragment) {
            encFoto.setVisibility(View.VISIBLE);
            encFotoDif.setVisibility(View.GONE);
            encNormal.setVisibility(View.GONE);

        } else if (id == R.id.empresaFragment || id == R.id.perfilAlumnoFragment) {
            encFoto.setVisibility(View.GONE);
            encFotoDif.setVisibility(View.VISIBLE);
            encNormal.setVisibility(View.GONE);

        }

    }

    public void agregarFotos() {
        // Configuramos las opciones de Glide
        RequestOptions requestOptions = new RequestOptions();
        int altura = 300;
        int anchura = 250;
        requestOptions = requestOptions.override(anchura, altura).transform(new MultiTransformation<>(new CircleCrop()));
        Glide.with(this).
                load(R.drawable.logo).apply(requestOptions).into(fotoEnc);
        if (fotoPerfilUsuario != null && fotoPerfilUsuario.isEmpty()) {
            Log.d(TAG, "la foto esta vacia");
            Glide.with(this).
                    load(R.drawable.logo).apply(requestOptions).into(fotoPerfilEnc);
        } else {
            if (fotoPerfilUsuario == null) {
                Glide.with(this).
                        load(R.drawable.logo).apply(requestOptions).into(fotoPerfilEnc);
                Log.d(TAG, "la foto es nula");
            } else {
                // aplicar transformaciones
                Glide.with(this)
                        .load(fotoPerfilUsuario)
                        .apply(requestOptions)
                        .into(fotoPerfilEnc);
            }

        }


    }

}
