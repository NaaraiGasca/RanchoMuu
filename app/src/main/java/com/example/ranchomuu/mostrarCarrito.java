package com.example.ranchomuu;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ranchomuu.adapter.PedidosAdapter;
import com.example.ranchomuu.model.Pedidos;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class mostrarCarrito extends AppCompatActivity {

    RecyclerView mRecyclerViewPedidos;
    PedidosAdapter pedidosAdapter;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    Button btnRealizarPedido, btnCancelarPedido;
    TextView tvTotal;
    int precioTotal = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Carrito de compras");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        String id = mAuth.getCurrentUser().getUid();

        setContentView(R.layout.activity_mostrar_carrito);
        mFirestore = FirebaseFirestore.getInstance();
        mRecyclerViewPedidos = findViewById(R.id.rvMostrarCarrito);
        btnRealizarPedido = findViewById(R.id.btnFinalizarPedido);
        tvTotal = findViewById(R.id.tvTotal);
        btnCancelarPedido = findViewById(R.id.btnCancelarPedido);
        btnCancelarPedido.setEnabled(false);
        mRecyclerViewPedidos.setLayoutManager(new LinearLayoutManager(this));
        Query query = mFirestore.collection("pedidos").whereEqualTo("id", id);



        FirestoreRecyclerOptions<Pedidos> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Pedidos>().setQuery(query, Pedidos.class).build();
        pedidosAdapter = new PedidosAdapter(firestoreRecyclerOptions, mostrarCarrito.this);
        pedidosAdapter.notifyDataSetChanged();
        mRecyclerViewPedidos.setAdapter(pedidosAdapter);
        calcularPedido(id);
        btnRealizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerPedido(id);
                //Toast.makeText(mostrarCarrito.this, "Pedido realizado correctamente.", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerIdDocumento(id);
            }
        });
    }

    private void obtenerIdDocumento(String id)
    {
        final String[] idDoc = {""};
        mFirestore.collection("pedidosAdmin").whereEqualTo("id", id)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult())
                        {
                            idDoc[0] = documentSnapshot.getId();
                        }
                        String idDocumento = idDoc[0];
                        Log.i("ID: ", idDocumento);
                        cancelarPedido(idDocumento);
                    }
                });
    }

    private void cancelarPedido(String id)
    {
        mFirestore.collection("pedidosAdmin").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(mostrarCarrito.this, "Pedido cancelado correctamente.", Toast.LENGTH_SHORT).show();
                btnCancelarPedido.setEnabled(false);
                btnRealizarPedido.setEnabled(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mostrarCarrito.this, "Error al cancelar pedido.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calcularPedido(String id)
    {
        final String[] nom = new String[3];
        nom[0] = "";
        nom[1] = "";
        nom[2] = "";
        final int[] cont = new int[1];
        cont[0] = 0;
        mFirestore.collection("pedidos")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult())
                            {
                                Log.i("DATOS: ", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                nom[0] = nom[0] + documentSnapshot.getString("nombreProducto") + ": " + documentSnapshot.getString("cantidadProducto") + " piezas." + "\n";
                                nom[1] = nom[1] + documentSnapshot.getString("precioProducto") + "-";
                                nom[2] = nom[2] + documentSnapshot.getString("cantidadProducto") + "-";
                                cont[0] = cont[0] + 1;

                            }

                            String productos = nom[0];
                            String precio = nom[1];
                            String cantidad = nom[2];

                            int contador = cont[0];

                            String[] costo = precio.split("-");
                            String[] cantidades = cantidad.split("-");
                            int n1 = 0;
                            int n2 = 0;
                            int n3 = 0;
                            for (int i=0; i<contador; i++)
                            {
                                n1 = Integer.parseInt(costo[i]);
                                n2 = Integer.parseInt(cantidades[i]);
                                n3 = n3 + (n1 * n2);
                            }
                            tvTotal.setText("Total: $"+ n3);
                            Log.i("CONTADOR: ", String.valueOf(contador));
                            Log.i("REPORTE: ", productos);
                            Log.i("PRECIO: ", String.valueOf(n3));

                        }else
                        {
                            Log.i(TAG, "ERROR: ", task.getException());
                        }
                    }
                });
    }

    private void obtenerPedido(String id)
    {
        final String[] nom = new String[3];
        nom[0] = "";
        nom[1] = "";
        nom[2] = "";
        final int[] cont = new int[1];
        cont[0] = 0;

        final String[] reporte = new String[2];
        reporte[0] = "";
        reporte[1] = "";

        final String[] usuario = new String[1];
        usuario[0] = "";

        String idUser = mAuth.getCurrentUser().getUid();
        mFirestore.collection("user").whereEqualTo("id", idUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult())
                            {
                                //Log.i("USUARIO: ", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                usuario[0] = documentSnapshot.getString("name");

                            }

                            mFirestore.collection("pedidos")
                                    .whereEqualTo("id", id)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful())
                                            {
                                                for (QueryDocumentSnapshot documentSnapshot : task.getResult())
                                                {
                                                    Log.i("DATOS: ", documentSnapshot.getId() + " => " + documentSnapshot.getData());

                                                    nom[1] = nom[1] + documentSnapshot.getString("precioProducto") + "-";
                                                    reporte[0] = documentSnapshot.getString("precioProducto");

                                                    nom[2] = nom[2] + documentSnapshot.getString("cantidadProducto") + "-";
                                                    reporte[1] = documentSnapshot.getString("cantidadProducto");
                                                    cont[0] = cont[0] + 1;

                                                    String prec = reporte[0];
                                                    String canti = reporte[1];

                                                    int pre = Integer.parseInt(prec);
                                                    int can = Integer.parseInt(canti);
                                                    int sum = pre * can;
                                                    nom[0] = nom[0] + documentSnapshot.getString("nombreProducto") + ": " +
                                                            documentSnapshot.getString("cantidadProducto")
                                                            + " piezas." + " = $" + sum + "." + "\n";
                                                }

                                                String productos = nom[0];
                                                String precio = nom[1];
                                                String cantidad = nom[2];

                                                int contador = cont[0];

                                                String[] costo = precio.split("-");
                                                String[] cantidades = cantidad.split("-");
                                                int n1 = 0;
                                                int n2 = 0;
                                                int n3 = 0;
                                                for (int i=0; i<contador; i++)
                                                {
                                                    n1 = Integer.parseInt(costo[i]);
                                                    n2 = Integer.parseInt(cantidades[i]);
                                                    n3 = n3 +(n1 * n2);
                                                }
                                                tvTotal.setText("Total: $"+ n3);
                                                Log.i("CONTADOR: ", String.valueOf(contador));
                                                Log.i("REPORTE: ", productos);
                                                Log.i("TOTAL: ", String.valueOf(n3));

                                                Log.i("MI CLIENTE ES: ", usuario[0]);
                                                if (productos == "")
                                                {
                                                    Toast.makeText(getApplicationContext(), "No tiene ningun producto en su carrito " +
                                                            "para realizar el pedido. Agregue productos e intentelo de nuevo.", Toast.LENGTH_SHORT).show();
                                                }else
                                                    {
                                                        altaPedidoFinal(productos, n3, usuario[0]);
                                                    }
                                            }else
                                            {
                                                Log.i(TAG, "ERROR: ", task.getException());
                                            }
                                        }
                                    });
                        }else
                        {
                            Log.i(TAG, "ERROR: ", task.getException());
                        }
                    }
                });


    }

    private void altaPedidoFinal(String reporte, int total, String nombre)
    {
        String id = mAuth.getCurrentUser().getUid();

        String tot = String.valueOf(total);
        Map<String, Object> productos = new HashMap<>();
        productos.put("id", id);
        productos.put("reportePedido", reporte);
        productos.put("totalPedido", tot);
        productos.put("cliente", nombre);

        mFirestore.collection("pedidosAdmin").add(productos).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Pedido enviado a la tienda.", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(mostrarCarrito.this, PedidosUsuario.class));
                btnCancelarPedido.setEnabled(true);
                btnRealizarPedido.setEnabled(false);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al enviar el pedido.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        pedidosAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        pedidosAdapter.stopListening();
    }
}