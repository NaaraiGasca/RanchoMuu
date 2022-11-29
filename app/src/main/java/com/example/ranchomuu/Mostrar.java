package com.example.ranchomuu;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.example.ranchomuu.adapter.productoAdapter;
import com.example.ranchomuu.model.producto;

public class Mostrar extends AppCompatActivity {

    RecyclerView mRecycler;
    productoAdapter mAdapter;
    FirebaseFirestore mFirestore;
    SearchView searchView;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar);
        mFirestore = FirebaseFirestore.getInstance();

        searchView = findViewById(R.id.svBusqueda);
        this.setTitle("Listar productos");

        setUpRecyclerView();
        searchViewB();
    }

    private void setUpRecyclerView()
    {
        mRecycler = findViewById(R.id.rvProductos);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        query = mFirestore.collection("productos");

        FirestoreRecyclerOptions<producto> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<producto>().setQuery(query, producto.class).build();

        mAdapter = new productoAdapter(firestoreRecyclerOptions, Mostrar.this, getSupportFragmentManager());
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
        mAdapter = new productoAdapter(firestoreRecyclerOptions, this, getSupportFragmentManager());
        mAdapter.startListening();
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}