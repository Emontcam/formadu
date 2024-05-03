package com.example.evaluablefinal;

import static com.example.evaluablefinal.Activity.IntroActivity.nombreUsuario;
import static com.example.evaluablefinal.Activity.LoginActivity.comprobarCorreo;
import static com.example.evaluablefinal.Activity.LoginActivity.comprobarSeleccion;
import static com.example.evaluablefinal.Activity.MainActivity.comprobarEncabezado;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.evaluablefinal.controlErrores.Comprobaciones;
import com.example.evaluablefinal.models.Alumno;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AnadirFragment extends Fragment implements Comprobaciones {

    private View view;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private EditText nombre;
    private EditText apellidos;
    private EditText correo;
    private EditText horas;
    private EditText empresa;
    private Button anadir;
    private Alumno alumno;
    private ScrollView scrollView;
    private LinearLayout todoBien;
    private TextView correoN;
    private TextView contrasenaN;
    private int colorDefecto;
    private boolean correcto;


    public AnadirFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Obtener una referencia a FirebaseAuth desde la actividad principal
        mAuth = FirebaseAuth.getInstance();
    }

    public static AnadirFragment newInstance(String param1, String param2) {
        AnadirFragment fragment = new AnadirFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_anadir, container, false);
        //asignamos  las variables
        nombre = view.findViewById(R.id.nombreAlumnoN);
        apellidos = view.findViewById(R.id.apellidosAlumnoN);
        correo = view.findViewById(R.id.correoAlumnoN);
        horas = view.findViewById(R.id.horasAlumnoN);
        empresa = view.findViewById(R.id.empresaAlumnoN);
        anadir = view.findViewById(R.id.anadirAlumno);
        scrollView = view.findViewById(R.id.scrollAnadir);
        todoBien = view.findViewById(R.id.confirmacionAlumnoN);
        todoBien.setVisibility(View.GONE);
        correoN = view.findViewById(R.id.correoN);
        contrasenaN = view.findViewById(R.id.contrasenaN);
        colorDefecto = getContext().getColor(R.color.azulOscuro);

        //base de datos
        // Inicializar Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Referencia a la tabla "alumnos"
        DatabaseReference alumnosRef = mDatabase.child("Alumnos");

        //funciones
        nombre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                comprobarSeleccion(v, hasFocus);
            }
        });
        apellidos.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                comprobarSeleccion(v, hasFocus);
            }
        });
        correo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                comprobarSeleccion(v, hasFocus);
            }
        });
        horas.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                comprobarSeleccion(v, hasFocus);
            }
        });
        empresa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                comprobarSeleccion(v, hasFocus);
            }
        });

        //boton
        anadir.setOnClickListener(l -> {
            crearAlumno(alumnosRef);
            Log.println(Log.INFO, "boton", "boton pulsado");
        });

        todoBien.setOnClickListener(l -> {
            if (scrollView.getVisibility() == View.GONE) {
                scrollView.setVisibility(View.VISIBLE);
                todoBien.setVisibility(View.GONE);
            }
        });

        comprobarEncabezado();
        return view;
    }

    private void crearAlumno(DatabaseReference alumnosRef) {

        if (nombre.getText() != null && !nombre.getText().toString().isEmpty()
                && apellidos.getText() != null && !apellidos.getText().toString().isEmpty()){
            Log.println(Log.INFO, "boton", "comprobando datos");
            //comprobamos los datos
            if(comprobarNombre(nombre.getText().toString(), view.getContext(), nombre, colorDefecto) &&
            comprobarCorreo(correo.getText().toString(), view.getContext(), correo, colorDefecto) &&
            comprobarCantidadEntera(Integer.parseInt(horas.getText().toString()), view.getContext(), horas, colorDefecto)){
                Log.println(Log.INFO, "boton", "datos correctos");
                //si todo es correcto
                alumno = new Alumno(nombre.getText().toString() + " " + apellidos.getText().toString()
                        , correo.getText().toString()
                        , Integer.parseInt(horas.getText().toString())
                        , empresa.getText().toString()
                        , nombreUsuario//tutor
                );

                anadirAlumno(alumnosRef, alumno);

            }
        }
    }

    private void anadirAlumno(DatabaseReference alumnosRef, Alumno alumno) {
        // Obtener un identificador único para el nuevo alumno
        String alumnoId = alumnosRef.push().getKey();

        // Escribir los datos del alumno en la ubicación correspondiente en la base de datos
        alumnosRef.child(alumnoId).setValue(alumno)
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Alumno añadido correctamente");
                    crearCuenta();
                })
                .addOnFailureListener(e -> System.out.println("Error al añadir alumno: " + e.getMessage()));

    }

    private void crearCuenta() {
        mAuth.createUserWithEmailAndPassword(alumno.getCorreo(), alumno.getContrasena())
                .addOnCompleteListener((Activity) requireContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            anadir.setBackgroundColor(getResources().getColor(R.color.verde));
                            scrollView.setVisibility(View.GONE);
                            todoBien.setVisibility(View.VISIBLE);
                            correoN.setText(alumno.getCorreo());
                            contrasenaN.setText(alumno.getContrasena());

                        } else {
                            // If sign in fails, display a message to the user.
                            anadir.setBackgroundColor(getResources().getColor(R.color.red));

                        }
                    }
                });


    }

}