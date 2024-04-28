package com.example.evaluablefinal.Activity;

import static com.example.evaluablefinal.Activity.LoginActivity.PREFS_NAME;
import static com.example.evaluablefinal.Activity.LoginActivity.PREF_EMAIL;
import static com.example.evaluablefinal.Activity.LoginActivity.PREF_NAME;
import static com.example.evaluablefinal.Activity.LoginActivity.PREF_PHOTO;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.evaluablefinal.R;
import com.example.evaluablefinal.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {

     ActivityIntroBinding binding;
    public static String nombreUsuario;
    public static String correoUsuario;
    public static String fotoPerfilUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();

    }

    private void setVariable() {
        binding.buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            //si ya ha iniciado sesión, no debe pasar por el login

                if (mAuth.getCurrentUser() != null) {
                    SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    if (prefs.contains(PREF_EMAIL)) {
                        // Si el usuario ya ha iniciado sesión, recuperar los datos del usuario
                        nombreUsuario = prefs.getString(PREF_NAME, "");
                        correoUsuario = prefs.getString(PREF_EMAIL, "");
                        fotoPerfilUsuario = prefs.getString(PREF_PHOTO, "");

                    }
                    startActivity(new Intent(IntroActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(IntroActivity.this, LoginActivity.class));
                }

            }
        });

    }
}