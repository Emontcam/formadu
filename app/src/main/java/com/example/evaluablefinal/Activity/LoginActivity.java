package com.example.evaluablefinal.Activity;

import static com.example.evaluablefinal.Activity.IntroActivity.correoUsuario;
import static com.example.evaluablefinal.Activity.IntroActivity.fotoPerfilUsuario;
import static com.example.evaluablefinal.Activity.IntroActivity.idUsuario;
import static com.example.evaluablefinal.Activity.IntroActivity.nombreUsuario;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.evaluablefinal.R;
import com.example.evaluablefinal.controlErrores.Comprobaciones;
import com.example.evaluablefinal.databinding.ActivityLoginBinding;
import com.example.evaluablefinal.models.Alumno;
import com.example.evaluablefinal.models.Profesor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * LoginActivity gestiona la funcionalidad de inicio de sesión de la aplicación.
 * Maneja la autenticación de usuarios con Firebase, la validación de entradas de usuario,
 * y las transiciones de interfaz para el inicio de sesión y la creación de cuentas.
 */
public class LoginActivity extends BaseActivity implements Comprobaciones {
    /**
     * View binding para activity_login.xml
     */
    ActivityLoginBinding binding;

    /**
     * Referencia a la base de datos de Firebase
     */
    public static DatabaseReference mDatabase;

    /**
     * Elementos de la interfaz de usuario
     */
    TextView tituloconfirm;
    TextView textoCrear;
    EditText confirm;
    View recuperar;
    Button crear;

    /**
     * Contexto de la aplicación
     */
    Context context = this;

    /**
     * Constantes para SharedPreferences
     */
    static final String PREFS_ID = "idUsuario";
    static final String PREFS_NAME = "UserInfo";
    static final String PREF_NAME = "nombreUsuario";
    static final String PREF_EMAIL = "correoUsuario";
    static final String PREF_PHOTO = "fotoPerfil";

    /**
     * Se llama cuando la actividad es creada por primera vez. Inicializa la referencia a Firebase,
     * enlaza los elementos de la interfaz y configura los listeners de eventos.
     *
     * @param savedInstanceState Si la actividad está siendo re-inicializada después de haber sido
     *                           previamente cerrada, este Bundle contiene los datos más recientes
     *                           suministrados en onSaveInstanceState.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializar Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Enlazamos los objetos con la interfaz
        tituloconfirm = findViewById(R.id.tituloContrasenaC);
        confirm = findViewById(R.id.contrasenaC);
        recuperar = findViewById(R.id.bloqueRecuperar);
        textoCrear = findViewById(R.id.crearTexto);
        crear = findViewById(R.id.crearCuenta);

        // Configurar listeners de eventos
        binding.acceder.setOnClickListener(v -> {
            entrar();
        });

        binding.usuario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //if con operador ternario
                comprobarSeleccion(v, hasFocus);

            }
        });

        binding.contrasena.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                comprobarSeleccion(v, hasFocus);
            }
        });

        binding.contrasenaC.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                comprobarSeleccion(v, hasFocus);
            }
        });

        binding.recuperarCuenta.setOnClickListener(l -> cambiarContrasena());

    }

    /**
     * Cambia el fondo del view en función de su estado de foco.
     *
     * @param v        El view cuyo fondo necesita ser cambiado.
     * @param hasFocus Indica si el view tiene foco o no.
     */
    public static void comprobarSeleccion(View v, boolean hasFocus) {
        v.setBackgroundResource(hasFocus ? R.drawable.campo_seleccionado : R.drawable.boton_azul);
    }

    /**
     * Alterna la interfaz entre los modos de inicio de sesión y creación de cuenta.
     *
     * @param view El view que desencadena este método.
     */
    public void cambiarInterfaz(View view) {

        String textoBoton = getResources().getString(R.string.crear);
        //Borramos el contenido de todos los campos
        confirm.setText("");
        binding.usuario.setText("");
        binding.contrasena.setText("");


        if (crear.getText().equals(textoBoton)) {
            // Cambiar al modo de creación de cuenta
            binding.tituloNombre.setVisibility(View.VISIBLE);
            binding.nombreNew.setVisibility(View.VISIBLE);
            tituloconfirm.setVisibility(View.VISIBLE);
            confirm.setVisibility(View.VISIBLE);
            //Ponemos invisible recuperar contraseña
            recuperar.setVisibility(View.GONE);
            //Cambiamos el texto del bloque crear cuenta
            textoCrear.setText(R.string.conCuenta);
            crear.setText(R.string.entrar);

            //Cambiamos la funcion del boton acceder
            binding.acceder.setOnClickListener(v -> {
                crearCuenta();
            });
        } else {
            // Cambiar al modo de inicio de sesión
            binding.tituloNombre.setVisibility(View.GONE);
            binding.nombreNew.setVisibility(View.GONE);
            tituloconfirm.setVisibility(View.GONE);
            confirm.setVisibility(View.GONE);
            recuperar.setVisibility(View.VISIBLE);
            textoCrear.setText(R.string.sinCuenta);
            crear.setText(R.string.crear);

            //Cambiamos la funcion del boton acceder
            binding.acceder.setOnClickListener(v -> {
                entrar();
            });
        }

    }

