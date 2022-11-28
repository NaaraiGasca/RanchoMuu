package com.example.ranchomuu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AltaProductoPedido extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    EditText nombre, precio, cantidad;
    ImageView fotoProducto;
    Button crearPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_producto_pedido);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        this.setTitle("Agregar producto a carrito");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nombre = findViewById(R.id.productoPedido);
        precio = findViewById(R.id.precioProductoPedido);
        cantidad = findViewById(R.id.productoCantidadPedido);
        fotoProducto = findViewById(R.id.btnFotoProductoPedido);
        crearPedido = findViewById(R.id.btnRegistroProductoPedido);

        nombre.setEnabled(false);
        precio.setEnabled(false);

        String id = getIntent().getStringExtra("id_producto");

        if (id!=null || id!="")
        {
            obtenerProducto(id);
            crearPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nom = nombre.getText().toString().trim();
                    String pr = precio.getText().toString().trim();
                    String can = cantidad.getText().toString().trim();
                    int cant= Integer.parseInt(can);

                    if (can.isEmpty() || cant <=0)
                    {
                        Toast.makeText(getApplicationContext(), "Introduzca la cantidad de productos a pedir.", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        mFirestore.collection("productos").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String can = documentSnapshot.getString("cantidadProducto");
                                int stock = Integer.parseInt(can);
                                if (cant > stock)
                                {
                                    Toast.makeText(AltaProductoPedido.this, "El stock disponible es: " + stock
                                            + ". Intente con una cantidad menor", Toast.LENGTH_SHORT).show();
                                }else
                                {
                                    altaProductos(nom, pr, can);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Error al obtener los datos.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            });
        }
    }

    private void validaStock(String id, int cant)
    {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    private void obtenerProducto(String id)
    {
        mFirestore.collection("productos").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String nom = documentSnapshot.getString("nombreProducto");
                String pr = documentSnapshot.getString("precioProducto");
                String can = documentSnapshot.getString("cantidadProducto");
                String photoProducto = documentSnapshot.getString("photo");
                nombre.setText(nom);
                precio.setText(pr);
                try {
                    if (!photoProducto.equals(""))
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0 , 200);
                        toast.show();
                        Picasso.with(AltaProductoPedido.this)
                                .load(photoProducto)
                                .resize(150, 150)
                                .into(fotoProducto);
                    }
                }catch (Exception e)
                {
                    Log.v("Error", "error: " + e);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al obtener los datos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void altaProductos(String nom, String pr, String can)
    {
        String id = mAuth.getCurrentUser().getUid();
        Map<String, Object> productos = new HashMap<>();
        productos.put("id", id);
        productos.put("nombreProducto", nom);
        productos.put("precioProducto", pr);
        productos.put("cantidadProducto", can);

        mFirestore.collection("pedidos").add(productos).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Pedido agregado al carrito correctamente", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AltaProductoPedido.this, PedidosUsuario.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}