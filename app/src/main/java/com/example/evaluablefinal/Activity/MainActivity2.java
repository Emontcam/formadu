package com.example.evaluablefinal.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.evaluablefinal.R;
import com.example.evaluablefinal.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;

//public class MainActivity2 extends BaseActivity {
//   private ActivityMainBinding binding;

//   @Override
//   protected void onCreate(Bundle savedInstanceState) {
//       super.onCreate(savedInstanceState);
//       binding = ActivityMainBinding.inflate(getLayoutInflater());
//       setContentView(binding.getRoot());

//     //  initBestFood();
//     //  initCategory();
//       setVariable();
//   }

//   private void setVariable() {
//       binding.logoutBtn.setOnClickListener(v -> {
//           FirebaseAuth.getInstance().signOut();
//           startActivity(new Intent(MainActivity2.this, LoginActivity.class));
//       });

//       binding.searchBtn.setOnClickListener(v -> {
//           String text = binding.searchEdt.getText().toString();
//           if (!text.isEmpty()) {
//               Intent intent = new Intent(MainActivity2.this, ListFoodsActivity.class);
//               intent.putExtra("text", text);
//               intent.putExtra("isSearch", true);
//               startActivity(intent);
//           }
//       });

//      //binding.cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
//   }

//   private void mostrar() {

//           // Creamos una tarjeta
//           CardView tarjeta = new CardView(this);

//           // Configuramos los márgenes para la tarjeta
//           LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                   LinearLayout.LayoutParams.MATCH_PARENT,
//                   LinearLayout.LayoutParams.WRAP_CONTENT
//           );
//           params.setMargins(0, 0, 0, 50);

//           // Añadimos los parámetros a la tarjeta
//           tarjeta.setLayoutParams(params);
//           tarjeta.setMinimumHeight(200);

//           tarjeta.setRadius(30);
//           tarjeta.setCardBackgroundColor(getResources().getColor(R.color.amarillo));

//           // Layout para la tarjeta
//           LinearLayout layoutTarjeta = new LinearLayout(this);
//           layoutTarjeta.setOrientation(LinearLayout.HORIZONTAL);
//           layoutTarjeta.setPadding(20, 40, 20, 40);
//           // Centramos el contenido de la tarjeta
//           layoutTarjeta.setGravity(Gravity.CENTER_VERTICAL);

//           // Creamos un texto para el título
//           TextView titulo = new TextView(this);
//           titulo.setText(nombre);
//           titulo.setTextSize(22);
//           titulo.setTextColor(getResources().getColor(R.color.verdeClaro));
//           titulo.setPadding(50, 20, 20, 20);

//           // Creamos un texto para la descripción
//           TextView desc = new TextView(this);
//           desc.setText(descripcion);
//           desc.setTextSize(15);
//           desc.setTextColor(getResources().getColor(R.color.verdeMedio));
//           desc.setPadding(50, 20, 20, 20);
//           String descripcionTexto = desc.getText().toString();
//           if (descripcionTexto.length() > 65) {
//               descripcionTexto = descripcionTexto.substring(0, 65) + "...";
//           }
//           desc.setText(descripcionTexto);
//           // Layout para el texto
//           LinearLayout layoutTexto = new LinearLayout(this);
//           layoutTexto.setOrientation(LinearLayout.VERTICAL);
//           // Añadimos el contenido al layout
//           layoutTexto.addView(titulo);
//           layoutTexto.addView(desc);

//           // Creamos una imagen
//           ImageView imagen = new ImageView(this);
//           int altura = 200;
//           int anchura = 200;
//           if (img.isEmpty()) {
//               Glide.with(this).load(R.drawable.logo).apply(new RequestOptions().override(anchura, altura)).into(imagen);
//           } else {
//               // Cargamos la imagen
//               Glide.with(this).load(img).apply(new RequestOptions().override(anchura, altura)).into(imagen);
//           }

