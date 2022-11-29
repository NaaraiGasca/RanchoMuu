package com.example.ranchomuu;
import android.os.Bundle;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ranchomuu.adapter.PedidosUsuarioAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.example.ranchomuu.model.PedidosUsuario;

public class mostrarPedidosUsuario extends AppCompatActivity {

    RecyclerView mRecycler;
    PedidosUsuarioAdapter mAdapter;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Mis pedidos");

        mAuth = FirebaseAuth.getInstance();
        String id = mAuth.getCurrentUser().getUid();

        setContentView(R.layout.activity_mostrar_pedidos_usuario);
        mFirestore = FirebaseFirestore.getInstance();
        mRecycler = findViewById(R.id.rvPedidosUsuario);
        searchView = findViewById(R.id.svPedidosUsuario);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        Query query = mFirestore.collection("pedidosAdmin").whereEqualTo("id", id);

        FirestoreRecyclerOptions<PedidosUsuario> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<PedidosUsuario>().setQuery(query, PedidosUsuario.class).build();

        mAdapter = new PedidosUsuarioAdapter(firestoreRecyclerOptions, this);
        mAdapter.notifyDataSetChanged();
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