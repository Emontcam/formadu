package com.example.evaluablefinal;

import static android.app.Activity.RESULT_OK;
import static com.example.evaluablefinal.Activity.IntroActivity.nombreUsuario;
import static com.example.evaluablefinal.Activity.LoginActivity.comprobarSeleccion;
import static com.example.evaluablefinal.Activity.MainActivity.comprobarEncabezado;
import static com.example.evaluablefinal.Activity.MainActivity.navController;
import static com.example.evaluablefinal.InicioFragment.alumnos;
import static com.example.evaluablefinal.InicioFragment.empresas;
import static com.example.evaluablefinal.transformations.BitmapUtils.bitmapToUri;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evaluablefinal.controlErrores.Comprobaciones;
import com.example.evaluablefinal.models.Alumno;
import com.example.evaluablefinal.models.Empresa;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

/**
 * Fragmento para añadir nuevos alumnos a la base de datos de Firebase.
 */
public class AnadirFragment extends Fragment implements Comprobaciones {

    private View view;
    private static final int PICK_IMAGE_REQUEST = 1;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private EditText nombre;
    private EditText apellidos;
    private EditText correo;
    private EditText horas;
    private RadioGroup empresasGrupo;
    private EditText horasDia;
    private Button anadir;
    private Alumno alumno;
    private ScrollView scrollView;
    private LinearLayout todoBien;
    private LinearLayout todoMal;
    private TextView errorMsg;
    private TextView nombreN;
    private TextView errorN;
    private TextView errorA;
    private TextView errorC;
    private TextView errorH;
    private TextView errorD;
    private TextView errorE;
    private TextView errorAnadir;

    private ImageView fotoPerfil;
    private int colorDefecto;

    private Uri uri = Uri.parse("");

    /**
     * Constructor vacío requerido para la creación del fragmento.
     */
    public AnadirFragment() {
    }

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
        horasDia = view.findViewById(R.id.lunesHorasN);
        empresasGrupo = view.findViewById(R.id.grupoEmpresas);
        anadir = view.findViewById(R.id.anadirAlumno);

        scrollView = view.findViewById(R.id.scrollAnadir);
        scrollView.setVisibility(View.VISIBLE);
        todoBien = view.findViewById(R.id.confirmacionAlumnoN);
        todoBien.setVisibility(View.GONE);
        todoMal = view.findViewById(R.id.errorAlumnoN);
        todoMal.setVisibility(View.GONE);
        errorMsg = view.findViewById(R.id.nombreError);

        nombreN = view.findViewById(R.id.nombreN);
        fotoPerfil = view.findViewById(R.id.fotoPerfilAlumno);
        colorDefecto = requireContext().getColor(R.color.azulOscuro);

        // Inicializar mensajes de error
        errorN = view.findViewById(R.id.errorNombre);
        errorA = view.findViewById(R.id.errorApellidos);
        errorC = view.findViewById(R.id.errorCorreo);
        errorH = view.findViewById(R.id.errorHoras);
        errorE = view.findViewById(R.id.errorEmpresa);
        errorD = view.findViewById(R.id.errorHorasDia);
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

        // Asignar listener al botón de añadir
        anadir.setOnClickListener(l -> {
            crearAlumno(alumnosRef);
            Log.println(Log.INFO, "boton", "boton pulsado");
        });

        // Asignar listener a la vista de confirmación
        todoBien.setOnClickListener(l -> {
            if (scrollView.getVisibility() == View.GONE) {
                todoBien.setVisibility(View.GONE);
                navController.navigate(R.id.action_anadirFragment_to_inicioFragment);
            }
        });

        // Asignar listener a la vista de error
        todoMal.setOnClickListener(l -> {
            if (scrollView.getVisibility() == View.GONE) {
                scrollView.setVisibility(View.VISIBLE);
                todoMal.setVisibility(View.GONE);
            }
        });

        // Asignar listener a la imagen de perfil
        fotoPerfil.setOnClickListener(l -> {
            abrirGaleria();
        });

