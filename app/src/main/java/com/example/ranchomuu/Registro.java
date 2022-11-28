package com.example.ranchomuu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {


    Button btnRegister;
    EditText name, email, password;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.nombreAct);
        email = findViewById(R.id.correoAct);
        password = findViewById(R.id.passwordAct);
        btnRegister = findViewById(R.id.btnRegistroProducto);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomUsuario = name.getText().toString().trim();
                String emailUsuario = email.getText().toString().trim();
                String passUsuario = password.getText().toString().trim();

                if (nomUsuario.isEmpty() || passUsuario.isEmpty() || emailUsuario.isEmpty())
                {
                    Toast.makeText(Registro.this, "Complete los campos.", Toast.LENGTH_SHORT).show();
                }else
                {
                    if(ValidarEmail(emailUsuario)==true){
                        if(passUsuario.length()>=9)
                        {
                            registrarUsuario(nomUsuario, emailUsuario, passUsuario);
                        }else
                        {
                            Toast.makeText(Registro.this, "Contraseña demasiado corta", Toast.LENGTH_SHORT).show();
                        }

                    }else
                    {
                        Toast.makeText(Registro.this, "Email no valido", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private void registrarUsuario(String nomUsuario, String emailUsuario, String passUsuario)
    {
        mAuth.createUserWithEmailAndPassword(emailUsuario, passUsuario).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String id = mAuth.getCurrentUser().getUid();
                Map<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("name", nomUsuario);
                map.put("email", emailUsuario);
                map.put("password", passUsuario);
                mFirestore.collection("user").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                        Toast.makeText(Registro.this, "Usuario registrado con éxito.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Registro.this, Login.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registro.this, "Error al guardar.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registro.this, "Error al registrar usuario.", Toast.LENGTH_SHORT).show();
            }
        });
        FloatingActionButton fab = findViewById(R.id.rfbtncerrarsesion);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(Registro.this, Login.class));
            }
        });
    }
    private boolean ValidarEmail(String email)
    {
        Pattern pattern= Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}