package com.example.ranchomuu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivityusu extends AppCompatActivity {

    Button btnVideo, btnMapa, btnPedidos, btnMisPedidos;
    TextView mensaje;
    FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String id = mAuth.getCurrentUser().getUid();
        setContentView(R.layout.activity_main_activityusu);
        btnMapa = findViewById(R.id.btnMapa);
        FloatingActionButton fab = findViewById(R.id.ufbtncerrarsesion);
        btnPedidos = findViewById(R.id.ubtnPedidos);
        btnMisPedidos = findViewById(R.id.ubtnVerMisPedidos);
        mensaje = findViewById(R.id.tvMensaje);
        obtenerUsuario(id);

        //mensaje.setText("Bienvenido " + id);
        //btnVideo = findViewById(R.id.btnVideo);

        //btnVideo.setOnClickListener(new View.OnClickListener() {
        //    @Override
         //   public void onClick(View view) {
           //     startActivity(new Intent(MainActivityusu.this, video.class));
          //  }
        //});

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                finish();
                startActivity(new Intent(MainActivityusu.this, Login.class));
            }
        });

        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivityusu.this, GPSTiendita.class));
            }
        });

        btnPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivityusu.this, PedidosUsuario.class));
            }
        });

        btnMisPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivityusu.this, mostrarPedidosUsuario.class));
            }
        });

    }

    private void obtenerUsuario(String id)
    {
        mFirestore.collection("user").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String emailUser = documentSnapshot.getString("email");
                String idUser = documentSnapshot.getString("id");
                String nameUser = documentSnapshot.getString("name");
                mensaje.setText("Bienvenido: " + nameUser);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al obtener los datos.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}