        mostrarEmpresasSeleccion();
        comprobarEncabezado();
        return view;
    }

    /**
     * Crea un nuevo alumno y lo añade a la base de datos.
     *
     * @param alumnosRef Referencia a la base de datos donde se guardarán los datos del alumno.
     */
    private void crearAlumno(DatabaseReference alumnosRef) {

        if (nombre.getText() != null && !nombre.getText().toString().isEmpty()
                && apellidos.getText() != null && !apellidos.getText().toString().isEmpty()) {

            RadioButton empresaSelect = view.findViewById(empresasGrupo.getCheckedRadioButtonId());
            if (datosCorrectos(empresaSelect.getText().toString())) {
                //si todo es correcto
                alumno = new Alumno(nombre.getText().toString() + " " + apellidos.getText().toString()
                        , correo.getText().toString()
                        , Integer.parseInt(horas.getText().toString())
                        , empresaSelect.getText().toString()
                        , nombreUsuario//tutor
                        , uri.toString()
                );
                anadirAlumno(alumnosRef, alumno);
            }
        } else {
            mostrarMensajeIncorrecto(getResources().getString(R.string.e_vacio));
            errorAnadir.setVisibility(View.VISIBLE);
        }
    }

    private boolean datosCorrectos(String empresa) {
        boolean valid = true;

        if (!comprobarNombre(nombre.getText().toString(), view.getContext(), nombre, colorDefecto, errorN)) {
            mostrarMensajeIncorrecto(getResources().getString(R.string.e_nombre));
            valid = false;
        }

        if (!comprobarApellidos(apellidos.getText().toString(), view.getContext(), apellidos, colorDefecto, errorA)) {
            mostrarMensajeIncorrecto(getResources().getString(R.string.e_apellidos));
            valid = false;
        }

        if (!comprobarCorreo(correo.getText().toString(), view.getContext(), correo, colorDefecto, errorC)) {
            mostrarMensajeIncorrecto(getResources().getString(R.string.e_correo));
            valid = false;
        }

        String horasStr = horas.getText().toString();
        if (!comprobarLong(view.getContext(), horasStr, horas, colorDefecto, errorH) || !comprobarCantidadEntera(Long.parseLong(horasStr), view.getContext(), horas, colorDefecto, errorH)) {
            mostrarMensajeIncorrecto(errorH.getText().toString());
            valid = false;
        }

        if (!comprobarHorasDia()) {
            valid = false;
        }

          if (!comprobarEmpresas(empresa, view.getContext(), errorE)) {
              mostrarMensajeIncorrecto(getResources().getString(R.string.e_empresa));
              valid = false;
          }

        if (!comprobarImagen(uri, fotoPerfil)) {
            mostrarMensajeIncorrecto(getResources().getString(R.string.e_img));
            valid = false;
        }

        return valid;
    }

    private boolean comprobarHorasDia() {

        Double horasAlumno = 0.0d;

        int[] horasIds = {
                R.id.lunesHorasN,
                R.id.martesHorasN,
                R.id.miercolesHorasN,
                R.id.juevesHorasN,
                R.id.viernesHorasN
        };

        for (int id : horasIds) {
            horasDia = view.findViewById(id);
            String horasDiaStr = horasDia.getText().toString();
            if (!comprobarDouble(view.getContext(), horasDiaStr, horasDia, colorDefecto, errorD)
                    || !intervaloHorasDia(view.getContext()
                    , Double.parseDouble(horasDiaStr), horasDia, colorDefecto, errorD)) {
                mostrarMensajeIncorrecto(errorD.getText().toString());
                return false;
            }
            horasAlumno = horasAlumno + Double.parseDouble(horasDiaStr);
        }

        //comprobamos que la suma de todas las horas no supere las 40h semanales
        if (!comprobarMaxHorasSemana(view.getContext(), horasAlumno, horasDia, colorDefecto, errorD)) {
            mostrarMensajeIncorrecto(errorD.getText().toString());
            return false;
        } else {
            return true;
        }

    }

    private boolean comprobarExistencia(DatabaseReference alumnosRef, Alumno alumno) {

        for (int i = 0; i < alumnos.size(); i++) {
            if (alumnos.get(i).getCorreo().equals(alumno.getCorreo())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Añade un nuevo alumno a la base de datos de Firebase.
     *
     * @param alumnosRef Referencia a la base de datos.
     * @param alumno     Objeto Alumno que se añadirá.
     */
    private void anadirAlumno(DatabaseReference alumnosRef, Alumno alumno) {
        if (!comprobarExistencia(alumnosRef, alumno)) {
            Alumno alumnoNuevo = anadirHoras(alumno);
            // Obtener un identificador único para el nuevo alumno
            String alumnoId = alumnosRef.push().getKey();
            // Escribir los datos del alumno en la ubicación correspondiente en la base de datos
            alumnosRef.child(alumnoId).setValue(alumnoNuevo)
                    .addOnSuccessListener(aVoid -> mostrarMensajeCorrecto())
                    .addOnFailureListener(e ->
                            mostrarMensajeIncorrecto(getResources().getString(R.string.e_random)));
        } else {
            mostrarMensajeIncorrecto(getResources().getString(R.string.e_duplicado));
        }


    }

    /**
     * Añade horas específicas al objeto Alumno.
     *
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
        nombreN.setText(alumno.getNombre());
    }

    /**
     * Muestra un mensaje de error si no se pudo añadir el alumno.
     */
    public void mostrarMensajeIncorrecto(String msg) {
        anadir.setBackgroundColor(getResources().getColor(R.color.red));
        scrollView.setVisibility(View.GONE);
        errorMsg.setText(msg);
        todoMal.setVisibility(View.VISIBLE);


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

    private void mostrarEmpresasSeleccion() {
        //eliminamos las empresas anteriores
        empresasGrupo.removeAllViews();
        for (Empresa empresa : empresas) {
            RadioButton radioButton = new RadioButton(requireContext());

            // Configuramos el texto del RadioButton con el nombre de la empresa
            radioButton.setText(empresa.getNombre());

            // Configuramos el estilo
            radioButton.setTextAppearance(R.style.RadioButtonStyle);

            // Configurar los parámetros de diseño
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
            );

            // Agregar el nuevo RadioButton al RadioGroup
            empresasGrupo.addView(radioButton, layoutParams);
        }
    }

}