package com.example.evaluablefinal.Activity;

import static com.example.evaluablefinal.Activity.IntroActivity.correoUsuario;
import static com.example.evaluablefinal.Activity.IntroActivity.fotoPerfilUsuario;
import static com.example.evaluablefinal.Activity.IntroActivity.idUsuario;
import static com.example.evaluablefinal.Activity.IntroActivity.nombreUsuario;
import static com.example.evaluablefinal.Activity.LoginActivity.PREFS_NAME;
import static com.example.evaluablefinal.Activity.LoginActivity.PREF_PHOTO;
import static com.example.evaluablefinal.Activity.LoginActivity.mDatabase;
import static com.example.evaluablefinal.transformations.BitmapUtils.bitmapToUri;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.evaluablefinal.PerfilFragment;
import com.example.evaluablefinal.controlErrores.Comprobaciones;
import com.example.evaluablefinal.databinding.ActivityMainBinding;


import com.example.evaluablefinal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Objects;


public class MainActivity extends BaseActivity implements Comprobaciones {
    View view;

    private ActivityMainBinding binding;
    private ImageView fotoPerfilEnc;
    public static ImageView fotoEnc;
    private ImageButton casa;
    private static ConstraintLayout encNormal;
    private static ConstraintLayout encFoto;
    private static ConstraintLayout encFotoDif;
    public static NavController navController;
    private Uri imageUri;

    public Context context = this;

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
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

          //-  NavigationUI.setupActionBarWithNavController(this, navController);
            getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    handleBackPressed();
                }
            });
        }
        //comprobamos si debemos ir a un frgamneto concreto o no
        // Manejar la navegación al fragmento específico
        Intent intent = getIntent();
        String fragmentName = intent.getStringExtra("fragment");
        if (fragmentName != null) {
            navegarAlFragmento();
        }

        atras();
        //eventos
        //comprobamos el fragment en el que está
        comprobarEncabezado();
        //inicio
        binding.perfil.setOnClickListener(this::cambioPantalla);

        //perfil
        binding.inicio.setOnClickListener(this::cambioPantalla);

        //anadir
        binding.masBoton.setOnClickListener(this::cambioPantalla);

        //cambio de foto de perfil
        fotoPerfilEnc.setOnClickListener(l -> abrirGaleria());
        agregarFotos();

    }


    public void cambioPantalla(View boton) {
        int id = Objects.requireNonNull(navController.getCurrentDestination()).getId();
        int idBoton = boton.getId();

        if (idBoton == R.id.inicio) {
            if (id == R.id.perfilFragment) {
                navController.navigate(R.id.action_perfil_a_inicio);
            } else if (id == R.id.empresaFragment) {
                navController.navigate(R.id.action_empresaFragment_to_inicioFragment);
            } else if (id == R.id.perfilAlumnoFragment) {
                navController.navigate(R.id.action_perfilAlumnoFragment_to_inicioFragment);
            } else if (id == R.id.anadirFragment) {
                navController.navigate(R.id.action_anadirFragment_to_inicioFragment);
            }

        } else if (idBoton == R.id.perfil) {
            if (id == R.id.inicioFragment) {
                navController.navigate(R.id.action_inicio_a_perfil);
            } else if (id == R.id.empresaFragment) {
                navController.navigate(R.id.action_empresaFragment_to_perfilFragment);
            } else if (id == R.id.perfilAlumnoFragment) {
                navController.navigate(R.id.action_perfilAlumnoFragment_to_perfilFragment);
            } else if (id == R.id.anadirFragment) {
                navController.navigate(R.id.action_anadirFragment_to_perfilFragment);
            }
        } else if (idBoton == R.id.masBoton) {
            if (id == R.id.inicioFragment) {
                navController.navigate(R.id.action_inicioFragment_to_anadirFragment);
            } else if (id == R.id.empresaFragment) {
                navController.navigate(R.id.action_empresaFragment_to_anadirFragment);
            } else if (id == R.id.perfilAlumnoFragment) {
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

    public void cambiarFoto() {
        DatabaseReference userRef = mDatabase.child("Profesores");
        // Escribir los datos del alumno en la ubicación correspondiente en la base de datos
        userRef.child(idUsuario).child("imagen").setValue(imageUri.toString())
                .addOnSuccessListener(aVoid -> {
                    mostrarMensajeCorrecto();
                    fotoPerfilUsuario = imageUri.toString();
                    cambiarFotoBD(userRef);
                    agregarFotos();
                })
                .addOnFailureListener(e -> mostrarMensajeIncorrecto());
    }

    public void cambiarFotoBD(DatabaseReference ref) {
        //actualizamos los datos locales
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(PREF_PHOTO, imageUri.toString()).apply();
        //actualizamos la base de datos de firebase
        Log.println(Log.INFO, "idUsuario", "LA CLAVE ES: " + idUsuario);
        ref.child(idUsuario).child("imagen").setValue(imageUri.toString());

    }

    public void mostrarMensajeCorrecto() {
        Toast.makeText(this, "¡Foto de perfil cambiada!", Toast.LENGTH_SHORT).show();

    }

    public void mostrarMensajeIncorrecto() {
        Toast.makeText(this, "Error al cambiar foto de perfil", Toast.LENGTH_SHORT).show();
    }

    private static final int PICK_IMAGE_REQUEST = 1;

    // Método para abrir la galería
    private void abrirGaleria() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), PICK_IMAGE_REQUEST);
    }

    // Manejamos el resultado de la selección de la imagen
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            try {
                imageUri = data.getData();
                // Obtener la imagen en formato Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                // Redimensionar la imagen a un tamaño estándar (por ejemplo, 300x300 píxeles)
                Bitmap imgEdit = Bitmap.createScaledBitmap
                        (bitmap, 300, 300, false);
                // fotoPerfil.setImageURI();
                //cambiamos el valor de uri por la imagen editada
                imageUri = bitmapToUri(this, imgEdit);

                if (comprobarImagen(imageUri, fotoPerfilEnc)) {
                    cambiarFoto();
                } else {
                    mostrarMensajeIncorrecto();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void navegarAlFragmento() {
        int id = Objects.requireNonNull(navController.getCurrentDestination()).getId();
        if (id == R.id.inicioFragment) {
            navController.navigate(R.id.action_inicio_a_perfil);
        }
    }
 private void atras(){


 }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handleBackPressed();
    }

    private void handleBackPressed() {
        // Obtén el id del fragmento actual
        int currentDestinationId = navController.getCurrentDestination().getId();

        // Verifica si el usuario está en el fragmento que no debe volver al inicio de sesión
        if (currentDestinationId == R.id.inicioFragment) {
            // Crear el diálogo
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(null);

            // Inflar el layout personalizado
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_salida, null);
            builder.setView(dialogView);

            // Crear el diálogo
            AlertDialog dialog = builder.create();

            // Configurar los botones
            Button buttonPositive = dialogView.findViewById(R.id.positiveButton);
            Button buttonNegative = dialogView.findViewById(R.id.negativeButton);

            buttonPositive.setText(getResources().getString(R.string.aceptar));
            buttonNegative.setText(getResources().getString(R.string.cancelar));

            buttonPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    finish();
                }
            });

            buttonNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();
        } else {
            // Navegar hacia atrás
            navController.navigateUp();
        }
    }




}
