package com.example.evaluablefinal.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.evaluablefinal.Activity.MainActivity;
import com.example.evaluablefinal.R;

import java.util.Locale;

public class CargaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga);

        // Obtener el idioma y el fragmento de la intención
        Intent intent = getIntent();
        String idioma = intent.getStringExtra("idioma").isEmpty()
                ? "es" : intent.getStringExtra("idioma");
        String fragmentName = intent.getStringExtra("fragment").isEmpty()
                ? "PerfilFragment" : intent.getStringExtra("fragment");

        // Cambiar el idioma y reiniciar MainActivity después de un breve retardo
        new Handler().postDelayed(() -> {
            Locale locale = new Locale(idioma);
            Locale.setDefault(locale);
            Resources resources = getResources();
            Configuration config = resources.getConfiguration();
            config.setLocale(locale);
            resources.updateConfiguration(config, resources.getDisplayMetrics());

            // Reiniciar MainActivity y navegar al fragmento especificado
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.putExtra("fragment", fragmentName);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);
            finish();
        }, 1500); // Retardo de 1.5 segundos para mostrar la pantalla de carga
    }
}
