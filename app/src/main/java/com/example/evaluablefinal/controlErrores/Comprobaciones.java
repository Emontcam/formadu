package com.example.evaluablefinal.controlErrores;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.example.evaluablefinal.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Comprobaciones {

    default boolean comprobarNombre(String nombre, Context context, EditText v, int colorDef) {
        String expresion = "^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ]+(?:\\s+[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ]+)*$";
        Pattern pattern = Pattern.compile(expresion);

        if (!pattern.matcher(nombre).matches()) {
            v.setTextColor(context.getColor(R.color.red));
            Toast.makeText(context, "Debe introducir un nombre válido", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            v.setTextColor(colorDef);
            return false;
        }

    }

    //boolean comprobarApellidos();
    default boolean comprobarCantidadEntera(Integer cant, Context context, EditText v, int colorDef) {
        String expresion = "^\\\\d+$";
        Pattern pattern = Pattern.compile(expresion);

        if (!pattern.matcher(cant.toString()).matches()) {
            v.setTextColor(context.getColor(R.color.red));
            Toast.makeText(context, "Debe introducir una cantidad válida mayor de 0", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            v.setTextColor(colorDef);
            return false;
        }

    }

    default boolean comprobarLista(List<String> nombres) {

        return true;
    }

    ;

    default boolean comprobarCorreo(String correo, Context context, EditText v, int colorDef) {

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            v.setTextColor(context.getColor(R.color.red));
            Toast.makeText(context, "Debe introducir su correo correctamente", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            v.setTextColor(colorDef);
            return true;
        }


    }

}
