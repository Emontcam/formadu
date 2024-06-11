package com.example.evaluablefinal;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.evaluablefinal.Activity.MainActivity.comprobarEncabezado;
import static com.example.evaluablefinal.Activity.MainActivity.fotoEnc;
import static com.example.evaluablefinal.Activity.MainActivity.navController;
import static com.example.evaluablefinal.InicioFragment.alumnosTutor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.evaluablefinal.models.Alumno;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Fragmento para mostrar y gestionar el perfil de un alumno.
 */
public class PerfilAlumnoFragment extends Fragment {

    View view;
    private DatabaseReference mDatabase;
    private LinearLayout layaoutAlumnos;
    private TextView nombrePerfil;
    private TextView nombreEmpresa;
    private ProgressBar barraHoras;
    private TextView horasRealizadas;
    private Button borrar;
    private String nombreAlumno;
    private String idAlumno;
    //horas cada dia de la semana
    private TextView horasDia;

    /**
     * Constructor vacío requerido para la creación del fragmento.
     */
    public PerfilAlumnoFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nombreAlumno = getArguments().getString("nombreAlumno");
            idAlumno = getArguments().getString("idAlumno");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        view = inflater.inflate(R.layout.fragment_perfil_alumno, container, false);

        // Inicializar vistas
        nombrePerfil = view.findViewById(R.id.nombrePerfilAlumno);
        nombreEmpresa = view.findViewById(R.id.empresaAlumno);
        barraHoras = view.findViewById(R.id.progressBarHoras);
        horasRealizadas = view.findViewById(R.id.horas);
        borrar = view.findViewById(R.id.borrarAlumno);

        // Establecer nombre del alumno en el perfil
        nombrePerfil.setText(nombreAlumno);

        // Comprobar encabezado
        comprobarEncabezado();
        fotoEnc.setOnClickListener(f->{});

        // Inicializar Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Referencia a la tabla "alumnos"
        DatabaseReference alumnosRef = mDatabase.child("Alumnos");

        // Buscar datos del alumno en la base de datos
        buscarDatos(alumnosRef);

        // Configurar el botón de borrar alumno
        borrar.findViewById(R.id.borrarAlumno).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarAlumn();
            }
        });
        return view;
    }

    /**
     * Busca los datos del alumno en la base de datos y actualiza la UI.
     * @param alumnosRef Referencia a la base de datos donde se encuentran los datos del alumno.
     */
    private void buscarDatos(DatabaseReference alumnosRef) {
        // Escuchar cambios en la tabla "alumnos"
        alumnosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Iteramos sobre cada registro en la tabla "alumnos"
                for (DataSnapshot alumnoSnapshot : dataSnapshot.getChildren()) {
                    // Obtener el ID del alumno
                    String alumId = alumnoSnapshot.getKey();

                    if (alumnoSnapshot.child("nombre").getValue(String.class).equals(nombreAlumno)) {
                        // Obtenemos los valores de cada campo del alumno
                        String tutor = alumnoSnapshot.child("tutor").getValue(String.class);
                        String imagen = alumnoSnapshot.child("imagen").getValue(String.class);
                        String correo = alumnoSnapshot.child("correo").getValue(String.class);
                        String empresa = alumnoSnapshot.child("empresa").getValue(String.class);

                        int horasTotales = alumnoSnapshot.hasChild("horasTotales") ?
                                alumnoSnapshot.child("horasTotales").getValue(Integer.class) : 0;
                        Double horasLunes = alumnoSnapshot.hasChild("l") ?
                                alumnoSnapshot.child("l").getValue(Double.class) : 0.0;
                        Double horasMartes = alumnoSnapshot.hasChild("m") ?
                                alumnoSnapshot.child("m").getValue(Double.class)
                                : 0.0;
                        Double horasMiercoles = alumnoSnapshot.hasChild("x") ?
                                alumnoSnapshot.child("x").getValue(Double.class) : 0.0;
                        Double horasJueves = alumnoSnapshot.hasChild("j") ?
                                alumnoSnapshot.child("j").getValue(Double.class) : 0.0;
                        Double horasViernes = alumnoSnapshot.hasChild("v") ?
                                alumnoSnapshot.child("v").getValue(Double.class) : 0.0;

                        Date fechaInicio = alumnoSnapshot.hasChild("fechaInicio") ?
                               (alumnoSnapshot.child("fechaInicio").getValue(Date.class)) : null;

                        Alumno alumn = new Alumno(nombreAlumno, correo, horasTotales, empresa, tutor, imagen, fechaInicio);
                        Double horasHechas = alumn.horasHechas();
                        //Asignamos los datos
                        nombreEmpresa.setText(String.format("%s %s", getResources().getText(R.string.empresa), empresa));
                        barraHoras.setMax(horasTotales);
                        int horasHechasInt = horasHechas.intValue();
                        barraHoras.setProgress(horasHechasInt, true);
                        horasRealizadas.setText(horasTotales - horasHechas + getResources().getString(R.string.horasN));
                        //actualizamos las horas realizadas
                        alumnoSnapshot.getRef().child("horasCubiertas").setValue(horasHechas);

                        // Actualizar las horas por semana
                        horasSemana(horasLunes, horasMartes, horasMiercoles, horasJueves, horasViernes);

                        // Agregar fotos del alumno
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

    /**
     * Actualiza las horas realizadas por semana en la UI.
     * @param horasLunes Horas realizadas el lunes.
     * @param horasMartes Horas realizadas el martes.
     * @param horasMiercoles Horas realizadas el miércoles.
     * @param horasJueves Horas realizadas el jueves.
     * @param horasViernes Horas realizadas el viernes.
     */
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

    /**
     * Agrega fotos del alumno a la UI.
     * @param img URL de la imagen del alumno.
     */
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
                // Aplicamos transformaciones
                Glide.with(this)
                        .load(img)
                        .apply(requestOptions)
                        .into(fotoEnc);
            }
        }
    }

    /**
     * Elimina el perfil del alumno de la base de datos.
     */
    public void eliminarAlumn() {
        // Referencia a la tabla "alumnos"
        DatabaseReference alumnosRef = mDatabase.child("Alumnos").child(idAlumno);
        // Elimina el alumno
        alumnosRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Se elimino corectamente
                    Toast.makeText(getContext(), "Alumno eliminado", Toast.LENGTH_SHORT).show();
                    List<Alumno> alumnosEliminar = new ArrayList<Alumno>();
                    alumnosTutor.stream().forEach(alumno -> {
                        if (alumno.getId().equals(idAlumno))
                            alumnosEliminar.add(alumno);
                    });
                    alumnosTutor.remove(alumnosEliminar.get(0));
                    toInicio();
                } else {
                    // Hubo un error al eliminar el alumno
                    Toast.makeText(getContext(), "Error al eliminar el alumno", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Navega de vuelta a la pantalla de inicio.
     */
    private void toInicio() {
        navController.navigate(R.id.action_perfilAlumnoFragment_to_inicioFragment);
    }
}