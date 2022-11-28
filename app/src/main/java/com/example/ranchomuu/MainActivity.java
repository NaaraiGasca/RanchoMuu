package com.example.ranchomuu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button btnAlta, btnMostrar, btnPedidos;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        btnAlta = findViewById(R.id.btnAltaProductos);
        btnMostrar = findViewById(R.id.btnMostrar);
        //btnBorrar = findViewById(R.id.btnEliminaP);
        btnPedidos = findViewById(R.id.btnPedidos);


        /*btnAlta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AltaProductoActivity.class));
            }
        });*/

        btnPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, mostrarPedidosAdmin.class));
            }
        });

        btnAlta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AltaProducto fm = new AltaProducto();
                fm.show(getSupportFragmentManager(), "Navegar a Alta");
            }
        });

        btnMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Mostrar.class));
            }
        });

        FloatingActionButton fab = findViewById(R.id.fbtncerrarsesion);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });


    }
}