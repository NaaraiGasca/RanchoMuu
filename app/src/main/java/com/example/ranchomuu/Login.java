package com.example.ranchomuu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    Activity activity = Login.this;
    Button btnLogin, btnRegis;
    EditText email, password;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.correoLog);
        password = findViewById(R.id.passwordLog);
        btnLogin = findViewById(R.id.btnIngresarLog);
        btnRegis = findViewById(R.id.btnRegistroRedirect);

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Registro.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailUser = email.getText().toString().trim();
                String passUser = password.getText().toString().trim();
                if (emailUser.isEmpty() || passUser.isEmpty())
                {
                    Toast.makeText(Login.this, "Debe completar los campos.", Toast.LENGTH_SHORT).show();
                }else
                    {
                            loginUser(emailUser, passUser);
                    }
            }
        });
    }

    private void loginUser(String emailUser, String passUser)
    {
        mAuth.signInWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {
                    finish();
                    if(emailUser.equals("admin@gmail.com"))
                    {
                        Toast.makeText(Login.this, "Bienvenido.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, MainActivity.class));
                    }else
                    {
                        Toast.makeText(Login.this, "Bienvenido.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, MainActivityusu.class));
                    }

                }else
                    {
                        Toast.makeText(Login.this, "Datos de usuario invalidos.", Toast.LENGTH_SHORT).show();
                        Limpiar();
                    }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "Error al iniciar sesión.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!= null)
        {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }
    }

    void Limpiar()
    {
        email.setText("");
        password.setText("");

    }
}