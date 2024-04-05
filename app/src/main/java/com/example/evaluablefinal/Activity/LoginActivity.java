package com.example.evaluablefinal.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evaluablefinal.R;
import com.example.evaluablefinal.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseActivity {
    ActivityLoginBinding binding;
    TextView tituloconfirm;
    TextView textoCrear;
    EditText confirm;
    View recuperar;
    Button crear;

    //colores
    int blanco;
    int rojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //enlazamos los objetos con la interfaz
        tituloconfirm = findViewById(R.id.tituloContrasenaC);
        confirm = findViewById(R.id.contrasenaC);
        recuperar = findViewById(R.id.bloqueRecuperar);
        textoCrear = findViewById(R.id.crearTexto);
        crear = findViewById(R.id.crearCuenta);

        rojo = getResources().getColor(R.color.red);
        blanco = getResources().getColor(R.color.white);
        binding.acceder.setOnClickListener(v -> {
            entrar();
        });

        binding.usuario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackgroundResource(R.drawable.campo_seleccionado);
                } else {
                    v.setBackgroundResource(R.drawable.boton_azul);
                }
            }
        });

        binding.contrasena.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackgroundResource(R.drawable.campo_seleccionado);
                } else {
                    v.setBackgroundResource(R.drawable.boton_azul);
                }
            }
        });

        binding.contrasenaC.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackgroundResource(R.drawable.campo_seleccionado);
                } else {
                    v.setBackgroundResource(R.drawable.boton_azul);
                }
            }
        });
    }

    public void cambiarInterfaz(View view){

        String textoBoton =getResources().getString(R.string.crear);
        //borramos el contenido de todos los campos
        confirm.setText("");
        binding.usuario.setText("");
        binding.contrasena.setText("");


        if (crear.getText().equals(textoBoton)){
            //ponemos visible el campo de confirmar contraseña
            tituloconfirm.setVisibility(View.VISIBLE);
            confirm.setVisibility(View.VISIBLE);
            //ponemos invisible recuperar contraseña
            recuperar.setVisibility(View.GONE);
            //cambiamos el texto del bloque crear cuenta
            textoCrear.setText(R.string.conCuenta);
            crear.setText(R.string.entrar);

            //cambiamos la funcion del boton acceder
            binding.acceder.setOnClickListener(v -> {
                crearCuenta();
            });
        }else{
            //ponemos visible el campo de confirmar contraseña
            tituloconfirm.setVisibility(View.GONE);
            confirm.setVisibility(View.GONE);
            //ponemos invisible recuperar contraseña
            recuperar.setVisibility(View.VISIBLE);
            //cambiamos el texto del bloque crear cuenta
            textoCrear.setText(R.string.sinCuenta);
            crear.setText(R.string.crear);
            //cambiamos la funcion del boton acceder
            binding.acceder.setOnClickListener(v -> {
                entrar();
            });
        }

    }

    private void crearCuenta(){
        String email = binding.usuario.getText().toString();
        String password = binding.contrasena.getText().toString();
        String password2 = binding.contrasenaC.getText().toString();

        if (!email.isEmpty() && !password.isEmpty() && !password2.isEmpty()){

            comprobarCorreo(email);

            if (password.length() < 6) {
                binding.contrasena.setTextColor(rojo);
                Toast.makeText(this, "Tu contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            } else if (!password.equals(password2)) {
                binding.contrasena.setTextColor(blanco);
                binding.contrasenaC.setTextColor(rojo);
                Toast.makeText(this, "Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show();
            }else {
                binding.contrasena.setTextColor(blanco);
                binding.contrasenaC.setTextColor(blanco);
            }
        }else{
            Toast.makeText(this, "Faltan datos", Toast.LENGTH_SHORT).show();
        }
        //si todo es correcto mandamos los datos y nos lleva a la página principal

        if( binding.usuario.getCurrentTextColor() == blanco
                && binding.contrasena.getCurrentTextColor() == blanco
                && binding.contrasenaC.getCurrentTextColor() == blanco){
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    binding.usuario.setTextColor(rojo);
                    binding.contrasena.setTextColor(rojo);
                    binding.contrasenaC.setTextColor(rojo);
                    Toast.makeText(this, "Ups... Algo ha fallado ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void entrar(){
        //comprobamos los datos
        String email=binding.usuario.getText().toString();
        String password=binding.contrasena.getText().toString();
        if(!email.isEmpty() && !password.isEmpty()){
            comprobarCorreo(email);

        }else{
            Toast.makeText(this, "Porfavor, escriba su usuario y contraseña", Toast.LENGTH_SHORT).show();
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }else{
                binding.usuario.setTextColor(rojo);
                binding.contrasena.setTextColor(rojo);
                Toast.makeText(this, "Ups... La contraseña o el usuario no es correcto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void comprobarCorreo(String email){
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.usuario.setTextColor(rojo);
            Toast.makeText(this, "Debe introducir su correo correctamente", Toast.LENGTH_SHORT).show();

        }else {
            binding.usuario.setTextColor(blanco);
        }
    }

}