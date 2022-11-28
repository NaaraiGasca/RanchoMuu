package com.example.ranchomuu;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.pascmsx.tienda.adapter.PedidosAdminAdapter;
import com.pascmsx.tienda.model.PedidosAdmin;

public class mostrarPedidosAdmin extends AppCompatActivity {

    RecyclerView mRecycler;
    PedidosAdminAdapter mAdapter;
    FirebaseFirestore mFirestore;
    SearchView searchView;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_pedidos_admin);
        mFirestore = FirebaseFirestore.getInstance();
        mRecycler = findViewById(R.id.rvPedidosAdmin);
        searchView = findViewById(R.id.svPedidosAdmin);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        Query query = mFirestore.collection("pedidosAdmin");

        FirestoreRecyclerOptions<PedidosAdmin> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<PedidosAdmin>().setQuery(query, PedidosAdmin.class).build();

        mAdapter = new PedidosAdminAdapter(firestoreRecyclerOptions, this);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);

        searchViewB();
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
        query = mFirestore.collection("pedidosAdmin");
        FirestoreRecyclerOptions<PedidosAdmin> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<PedidosAdmin>()
                        .setQuery(query.orderBy("cliente")
                                .startAt(s).endAt(s+"~"), PedidosAdmin.class).build();
        mAdapter = new PedidosAdminAdapter(firestoreRecyclerOptions, this);
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
        mAdapter.stopListening();
    }
}