    /**
     * Crea una nueva cuenta de usuario usando el correo electrónico y la contraseña proporcionados.
     * Valida la entrada y muestra mensajes de error apropiados.
     */
    private void crearCuenta() {
        String nombre = binding.nombreNew.getText().toString();
        String email = binding.usuario.getText().toString();
        String password = binding.contrasena.getText().toString();
        String password2 = binding.contrasenaC.getText().toString();

        if (!nombre.isEmpty() && !email.isEmpty() && !password.isEmpty() && !password2.isEmpty()) {
            comprobarNombreCompleto(nombre, this, binding.nombreNew, this.getColor(R.color.white));
            comprobarCorreo(email, this, binding.usuario, this.getColor(R.color.white));

            if (password.length() < 6) {
                binding.contrasena.setTextColor(context.getColor(R.color.red));
                Toast.makeText(this, "Tu contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();

            } else if (!password.equals(password2)) {
                binding.contrasena.setTextColor(context.getColor(R.color.white));
                binding.contrasenaC.setTextColor(context.getColor(R.color.red));
                Toast.makeText(this, "Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show();
            } else {
                binding.contrasena.setTextColor(context.getColor(R.color.white));
                binding.contrasenaC.setTextColor(context.getColor(R.color.white));
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.e_vacio), Toast.LENGTH_SHORT).show();
        }

        //si todo es correcto mandamos los datos y nos lleva a la página principal
        if (binding.usuario.getCurrentTextColor() == context.getColor(R.color.white)
                && binding.contrasena.getCurrentTextColor() == context.getColor(R.color.white)
                && binding.contrasenaC.getCurrentTextColor() == context.getColor(R.color.white)
                && binding.nombreNew.getCurrentTextColor() == context.getColor(R.color.white)) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    Profesor prof = new Profesor(nombre, email);
                    crearProfesor(mDatabase.child("Profesores"), prof);
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    binding.usuario.setTextColor(context.getColor(R.color.red));
                    binding.contrasena.setTextColor(context.getColor(R.color.red));
                    binding.contrasenaC.setTextColor(context.getColor(R.color.red));
                    Toast.makeText(this, "Ups... Algo ha fallado ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Autentica al usuario con el correo electrónico y la contraseña proporcionados.
     * Valida la entrada y muestra mensajes de error apropiados.
     */
    private void entrar() {
        //comprobamos los datos
        String email = binding.usuario.getText().toString();
        String password = binding.contrasena.getText().toString();
        if (!email.isEmpty() && !password.isEmpty()) {

            comprobarCorreo(email, this, binding.usuario, this.getColor(R.color.white));

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    // Referencia a la tabla "profesores"
                    DatabaseReference profRef = mDatabase.child("Profesores");
                    recogerDatos(profRef, email);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    binding.usuario.setTextColor(context.getColor(R.color.red));
                    binding.contrasena.setTextColor(context.getColor(R.color.red));
                    Toast.makeText(this, "Ups... La contraseña o el usuario no es correcto", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Porfavor, escriba su usuario y contraseña", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Guarda los datos del usuario en las preferencias compartidas.
     */
    private void guardarDatosUsuario() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFS_ID, idUsuario);
        editor.putString(PREF_NAME, nombreUsuario);
        editor.putString(PREF_EMAIL, correoUsuario);
        editor.putString(PREF_PHOTO, fotoPerfilUsuario);
        editor.apply();
    }

    /**
     * Recoge los datos del usuario de la base de datos y los guarda en las preferencias compartidas.
     *
     * @param ref   La referencia a la tabla "Profesores" en la base de datos.
     * @param email El correo electrónico del usuario.
     */
    private void recogerDatos(DatabaseReference ref, String email) {

        // Escuchar cambios en la tabla "profesores"
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Iterar sobre cada registro en la tabla "alumnos"
                for (DataSnapshot profSnapshot : dataSnapshot.getChildren()) {
                    String correo = profSnapshot.child("correo").getValue(String.class);
                    if (correo.equals(email)) {
                        // Obtenemos la clave del profesor(nombre de la coleccion)
                        idUsuario = profSnapshot.getKey();
                        Log.println(Log.INFO, "idUsuario", "LA CLAVE ES: " + idUsuario);
                        nombreUsuario = profSnapshot.child("nombre").getValue(String.class);
                        correoUsuario = profSnapshot.child("correo").getValue(String.class);
                        fotoPerfilUsuario = profSnapshot.child("imagen").getValue(String.class);
                        guardarDatosUsuario();
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

    private void crearProfesor(DatabaseReference profesorRef, Profesor profesor) {

        // Obtener un identificador único para el nuevo profesor
        String profesorId = profesorRef.push().getKey();
        // Escribimos los datos del alumno en la ubicación correspondiente en la base de datos
        profesorRef.child(profesorId).setValue(profesor)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, context.getResources().getString(R.string.profesorNuevo), Toast.LENGTH_LONG).show();
                    recogerDatos(profesorRef, profesor.getCorreo());
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, context.getResources().getString(R.string.e_random), Toast.LENGTH_SHORT).show());

    }

    /**
     * Envía un correo electrónico para restablecer la contraseña del usuario.
     */
    private void cambiarContrasena() {


        mAuth.sendPasswordResetEmail(correoUsuario)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }
}