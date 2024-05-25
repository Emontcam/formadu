package com.example.evaluablefinal;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.evaluablefinal.Activity.LoginActivity.mDatabase;
import static com.example.evaluablefinal.Activity.MainActivity.comprobarEncabezado;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.evaluablefinal.databinding.ActivityMainBinding;
import com.example.evaluablefinal.models.Alumno;
import com.example.evaluablefinal.models.Empresa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InicioFragment extends Fragment {
    //conexion diseño
    View view;
    private LinearLayout layaoutAlumno;
    private LinearLayout layaoutEmpresa;
    public static Typeface fuenteTitulo;
    public static Typeface fuenteSub;
    public static Typeface fuenteSubN;
    private ProgressBar barraProgreso;
    private NavController navController;
    private EditText buscador;
    private ImageView lupa;

    public InicioFragment() {
        // Required empty public constructor
    }

    public static InicioFragment newInstance(String param1, String param2) {
        InicioFragment fragment = new InicioFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_inicio, container, false);


        //comprobamos el encabezado
        comprobarEncabezado();
        //navigation
        // Obtenemos el controlador de navegación
        navController = NavHostFragment.findNavController(this);
        //asignamos las variables
        buscador = view.findViewById(R.id.buscador);
        lupa = view.findViewById(R.id.lupa);
        layaoutAlumno = view.findViewById(R.id.tarjeta);
        layaoutEmpresa = view.findViewById(R.id.tarjeta2);
        //barra de carga
        barraProgreso = view.findViewById(R.id.progressBarAlumnos);
        //borramos los datos
        layaoutAlumno.removeAllViews();
        layaoutEmpresa.removeAllViews();
        //establecemos fuentes
        fuenteTitulo = getResources().getFont(R.font.peralta);
        fuenteSub = getResources().getFont(R.font.roboto_light);
        fuenteSubN = getResources().getFont(R.font.roboto);


        // Inicializar Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Referencia a la tabla "alumnos"
        DatabaseReference alumnosRef = mDatabase.child("Alumnos");

        // Referencia a la tabla "empresas"
        DatabaseReference empresasRef = mDatabase.child("Empresas");

        //asignamos el metodo para filtrar
        lupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarEmpresas(empresasRef);
                buscarAlumnos(alumnosRef);
            }
        });
        buscador.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Verificamos si la acción es el botón Enter del teclado
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                                && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    //buscamos las empresas y alumnos segun el contenido del buscador
                    buscarEmpresas(empresasRef);
                    buscarAlumnos(alumnosRef);
                    return true;
                }
                return false;
            }
        });
        //recogemos todos los datos de la bd relacionados con estas tablas
        buscarAlumnos(alumnosRef);
        buscarEmpresas(empresasRef);
        return view;
    }

    //funciones
    private void buscarAlumnos(DatabaseReference alumnosRef) {
        // Escuchar cambios en la tabla "alumnos"
        alumnosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //borramos los datos
                layaoutAlumno.removeAllViews();
                // Iterar sobre cada registro en la tabla "alumnos"
                for (DataSnapshot alumnoSnapshot : dataSnapshot.getChildren()) {
                    String id = alumnoSnapshot.getKey();
                    // Obtener los valores de cada campo del alumno
                    //si tienen de profesor al usuario
                    String nombre = alumnoSnapshot.child("nombre").getValue(String.class);
                    String empresa = alumnoSnapshot.child("empresa").getValue(String.class);
                    String imagen = alumnoSnapshot.child("imagen").getValue(String.class);

                    Alumno alumno = new Alumno(id, nombre, empresa, imagen);
                    filtro(buscador.getText().toString(), alumno);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores de lectura de la base de datos
                Log.e("Firebase", "Error al leer datos", databaseError.toException());
            }
        });
    }

    private void buscarEmpresas(DatabaseReference empresasRef) {
        // Escuchar cambios en la tabla "empresas"
        empresasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //borramos los datos
                layaoutEmpresa.removeAllViews();
                // Iterar sobre cada registro en la tabla "empmresas"
                for (DataSnapshot empresaSnapshot : dataSnapshot.getChildren()) {

                    // Obtener los valores de cada campo de la empresa
                    String nombre = empresaSnapshot.child("nombre").getValue(String.class);
                    String tipo = empresaSnapshot.child("tipo").getValue(String.class);
                    String descrip = empresaSnapshot.child("descripcion").getValue(String.class);
                    String img = empresaSnapshot.child("imagen").getValue(String.class);
                    //creamos un objeto emrpresa
                    Empresa emp = new Empresa(nombre, tipo, descrip, img);
                    filtro(buscador.getText().toString(), emp);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores de lectura de la base de datos
                Log.e("Firebase", "Error al leer datos", databaseError.toException());
            }
        });
    }

    private void mostrarAlumnos(Alumno alum) {

        String nombre = alum.getNombre();
        String empresa = alum.getEmpresa();
        String img = alum.getImagen();

        if (nombre != null && empresa != null) {
            // Creamos una tarjeta
            CardView tarjeta = new CardView(requireContext());

            // Configuramos los márgenes para la tarjeta
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(20, 0, 30, 0);

            // Añadimos los parámetros a la tarjeta
            tarjeta.setLayoutParams(params);
            tarjeta.setMinimumHeight(200);
            //Añadimos la función que nos lleva al perfil del alumno
            tarjeta.setOnClickListener(f -> perfilAlumno(nombre, alum.getId()));

            tarjeta.setRadius(40);
            tarjeta.setCardBackgroundColor(getResources().getColor(R.color.azulOscuro));
            tarjeta.setCardElevation(20);
            tarjeta.setTranslationZ(-5);

            // Layout para la tarjeta
            LinearLayout layoutTarjeta = new LinearLayout(requireContext());
            layoutTarjeta.setPadding(20, 20, 30, 20);
            layoutTarjeta.setOrientation(LinearLayout.VERTICAL);
            // Centramos el contenido de la tarjeta
            layoutTarjeta.setGravity(Gravity.CENTER_VERTICAL);

            // Creamos un texto para el título de la tarjeta
            TextView titulo = new TextView(requireContext());
            titulo.setText(nombre);
            titulo.setTextSize(13);
            titulo.setTextColor(getResources().getColor(R.color.white));
            // Aplicamos la fuente desde la carpeta "fonts"
            titulo.setTypeface(fuenteTitulo);
            titulo.setPadding(10, 20, 10, 0);
            //acortamos
            String texto = titulo.getText().toString();
            if (texto.length() > 13) {
                texto = texto.substring(0, 13) + "...";
            }
            titulo.setText(texto);

            // Creamos un texto para la descripción
            TextView desc = new TextView(requireContext());
            desc.setText(empresa);
            desc.setTextSize(13);
            desc.setTextColor(getResources().getColor(R.color.white));
            desc.setTypeface(fuenteSub);
            desc.setPadding(10, 20, 10, 20);

            // Creamos una imagen
            ImageView imagen = new ImageView(requireContext());
            imagen.setPadding(0, 2, 0, 0);
            int altura = 300;
            int anchura = 300;
            if (img != null && img.isEmpty()) {

                Glide.with(this).
                        load(R.drawable.logo).apply(new RequestOptions().override(anchura, altura)).into(imagen);
            } else {
                if (img == null) {
                    Glide.with(this).
                            load(R.drawable.logo).apply(new RequestOptions().override(anchura, altura)).into(imagen);
                } else {
                    // Cargamos la imagen
                    Glide.with(this).load(img).apply(new RequestOptions().override(anchura, altura)).into(imagen);
                }

            }
            // Agregamos la imagen al layout de la tarjeta
            layoutTarjeta.addView(imagen);

            // Agregamos el layout de la tarjeta a la tarjeta
            tarjeta.addView(layoutTarjeta);
            // Añadimos el contenido al layout
            layoutTarjeta.addView(titulo);
            layoutTarjeta.addView(desc);

            // Agregamos la tarjeta al layout
            layaoutAlumno.addView(tarjeta);
            //ocultamos la carga
            barraProgreso.setVisibility(View.GONE);
        } else {
            Log.e(TAG, nombre + " " + empresa);
        }
    }

    private void mostrarEpresas(Empresa emp) {
        String nombre = emp.getNombre();
        String tipo = emp.getTipo();
        String descrip = emp.getDescrip();
        String img = emp.getImg();

        if (nombre != null && tipo != null) {
            // Creamos una tarjeta
            CardView tarjeta = new CardView(requireContext());

            // Configuramos los márgenes para la tarjeta
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(10, 20, 10, 30);

            // Añadimos los parámetros a la tarjeta
            tarjeta.setLayoutParams(params);
            tarjeta.setMinimumHeight(200);
            //tarjeta.getId();
            //Añadimos la función que nos lleva al perfil de empresa
            tarjeta.setOnClickListener(f -> perfilEmpresa(nombre));

            tarjeta.setRadius(40);
            tarjeta.setCardBackgroundColor(getResources().getColor(R.color.azulOscuro));
            tarjeta.setCardElevation(20);
            tarjeta.setTranslationZ(-5);

            // Layout para la tarjeta
            LinearLayout layoutTarjeta = new LinearLayout(requireContext());
            layoutTarjeta.setPadding(20, 20, 30, 20);
            layoutTarjeta.setOrientation(LinearLayout.HORIZONTAL);

            //creamos un layout para el texto
            LinearLayout layoutTexto = new LinearLayout(requireContext());
            layoutTexto.setPadding(20, 20, 30, 20);
            layoutTexto.setOrientation(LinearLayout.VERTICAL);
            // Centramos el contenido de la tarjeta
            layoutTexto.setGravity(Gravity.CENTER_VERTICAL);

            // Creamos un texto para el título
            TextView titulo = new TextView(requireContext());
            titulo.setText(nombre);
            titulo.setTextSize(18);
            titulo.setTextColor(getResources().getColor(R.color.white));
            // Aplicamos la fuente desde la carpeta "fonts"
            titulo.setTypeface(fuenteTitulo);
            titulo.setPadding(10, 20, 10, 0);
            //acortamos
            String texto = titulo.getText().toString();
            if (texto.length() > 13) {
                texto = texto.substring(0, 13) + "...";
            }
            titulo.setText(texto);

            // Creamos un texto para el tipo
            TextView tipoEmpresa = new TextView(requireContext());
            tipoEmpresa.setText(tipo);
            tipoEmpresa.setTextSize(13);
            tipoEmpresa.setTextColor(getResources().getColor(R.color.white));
            tipoEmpresa.setTypeface(fuenteSubN);
            tipoEmpresa.setPadding(10, 20, 10, 20);

            // Creamos un texto para la descripción
            TextView desc = new TextView(requireContext());
            desc.setText(descrip);
            desc.setTextSize(13);
            desc.setTextColor(getResources().getColor(R.color.white));
            desc.setTypeface(fuenteSub);
            desc.setPadding(10, 20, 10, 20);
            //acortamos
            String descCorta = desc.getText().toString();
            if (descCorta.length() > 90) {
                descCorta = descCorta.substring(0, 90) + "...";
            }
            desc.setText(descCorta);

            // Creamos una imagen
            ImageView imagen = new ImageView(requireContext());
            imagen.setPadding(0, 0, 0, 0);
            int altura = 400;
            int anchura = 400;
            if (img != null && img.isEmpty()) {

                Glide.with(this).
                        load(R.drawable.logo).apply(new RequestOptions().override(anchura, altura)).into(imagen);
            } else {
                if (img == null) {
                    Glide.with(this).
                            load(R.drawable.logo).apply(new RequestOptions().override(anchura, altura)).into(imagen);
                } else {
                    // Cargamos la imagen
                    Glide.with(this).load(img).apply(new RequestOptions().override(anchura, altura)).into(imagen);
                }

            }
            imagen.setImageAlpha(200);
            // Agregamos la imagen al layout de la tarjeta
            layoutTarjeta.addView(imagen);

            // Añadimos el contenido al layoutTexto
            layoutTexto.addView(titulo);
            layoutTexto.addView(tipoEmpresa);
            layoutTexto.addView(desc);

            //Agregamos el layout texto al de la tarjeta
            layoutTarjeta.addView(layoutTexto);

            // Agregamos el layout de la tarjeta a la tarjeta
            tarjeta.addView(layoutTarjeta);

            // Agregamos la tarjeta al layout
            layaoutEmpresa.addView(tarjeta);
            //ocultamos la carga
            barraProgreso = view.findViewById(R.id.progressBarEmpresas);
            barraProgreso.setVisibility(View.GONE);
        }
    }


    public void perfilEmpresa(String nombreEmpresa) {

        int id = navController.getCurrentDestination().getId();
        // Creamos un Bundle para pasar los argumentos
        Bundle args = new Bundle();
        args.putString("nombreEmpresa", nombreEmpresa);
        if (id != R.id.empresaFragment) {
            navController.navigate(R.id.action_inicioFragment3_to_empresaFragment2, args);

        }
    }

    public void perfilAlumno(String nombreAlumno, String idAlumno) {

        int id = navController.getCurrentDestination().getId();
        // Creamos un Bundle para pasar los argumentos
        Bundle args = new Bundle();
        args.putString("nombreAlumno", nombreAlumno);
        args.putString("idAlumno", idAlumno);
        if (id != R.id.empresaFragment) {
            navController.navigate(R.id.action_inicioFragment3_to_perfilAlumnoFragment2, args);

        }
    }

    public void filtro(String textBusqueda, Object user) {
        if (user == null) {
            return;
        }

        String textoBuscado = textBusqueda.toLowerCase();
        boolean isBusquedaVacia = textBusqueda.isEmpty();

        if (user instanceof Alumno) {
            Alumno alumno = (Alumno) user;
            if (isBusquedaVacia || alumno.getNombre().toLowerCase().contains(textoBuscado)) {
                mostrarAlumnos(alumno);
            }
        } else if (user instanceof Empresa) {
            Empresa empresa = (Empresa) user;
            if (isBusquedaVacia || empresa.getNombre().toLowerCase().contains(textoBuscado)) {
                mostrarEpresas(empresa);
            }
        }
    }

}