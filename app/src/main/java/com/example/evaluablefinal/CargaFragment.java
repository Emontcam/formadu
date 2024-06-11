package com.example.evaluablefinal;

import static com.example.evaluablefinal.Activity.LoginActivity.PREF_IDIOM;
import static com.example.evaluablefinal.Activity.MainActivity.navController;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.evaluablefinal.Activity.MainActivity;

import java.util.Locale;

public class CargaFragment extends Fragment {

    private View view;
    private String idioma;

    public CargaFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idioma = getArguments().getString(PREF_IDIOM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_carga, container, false);

        TextView idiomaFrase = view.findViewById(R.id.cambioIdiomaTit);
        idiomaFrase.setText(requireContext().getResources().getString(R.string.cambioIdioma));

        new Handler().postDelayed(() -> {
            Locale locale = new Locale(idioma);
            Locale.setDefault(locale);
            Resources resources = getResources();
            Configuration config = resources.getConfiguration();
            config.setLocale(locale);
            resources.updateConfiguration(config, resources.getDisplayMetrics());
            navController.navigate(R.id.action_cargaFragment_to_perfilFragment);
        }, 1000);

        return view;
    }
}