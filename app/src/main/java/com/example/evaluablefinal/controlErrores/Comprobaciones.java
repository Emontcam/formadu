package com.example.evaluablefinal.controlErrores;

import android.content.Context;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evaluablefinal.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Comprobaciones {

    default boolean comprobarNombre(String nombre, Context context, EditText v, int colorDef, TextView errorCampo) {
        String expresion = "^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ]+(?:\\s+[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ]+)*$";
        Pattern pattern = Pattern.compile(expresion);

        if (!pattern.matcher(nombre).matches()) {
            v.setTextColor(context.getColor(R.color.red));
            errorCampo.setVisibility(View.VISIBLE);
            //Toast.makeText(context, "Debe introducir un nombre válido", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            errorCampo.setVisibility(View.GONE);
            v.setTextColor(colorDef);
            return true;
        }

    }

    default boolean comprobarApellidos(String apellidos, Context context, EditText v, int colorDef, TextView errorCampo) {
        String expresion = "^\\w+\\s\\w+$";
        Pattern pattern = Pattern.compile(expresion);

        if (!pattern.matcher(apellidos).matches()) {
            v.setTextColor(context.getColor(R.color.red));
            errorCampo.setVisibility(View.VISIBLE);
           // Toast.makeText(context, "Debe introducir los dos apellidos", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            errorCampo.setVisibility(View.GONE);
            v.setTextColor(colorDef);
            return true;
        }

    }

    //boolean comprobarApellidos();
    default boolean comprobarCantidadEntera(Integer cant, Context context, EditText v, int colorDef, TextView errorCampo) {
        String expresion = "^\\d+$";
        Pattern pattern = Pattern.compile(expresion);

        if (!pattern.matcher(cant.toString()).matches()) {
            v.setTextColor(context.getColor(R.color.red));
            Toast.makeText(context, "Debe introducir un valor numerico válido", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            errorCampo.setVisibility(View.VISIBLE);
            if (cant < 240) {
                errorCampo.setText(R.string.e_horasMin);
                return false;
            } else if (cant > 900) {
                errorCampo.setText(R.string.e_horasMax);
                return false;
            } else {
                v.setTextColor(colorDef);
                errorCampo.setVisibility(View.GONE);
                return true;
            }



        }

    }

    default boolean comprobarLista(List<String> nombres, TextView errorCampo) {

        return true;
    }

    ;

    default boolean comprobarCorreo(String correo, Context context, EditText v, int colorDef, TextView errorCampo) {

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            v.setTextColor(context.getColor(R.color.red));
            errorCampo.setVisibility(View.VISIBLE);
            //Toast.makeText(context, "Debe introducir su correo correctamente", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            v.setTextColor(colorDef);
            errorCampo.setVisibility(View.GONE);

            return true;
        }


    }

}
