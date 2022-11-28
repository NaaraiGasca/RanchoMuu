package com.example.ranchomuu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class AltaProducto extends DialogFragment {
    ImageView fotoProducto;
    String id_producto;
    Button btnAdd, btnAgregarFoto, btnEliminarFoto;
    EditText nombre, precio, cantidad;
    FirebaseFirestore mfirestore;

    StorageReference storageReference;
    String storage_path = "productos/*";

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    //private Uri image_url;
    String photo = "foto";
    String idd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            id_producto = getArguments().getString("id_producto");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_alta_producto, container, false);
        mfirestore = FirebaseFirestore.getInstance();

        nombre = v.findViewById(R.id.productoN);
        precio = v.findViewById(R.id.precioProductoN);
        cantidad = v.findViewById(R.id.stockProductoN);
        btnAdd = v.findViewById(R.id.btnRegistroProducto);

        if (id_producto== null || id_producto=="")
        {
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nom = nombre.getText().toString().trim();
                    String pr = precio.getText().toString().trim();
                    String can = cantidad.getText().toString().trim();

                    if (nom.isEmpty() || pr.isEmpty() || can.isEmpty())
                    {
                        Toast.makeText(getContext(), "Complete los campos.", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        altaProductos(nom, pr, can);
                    }
                }
            });
        }else
            {
                obtenerProducto();
                btnAdd.setText("Actualizar");
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nom = nombre.getText().toString().trim();
                        String pr = precio.getText().toString().trim();
                        String can = cantidad.getText().toString().trim();

                        if (nom.isEmpty() || pr.isEmpty() || can.isEmpty())
                        {
                            Toast.makeText(getContext(), "Complete los campos.", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            actualizarProductos(nom, pr, can);
                        }
                    }
                });
            }


        return v;
    }

    private void actualizarProductos(String nom, String pr, String can)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("nombreProducto", nom);
        map.put("precioProducto", pr);
        map.put("cantidadProducto", can);

        mfirestore.collection("productos").document(id_producto).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), "Información actualizada correctamente.", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al actualizar.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerProducto()
    {
        mfirestore.collection("productos").document(id_producto).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String nom = documentSnapshot.getString("nombreProducto");
                String pr = documentSnapshot.getString("precioProducto");
                String can = documentSnapshot.getString("cantidadProducto");
                nombre.setText(nom);
                precio.setText(pr);
                cantidad.setText(can);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al obtener los datos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void altaProductos(String nom, String pr, String can)
    {
        Map<String, Object> productos = new HashMap<>();
        productos.put("nombreProducto", nom);
        productos.put("precioProducto", pr);
        productos.put("cantidadProducto", can);

        mfirestore.collection("productos").add(productos).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getContext(), "Dato ingresado correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}