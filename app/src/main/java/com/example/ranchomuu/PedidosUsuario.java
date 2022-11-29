package com.example.ranchomuu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.example.ranchomuu.adapter.usuariosAdapter;
import com.example.ranchomuu.model.producto;

public class PedidosUsuario extends AppCompatActivity {

    RecyclerView mRecycler;
    usuariosAdapter mAdapter;
    FirebaseFirestore mFirestore;
    SearchView searchView;
    Query query;
    Button btnverCarrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_usuario);
        mFirestore = FirebaseFirestore.getInstance();
        btnverCarrito = findViewById(R.id.btnVerCarrito);
        searchView = findViewById(R.id.svBusquedaPedidos);
        //searchView.setQuery("Coca", false);
        this.setTitle("Pedidos de productos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpRecyclerView();
        searchViewB();

        btnverCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PedidosUsuario.this, mostrarCarrito.class));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    private void setUpRecyclerView()
    {
        mRecycler = findViewById(R.id.rvProductosPedidos);
        mRecycler.setLayoutManager(new LinearLayoutManager(PedidosUsuario.this));
        query = mFirestore.collection("productos");

        FirestoreRecyclerOptions<producto> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<producto>().setQuery(query, producto.class).build();

        mAdapter = new usuariosAdapter(firestoreRecyclerOptions, PedidosUsuario.this, getSupportFragmentManager());
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);
    }

    private void searchViewB()
    {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                textSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                textSearch(s);
                return false;
            }
        });
    }

    private void textSearch(String s)
    {
        FirestoreRecyclerOptions<producto> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<producto>()
                        .setQuery(query.orderBy("nombreProducto")
                                .startAt(s).endAt(s+"~"), producto.class).build();
        mAdapter = new usuariosAdapter(firestoreRecyclerOptions, PedidosUsuario.this, getSupportFragmentManager());
        mAdapter.startListening();
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.startListening();
    }
}