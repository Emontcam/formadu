package com.example.evaluablefinal;

import static android.app.Activity.RESULT_OK;
import static com.example.evaluablefinal.Activity.IntroActivity.nombreUsuario;
import static com.example.evaluablefinal.Activity.LoginActivity.comprobarSeleccion;
import static com.example.evaluablefinal.Activity.MainActivity.comprobarEncabezado;
import static com.example.evaluablefinal.transformations.BitmapUtils.bitmapToUri;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Fragmento para añadir nuevos alumnos a la base de datos de Firebase.
 */
public class AnadirFragment extends Fragment implements Comprobaciones {

    private View view;
    private static final int PICK_IMAGE_REQUEST = 1;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ImageButton cerrarTodoBien;

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
    private TextView errorN;
    private TextView errorA;
    private TextView errorC;
    private TextView errorH;
    private TextView errorE;
    private TextView errorAnadir;
    private ImageView fotoPerfil;
    private int colorDefecto;

    private Uri uri = Uri.parse("");
    private boolean correcto;

    /**
     * Constructor vacío requerido para la creación del fragmento.
     */
    public AnadirFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Obtener una referencia a FirebaseAuth desde la actividad principal
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        view = inflater.inflate(R.layout.fragment_anadir, container, false);
        // Inicializar vistas
        nombre = view.findViewById(R.id.nombreAlumnoN);
        apellidos = view.findViewById(R.id.apellidosAlumnoN);
        correo = view.findViewById(R.id.correoAlumnoN);
        horas = view.findViewById(R.id.horasAlumnoN);
        empresa = view.findViewById(R.id.empresaAlumnoN);
        anadir = view.findViewById(R.id.anadirAlumno);
        scrollView = view.findViewById(R.id.scrollAnadir);
        scrollView.setVisibility(View.VISIBLE);
        todoBien = view.findViewById(R.id.confirmacionAlumnoN);
        todoBien.setVisibility(View.GONE);
        correoN = view.findViewById(R.id.correoN);
        contrasenaN = view.findViewById(R.id.contrasenaN);
        fotoPerfil = view.findViewById(R.id.fotoPerfilAlumno);
        colorDefecto = getContext().getColor(R.color.azulOscuro);
        cerrarTodoBien = view.findViewById(R.id.cerrarConfirm);

        // Inicializar mensajes de error
        errorN = view.findViewById(R.id.errorNombre);
        errorA = view.findViewById(R.id.errorApellidos);
        errorC = view.findViewById(R.id.errorCorreo);
        errorH = view.findViewById(R.id.errorHoras);
        errorE = view.findViewById(R.id.errorEmpresa);
        errorAnadir = view.findViewById(R.id.errorAnadir);

        // Inicializar Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Referencia a la tabla "alumnos"
        DatabaseReference alumnosRef = mDatabase.child("Alumnos");

        // Asignar listeners a los campos de texto
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


        // Asignar listener al botón de añadir
        anadir.setOnClickListener(l -> {
            crearAlumno(alumnosRef);
            Log.println(Log.INFO, "boton", "boton pulsado");
        });

        // Asignar listener a la vista de confirmación
        todoBien.setOnClickListener(l -> {
            if (scrollView.getVisibility() == View.GONE) {
                scrollView.setVisibility(View.VISIBLE);
                todoBien.setVisibility(View.GONE);
            }
        });

        // Asignar listener al botón de cerrar confirmación
        cerrarTodoBien.setOnClickListener(l -> {
            if (scrollView.getVisibility() == View.GONE) {
                scrollView.setVisibility(View.VISIBLE);
                todoBien.setVisibility(View.GONE);
            }
        });

        // Asignar listener a la imagen de perfil
        fotoPerfil.setOnClickListener(l -> {
            abrirGaleria();
        });

