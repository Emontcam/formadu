package com.example.evaluablefinal;


import static com.example.evaluablefinal.Activity.IntroActivity.correoUsuario;
import static com.example.evaluablefinal.Activity.IntroActivity.fotoPerfilUsuario;
import static com.example.evaluablefinal.Activity.IntroActivity.idUsuario;
import static com.example.evaluablefinal.Activity.IntroActivity.idioma;
import static com.example.evaluablefinal.Activity.IntroActivity.nombreUsuario;
import static com.example.evaluablefinal.Activity.LoginActivity.PREFS_NAME;
import static com.example.evaluablefinal.Activity.LoginActivity.PREF_IDIOM;
import static com.example.evaluablefinal.Activity.LoginActivity.mDatabase;
import static com.example.evaluablefinal.Activity.MainActivity.navController;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.evaluablefinal.Activity.CargaActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Locale;


public class PerfilFragment extends Fragment {

    View view;
    private Button ingles;
    private Button espanol;
    private Button cerrar;
    private TextView nombre;
    private TextView correo;


    public PerfilFragment() {
        // Required empty public constructor
    }


    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_perfil, container, false);

        ingles = view.findViewById(R.id.ingles);
        espanol = view.findViewById(R.id.espanol);
        cerrar = view.findViewById(R.id.cerrarSesion);
        nombre = view.findViewById(R.id.nombreDato);
        correo = view.findViewById(R.id.correoDato);
        nombre.setText(nombreUsuario);
        correo.setText(correoUsuario);

        obtenerIdioma();


        cerrar.setOnClickListener(l -> {
            nombreUsuario = "";
            correoUsuario = "";
            fotoPerfilUsuario = "";
            idioma = "es";
            FirebaseAuth.getInstance().signOut();
            //volvemos a la intro
            navController.navigate(R.id.action_perfilFragment_to_introActivity);
        });

        //idioma
        ingles.setOnClickListener(l -> {
            cambioIdiomaApp("en");
            ingles.setBackgroundColor(getResources().getColor(R.color.naranja));
            espanol.setBackgroundColor(getResources().getColor(R.color.azulMenosSaturado));
        });

        espanol.setOnClickListener(l -> {
            cambioIdiomaApp("es");
            espanol.setBackgroundColor(getResources().getColor(R.color.naranja));
            ingles.setBackgroundColor(getResources().getColor(R.color.azulMenosSaturado));
        });


        return view;
    }

    private void cambioIdiomaApp(String idioma) {
        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        guardarIdioma(idioma);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    private void guardarIdioma(String idioma) {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_IDIOM, idioma);
        cambiarIdioma(idioma);
        editor.apply();
    }

    public void cambiarIdioma(String idioma) {
        DatabaseReference userRef = mDatabase.child("Profesores");
        // Escribir los datos del profesor en la ubicación correspondiente en la base de datos
        userRef.child(idUsuario).child(PREF_IDIOM).setValue(idioma)
                .addOnSuccessListener(aVoid -> {
                    reiniciarActivity(idioma);
                })
                .addOnFailureListener(e -> System.out.println());
    }

    private void obtenerIdioma(){
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String idiomaPref = prefs.getString(PREF_IDIOM, "es");
        if(idiomaPref.equals("es")){
            espanol.setBackgroundColor(getResources().getColor(R.color.naranja));
            ingles.setBackgroundColor(getResources().getColor(R.color.azulMenosSaturado));
        }else if (idiomaPref.equals("en")){
            ingles.setBackgroundColor(getResources().getColor(R.color.naranja));
            espanol.setBackgroundColor(getResources().getColor(R.color.azulMenosSaturado));
        }
    }

    private void reiniciarActivity(String idioma) {
        Intent intent = new Intent(getActivity(), CargaActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(PREF_IDIOM, idioma);
        intent.putExtra("fragment", "PerfilFragment");
        startActivity(intent);
        getActivity().finish();
    }


}