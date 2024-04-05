package com.example.evaluablefinal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PerfilAlumnoFragment extends Fragment {
    public PerfilAlumnoFragment() {
        // Required empty public constructor
    }

//    public static PerfilAlumnoFragment newInstance(String param1, String param2) {
//        PerfilAlumnoFragment fragment = new PerfilAlumnoFragment();
//        Bundle args = new Bundle();
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil_alumno, container, false);
    }
}