//           // Agregamos la imagen al layout de la tarjeta
//           layoutTarjeta.addView(imagen);
//           // Añadimos el layout de texto al de la tarjeta
//           layoutTarjeta.addView(layoutTexto);

//           // Agregamos el layout de la tarjeta a la tarjeta
//           tarjeta.addView(layoutTarjeta);

//           // Agregamos la tarjeta al layout
//           layaoutAlumno.addView(tarjeta);


//   }

//           @Override
//           public void onCancelled(@NonNull DatabaseError error) {

//           }
//       });
//   }

//   private void initCategory() {
//       DatabaseReference myRef = database.getReference("Category");
//       binding.progressBarCategory.setVisibility(View.VISIBLE);
//       ArrayList<Category> list = new ArrayList<>();

//       myRef.addValueEventListener(new ValueEventListener() {
//           @Override
//           public void onDataChange(@NonNull DataSnapshot snapshot) {
//               list.clear(); // Limpiar la lista antes de actualizarla
//               for (DataSnapshot issue : snapshot.getChildren()) {
//                   list.add(issue.getValue(Category.class));
//               }
//               if (list.size() > 0) {
//                   binding.categoryView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
//                   RecyclerView.Adapter adapter = new CategoryAdapter(list);
//                   binding.categoryView.setAdapter(adapter);
//               }
//               binding.progressBarCategory.setVisibility(View.GONE);
//           }

//           @Override
//           public void onCancelled(@NonNull DatabaseError error) {
//               // Manejar errores de lectura de la base de datos
//           }
//       });
//   }


// //  private void initLocation() {
// //      DatabaseReference myRef = database.getReference("Location");
// //      ArrayList<Location> list = new ArrayList<>();
// //      myRef.addListenerForSingleValueEvent(new ValueEventListener() {
// //          @Override
// //          public void onDataChange(@NonNull DataSnapshot snapshot) {
// //              if (snapshot.exists()) {
// //                  for (DataSnapshot issue : snapshot.getChildren()) {
// //                      list.add(issue.getValue(Location.class));
// //                  }
// //                  ArrayAdapter<Location> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item, list);
// //                  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// //                  binding.locationSp.setAdapter(adapter);
// //              }
// //          }
///
// //          @Override
// //          public void onCancelled(@NonNull DatabaseError error) {
///
// //          }
// //      });
// //  }

// //  private void initTime() {
// //      DatabaseReference myRef = database.getReference("Time");
// //      ArrayList<Time> list = new ArrayList<>();
// //      myRef.addListenerForSingleValueEvent(new ValueEventListener() {
// //          @Override
// //          public void onDataChange(@NonNull DataSnapshot snapshot) {
// //              if (snapshot.exists()) {
// //                  for (DataSnapshot issue : snapshot.getChildren()) {
// //                      list.add(issue.getValue(Time.class));
// //                  }
// //                  ArrayAdapter<Time> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item, list);
// //                  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// //                  binding.timeSp.setAdapter(adapter);
// //              }
// //          }
///
// //          @Override
// //          public void onCancelled(@NonNull DatabaseError error) {
///
// //          }
// //      });
// //  }

//// private void initPrice() {
////     DatabaseReference myRef = database.getReference("Price");
////     ArrayList<Price> list = new ArrayList<>();
////     myRef.addListenerForSingleValueEvent(new ValueEventListener() {
////         @Override
////         public void onDataChange(@NonNull DataSnapshot snapshot) {
////             if (snapshot.exists()) {
////                 for (DataSnapshot issue : snapshot.getChildren()) {
////                     list.add(issue.getValue(Price.class));
////                 }
////                 ArrayAdapter<Price> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item, list);
////                 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////                 binding.priceSp.setAdapter(adapter);
////             }
////         }

////         @Override
////         public void onCancelled(@NonNull DatabaseError error) {

////         }
////     });
//// }

//