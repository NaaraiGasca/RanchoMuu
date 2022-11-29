package com.example.ranchomuu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.example.ranchomuu.adapter.productoAdapter;
import com.example.ranchomuu.model.producto;

public class Eliminar extends AppCompatActivity {

    RecyclerView mRecycler;
    productoAdapter mAdapter;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar);

        this.setTitle("Listar productos");
        mFirestore = FirebaseFirestore.getInstance();
        mRecycler = findViewById(R.id.rvProductos);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        Query query = mFirestore.collection("productos");

        FirestoreRecyclerOptions<producto> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<producto>().setQuery(query, producto.class).build();
        mAdapter = new productoAdapter(firestoreRecyclerOptions, this, getSupportFragmentManager());
        mAdapter.notifyDataSetChanged();
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