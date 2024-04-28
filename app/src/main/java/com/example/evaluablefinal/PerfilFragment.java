package com.example.evaluablefinal;



import static com.example.evaluablefinal.Activity.IntroActivity.correoUsuario;
import static com.example.evaluablefinal.Activity.IntroActivity.nombreUsuario;
import static com.example.evaluablefinal.Activity.MainActivity.navController;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;


public class PerfilFragment extends Fragment {

    View view;
    private Button cambiarContrasena;
    private Button cerrar;
    private Button cancelarCambio;
    private LinearLayout nuevaContrasena;
    private LinearLayout confirmContrasena;
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
        nuevaContrasena = view.findViewById(R.id.nuevaContrasenaUser);
        confirmContrasena = view.findViewById(R.id.confirmContrasenaUser);
        cancelarCambio = view.findViewById(R.id.cancelar);
        cambiarContrasena = view.findViewById(R.id.cambioContrasena);
        cerrar = view.findViewById(R.id.cerrarSesion);
        nombre = view.findViewById(R.id.nombreDato);
        correo = view.findViewById(R.id.correoDato);
        nombre.setText(nombreUsuario);
        correo.setText(correoUsuario);

        cerrar.setOnClickListener(l ->{
            FirebaseAuth.getInstance().signOut();
            //volvemos a la intro
            navController.navigate(R.id.action_perfilFragment_to_introActivity);
        });

        cambiarContrasena.setOnClickListener(l -> {
            nuevaContrasena.setVisibility(View.VISIBLE);
            confirmContrasena.setVisibility(View.VISIBLE);
            cancelarCambio.setVisibility(View.VISIBLE);
        });

        cancelarCambio.setOnClickListener(l -> {
            nuevaContrasena.setVisibility(View.GONE);
            confirmContrasena.setVisibility(View.GONE);
            cancelarCambio.setVisibility(View.INVISIBLE);
        });
        return view;
    }

}