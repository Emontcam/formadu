package com.example.evaluablefinal.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.evaluablefinal.databinding.ActivityMainBinding;


import com.example.evaluablefinal.R;


public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private ImageButton casa;
    private ConstraintLayout encNormal;
    private ConstraintLayout encFoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //asignamos variables
        casa = findViewById(R.id.inicio);
        encNormal = findViewById(R.id.constraintLayou2);
        encFoto = findViewById(R.id.constraintLayout);
        //navigation
        //navController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        //botones
        ImageButton perfil = findViewById(R.id.inicio);
        ImageButton inicio= findViewById(R.id.perfilUser);
        //proximamente
        //ImageButton atras = findViewById();


        //eventos
        //inicio
        perfil.setOnClickListener(view ->{
            //navegar hacia la pagina principal
            int id =  navController.getCurrentDestination().getId();
            if (id == R.id.perfilFragment){
                //encabezado normal (sin foto)
                encFoto.setVisibility(View.GONE);
                encNormal.setVisibility(View.VISIBLE);
                navController.navigate(R.id.action_perfil_a_inicio);
            }

        });

        //perfil
        inicio.setOnClickListener(view ->{
            int id =  navController.getCurrentDestination().getId();
            if (id == R.id.inicioFragment){
                //cuando entre en el perfil se cambia el encabezado
                encNormal.setVisibility(View.GONE);
                encFoto.setVisibility(View.VISIBLE);
                navController.navigate(R.id.action_inicio_a_perfil);

            }
        });

        //hacia atrás
        //atras.setOnClickListener(view ->{
            //navegar hacia atrás
           // navController.navigateUp();
       // });

    }

    public void inicio(View view) {


    }


}
