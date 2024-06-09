package com.example.evaluablefinal.controlErrores;

import static com.example.evaluablefinal.InicioFragment.empresas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evaluablefinal.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    default boolean comprobarNombreCompleto(String nombre, Context context, EditText v, int colorDef) {
        // Expresión regular para verificar que haya al menos un nombre y un apellido separados por al menos un espacio
        String expresion = "^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ]+\\s+[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ]+(?:\\s+[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ]+)*$";
        Pattern pattern = Pattern.compile(expresion);

        if (!pattern.matcher(nombre).matches()) {
            v.setTextColor(context.getColor(R.color.red));
            Toast.makeText(context, context.getResources().getString(R.string.e_nombre), Toast.LENGTH_SHORT).show();
            return false;
        } else {
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
            Toast.makeText(context, context.getResources().getString(R.string.e_apellidos), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            errorCampo.setVisibility(View.GONE);
            v.setTextColor(colorDef);
            return true;
        }

    }

    //boolean comprobarApellidos();
    default boolean comprobarCantidadEntera(Long cant, Context context, EditText v, int colorDef, TextView errorCampo) {
        String expresion = "^\\d+$";
        Pattern pattern = Pattern.compile(expresion);

        if (!pattern.matcher(cant.toString()).matches()) {
            v.setTextColor(context.getColor(R.color.red));
            errorCampo.setText(R.string.e_random);
            errorCampo.setVisibility(View.VISIBLE);
            return false;
        } else {
            errorCampo.setVisibility(View.VISIBLE);
            if (cant < 240) {
                String message = String.format(context.getResources().getString(R.string.e_horasMin), "240");
                errorCampo.setText(message);
                return false;
            } else if (cant > 900) {
                String message = String.format(context.getResources().getString(R.string.e_horasMax), "900", cant.toString());
                errorCampo.setText(message);
                return false;
            } else {
                v.setTextColor(colorDef);
                errorCampo.setVisibility(View.GONE);
                return true;
            }
        }

    }

    default boolean intervaloHorasDia(Context context, Double cant, EditText v, int colorDef, TextView errorCampo) {
        if (cant < 0) {
            String message = String.format(context.getResources().getString(R.string.e_horasMin), "0");
            v.setTextColor(context.getColor(R.color.red));
            errorCampo.setText(message);
            errorCampo.setVisibility(View.VISIBLE);
            return false;
        } else if (cant > 14) {
            String message = String.format(context.getResources().getString(R.string.e_horasMax), "14", cant.toString());
            v.setTextColor(context.getColor(R.color.red));
            errorCampo.setText(message);
            errorCampo.setVisibility(View.VISIBLE);
            return false;
        } else {
            v.setTextColor(colorDef);
            errorCampo.setVisibility(View.GONE);
            return true;
        }
    }

    default boolean comprobarMaxHorasSemana(Context context, Double cant, EditText v, int colorDef, TextView errorCampo) {
        if (cant > 40) {

            String message = String.format(context.getResources().getString(R.string.e_horasMax), "40", cant.toString());
            v.setTextColor(context.getColor(R.color.red));
            errorCampo.setText(message);
            errorCampo.setVisibility(View.VISIBLE);
            return false;
        } else {
            v.setTextColor(colorDef);
            errorCampo.setVisibility(View.GONE);
            return true;
        }
    }

    default boolean comprobarLong(Context context, String str, EditText v, int colorDef, TextView errorCampo) {
        try {
            Long.parseLong(str);
            v.setTextColor(colorDef);
            return true;
        } catch (NumberFormatException e) {
            String message = context.getResources().getString(R.string.e_horasCompletas);
            v.setTextColor(context.getColor(R.color.red));
            errorCampo.setText(message);
            errorCampo.setVisibility(View.VISIBLE);
            return false;
        }
    }

    default boolean comprobarDouble(Context context, String str, EditText v, int colorDef, TextView errorCampo) {
        try {
            Double.parseDouble(str);
            v.setTextColor(colorDef);
            return true;
        } catch (NumberFormatException e) {
            String message = context.getResources().getString(R.string.e_random);
            v.setTextColor(context.getColor(R.color.red));
            errorCampo.setText(message);
            errorCampo.setVisibility(View.VISIBLE);
            return false;
        }
    }

    default boolean comprobarEmpresas(String empresa, Context context, TextView errorCampo) {
        if (empresas == null) {
            empresas = new ArrayList<>();
        }
        String nombreEmpresa = empresa.toLowerCase();
        List<String> empresasMin = empresas.stream()
                .map(e -> e.getNombre().toLowerCase())
                .collect(Collectors.toList());
        boolean existe = empresasMin.stream().anyMatch(e -> e.equals(nombreEmpresa)) ?
                true : false;

        if (!existe) {
            errorCampo.setText(context.getResources().getString(R.string.e_empresa));
            errorCampo.setVisibility(View.VISIBLE);
        } else {
            errorCampo.setVisibility(View.GONE);
        }
        return existe;
    }

    default boolean comprobarCorreo(String correo, Context context, EditText v, int colorDef, TextView errorCampo) {

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            v.setTextColor(context.getColor(R.color.red));
            errorCampo.setVisibility(View.VISIBLE);
            Toast.makeText(context, context.getResources().getString(R.string.e_correo)
                    , Toast.LENGTH_SHORT).show();
            return false;
        } else {
            v.setTextColor(colorDef);
            errorCampo.setVisibility(View.GONE);
            return true;
        }
    }

    default boolean comprobarCorreo(String correo, Context context, EditText v, int colorDef) {

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            v.setTextColor(context.getColor(R.color.red));
            Toast.makeText(context, context.getResources().getString(R.string.e_correo)
                    , Toast.LENGTH_SHORT).show();
            return false;
        } else {
            v.setTextColor(colorDef);
            return true;
        }
    }

    default boolean comprobarImagen(Uri uri, ImageView fotoPerfil) {
        if (uri == null) {
            return false;
        }else if (uri.toString().isEmpty()) {
            fotoPerfil.setImageURI(uri);
        }

        return true;
    }

}