        comprobarEncabezado();
        return view;
    }

    /**
     * Crea un nuevo alumno y lo añade a la base de datos.
     * @param alumnosRef Referencia a la base de datos donde se guardarán los datos del alumno.
     */
    private void crearAlumno(DatabaseReference alumnosRef) {

        if (nombre.getText() != null && !nombre.getText().toString().isEmpty()
                && apellidos.getText() != null && !apellidos.getText().toString().isEmpty()) {
            Log.println(Log.INFO, "boton", "comprobando datos");
            //comprobamos los datos
            if (comprobarNombre(nombre.getText().toString(), view.getContext(), nombre, colorDefecto, errorN) &&
                    comprobarApellidos(apellidos.getText().toString(), view.getContext(), apellidos, colorDefecto, errorA) &&
                    comprobarCorreo(correo.getText().toString(), view.getContext(), correo, colorDefecto, errorC) &&
                    comprobarCantidadEntera(Integer.parseInt(horas.getText().toString()), view.getContext(), horas, colorDefecto, errorH) &&
                    comprobarEmpresas(empresa.getText().toString(), view.getContext(), empresa, colorDefecto, errorE)
                    && comprobarImagen(uri, fotoPerfil)) {
                //si todo es correcto
                alumno = new Alumno(nombre.getText().toString() + " " + apellidos.getText().toString()
                        , correo.getText().toString()
                        , Integer.parseInt(horas.getText().toString())
                        , empresa.getText().toString()
                        , nombreUsuario//tutor
                        , uri.toString()
                );

                anadirAlumno(alumnosRef, alumno);

            }
        } else {
            errorAnadir.setText(R.string.e_vacio);
            errorAnadir.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Añade un nuevo alumno a la base de datos de Firebase.
     * @param alumnosRef Referencia a la base de datos.
     * @param alumno Objeto Alumno que se añadirá.
     */
    private void anadirAlumno(DatabaseReference alumnosRef, Alumno alumno) {
        Alumno alumnoNuevo = anadirHoras(alumno);
        // Obtener un identificador único para el nuevo alumno
        String alumnoId = alumnosRef.push().getKey();
        // Escribir los datos del alumno en la ubicación correspondiente en la base de datos
        alumnosRef.child(alumnoId).setValue(alumnoNuevo)
                .addOnSuccessListener(aVoid -> mostrarMensajeCorrecto())
                .addOnFailureListener(e -> mostrarMensajeIncorrecto());

    }

    /**
     * Añade horas específicas al objeto Alumno.
     * @param alumno Objeto Alumno al que se añadirán las horas.
     * @return Objeto Alumno con las horas añadidas.
     */
    private Alumno anadirHoras(Alumno alumno) {
        EditText horasL = view.findViewById(R.id.lunesHorasN);
        EditText horasM = view.findViewById(R.id.martesHorasN);
        EditText horasX = view.findViewById(R.id.miercolesHorasN);
        EditText horasJ = view.findViewById(R.id.juevesHorasN);
        EditText horasV = view.findViewById(R.id.viernesHorasN);
        if (!horasL.getText().toString().isEmpty()) {
            alumno.setL(Double.parseDouble(horasL.getText().toString()));
        }
        if (!horasM.getText().toString().isEmpty()) {
            alumno.setM(Double.parseDouble(horasM.getText().toString()));
        }
        if (!horasX.getText().toString().isEmpty()) {
            alumno.setX(Double.parseDouble(horasX.getText().toString()));
        }
        if (!horasJ.getText().toString().isEmpty()) {
            alumno.setJ(Double.parseDouble(horasJ.getText().toString()));
        }
        if (!horasV.getText().toString().isEmpty()) {
            alumno.setV(Double.parseDouble(horasV.getText().toString()));
        }
        return alumno;
    }

    /**
     * Muestra un mensaje de confirmación tras añadir el alumno correctamente.
     */
    public void mostrarMensajeCorrecto() {
        anadir.setBackgroundColor(getResources().getColor(R.color.verde));
        scrollView.setVisibility(View.GONE);
        todoBien.setVisibility(View.VISIBLE);
        correoN.setText(alumno.getCorreo());
        contrasenaN.setText(alumno.getContrasena());

    }

    /**
     * Muestra un mensaje de error si no se pudo añadir el alumno.
     */
    public void mostrarMensajeIncorrecto() {
        anadir.setBackgroundColor(getResources().getColor(R.color.red));
        errorAnadir.setVisibility(View.VISIBLE);
        errorAnadir.setText(R.string.e_repeticion);

    }

    /**
     * Abre la galería para seleccionar una imagen.
     */
    private void abrirGaleria() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), PICK_IMAGE_REQUEST);
    }

    // Manejamos el resultado de la selección de la imagen
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            try {
                uri = data.getData();
                ContentResolver resolver = requireContext().getContentResolver();
                // Obtener la imagen en formato Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver, uri);

                // Redimensionar la imagen a un tamaño estándar (por ejemplo, 300x300 píxeles)
                Bitmap imgEdit = Bitmap.createScaledBitmap
                        (bitmap, 300, 300, false);
                // fotoPerfil.setImageURI();
                //cambiamos el valor de uri por la imagen editada
                uri = bitmapToUri(requireContext(), imgEdit);
                comprobarImagen(uri, fotoPerfil);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}