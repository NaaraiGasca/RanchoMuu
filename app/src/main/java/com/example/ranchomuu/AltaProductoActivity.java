package com.example.ranchomuu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AltaProductoActivity extends AppCompatActivity {
    ImageView fotoProducto;
    Button btnAgregarProducto, btnAgregarFoto, btnEliminarFoto;
    EditText nombre, precio, cantidad;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    StorageReference storageReference;
    String storage_path = "productos/*";

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    private Uri image_url;
    String photo = "photo";
    String idd;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_producto);
        this.setTitle("Agregar productos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        String id = getIntent().getStringExtra("id_producto");
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        nombre = findViewById(R.id.productoN);
        precio = findViewById(R.id.precioProductoN);
        cantidad = findViewById(R.id.stockProductoN);
        btnAgregarProducto = findViewById(R.id.btnRegistroProducto);

        fotoProducto = findViewById(R.id.btnFotoProducto);
        btnAgregarFoto = findViewById(R.id.btnAgregarFoto);
        btnEliminarFoto = findViewById(R.id.btnBorrarFoto);

        btnAgregarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPhoto();
            }
        });

        btnEliminarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("photo", "");
                mFirestore.collection("productos").document(idd).update(map);
                Toast.makeText(AltaProductoActivity.this, "Foto eliminada.", Toast.LENGTH_SHORT).show();
            }
        });

        if (id== null || id=="")
        {
            btnAgregarProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String nom = nombre.getText().toString().trim();
                    String pre = precio.getText().toString().trim();
                    String can = cantidad.getText().toString().trim();

                    if (nom.isEmpty() || pre.isEmpty() || can.isEmpty())
                    {
                        Toast.makeText(AltaProductoActivity.this, "Llenar todos los campos.", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        altaProductos(nom, pre, can);
                    }
                }
            });
        }else
            {
                idd = id;
                obtenerProducto(id);
                btnAgregarProducto.setText("Actualizar");
                btnAgregarProducto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nom = nombre.getText().toString().trim();
                        String pr = precio.getText().toString().trim();
                        String can = cantidad.getText().toString().trim();

                        if (nom.isEmpty() || pr.isEmpty() || can.isEmpty())
                        {
                            Toast.makeText(getApplicationContext(), "Complete los campos.", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            actualizarProductos(nom, pr, can, id);
                        }
                    }
                });
            }
    }

    private void uploadPhoto()
    {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");

        startActivityForResult(i, COD_SEL_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Log.d("image_url", "requestCode - RESULT_OK: " + requestCode+" "+RESULT_OK);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == COD_SEL_IMAGE)
            {
                image_url = data.getData();
                subirFoto(image_url);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void subirFoto(Uri image_url)
    {
        progressDialog.setMessage("Actualizando foto.");
        progressDialog.show();
        String rute_storage_photo = storage_path + "" + photo + "" + mAuth.getUid() +""+ idd;
        StorageReference reference = storageReference.child(rute_storage_photo);
        reference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                    if (uriTask.isSuccessful())
                    {
                        uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String download_uri = uri.toString();
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("photo", download_uri);
                                mFirestore.collection("productos").document(idd).update(map);
                                Toast.makeText(AltaProductoActivity.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AltaProductoActivity.this, "Error al cargar foto.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarProductos(String nom, String pr, String can, String id)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("nombreProducto", nom);
        map.put("precioProducto", pr);
        map.put("cantidadProducto", can);

        mFirestore.collection("productos").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Información actualizada correctamente.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al actualizar.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerProducto(String id)
    {
        mFirestore.collection("productos").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String nom = documentSnapshot.getString("nombreProducto");
                String pr = documentSnapshot.getString("precioProducto");
                String can = documentSnapshot.getString("cantidadProducto");
                String photoProducto = documentSnapshot.getString("photo");
                nombre.setText(nom);
                precio.setText(pr);
                cantidad.setText(can);
                try {
                    if (!photoProducto.equals(""))
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0 , 200);
                        toast.show();
                        Picasso.with(AltaProductoActivity.this)
                                .load(photoProducto)
                                .resize(150, 150)
                                .into(fotoProducto);
                    }
                }catch (Exception e)
                {
                    Log.v("Error", "error: " + e);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al obtener los datos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void altaProductos(String nom, String pr, String can)
    {
        Map<String, Object> productos = new HashMap<>();
        productos.put("nombreProducto", nom);
        productos.put("precioProducto", pr);
        productos.put("cantidadProducto", can);

        mFirestore.collection("productos").add(productos).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Dato ingresado correctamente", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AltaProductoActivity.this, MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}